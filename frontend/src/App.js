import './App.css';
import React from 'react';
import { Routes, Route, BrowserRouter, Navigate } from 'react-router-dom'; // Sử dụng Navigate để chuyển hướng
import Navbar from './component/common/Navbar';
import LeaveRequestPage from './component/leave_request/LeaveRequestPage';
import LoginPage from './component/auth/LoginPage';

function App() {
  return (
    <BrowserRouter>
      <div className="App">
        {/* Hiển thị Navbar cho tất cả các trang trừ /login */}
        <Routes>
          {/* Nếu đang ở trang login, không hiển thị Navbar */}
          <Route path="/login" element={<LoginPage />} />
          {/* Các route khác, ví dụ: */}
          {/* <Route path="/profile" element={<ProtectedRoute element={<ProfilePage />} />} /> */}
          <Route path="/leave-request" element={<><Navbar /><LeaveRequestPage /></>} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}

export default App;
