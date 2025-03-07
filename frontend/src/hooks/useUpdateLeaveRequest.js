import { useState } from "react";
import axios from "axios";

const useUpdateRequest = () => {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);
  const [success, setSuccess] = useState(false); // Thêm trạng thái thành công

  const updateRequest = async (id, updatedData, onSuccess) => {
    setLoading(true);
    setError(null);
    setSuccess(false);

    try {
      console.log("Data before updating:", updatedData);
      const response = await axios.put(
        `http://localhost:8081/hiep/leave-requests/${id}`, // Endpoint cập nhật
        updatedData,
        {
          headers: {
            "Content-Type": "application/json; charset=utf-8",
          },
        }
      );

      console.log("Leave request updated successfully:", response.data);
      setSuccess(true);

      if (onSuccess) onSuccess(response.data); // Gọi callback với dữ liệu trả về (nếu có)
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to update leave request. Please try again.";
      setError(errorMessage);
      console.error("Error updating leave request:", err);
    } finally {
      setLoading(false);
    }
  };

  // Reset trạng thái (nếu cần)
  const reset = () => {
    setLoading(false);
    setError(null);
    setSuccess(false);
  };

  return { loading, error, success, updateRequest, reset };
};

export default useUpdateRequest;