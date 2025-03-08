import { useState } from "react";
import axios from "axios";

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
      await axios.delete(`http://localhost:8081/hiep/leave-requests/${id}`, {
        headers: {
          Authorization: `Bearer ${localStorage.getItem("TOKEN")}`,
          "Content-Type": "application/json; charset=utf-8",
        },
      });

      setSuccess(true);

      if (onSuccess) onSuccess();
    } catch (err) {
      const errorMessage =
        err.response?.data?.message ||
        "Failed to delete leave request. Please try again.";
      setError(errorMessage);
      console.error("Error deleting leave request:", err);
    } finally {
      setLoading(false);
    }
  };

  const reset = () => {
    setLoading(false);
    setError(null);
    setSuccess(false);
  };

  return { deleteRequest, loading, error, success, reset };
};

export default useDeleteRequest;