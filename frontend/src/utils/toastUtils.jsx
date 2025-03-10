// src/utils/toastUtils.js
import { toast } from "react-toastify";

/**
 * Hiển thị thông báo lỗi với react-toastify.
 * @param {string} errorMessage - Nội dung lỗi cần hiển thị.
 */
export const showErrorToast = (errorMessage) => {
  toast.error(errorMessage, {
    position: "top-right",
    autoClose: 5000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "colored",
  });
};

/**
 * Hiển thị thông báo thành công với react-toastify.
 * @param {string} successMessage - Nội dung thông báo thành công.
 */
export const showSuccessToast = (successMessage) => {
  toast.success(successMessage, {
    position: "top-right",
    autoClose: 3000,
    hideProgressBar: false,
    closeOnClick: true,
    pauseOnHover: true,
    draggable: true,
    progress: undefined,
    theme: "colored",
  });
};
