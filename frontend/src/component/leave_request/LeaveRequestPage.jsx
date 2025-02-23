import React, { useState, useEffect } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import ApiService from "../../service/ApiService";
import { Modal, Button } from "react-bootstrap";

function LeaveRequestPage() {
  const [leaveRequest, setLeaveRequest] = useState({
    startDate: "",
    endDate: "",
    reason: "",
  });

  const [error, setError] = useState("");
  const [showErrorModal, setShowErrorModal] = useState(false);
  const [leaveHistory, setLeaveHistory] = useState([]);

  // Load leave history when the component is mounted
  useEffect(() => {
    const fetchLeaveHistory = async () => {
      try {
        const response = await ApiService.getMyInfo(); // Lấy thông tin người dùng từ ApiService
        const leaveRequests = response.leaveRequests || []; // Lấy mảng leaveRequests từ response, nếu không có gán mảng trống

        setLeaveHistory(leaveRequests); // Update leaveHistory with leave requests
      } catch (error) {
        setError("Error fetching leave history.");
        setShowErrorModal(true); // Hiển thị modal nếu có lỗi
      }
    };

    fetchLeaveHistory();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setLeaveRequest((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleCloseModal = () => {
    setShowErrorModal(false);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (
      !leaveRequest.startDate ||
      !leaveRequest.endDate ||
      !leaveRequest.reason
    ) {
      setError("Please fill in all fields.");
      setShowErrorModal(true);
      return;
    }
    const leaveData = {
      startDate: leaveRequest.startDate,
      endDate: leaveRequest.endDate,
      reason: leaveRequest.reason,
    };

    try {
      const response = await ApiService.saveLeaveRequest(leaveData);

      if (response.statusCode === 200) {
        alert("Leave request submitted successfully!");
      
      }
    } catch (error) {
      setError(error.response?.data?.message || error.message);
      setShowErrorModal(true);
    }
  };

  return (
    <div className="container my-5">
      {error && <p className="error-message">{error}</p>}

      {/* Phần nhập form Leave Request */}
      <div className="row">
        <div className="col-md-6 col-lg-4 mx-auto">
          <div className="card p-4 shadow">
            <h3 className="text-center mb-4">Leave Request Form</h3>
            <form onSubmit={handleSubmit}>
              <div className="row">
                <div className="col-6">
                  <label className="form-label">Leave Start Date</label>
                  <input
                    type="date"
                    className="form-control"
                    name="startDate"
                    value={leaveRequest.startDate}
                    onChange={handleChange}
                    required
                  />
                </div>
                <div className="col-6">
                  <label className="form-label">Leave End Date</label>
                  <input
                    type="date"
                    className="form-control"
                    name="endDate"
                    value={leaveRequest.endDate}
                    onChange={handleChange}
                    required
                  />
                </div>
              </div>

              <div className="mb-3">
                <label className="form-label">Reason</label>
                <textarea
                  className="form-control"
                  name="reason"
                  value={leaveRequest.reason}
                  onChange={handleChange}
                  rows="3"
                  required
                />
              </div>

              <div className="text-center">
                <button type="submit" className="btn btn-primary btn-sm">
                  Submit Request
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      {/* Phần hiển thị lịch sử nghỉ phép */}
      <div className="row mt-5">
        <div className="col-md-12">
          <div className="card p-4 shadow">
            <h3 className="text-center mb-4">Leave Request History</h3>
            <table className="table table-bordered table-hover">
              <thead className="table-dark">
                <tr>
                  <th>ID</th>
                  <th>Full Name</th>
                  <th>Start Date</th>
                  <th>End Date</th>
                  <th>Status</th>
                  <th>Processor</th>
                  <th>Reason</th>
                  <th>Comment</th>
                </tr>
              </thead>
              <tbody>
                {leaveHistory.map((leave, index) => (
                  <tr key={index}>
                    <td>{leave.creator.id}</td>
                    <td>{leave.creator.fullName}</td>
                    <td>{leave.startDate}</td>
                    <td>{leave.endDate}</td>
                    <td>{leave.status}</td>
                    <td>{leave?.processor?.fullName}</td>
                    <td>{leave.reason}</td>
                    <td>{leave.comment}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </div>
      </div>

      {/* Modal thông báo lỗi */}
      <Modal show={showErrorModal} onHide={handleCloseModal}>
        <Modal.Header closeButton>
          <Modal.Title>Error</Modal.Title>
        </Modal.Header>
        <Modal.Body>{error}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={handleCloseModal}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>
    </div>
  );
}

export default LeaveRequestPage;
