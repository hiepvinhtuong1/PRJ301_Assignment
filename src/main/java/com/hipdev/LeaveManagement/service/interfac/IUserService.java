package com.hipdev.LeaveManagement.service.interfac;

import com.hipdev.LeaveManagement.dto.LoginRequest;
import com.hipdev.LeaveManagement.dto.Response;

public interface IUserService {
    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserRequestHistory(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String username);

    Response deleteUser(String userId);

    Response getUserByRole(String username);

    Response isLeaderOfUser(String userId, String leaderId);

    Response isManagerOfUser(String userId, String managerId);
}
