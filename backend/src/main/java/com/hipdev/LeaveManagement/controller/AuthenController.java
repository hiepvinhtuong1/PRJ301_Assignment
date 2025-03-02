package com.hipdev.LeaveManagement.controller;

import com.hipdev.LeaveManagement.dto.request.AuthenticationRequest;
import com.hipdev.LeaveManagement.dto.request.IntrospectRequest;
import com.hipdev.LeaveManagement.dto.request.RegisterRequest;
import com.hipdev.LeaveManagement.dto.response.ApiResponse;
import com.hipdev.LeaveManagement.dto.response.AuthenticationResponse;
import com.hipdev.LeaveManagement.dto.response.IntrospectResponse;
import com.hipdev.LeaveManagement.dto.response.RegisterResponse;
import com.hipdev.LeaveManagement.service.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
public class AuthenController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    ApiResponse<RegisterResponse> createUser(@RequestBody @Valid RegisterRequest request) throws JOSEException {

        return ApiResponse.<RegisterResponse>builder()
                .message("success")
                .data(authenticationService.register(request))
                .build();
    }


    @PostMapping("/login")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest authenticationRequest)  {
        var result = authenticationService.login(authenticationRequest);
        return ApiResponse.<AuthenticationResponse>builder().data(result).build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        var result = authenticationService.introspect(request);
        return ApiResponse.<IntrospectResponse>builder()
                .data(result)
                .build();
    }
}
