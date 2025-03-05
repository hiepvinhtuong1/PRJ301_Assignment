import { useState } from "react";
import { useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";

const AddLeaveRequest = () => {
  const navigate = useNavigate();
  const [leaveRequest, setLeaveRequest] = useState({
    startDate: "",
    endDate: "",
    duration: "",
    leaveType: "paid leave",
    reason: "",
    status: "Planned",
  });

  const handleChange = (e) => {
    setLeaveRequest({ ...leaveRequest, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    console.log("Leave request submitted:", leaveRequest);
    navigate("/leave-requests");
  };

  return (
    <div className="container mt-4 d-flex justify-content-center">
      <div className="card p-4" style={{ width: "400px" }}>
        <h4 className="text-center">Request Leave</h4>
        <form onSubmit={handleSubmit}>
          <div className="mb-2">
            <label className="form-label">Start Date</label>
            <input
              type="date"
              className="form-control"
              name="startDate"
              value={leaveRequest.startDate}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-2">
            <label className="form-label">End Date</label>
            <input
              type="date"
              className="form-control"
              name="endDate"
              value={leaveRequest.endDate}
              onChange={handleChange}
              required
            />
          </div>
          <div className="mb-2">
            <label className="form-label">Reason</label>
            <textarea
              className="form-control"
              name="reason"
              value={leaveRequest.reason}
              onChange={handleChange}
              required
            ></textarea>
          </div>
          <div className="d-flex justify-content-between mt-3">
            <button type="submit" className="btn btn-primary">
              Submit
            </button>
            <button
              type="button"
              className="btn btn-danger"
              onClick={() => navigate("/leave_request/list")}
            >
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
};

export default AddLeaveRequest;
