package com.hipdev.LeaveManagement.service.interfac;

import com.hipdev.LeaveManagement.dto.LoginRequest;
import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.entity.User;

public interface IUserService {
    Response register(User user);

    Response login(LoginRequest loginRequest);

    Response getAllUsers();

    Response getUserRequestHistory(String userId);

    Response getUserById(String userId);

    Response getMyInfo(String username);

    Response deleteUser(String userId);

    Response getUserByRole(String username);

    Response getUsersByRole(String username);

    boolean isLeaderOrMangerOfUser(String userId, String username);

}
