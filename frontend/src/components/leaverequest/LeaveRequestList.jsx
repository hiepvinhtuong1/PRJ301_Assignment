import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaEye, FaEdit, FaTrash } from "react-icons/fa";

const LeaveRequestList = () => {
  const [searchText, setSearchText] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [status, setStatus] = useState("");

  const leaveRequests = [
    {
      id: 1,
      reason: "Vacation",
      startDate: "2025-03-10",
      endDate: "2025-03-20",
      status: "Approved",
      comment: "Enjoy your trip!",
      creator: { name: "John Doe" },
      processor: { name: "HR Manager" },
    },
    {
      id: 2,
      reason: "Medical Leave",
      startDate: "2025-04-01",
      endDate: "2025-04-10",
      status: "Pending",
      comment: "Awaiting approval",
      creator: { name: "Jane Smith" },
      processor: { name: "HR Manager" },
    },
  ];

  const filteredData = leaveRequests.filter((item) => {
    return (
      (searchText === "" ||
        Object.values(item).some((val) =>
          String(val).toLowerCase().includes(searchText.toLowerCase())
        )) &&
      (startDate === "" || item.startDate >= startDate) &&
      (endDate === "" || item.endDate <= endDate) &&
      (status === "" || item.status.toLowerCase() === status.toLowerCase())
    );
  });

  return (
    <div className="container mt-4">
      <h2>Leave Request List</h2>
      <div className="row mb-3">
        <div className="col-md-3">
          <input
            type="text"
            className="form-control"
            placeholder="Search..."
            onChange={(e) => setSearchText(e.target.value)}
          />
        </div>
        <div className="col-md-3">
          <input
            type="date"
            className="form-control"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />
        </div>
        <div className="col-md-3">
          <input
            type="date"
            className="form-control"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
        </div>
        <div className="col-md-3">
          <select
            className="form-control"
            value={status}
            onChange={(e) => setStatus(e.target.value)}
          >
            <option value="">All Status</option>
            <option value="Approved">Approved</option>
            <option value="Pending">Pending</option>
            <option value="Rejected">Rejected</option>
          </select>
        </div>
      </div>
      <table className="table table-striped table-bordered">
        <thead className="thead-dark">
          <tr>
            <th>#</th>
            <th>Reason</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Status</th>
            <th>Comment</th>
            <th>Creator</th>
            <th>Processor</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          {filteredData.map((request) => (
            <tr key={request.id}>
              <td>{request.id}</td>
              <td>{request.reason}</td>
              <td>{request.startDate}</td>
              <td>{request.endDate}</td>
              <td>{request.status}</td>
              <td>{request.comment}</td>
              <td>{request.creator.name}</td>
              <td>{request.processor.name}</td>
              <td>
                <button className="btn btn-info btn-sm me-2">
                  <FaEye />
                </button>
                <button className="btn btn-warning btn-sm me-2">
                  <FaEdit />
                </button>
                <button className="btn btn-danger btn-sm">
                  <FaTrash />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>
    </div>
  );
};

export default LeaveRequestList;
