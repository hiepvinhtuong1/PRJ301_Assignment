import { useState, useCallback } from "react";
import apiClient from "../utils/apiClient"; // Import apiClient để thay thế axios
import { showErrorToast } from "../utils/toastUtils"; // Import để hiển thị lỗi

const useProfile = () => {
  const [profile, setProfile] = useState(null); // Lưu thông tin profile của người dùng
  const [loading, setLoading] = useState(false); // Trạng thái loading
  const [error, setError] = useState(null); // Trạng thái lỗi

  // Hàm fetch thông tin profile
  const fetchProfile = useCallback(async () => {
    setLoading(true);
    setError(null);

    try {
      const response = await apiClient.get("/users/my-info");

      console.log("==================", response)
      // Giả định response.data.data chứa thông tin profile
      setProfile(response.data.data);
    } catch (err) {
      const errorMessage = err.response?.data?.message || "Failed to fetch user profile.";
      setError(errorMessage);
      showErrorToast(errorMessage); // Hiển thị toast lỗi
    } finally {
      setLoading(false);
    }
  }, []); // Dependency array rỗng để chỉ tạo function một lần

  return { profile, loading, error, fetchProfile };
};

export default useProfile;