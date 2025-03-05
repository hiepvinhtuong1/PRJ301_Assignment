import { useState } from "react";
import { Button, Modal, Table } from "react-bootstrap";
import { FaEdit } from "react-icons/fa";

const LeaveRequestHandle = () => {
  const [leaveRequests, setLeaveRequests] = useState([
    {
      id: 1,
      startDate: "2025-04-01",
      endDate: "2025-04-10",
      status: "Pending",
      comment: "Medical Leave",
      creator: "Jane Smith",
    },
    {
      id: 2,
      startDate: "2025-05-01",
      endDate: "2025-05-05",
      status: "Pending",
      comment: "Family Emergency",
      creator: "John Doe",
    },
  ]);

  const [selectedRequest, setSelectedRequest] = useState(null);
  const [showModal, setShowModal] = useState(false);

  const handleShowModal = (request) => {
    setSelectedRequest(request);
    setShowModal(true);
  };

  const handleUpdateStatus = (newStatus) => {
    setLeaveRequests((prevRequests) =>
      prevRequests.map((req) =>
        req.id === selectedRequest.id ? { ...req, status: newStatus } : req
      )
    );
    setShowModal(false);
  };

  return (
    <div className="container mt-4">
      <h2>Process Leave Requests</h2>
      <Table striped bordered hover>
        <thead>
          <tr>
            <th>#</th>
            <th>Start Date</th>
            <th>End Date</th>
            <th>Status</th>
            <th>Comment</th>
            <th>Creator</th>
            <th>Action</th>
          </tr>
        </thead>
        <tbody>
          {leaveRequests
            .filter((req) => req.status === "Pending")
            .map((request, index) => (
              <tr key={request.id}>
                <td>{index + 1}</td>
                <td>{request.startDate}</td>
                <td>{request.endDate}</td>
                <td>{request.status}</td>
                <td>{request.comment}</td>
                <td>{request.creator}</td>
                <td>
                  <Button
                    variant="warning"
                    size="sm"
                    onClick={() => handleShowModal(request)}
                  >
                    <FaEdit />
                  </Button>
                </td>
              </tr>
            ))}
        </tbody>
      </Table>

      {/* Modal for updating status */}
      <Modal show={showModal} onHide={() => setShowModal(false)}>
        <Modal.Header closeButton>
          <Modal.Title>Update Leave Request Status</Modal.Title>
        </Modal.Header>
        <Modal.Body>
          <p>
            Change the status for {selectedRequest?.creator}'s leave request:
          </p>
          <Button
            variant="success"
            className="me-2"
            onClick={() => handleUpdateStatus("Approved")}
          >
            Approve
          </Button>
          <Button
            variant="danger"
            onClick={() => handleUpdateStatus("Rejected")}
          >
            Reject
          </Button>
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

export default LeaveRequestHandle;
