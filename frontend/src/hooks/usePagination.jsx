import { useCallback } from "react";
import { Pagination } from "react-bootstrap";

const usePagination = ({
  pagination,
  totalPages,
  totalElements,
  loading,
  onPageChange,
}) => {
  const renderPaginationItems = useCallback(() => {
    const items = [];
    const maxVisiblePages = 5; // Số trang tối đa hiển thị

    let startPage = Math.max(
      1,
      pagination.current - Math.floor(maxVisiblePages / 2)
    );
    let endPage = Math.min(totalPages, startPage + maxVisiblePages - 1);

    // Điều chỉnh startPage nếu endPage gần cuối
    if (endPage - startPage + 1 < maxVisiblePages) {
      startPage = Math.max(1, endPage - maxVisiblePages + 1);
    }

    // Nút "Previous"
    items.push(
      <Pagination.Prev
        key="prev"
        disabled={pagination.current === 1 || loading}
        onClick={() => onPageChange(pagination.current - 1)}
      />
    );

    // Hiển thị trang đầu tiên
    if (startPage > 1) {
      items.push(
        <Pagination.Item key={1} onClick={() => onPageChange(1)}>
          1
        </Pagination.Item>
      );
      if (startPage > 2) {
        items.push(<Pagination.Ellipsis key="start-ellipsis" />);
      }
    }

    // Hiển thị các trang trong khoảng startPage -> endPage
    for (let page = startPage; page <= endPage; page++) {
      items.push(
        <Pagination.Item
          key={page}
          active={page === pagination.current}
          onClick={() => onPageChange(page)}
          disabled={loading}
        >
          {page}
        </Pagination.Item>
      );
    }

    // Hiển thị trang cuối cùng
    if (endPage < totalPages) {
      if (endPage < totalPages - 1) {
        items.push(<Pagination.Ellipsis key="end-ellipsis" />);
      }
      items.push(
        <Pagination.Item
          key={totalPages}
          onClick={() => onPageChange(totalPages)}
        >
          {totalPages}
        </Pagination.Item>
      );
    }

    // Nút "Next"
    items.push(
      <Pagination.Next
        key="next"
        disabled={pagination.current === totalPages || loading}
        onClick={() => onPageChange(pagination.current + 1)}
      />
    );

    // Hiển thị tổng số bản ghi
    if (totalElements !== undefined) {
      items.push(
        <Pagination.Item key="total" disabled>
          Total: {totalElements} records
        </Pagination.Item>
      );
    }

    return items;
  }, [pagination, totalPages, totalElements, loading, onPageChange]);

  return { renderPaginationItems };
};

export default usePagination;
