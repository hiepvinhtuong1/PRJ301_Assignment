// src/utils/apiClient.js
import axios from "axios";

const apiClient = axios.create({
  baseURL: "http://localhost:8081/hiep", // Đặt baseURL cho toàn bộ request
  headers: {
    "Content-Type": "application/json; charset=utf-8",
  },
});

// Thêm interceptor để tự động gắn token vào tất cả các request
apiClient.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("TOKEN");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => Promise.reject(error)
);

export default apiClient;
