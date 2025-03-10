import { useState } from "react";
import apiClient from "../utils/apiClient"; // Import apiClient để thay thế axios
import { showErrorToast, showSuccessToast } from "../utils/toastUtils"; // Import toast utils

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
      const response = await apiClient.put(`/leave-requests/${id}`, updateLeaveRequest);

      setSuccess(true);
      showSuccessToast("Leave request updated successfully!");

      if (onSuccess) onSuccess(response.data);
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to update leave request. Please try again.";
      setError(errorMessage);
      showErrorToast(errorMessage);
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

  return { updateLeaveRequest, handleChange, updateRequest, loading, error, success, reset, setUpdateLeaveRequest };
};

export default useUpdateRequest;
