import { useState, useCallback } from "react";
import apiClient from "../utils/apiClient"; // Import apiClient để gọi API
import { showErrorToast } from "../utils/toastUtils"; // Import để hiển thị lỗi

const useGetCalendar = () => {
  const [calendarData, setCalendarData] = useState([]); // Lưu danh sách CalendarResponse
  const [loading, setLoading] = useState(false); // Trạng thái loading
  const [error, setError] = useState(null); // Trạng thái lỗi

  // Hàm fetch dữ liệu từ API
  const fetchCalendar = useCallback(async (startDate, endDate) => {
    setLoading(true);
    setError(null);

    try {
      const response = await apiClient.get("/leave-requests/calendar", {
        params: {
          startDate: startDate ? startDate.format("YYYY-MM-DD") : null,
          endDate: endDate ? endDate.format("YYYY-MM-DD") : null,
        },
      });
      console.log("hello: ", response)
      setCalendarData(response.data.data); // Lưu dữ liệu từ API
      // showSuccessToast("Successfully fetched calendar data!"); // Tùy chọn hiển thị toast thành công
    } catch (err) {
      const errorMessage =
        err.response?.data?.message || "Failed to fetch calendar data";
      setError(errorMessage);
      showErrorToast(errorMessage);
    } finally {
      setLoading(false);
    }
  }, []);

  // Hàm reset trạng thái
  const reset = () => {
    setCalendarData([]);
    setError(null);
  };

  return { calendarData, loading, error, fetchCalendar, reset };
};

export default useGetCalendar;