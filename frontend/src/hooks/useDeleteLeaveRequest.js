import { useState } from "react";
import apiClient from "../utils/apiClient";
import { showErrorToast, showSuccessToast } from "../utils/toastUtils"; // Import toast utils

const useDeleteRequest = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const deleteRequest = async (id, onSuccess) => {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      console.log(`Deleting leave request with id: ${id}`);
      await apiClient.delete(`/leave-requests/${id}`);

      setSuccess(true);
      showSuccessToast("Leave request deleted successfully! ✅"); // Hiển thị toast thành công

      if (onSuccess) onSuccess();
    } catch (err) {
      console.log("======================================", err)
      const errorMessage =
        err.response?.data?.message ||
        "Failed to delete leave request. Please try again.";
      setError(errorMessage);
      showErrorToast(errorMessage); // Hiển thị toast lỗi
    } finally {
      setLoading(false);
    }
  };

  return { deleteRequest, loading };
};

export default useDeleteRequest;
