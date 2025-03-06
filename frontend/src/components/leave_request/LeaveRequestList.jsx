import { useState, useEffect } from "react";
import { Link, useNavigate } from "react-router-dom";
import "bootstrap/dist/css/bootstrap.min.css";
import { FaEye, FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import { Modal, Button } from "react-bootstrap";
import axios from "axios";

const LeaveRequestList = () => {
  const [searchText, setSearchText] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [status, setStatus] = useState("");
  const [showModal, setShowModal] = useState(false);
  const [modalContent, setModalContent] = useState({ title: "", content: "" });
  const [displayedRequests, setDisplayedRequests] = useState([]);
  const [loading, setLoading] = useState(false);

  const navigate = useNavigate();

  const initialRequests = [
    {
      id: 1,
      reason:
        "Vacation with extended details that might be too long to fit in a single row.",
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

  useEffect(() => {
    fetchLeaveRequests();
  }, []);

  // Hàm gọi API để lấy dữ liệu
  const fetchLeaveRequests = async () => {
    setLoading(true);

    // Tạo object params động, chỉ thêm tham số nếu có giá trị
    const params = {
      page: 1,
      size: 10,
    };
    if (searchText) params.searchText = searchText;
    if (startDate) params.startDate = startDate;
    if (endDate) params.endDate = endDate;
    if (status) params.status = status;

    try {
      const response = await axios.get(
        "http://localhost:8081/hiep/leave-requests",
        {
          params, // Truyền params động
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("TOKEN")}`,
          },
        }
      );
      console.log("API Response:", response.data);
      setDisplayedRequests(response.data.data.content || []);
    } catch (error) {
      console.error("Error fetching leave requests:", error);
      setDisplayedRequests(initialRequests);
    } finally {
      setLoading(false);
    }
  };

  // Xử lý khi nhấn nút Search
  const handleSearch = async () => {
    const params = new URLSearchParams();
    if (searchText) params.set("searchText", searchText);
    if (startDate) params.set("startDate", startDate);
    if (endDate) params.set("endDate", endDate);
    if (status) params.set("status", status);
    params.set("page", "1");
    params.set("size", "10");

    navigate(`?${params.toString()}`);

    // Tạo object params động cho API, chỉ thêm tham số nếu có giá trị
    const apiParams = {
      page: 1,
      size: 10,
    };
    if (searchText) apiParams.searchText = searchText;
    if (startDate) apiParams.startDate = startDate;
    if (endDate) apiParams.endDate = endDate;
    if (status) apiParams.status = status;

    setLoading(true);
    try {
      const response = await axios.get(
        "http://localhost:8081/hiep/leave-requests",
        {
          params: apiParams, // Truyền params động
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("TOKEN")}`,
          },
        }
      );
      setDisplayedRequests(response.data.data.content || []);
    } catch (error) {
      console.error("Error fetching leave requests:", error);
      setDisplayedRequests(
        initialRequests.filter((item) => {
          return (
            (searchText === "" ||
              Object.values(item).some((val) =>
                String(val).toLowerCase().includes(searchText.toLowerCase())
              )) &&
            (startDate === "" || item.startDate >= startDate) &&
            (endDate === "" || item.endDate <= endDate) &&
            (status === "" ||
              item.status.toLowerCase() === status.toLowerCase())
          );
        })
      );
    } finally {
      setLoading(false);
    }
  };

  const handleShowModal = (title, content) => {
    setModalContent({ title, content });
    setShowModal(true);
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
            value={searchText}
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
      <div className="row mb-3">
        <div className="col">
          <Button variant="primary" onClick={handleSearch} disabled={loading}>
            {loading ? "Searching..." : "Search"}
          </Button>
        </div>
      </div>
      <table className="table table-striped table-bordered text-wrap">
        <thead className="thead-dark">
          <tr>
            <th style={{ width: "5%" }}>#</th>
            <th style={{ width: "10%" }}>Start Date</th>
            <th style={{ width: "10%" }}>End Date</th>
            <th style={{ width: "10%" }}>Status</th>
            <th style={{ width: "15%" }}>Comment</th>
            <th style={{ width: "10%" }}>Creator</th>
            <th style={{ width: "10%" }}>Processor</th>
            <th style={{ width: "15%" }}>Reason</th>
            <th style={{ width: "15%" }}>Actions</th>
          </tr>
        </thead>
        <tbody>
          {displayedRequests.map((request, index) => (
            <tr key={request.id}>
              <td>{index + 1}</td>
              <td>{request.startDate}</td>
              <td>{request.endDate}</td>
              <td>{request.status}</td>
              <td>{request.comment}</td>
              <td>{request.creator.name}</td>
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

      <div className="position-fixed bottom-0 end-0 p-4">
        <Link
          to="/leave_request/create"
          className="btn btn-success btn-lg rounded-circle d-flex align-items-center justify-content-center shadow"
          style={{ width: "60px", height: "60px" }}
        >
          <FaPlus size={30} />
        </Link>
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
    </div>
  );
};

export default LeaveRequestList;
