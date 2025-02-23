import React from 'react';
import { NavLink } from 'react-router-dom';
import ApiService from '../../service/ApiService';
import 'bootstrap/dist/css/bootstrap.min.css'; // Import Bootstrap CSS
import { Navbar, Nav, Container } from 'react-bootstrap'; // Import Bootstrap components

function NavigationBar() {
  const isAuthenticated = ApiService.isAuthenticated();
  const isUser = ApiService.isUser();
  const isLeader = ApiService.isLeader();
  const isManager = ApiService.isManager();
  const isAdmin = ApiService.isAdmin();

  const handleLogout = () => {
    ApiService.logout();
    window.location.href = '/login'; // Chuyển hướng người dùng sau khi logout
  };

  return (
    <Navbar bg="dark" variant="dark" expand="lg" className="mb-4">
      <Container>
        <Navbar.Toggle aria-controls="navbar-nav" />
        <Navbar.Collapse id="navbar-nav">
          <Nav className="me-auto">
            {/* Hiển thị Leave Request cho tất cả người dùng đã đăng nhập */}
            {isAuthenticated && (
              <Nav.Link as={NavLink} to="/leave-request" activeclassname="active">
                Leave Request
              </Nav.Link>
            )}

            {/* Hiển thị Profile chỉ khi người dùng đã đăng nhập */}
            {isAuthenticated && isUser && (
              <Nav.Link as={NavLink} to="/profile" activeclassname="active">
                Profile
              </Nav.Link>
            )}

            {/* Hiển thị Calendar cho Leader, Manager, và Admin */}
            {(isLeader || isManager || isAdmin) && (
              <Nav.Link as={NavLink} to="/calendar" activeclassname="active">
                Calendar
              </Nav.Link>
            )}
          </Nav>

          <Nav>
            {/* Hiển thị Login nếu người dùng chưa đăng nhập */}
            {!isAuthenticated && (
              <Nav.Link as={NavLink} to="/login" activeclassname="active">
                Login
              </Nav.Link>
            )}

            {/* Hiển thị Logout nếu người dùng đã đăng nhập */}
            {isAuthenticated && (
              <Nav.Link onClick={handleLogout} className="text-danger">
                Logout
              </Nav.Link>
            )}
          </Nav>
        </Navbar.Collapse>
      </Container>
    </Navbar>
  );
}

export default NavigationBar;
