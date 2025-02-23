import React from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import ApiService from './ApiService';


export const ProtectedRoute = ({ element: Component }) => {
  const location = useLocation(); // Lấy thông tin về địa điểm (route hiện tại).

  return ApiService.isAuthenticated() ? (
    Component // Nếu người dùng đã đăng nhập, hiển thị component cần bảo vệ.
  ) : (
    <Navigate to="/login" replace state={{ from: location }} /> // Nếu người dùng chưa đăng nhập, chuyển hướng họ đến trang đăng nhập.
  );
};



export const AdminRoute = ({ element: Component }) => {
  const location = useLocation(); // Lấy thông tin về địa điểm (route hiện tại).

  return ApiService.isAdmin() ? (
    Component // Nếu người dùng là admin, hiển thị component yêu cầu quyền admin.
  ) : (
    <Navigate to="/login" replace state={{ from: location }} /> // Nếu không phải admin, chuyển hướng họ đến trang đăng nhập.
  );
};
