import { useState, useCallback } from "react";
import apiClient from "../utils/apiClient"; // Import apiClient để thay thế axios
import { showErrorToast, showSuccessToast } from "../utils/toastUtils"; // Import toast utils

const useProcessRequest = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false);

  const processRequest = useCallback(async (requestId, newStatus, comment, onSuccess) => {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      const requestBody = {
        status: newStatus,
        comment: comment || null,
      };

      const response = await apiClient.post(`/leave-requests/process/${requestId}`, requestBody);

      setSuccess(true);
      showSuccessToast("Request processed successfully!");

      if (onSuccess) onSuccess();
      return response.data;
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to process request";
      setError(errorMessage);
      showErrorToast(errorMessage);
      throw err;
    } finally {
      setLoading(false);
    }
  }, []);

  const reset = () => {
    setError(null);
    setSuccess(false);
  };

  return { processRequest, loading, error, success, reset };
};

export default useProcessRequest;
