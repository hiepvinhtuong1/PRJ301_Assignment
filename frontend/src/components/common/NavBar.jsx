import React, { useState } from "react";
import { Link } from "react-router-dom";
import { Dropdown } from "react-bootstrap";
import avatar from "../../assets/avatar_icon.png";
const NavBar = () => {
  const [showMenu, setShowMenu] = useState(false);

  return (
    <nav className="navbar navbar-expand-lg navbar-dark bg-dark mb-5">
      <div className="container-fluid">
        <Link className="navbar-brand" to={"/"}>
          LEAVE MANAGEMENT
        </Link>
        <button
          className="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNavAltMarkup"
          aria-controls="navbarNavAltMarkup"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span className="navbar-toggler-icon"></span>
        </button>
        <div className="collapse navbar-collapse" id="navbarNavAltMarkup">
          <div className="navbar-nav">
            <Link className="nav-link active" to={"/profile"}>
              PROFILE
            </Link>
            <Link className="nav-link" to={"/leave_request/list"}>
              LEAVE REQUEST
            </Link>
            <Link className="nav-link" to={"/leave_request/handle"}>
              APPLICATION PROCESSING
            </Link>
            <Link className="nav-link" to={"/calendar/view"}>
              CALENDAR
            </Link>
          </div>
          <div className="ms-auto">
            <Dropdown show={showMenu} onToggle={() => setShowMenu(!showMenu)}>
              <Dropdown.Toggle
                as="div"
                onClick={() => setShowMenu(!showMenu)}
                style={{ cursor: "pointer" }}
              >
                <img
                  src={avatar} // Thay bằng URL avatar thật
                  alt="User Avatar"
                  className="rounded-circle"
                  width="40"
                  height="40"
                />
              </Dropdown.Toggle>
              <Dropdown.Menu align="end">
                <Dropdown.Item as={Link} to="/login">
                  Logout
                </Dropdown.Item>
              </Dropdown.Menu>
            </Dropdown>
          </div>
        </div>
      </div>
    </nav>
  );
};

export default NavBar;
