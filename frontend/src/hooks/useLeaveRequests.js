import { useState, useCallback } from "react";
import apiClient from "../utils/apiClient"; // Import apiClient để thay thế axios
import { showErrorToast } from "../utils/toastUtils"; // Import để hiển thị lỗi

const useLeaveRequests = () => {
  const [requests, setRequests] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchLeaveRequests = useCallback(async (filters, page, size) => {
    setLoading(true);
    setError(null);

    // Chỉ thêm các giá trị hợp lệ vào params
    const params = {
      page,
      size,
      ...(filters.searchText?.trim() && { searchText: filters.searchText }),
      ...(filters.startDate?.trim() && { startDate: filters.startDate }),
      ...(filters.endDate?.trim() && { endDate: filters.endDate }),
      ...(filters.status?.trim() && { status: filters.status }),
    };

    try {
      const response = await apiClient.get("/leave-requests", { params });

      setRequests(response.data.data.content);
      setTotalPages(response.data.data.page.totalPages);
      setTotalElements(response.data.data.page.totalElements);
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to fetch leave requests.";
      setError(errorMessage);
      showErrorToast(errorMessage); // Hiển thị toast lỗi
    } finally {
      setLoading(false);
    }
  }, []);

  return { requests, totalPages, totalElements, loading, error, fetchLeaveRequests };
};

export default useLeaveRequests;
