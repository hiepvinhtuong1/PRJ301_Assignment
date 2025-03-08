import { useState } from "react";
import axios from "axios";

const useUpdateRequest = () => {
  const [updateLeaveRequest, setUpdateLeaveRequest] = useState({
    title: "",
    startDate: "",
    endDate: "",
    reason: "",
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const handleChange = (e) => {
    setUpdateLeaveRequest({ ...updateLeaveRequest, [e.target.name]: e.target.value });
  };

  const updateRequest = async (id, onSuccess) => {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      console.log("Data before updating:", updateLeaveRequest);
      const response = await axios.put(
        `http://localhost:8081/hiep/leave-requests/${id}`,
        updateLeaveRequest,
        {
          headers: {
            Authorization: `Bearer ${localStorage.getItem("TOKEN")}`,
            "Content-Type": "application/json; charset=utf-8",
          },
        }
      );

      setSuccess(true);

      if (onSuccess) onSuccess(response.data);
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to update leave request. Please try again.";
      setError(errorMessage);
      console.error("Error updating leave request:", err);
    } finally {
      setLoading(false);
    }
  };

  const reset = () => {
    setUpdateLeaveRequest({
      title: "",
      startDate: "",
      endDate: "",
      reason: "",
    });
    setLoading(false);
    setError(null);
    setSuccess(false);
  };

  return { updateLeaveRequest, handleChange, updateRequest, loading, error, success, reset };
};

export default useUpdateRequest;