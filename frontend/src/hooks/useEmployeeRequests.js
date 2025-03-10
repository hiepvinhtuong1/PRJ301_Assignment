import { useState, useCallback } from "react";
import apiClient from "../utils/apiClient"; // Import apiClient để thay thế axios
import { showErrorToast } from "../utils/toastUtils"; // Import để hiển thị lỗi

const useEmployeeRequests = () => {
  const [requests, setRequests] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [totalElements, setTotalElements] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchEmployeeRequests = useCallback(async (page, pageSize, filters = {}) => {
    setLoading(true);
    setError(null);

    try {
      const params = {
        page,
        size: pageSize,
        fullName: filters.employeeName || "",
        startDate: filters.startDate || "",
        endDate: filters.endDate || "",
        status: filters.status || "",
      };

      const response = await apiClient.get("/leave-requests/req-of-emp", { params });

      setRequests(response.data.data.content);
      setTotalPages(response.data.data.page.totalPages);
      setTotalElements(response.data.data.page.totalElements);
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to fetch employee requests.";
      setError(errorMessage);
      showErrorToast(errorMessage); // Hiển thị toast lỗi
    } finally {
      setLoading(false);
    }
  }, []);

  return { requests, totalPages, totalElements, loading, error, fetchEmployeeRequests };
};

export default useEmployeeRequests;
