import { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaEye, FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import { Modal, Button } from "react-bootstrap";
import useLeaveRequests from "../../hooks/useLeaveRequests";
import useCreateLeaveRequest from "../../hooks/useCreateLeaveRequest";

const LeaveRequestList = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const getParamsFromUrl = () => {
    const params = new URLSearchParams(location.search);
    return {
      filters: {
        searchText: params.get("searchText") || "",
        startDate: params.get("startDate") || "",
        endDate: params.get("endDate") || "",
        status: params.get("status") || "",
      },
      pagination: {
        current: parseInt(params.get("page")) || 1,
        pageSize: parseInt(params.get("pageSize")) || 10,
      },
    };
  };

  const [filters, setFilters] = useState(getParamsFromUrl().filters);
  const [pagination, setPagination] = useState(getParamsFromUrl().pagination);
  const { requests, totalPages, loading, error, fetchLeaveRequests } =
    useLeaveRequests();

  const updateUrl = (newFilters, newPagination) => {
    const params = new URLSearchParams();
    if (newFilters.searchText) params.set("searchText", newFilters.searchText);
    if (newFilters.startDate) params.set("startDate", newFilters.startDate);
    if (newFilters.endDate) params.set("endDate", newFilters.endDate);
    if (newFilters.status) params.set("status", newFilters.status);
    params.set("page", newPagination.current);
    params.set("pageSize", newPagination.pageSize);

    navigate({
      pathname: location.pathname,
      search: params.toString(),
    });
  };

  useEffect(() => {
    const { filters: urlFilters, pagination: urlPagination } =
      getParamsFromUrl();
    setFilters(urlFilters);
    setPagination(urlPagination);
    fetchLeaveRequests(
      urlFilters,
      urlPagination.current,
      urlPagination.pageSize
    );
  }, [location.search, fetchLeaveRequests]);

  const handleSearch = () => {
    const newPagination = { ...pagination, current: 1 };
    setPagination(newPagination);
    updateUrl(filters, newPagination);
  };

  const handlePageChange = (newPagination) => {
    setPagination(newPagination);
    updateUrl(filters, newPagination);
  };

  const [showModal, setShowModal] = useState(false);
  const [modalContent, setModalContent] = useState({ title: "", content: "" });
  const handleShowModal = (title, content) => {
    setModalContent({ title, content });
    setShowModal(true);
  };

  const [showCreateModal, setShowCreateModal] = useState(false);
  const {
    leaveRequest,
    handleChange,
    createLeaveRequest,
    loading: createLoading,
    error: createError,
  } = useCreateLeaveRequest();

  const handleSubmit = (e) => {
    e.preventDefault();
    createLeaveRequest(() => {
      setShowCreateModal(false);
      const newPagination = { ...pagination, current: 1 }; // Chuyển về trang 1
      setPagination(newPagination);
      updateUrl(filters, newPagination); // Cập nhật URL
      fetchLeaveRequests(filters, 1, newPagination.pageSize); // Gọi lại API để cập nhật dữ liệu
    });
  };

  return (
    <div className="container mt-4">
      <h2>Leave Request List</h2>
      <div className="row mb-3">
        <div className="col-md-3">
          <input
            type="text"
            className="form-control"
            placeholder="Search..."
            value={filters.searchText}
            onChange={(e) =>
              setFilters({ ...filters, searchText: e.target.value })
            }
          />
        </div>
        <div className="col-md-3">
          <input
            type="date"
            className="form-control"
            value={filters.startDate}
            onChange={(e) =>
              setFilters({ ...filters, startDate: e.target.value })
            }
          />
        </div>
        <div className="col-md-3">
          <input
            type="date"
            className="form-control"
            value={filters.endDate}
            onChange={(e) =>
              setFilters({ ...filters, endDate: e.target.value })
            }
          />
        </div>
        <div className="col-md-3">
          <select
            className="form-control"
            value={filters.status}
            onChange={(e) => setFilters({ ...filters, status: e.target.value })}
          >
            <option value="">All Status</option>
            <option value="Approved">Approved</option>
            <option value="Pending">Pending</option>
            <option value="Rejected">Rejected</option>
          </select>
        </div>
      </div>
      <div className="row mb-3">
        <div className="col">
          <Button variant="primary" onClick={handleSearch} disabled={loading}>
            {loading ? "Searching..." : "Search"}
          </Button>
        </div>
      </div>
      {error && <div className="alert alert-danger">{error}</div>}
      <table className="table table-striped table-bordered text-wrap">
        <thead className="thead-dark">
          <tr>
            <th style={{ width: "5%" }}>#</th>
            <th style={{ width: "10%" }}>Title</th>
            <th style={{ width: "10%" }}>Start Date</th>
            <th style={{ width: "10%" }}>End Date</th>
            <th style={{ width: "10%" }}>Status</th>
            <th style={{ width: "15%" }}>Comment</th>
            <th style={{ width: "10%" }}>Processor</th>
            <th style={{ width: "15%" }}>Reason</th>
            <th style={{ width: "15%" }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {requests.map((request, index) => (
            <tr key={request.id}>
              <td>
                {(pagination.current - 1) * pagination.pageSize + index + 1}
              </td>
              <td>{request.title}</td>
              <td>{request.startDate}</td>
              <td>{request.endDate}</td>
              <td>{request.status}</td>
              <td>{request.comment}</td>
              <td>{request.processor?.name || "N/A"}</td>
              <td>
                <Button
                  variant="link"
                  onClick={() =>
                    handleShowModal("Reason Details", request.reason)
                  }
                >
                  View Reason
                </Button>
              </td>
              <td>
                <Link
                  to={`/leave-request/${request.id}`}
                  className="btn btn-info btn-sm me-2"
                >
                  <FaEye />
                </Link>
                <Link
                  to={`/leave_request/update/${request.id}`}
                  className="btn btn-warning btn-sm me-2"
                >
                  <FaEdit />
                </Link>
                <button className="btn btn-danger btn-sm">
                  <FaTrash />
                </button>
              </td>
            </tr>
          ))}
        </tbody>
      </table>

      <div className="d-flex justify-content-center mt-3">
        <Button
          variant="outline-primary"
          disabled={pagination.current === 1 || loading}
          onClick={() =>
            handlePageChange({ ...pagination, current: pagination.current - 1 })
          }
        >
          Previous
        </Button>
        <span className="mx-3">
          Page {pagination.current} of {totalPages}
        </span>
        <Button
          variant="outline-primary"
          disabled={pagination.current === totalPages || loading}
          onClick={() =>
            handlePageChange({ ...pagination, current: pagination.current + 1 })
          }
        >
          Next
        </Button>
      </div>

      <div className="position-fixed bottom-0 end-0 p-4">
        <Button
          onClick={() => setShowCreateModal(true)}
          className="btn btn-success btn-lg rounded-circle d-flex align-items-center justify-content-center shadow"
          style={{ width: "60px", height: "60px" }}
        >
          <FaPlus size={30} />
        </Button>
      </div>

      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>{modalContent.title}</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <div
            style={{
              maxHeight: "300px",
              overflowY: "auto",
              wordWrap: "break-word",
            }}
          >
            {modalContent.content}
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={() => setShowModal(false)}>
            Close
          </Button>
        </Modal.Footer>
      </Modal>

      <Modal show={showCreateModal} onHide={() => setShowCreateModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Request Leave</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          {createError && (
            <div className="alert alert-danger">{createError}</div>
          )}
          <form onSubmit={handleSubmit}>
            <div className="mb-2">
              <label className="form-label">Title</label>
              <input
                type="text"
                className="form-control"
                name="title"
                value={leaveRequest.title}
                onChange={handleChange}
                required
                disabled={createLoading}
              />
            </div>
            <div className="mb-2">
              <label className="form-label">Start Date</label>
              <input
                type="date"
                className="form-control"
                name="startDate"
                value={leaveRequest.startDate}
                onChange={handleChange}
                required
                disabled={createLoading}
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
                disabled={createLoading}
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
                disabled={createLoading}
              ></textarea>
            </div>
            <div className="d-flex justify-content-between mt-3">
              <Button type="submit" variant="primary" disabled={createLoading}>
                {createLoading ? "Submitting..." : "Submit"}
              </Button>
              <Button
                variant="danger"
                onClick={() => setShowCreateModal(false)}
                disabled={createLoading}
              >
                Cancel
              </Button>
            </div>
          </form>
        </Modal.Body>
      </Modal>
    </div>
  );
};

export default LeaveRequestList;
