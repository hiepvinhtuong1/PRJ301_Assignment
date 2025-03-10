import { useState } from "react";
import apiClient from "../utils/apiClient";
import { showErrorToast, showSuccessToast } from "../utils/toastUtils"; // Import toast utils

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
      const response = await apiClient.post("/leave-requests", createLeaveRequest);

      console.log("Leave request created successfully:", response.data);
      setCreateLeaveRequest({
        title: "",
        startDate: "",
        endDate: "",
        reason: "",
      });

      showSuccessToast("Leave request created successfully! 🎉"); // Hiển thị toast thành công

      if (onSuccess) onSuccess();
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to create leave request. Please try again.";
      setError(errorMessage);
      showErrorToast(errorMessage); // Hiển thị toast lỗi
    } finally {
      setLoading(false);
    }
  };

  return {
    createLeaveRequest,
    handleChange,
    submitCreateLeaveRequest,
    loading,
    error,
  };
};

export default useCreateLeaveRequest;
