import { useState } from "react";
import axios from "axios";

const useCreateLeaveRequest = () => {
  const [createLeaveRequest, setCreateLeaveRequest] = useState({
    title: "",
    startDate: "",
    endDate: "",
    reason: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleChange = (e) => {
    setCreateLeaveRequest({ ...createLeaveRequest, [e.target.name]: e.target.value });
  };

  const submitCreateLeaveRequest = async (onSuccess) => {
    setLoading(true);
    setError(null);
    try {
      console.log("Data before sending:", createLeaveRequest);
      const response = await axios.post(
        "http://localhost:8081/hiep/leave-requests",
        createLeaveRequest,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("TOKEN")}`,
            "Content-Type": "application/json; charset=utf-8",
          },
        }
      );

      console.log("Leave request created successfully:", response.data);
      setCreateLeaveRequest({
        title: "",
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

  const reset = () => {
    setCreateLeaveRequest({
      title: "",
      startDate: "",
      endDate: "",
      reason: "",
    });
    setError(null);
    setLoading(false);
  };

  return {
    createLeaveRequest,
    handleChange,
    submitCreateLeaveRequest, // Đổi tên để tránh trùng
    loading,
    error,
    reset
  };
};

export default useCreateLeaveRequest;