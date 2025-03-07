import React, { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaFacebookF, FaTwitter, FaLinkedinIn } from "react-icons/fa";
import "./login.css";
import ApiService from "../../service/AppService";
import { useNavigate } from "react-router-dom"; // Thêm useNavigate để điều hướng

const Login = () => {
  const [loginRequest, setLoginRequest] = useState({
    username: "",
    password: "",
  });
  const navigate = useNavigate(); // Khởi tạo useNavigate

  const handleInputChange = (e) => {
    const name = e.target.name;
    const value = e.target.value;
    setLoginRequest({ ...loginRequest, [name]: value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const response = await ApiService.loginUser(loginRequest);
      console.log(response);
      if (response?.data?.code === 200) {
        // Lưu token vào localStorage
        localStorage.setItem("TOKEN", response.data?.data?.token);
        console.log("Token stored:", localStorage.getItem("TOKEN"));
        console.log("Login successful");

        // Điều hướng đến trang chính (hoặc trang bạn muốn) sau khi đăng nhập thành công
        navigate("/profile"); // Ví dụ: chuyển hướng đến trang dashboard
      }
    } catch (error) {
      console.error("Login failed:", error);
      // Có thể thêm thông báo lỗi cho người dùng
      alert("Đăng nhập thất bại. Vui lòng kiểm tra lại thông tin.");
    }
  };

  return (
    <section className="vh-100">
      <div className="container-fluid h-custom border-danger shadow">
        <div className="row d-flex justify-content-center align-items-center h-100">
          <div className="col-md-9 col-lg-6 col-xl-5">
            <img
              src="https://mdbcdn.b-cdn.net/img/Photos/new-templates/bootstrap-login-form/draw2.webp"
              className="img-fluid"
              alt="Sample image"
            />
          </div>
          <div className="col-md-8 col-lg-6 col-xl-4 offset-xl-1">
            <form onSubmit={handleSubmit}>
              <div className="d-flex flex-row align-items-center justify-content-center justify-content-lg-start">
                <p className="lead fw-normal mb-0 me-3">Sign in with</p>
                <button
                  type="button"
                  className="btn btn-primary btn-floating mx-1"
                >
                  <FaFacebookF />
                </button>
                <button
                  type="button"
                  className="btn btn-primary btn-floating mx-1"
                >
                  <FaTwitter />
                </button>
                <button
                  type="button"
                  className="btn btn-primary btn-floating mx-1"
                >
                  <FaLinkedinIn />
                </button>
              </div>

              <div className="divider d-flex align-items-center my-4">
                <p className="text-center fw-bold mx-3 mb-0">Or</p>
              </div>

              <div data-mdb-input-init className="form-outline mb-4">
                <input
                  type="text"
                  id="form3Example3"
                  name="username"
                  className="form-control form-control-lg"
                  placeholder="Username"
                  value={loginRequest.username}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div data-mdb-input-init className="form-outline mb-3">
                <input
                  type="password"
                  id="form3Example4"
                  name="password"
                  className="form-control form-control-lg"
                  placeholder="Password"
                  value={loginRequest.password}
                  onChange={handleInputChange}
                  required
                />
              </div>

              <div className="text-center text-lg-start mt-4 pt-2">
                <button
                  type="submit"
                  className="btn btn-primary btn-lg"
                  style={{ paddingLeft: "2.5rem", paddingRight: "2.5rem" }}
                >
                  Login
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>
    </section>
  );
};

export default Login;
