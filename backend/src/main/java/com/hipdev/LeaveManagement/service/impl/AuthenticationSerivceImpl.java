package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.request.*;
import com.hipdev.LeaveManagement.dto.response.IntrospectResponse;
import com.hipdev.LeaveManagement.dto.response.AuthenticationResponse;
import com.hipdev.LeaveManagement.dto.response.RegisterResponse;
import com.hipdev.LeaveManagement.entity.InvalidatedToken;
import com.hipdev.LeaveManagement.entity.User;
import com.hipdev.LeaveManagement.exception.AppException;
import com.hipdev.LeaveManagement.exception.ErrorCode;
import com.hipdev.LeaveManagement.repository.InvalidatedTokenRepository;
import com.hipdev.LeaveManagement.repository.UserRepository;
import com.hipdev.LeaveManagement.service.AuthenticationService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;


@Service
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationSerivceImpl implements AuthenticationService {

    // Inject repository for invalidated tokens and user data
    @Autowired
    InvalidatedTokenRepository invalidatedTokenRepository;

    @Autowired
    private UserRepository userRepository;

    // Configuration values for JWT signing and durations
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    // Password encoder used for hashing passwords
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Register a new user in the system
    @Override
    public RegisterResponse register(RegisterRequest request) {
        // Check if the user already exists
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED); // Throw error if user exists
        }

        // Create a new user object and set the username and hashed password
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword())); // Encode password

        // Save the new user in the database
        userRepository.save(user);

        // Return response with the registered username
        return RegisterResponse.builder()
                .username(request.getUsername())
                .password(request.getPassword()) // Ideally, do not return password in response
                .build();
    }

    // Introspect (validate) a given token
    @Override
    public IntrospectResponse introspect(IntrospectRequest request) throws ParseException, JOSEException {
        var token = request.getToken();
        boolean isValid = true;

        try {
            // Try to verify the token
            verifyToken(token, false);
        } catch (AppException e) {
            // If token is invalid, set validity to false
            isValid = false;
        }

        // Return response indicating whether the token is valid
        return IntrospectResponse.builder().valid(isValid).build();
    }

    // Handle user login and generate JWT token if successful
    @Override
    public AuthenticationResponse login(AuthenticationRequest request) {
        // Find user by username
        var user = userRepository.findByUsername(request.getUsername()).orElseThrow(
                () -> new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION) // Throw error if user not found
        );

        // Check if the password matches the stored hashed password
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPassword());

        // If authentication fails, throw an error
        if (!authenticated) {
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);
        }

        // Generate JWT token for the authenticated user
        var token = generateToken(user);

        // Return response with the token and authentication status
        return AuthenticationResponse.builder().token(token).authenticated(authenticated).build();
    }

    // Refresh an existing token (validates old one, generates a new one)
    @Override
    public AuthenticationResponse refresh(RefreshRequest request) throws ParseException, JOSEException {
        // Verify the provided refresh token
        var signedJWT = verifyToken(request.getToken(), true);

        // Extract the JWT ID and expiry time
        var jit = signedJWT.getJWTClaimsSet().getJWTID();
        var expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Create a new invalidated token record to mark the old token as invalid
        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jit)
                .expiryTime(expiryTime)
                .build();

        // Retrieve the username from the JWT claims
        var username = signedJWT.getJWTClaimsSet().getSubject();

        // Find the user by username
        var user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.USER_NOT_EXISTED)
        );

        // Generate a new token for the user
        var token = generateToken(user);

        // Return the new token and mark the refresh as successful
        return AuthenticationResponse.builder().token(token).authenticated(true).build();
    }

    @Override
    public void logout(LogoutRequest request) throws ParseException, JOSEException {
        try {
            var signToken = verifyToken(request.getToken(), true);

            String jit = signToken.getJWTClaimsSet().getJWTID();
            Date expiryTime = signToken.getJWTClaimsSet().getExpirationTime();

            InvalidatedToken invalidatedToken =
                    InvalidatedToken.builder().id(jit).expiryTime(expiryTime).build();

            invalidatedTokenRepository.save(invalidatedToken);
        } catch (AppException exception) {
            log.info("Token already expired");
        }
    }

    // Verify the validity of a given token
    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        // Create a verifier using the configured signer key
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // Parse the token to a SignedJWT object
        SignedJWT signedJWT = SignedJWT.parse(token);

        // Calculate the expiration time, considering refreshable duration if applicable
        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        // Verify the token's signature
        var verified = signedJWT.verify(verifier);

        // If the signature is invalid or the token has expired, throw an error
        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        // Check if the token is invalidated (if it's in the invalidated tokens list)
        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNCATEGORIZED_EXCEPTION);

        return signedJWT;
    }

    // Generate a new JWT token for a user
    private String generateToken(User user) {
        // Create JWT header with HS512 algorithm
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        // Build JWT claims with user info (e.g., username, issue time, expiration, scope)
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli() // Token expires in 1 hour
                ))
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user)) // Add user roles and permissions as scope
                .build();

        // Create a new JWT payload
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        // Create the JWS object and sign it
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            // Sign the JWT using the configured signer key
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize(); // Return the serialized token
        } catch (JOSEException e) {
            // Log error and throw exception if signing fails
            System.out.println("e.getMessage() = " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // Build the "scope" claim for the JWT token, representing the user's roles and permissions
    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        // Add roles to the scope if the user has any
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add("ROLE_" + role.getRoleName());
                // Add permissions to the scope if the role has any
                if (!CollectionUtils.isEmpty(role.getPermissions()))
                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getPermissionName()));
            });

        return stringJoiner.toString(); // Return the built scope
    }
}

