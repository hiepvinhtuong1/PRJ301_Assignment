import React, { useState } from "react";
import { Table, Button, Form } from "react-bootstrap";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa";

const EmployeeList = () => {
  // Dữ liệu mẫu danh sách nhân viên
  const employeesData = [
    {
      id: 1,
      name: "Thomas Hardy",
      address: "89 Chiaroscuro Rd.",
      city: "Portland",
      pinCode: "97219",
      country: "USA",
    },
    {
      id: 2,
      name: "Maria Anders",
      address: "Obere Str. 57",
      city: "Berlin",
      pinCode: "12209",
      country: "Germany",
    },
    {
      id: 3,
      name: "Fran Wilson",
      address: "C/ Araquil, 67",
      city: "Madrid",
      pinCode: "28023",
      country: "Spain",
    },
    {
      id: 4,
      name: "Dominique Perrier",
      address: "25, rue Lauriston",
      city: "Paris",
      pinCode: "75016",
      country: "France",
    },
    {
      id: 5,
      name: "Martin Blank",
      address: "Via Monte Bianco 34",
      city: "Turin",
      pinCode: "10100",
      country: "Italy",
    },
    {
      id: 6,
      name: "John Smith",
      address: "123 Main St",
      city: "New York",
      pinCode: "10001",
      country: "USA",
    },
    {
      id: 7,
      name: "Emma Watson",
      address: "45 Oxford Rd",
      city: "London",
      pinCode: "SW1A 1AA",
      country: "UK",
    },
  ];

  const [employees, setEmployees] = useState(employeesData);
  const [searchTerm, setSearchTerm] = useState("");
  const [currentPage, setCurrentPage] = useState(1);
  const employeesPerPage = 5;

  // Lọc danh sách nhân viên theo tìm kiếm
  const filteredEmployees = employees.filter((emp) =>
    emp.name.toLowerCase().includes(searchTerm.toLowerCase())
  );

  // Lấy dữ liệu theo trang
  const indexOfLastEmployee = currentPage * employeesPerPage;
  const indexOfFirstEmployee = indexOfLastEmployee - employeesPerPage;
  const currentEmployees = filteredEmployees.slice(
    indexOfFirstEmployee,
    indexOfLastEmployee
  );

  // Hàm thay đổi trang
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  return (
    <div className="container mt-5">
      <h2 className="mb-4">
        Employee <strong>List</strong>
      </h2>

      {/* Thanh tìm kiếm */}
      <Form className="mb-3">
        <Form.Control
          type="text"
          placeholder="🔍 Search..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
      </Form>

      {/* Bảng danh sách nhân viên */}
      <Table striped bordered hover>
        <thead className="table-light">
          <tr>
            <th>#</th>
            <th>Name</th>
            <th>Address</th>
            <th>City</th>
            <th>Pin Code</th>
            <th>Country</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {currentEmployees.map((employee, index) => (
            <tr key={employee.id}>
              <td>{indexOfFirstEmployee + index + 1}</td>
              <td>{employee.name}</td>
              <td>{employee.address}</td>
              <td>{employee.city}</td>
              <td>{employee.pinCode}</td>
              <td>{employee.country}</td>
              <td className="text-center">
                <Button variant="info" size="sm" className="me-2">
                  <FaEye />
                </Button>
                <Button variant="warning" size="sm" className="me-2">
                  <FaEdit />
                </Button>
                <Button variant="danger" size="sm">
                  <FaTrash />
                </Button>
              </td>
            </tr>
          ))}
        </tbody>
      </Table>

      {/* Phân trang */}
      <div className="d-flex justify-content-between">
        <p>
          Showing <strong>{currentEmployees.length}</strong> out of{" "}
          <strong>{filteredEmployees.length}</strong> entries
        </p>
        <div>
          {Array.from(
            { length: Math.ceil(filteredEmployees.length / employeesPerPage) },
            (_, i) => (
              <Button
                key={i}
                variant={currentPage === i + 1 ? "primary" : "outline-primary"}
                className="me-2"
                onClick={() => paginate(i + 1)}
              >
                {i + 1}
              </Button>
            )
          )}
        </div>
      </div>
    </div>
  );
};

export default EmployeeList;
