import { useState, useCallback } from "react";
import apiClient from "../utils/apiClient";
import { showErrorToast, showSuccessToast } from "../utils/toastUtils";

const useGetEmployeesOfLeader = () => {
  const [employees, setEmployees] = useState([]); // Danh sách nhân viên
  const [loading, setLoading] = useState(false); // Trạng thái loading
  const [error, setError] = useState(null);     // Trạng thái lỗi

  const fetchEmployeesOfLeader = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await apiClient.get("leave-requests/employees"); // Giả định endpoint để lấy danh sách nhân viên của leader

      const employeesData = response.data.data; // Điều chỉnh theo cấu trúc response của API
      setEmployees(employeesData);
      showSuccessToast("Successfully fetched employees of leader!");
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to get employees of leader";
      setError(errorMessage);
      showErrorToast(errorMessage);
    } finally {
      setLoading(false);
    }
  }, []);

  return { employees, loading, error, fetchEmployeesOfLeader };
};

export default useGetEmployeesOfLeader;