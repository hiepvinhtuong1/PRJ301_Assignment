import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

const LeaveRequestUpdate = ({ request, onUpdate }) => {
  const [formData, setFormData] = useState({
    reason: request?.reason || "",
    startDate: request?.startDate || "",
    endDate: request?.endDate || "",
    status: request?.status || "Pending",
    comment: request?.comment || "",
  });

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    onUpdate(formData);
  };

  return (
    <div className="container mt-4">
      <h2>Update Leave Request</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-3">
          <label className="form-label">Reason</label>
          <input
            type="text"
            className="form-control"
            name="reason"
            value={formData.reason}
            onChange={handleChange}
            required
          />
        </div>
        <div className="row mb-3">
          <div className="col">
            <label className="form-label">Start Date</label>
            <input
              type="date"
              className="form-control"
              name="startDate"
              value={formData.startDate}
              onChange={handleChange}
              required
            />
          </div>
          <div className="col">
            <label className="form-label">End Date</label>
            <input
              type="date"
              className="form-control"
              name="endDate"
              value={formData.endDate}
              onChange={handleChange}
              required
            />
          </div>
        </div>
        <div className="mb-3">
          <label className="form-label">Status</label>
          <select
            className="form-control"
            name="status"
            value={formData.status}
            onChange={handleChange}
          >
            <option value="Pending">Pending</option>
            <option value="Approved">Approved</option>
            <option value="Rejected">Rejected</option>
          </select>
        </div>
        <div className="mb-3">
          <label className="form-label">Comment</label>
          <textarea
            className="form-control"
            name="comment"
            value={formData.comment}
            onChange={handleChange}
          ></textarea>
        </div>
        <button type="submit" className="btn btn-primary">
          Update
        </button>
      </form>
    </div>
  );
};

export default LeaveRequestUpdate;
