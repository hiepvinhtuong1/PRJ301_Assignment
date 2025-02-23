import axios from "axios";

const BASE_URL = "http://localhost:8081/hiep"; // URL của API backend

export default class ApiService {

  static getHeader() {
    const token = localStorage.getItem("token");
    return {
      Authorization: `Bearer ${token}`,
      "Content-Type": "application/json"
    };
  }

  /** AUTH */
  /* This logs in a registered user */
  static async loginUser(loginDetails) {
    const response = await axios.post(`${BASE_URL}/auth/login`, loginDetails);
    return response.data;
  }

  /* This registers a new user */
  static async registerUser(registration) {
    const response = await axios.post(`${BASE_URL}/auth/register`, registration);
    return response.data;
  }

  /** USERS */
  /* This gets the current logged-in user's profile information */
  static async getMyInfo() {
    const response = await axios.get(`${BASE_URL}/users/get-my-info`, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /* This gets all users */
  static async getAllUsers() {
    const response = await axios.get(`${BASE_URL}/users/all`, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /* This gets a single user by user ID */
  static async getUserById(userId) {
    const response = await axios.get(`${BASE_URL}/users/get-by-id/${userId}`, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /* This gets users based on their roles */
  static async getUsersByRole() {
    const response = await axios.get(`${BASE_URL}/users/get-users-by-role`, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /** LEAVE REQUESTS */
  /* This saves a new leave request */
  static async saveLeaveRequest(leaveRequest) {
    const response = await axios.post(`${BASE_URL}/leave-requests/save`, leaveRequest, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /* This processes a leave request (approve/reject) */
  static async processLeaveRequest(requestId, updatedRequest) {
    const response = await axios.put(`${BASE_URL}/leave-requests/process-request/${requestId}`, updatedRequest, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /* This gets all leave requests */
  static async getAllLeaveRequests() {
    const response = await axios.get(`${BASE_URL}/leave-requests/all`, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /* This gets a specific leave request by its ID */
  static async getLeaveRequestById(requestId) {
    const response = await axios.get(`${BASE_URL}/leave-requests/get-request-by-id/${requestId}`, {
      headers: this.getHeader()
    });
    return response.data;
  }

  /** AUTHENTICATION CHECKER */
  static logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
  }

  static isAuthenticated() {
    const token = localStorage.getItem('token');
    return !!token;
  }

  static isAdmin() {
    const role = localStorage.getItem('role');
    return role === 'ADMIN';
  }

  static isUser() {
    const role = localStorage.getItem('role');
    return role === 'USER';
  }

  static isLeader() {
    const role = localStorage.getItem('role');
    return role === 'LEADER';
  }

  static isManager() {
    const role = localStorage.getItem('role');
    return role === 'MANAGER';
  }
}
