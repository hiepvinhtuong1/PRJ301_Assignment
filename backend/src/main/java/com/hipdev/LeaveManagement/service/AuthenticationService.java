package com.hipdev.LeaveManagement.service;

import com.hipdev.LeaveManagement.dto.request.auth.*;
import com.hipdev.LeaveManagement.dto.response.IntrospectResponse;
import com.hipdev.LeaveManagement.dto.response.AuthenticationResponse;
import com.hipdev.LeaveManagement.dto.response.RegisterResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    public RegisterResponse register(RegisterRequest request) ;
    public IntrospectResponse introspect(IntrospectRequest request)throws ParseException, JOSEException;
    public AuthenticationResponse login(AuthenticationRequest request);
    public AuthenticationResponse refresh(RefreshRequest request) throws ParseException, JOSEException;
    public void logout(LogoutRequest request) throws ParseException, JOSEException;
}
