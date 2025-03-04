import React from "react";
import { Card, Button, Table } from "react-bootstrap";
import avatar from "../../assets/avatar_icon.png";

const EmployeeProfile = () => {
  const employee = {
    avatar: "https://via.placeholder.com/150", // Thay bằng URL avatar thật
    name: "John Doe",
    position: "Full Stack Developer",
    location: "Bay Area, San Francisco, CA",
    fullName: "Kenneth Valdez",
    email: "fip@jukmuh.al",
    gender: "Male",
    dob: "January 1, 1990",
    role: "Software Engineer",
    department: "IT",
  };

  return (
    <div className="container-fluid mt-5 d-flex justify-content-center">
      <div className="row w-100">
        {/* Avatar Card */}
        <div className="col-md-4 d-flex justify-content-center">
          <Card className="text-center p-4 shadow-lg w-75">
            <Card.Img
              variant="top"
              src={avatar}
              className="rounded-circle mx-auto"
              style={{ width: "150px", height: "150px" }}
            />
            <Card.Body>
              <Card.Title className="fs-3">{employee.name}</Card.Title>
              <Card.Text className="text-muted fs-5">
                {employee.position}
              </Card.Text>
              <Card.Text className="text-muted fs-5">
                {employee.location}
              </Card.Text>
            </Card.Body>
          </Card>
        </div>

        {/* Employee Info Table */}
        <div className="col-md-8 d-flex justify-content-center">
          <Card className="p-4 shadow-lg w-75">
            <Table bordered hover className="fs-5">
              <tbody>
                <tr>
                  <td>
                    <strong>Full Name</strong>
                  </td>
                  <td>{employee.fullName}</td>
                </tr>
                <tr>
                  <td>
                    <strong>Email</strong>
                  </td>
                  <td>{employee.email}</td>
                </tr>
                <tr>
                  <td>
                    <strong>Gender</strong>
                  </td>
                  <td>{employee.gender}</td>
                </tr>
                <tr>
                  <td>
                    <strong>Date of Birth</strong>
                  </td>
                  <td>{employee.dob}</td>
                </tr>
                <tr>
                  <td>
                    <strong>Role</strong>
                  </td>
                  <td>{employee.role}</td>
                </tr>
                <tr>
                  <td>
                    <strong>Department</strong>
                  </td>
                  <td>{employee.department}</td>
                </tr>
              </tbody>
            </Table>

            {/* Button chỉnh sửa - căn giữa & rộng 30% */}
            <div className="d-flex justify-content-center mt-3">
              <Button variant="info" style={{ width: "30%" }}>
                Edit
              </Button>
            </div>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default EmployeeProfile;
