package com.hipdev.LeaveManagement.service.impl;

import com.hipdev.LeaveManagement.dto.LoginRequest;
import com.hipdev.LeaveManagement.dto.Response;
import com.hipdev.LeaveManagement.dto.UserDTO;
import com.hipdev.LeaveManagement.entity.User;
import com.hipdev.LeaveManagement.exception.MyException;
import com.hipdev.LeaveManagement.repo.DepartmentRepository;
import com.hipdev.LeaveManagement.repo.LeaveRequestRepository;
import com.hipdev.LeaveManagement.repo.UserRepository;
import com.hipdev.LeaveManagement.service.interfac.IUserService;
import com.hipdev.LeaveManagement.utils.JWTUtils;
import com.hipdev.LeaveManagement.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService implements IUserService {

    private JWTUtils jwtUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private LeaveRequestRepository leaveRequestRepository;
    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public Response login(LoginRequest loginRequest) {
        Response response = new Response();

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

            var user = userRepository.findByUsername(loginRequest.getUsername()).orElseThrow(() -> new MyException("User not found"));

            var jwt = jwtUtils.generateToken(user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRole(user.getRole());
            response.setExpirationTime("7 Days");
            response.setMessage("Success");

        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured drung user's login: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getAllUsers() {

        Response response = new Response();

        try {
            List<User> users = userRepository.findAll();
            List<UserDTO> userDTOs = Utils.mapUserListEntityToListDTO(users);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUsers(userDTOs);
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured drung user's getAllUsers: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserRequestHistory(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToDTOPlusRequest(user);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUser(userDTO);
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured drung user's getUserRequestHistory: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getUserById(String userId) {
        Response response = new Response();

        try {
            User user = userRepository.findById(Long.valueOf(userId)).orElseThrow(() -> new MyException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToDTO(user);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUser(userDTO);
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured drung user's getUserById: " + e.getMessage());
        }
        return response;
    }

    @Override
    public Response getMyInfo(String username) {
        Response response = new Response();

        try {
            User user = userRepository.findByUsername(username).orElseThrow(() -> new MyException("User not found"));
            UserDTO userDTO = Utils.mapUserEntityToDTO(user);
            response.setStatusCode(200);
            response.setMessage("Success");
            response.setUser(userDTO);
        } catch (MyException e) {
            response.setStatusCode(404);
            response.setMessage(e.getMessage());
        } catch (Exception e) {
            response.setStatusCode(500);
            response.setMessage("Error occured drung user's getUserById: " + e.getMessage());
        }
        return response;
    }
}
