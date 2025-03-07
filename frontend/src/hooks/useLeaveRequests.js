// src/hooks/useLeaveRequests.js
import { useState, useCallback } from "react";
import axios from "axios";

const useLeaveRequests = () => {
  const [requests, setRequests] = useState([]);
  const [totalPages, setTotalPages] = useState(1);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const fetchLeaveRequests = useCallback(async (filters, page, size) => {
    setLoading(true);
    setError(null);

    // Tạo object params chỉ với các field có giá trị hợp lệ
    const params = {
      page,
      size,
    };
    if (filters.searchText && filters.searchText.trim() !== "") {
      params.searchText = filters.searchText;
    }
    if (filters.startDate && filters.startDate.trim() !== "") {
      params.startDate = filters.startDate;
    }
    if (filters.endDate && filters.endDate.trim() !== "") {
      params.endDate = filters.endDate;
    }
    if (filters.status && filters.status.trim() !== "") {
      params.status = filters.status;
    }

    try {
      const response = await axios.get("http://localhost:8081/hiep/leave-requests", {
        params, // Sử dụng object params đã lọc
        headers: {
          Authorization: `Bearer ${localStorage.getItem("TOKEN")}`,
        },
      });
      console.log(response);
      setRequests(response.data.data.content);
      setTotalPages(response.data.data.totalPages);
    } catch (err) {
      setError("Failed to fetch leave requests");
      console.error(err);
    } finally {
      setLoading(false);
    }
  }, []);

  return { requests, totalPages, loading, error, fetchLeaveRequests };
};

export default useLeaveRequests;