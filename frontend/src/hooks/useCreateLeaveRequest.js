import { useState } from "react";
import axios from "axios";

const useCreateLeaveRequest = () => {
  const [leaveRequest, setLeaveRequest] = useState({
    title: "", // Thêm trường title
    startDate: "",
    endDate: "",
    reason: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setLeaveRequest({ ...leaveRequest, [e.target.name]: e.target.value });
  };

  const createLeaveRequest = async (onSuccess) => {
    setLoading(true);
    setError(null);
    try {
      const response = await axios.post(
        "http://localhost:8081/hiep/leave-requests",
        leaveRequest,
        {
          headers: {
            "Content-Type": "application/json",
            Authorization: `Bearer ${localStorage.getItem("TOKEN")}`,
          },
        }
      );

      console.log("Leave request created successfully:", response.data);

      setLeaveRequest({
        title: "", // Reset trường title
        startDate: "",
        endDate: "",
        reason: "",
      });

      if (onSuccess) onSuccess();
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to create leave request. Please try again.";
      setError(errorMessage);
      console.error("Error creating leave request:", err);
    } finally {
      setLoading(false);
    }
  };

  return { leaveRequest, handleChange, createLeaveRequest, loading, error };
};

export default useCreateLeaveRequest;