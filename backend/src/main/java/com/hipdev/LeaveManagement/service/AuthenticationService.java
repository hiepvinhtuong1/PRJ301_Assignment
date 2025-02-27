package com.hipdev.LeaveManagement.service;

import com.hipdev.LeaveManagement.dto.request.IntrospectRequest;
import com.hipdev.LeaveManagement.dto.response.IntrospectResponse;
import com.nimbusds.jose.JOSEException;

import java.text.ParseException;

public interface AuthenticationService {
    public IntrospectResponse introspect(IntrospectRequest request)throws ParseException, JOSEException;
}
