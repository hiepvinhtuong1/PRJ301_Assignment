import { useState, useEffect } from "react";
import { Link, useNavigate, useLocation } from "react-router-dom";
import {
  Table,
  Input,
  DatePicker,
  Select,
  Button,
  Modal,
  Form,
  Space,
  Spin,
  Pagination,
} from "antd";
import { FaEdit } from "react-icons/fa";
import useEmployeeRequests from "../../hooks/useEmployeeRequests";
import useProcessRequest from "../../hooks/useProcessRequest";
import usePagination from "../../hooks/usePagination";
import styled from "styled-components";

const { Option } = Select;
const { TextArea } = Input;

const StyledContainer = styled.div`
  padding: 24px;
  background: #f5f5f5;
  min-height: 100vh;

  h2 {
    color: #2e8b57;
    font-weight: 600;
    margin-bottom: 24px;
    text-align: center;
  }
`;

const StyledTable = styled(Table)`
  .ant-table-thead > tr > th {
    background: #2e8b57;
    color: white;
    font-weight: 600;
    border-bottom: 2px solid #cccccc;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }

  .ant-table-tbody > tr:nth-child(even) {
    background-color: #ffffff;
  }

  .ant-table-tbody > tr:nth-child(odd) {
    background-color: #e0e0e0;
  }

  .ant-table-tbody > tr:hover > td {
    background: #f0f0f0;
  }

  .ant-table-cell {
    padding: 12px !important;
    white-space: nowrap;
    text-overflow: ellipsis;
    overflow: hidden;
  }
`;

const FilterPanel = styled.div`
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  gap: 16px;
  margin-bottom: 24px;
  padding: 16px;
  background: #f0f0f0;
  border-radius: 8px;
  border: 1px solid #cccccc;
`;

const StyledButton = styled(Button)`
  padding: 0 24px;
  height: 40px;
  border-radius: 6px;
  font-weight: 500;
  background: #3cb371;
  border-color: #3cb371;
  color: white;

  &:hover {
    background: #2e8b57 !important;
    border-color: #2e8b57 !important;
  }

  &:disabled {
    background: #a9a9a9;
    border-color: #a9a9a9;
    color: #ffffff;
  }
`;

const ModalStyled = styled(Modal)`
  .ant-modal-content {
    border-radius: 10px;
    border: 1px solid #cccccc;
  }
  .ant-modal-header {
    background: #2e8b57;
    color: white;
    border-bottom: 1px solid #cccccc;
  }
  .ant-modal-body {
    padding: 20px;
  }
  .ant-form-item-label > label {
    font-weight: 500;
    color: #333333;
  }
  .ant-input,
  .ant-input-textarea {
    border-radius: 6px;
    border-color: #2e8b57;
  }
`;

const LeaveRequestHandle = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const getParamsFromUrl = () => {
    const params = new URLSearchParams(location.search);
    return {
      filters: {
        employeeName: params.get("employeeName") || "",
        startDate: params.get("startDate") || "",
        endDate: params.get("endDate") || "",
        status: params.get("status") || "",
      },
      pagination: {
        current: parseInt(params.get("page")) || 1,
        pageSize: parseInt(params.get("pageSize")) || 10,
      },
    };
  };

  const [filters, setFilters] = useState(getParamsFromUrl().filters);
  const [pagination, setPagination] = useState(getParamsFromUrl().pagination);
  const {
    requests,
    totalPages,
    totalElements,
    loading,
    error,
    fetchEmployeeRequests,
  } = useEmployeeRequests();

  const {
    processRequest,
    loading: processLoading,
    error: processError,
    success: processSuccess,
    reset: resetProcess,
  } = useProcessRequest();

  const updateUrl = (newFilters, newPagination) => {
    const params = new URLSearchParams();
    if (newFilters.employeeName)
      params.set("employeeName", newFilters.employeeName);
    if (newFilters.startDate) params.set("startDate", newFilters.startDate);
    if (newFilters.endDate) params.set("endDate", newFilters.endDate);
    if (newFilters.status) params.set("status", newFilters.status);
    params.set("page", newPagination.current);
    params.set("pageSize", newPagination.pageSize);

    navigate({
      pathname: location.pathname,
      search: params.toString(),
    });
  };

  useEffect(() => {
    const { filters: urlFilters, pagination: urlPagination } =
      getParamsFromUrl();
    setFilters(urlFilters);
    setPagination(urlPagination);
    fetchEmployeeRequests(
      urlPagination.current,
      urlPagination.pageSize,
      urlFilters
    );
  }, [location.search, fetchEmployeeRequests]);

  const handleSearch = () => {
    const newPagination = { ...pagination, current: 1 };
    setPagination(newPagination);
    updateUrl(filters, newPagination);
  };

  const handlePageChange = (newPage) => {
    const newPagination = { ...pagination, current: newPage };
    setPagination(newPagination);
    updateUrl(filters, newPagination);
  };

  const [showModal, setShowModal] = useState(false);
  const [selectedRequest, setSelectedRequest] = useState(null);
  const [comment, setComment] = useState("");

  const handleShowModal = (request) => {
    setSelectedRequest(request);
    setComment("");
    setShowModal(true);
  };

  const handleProcessRequest = async (newStatus) => {
    if (selectedRequest) {
      try {
        await processRequest(selectedRequest.id, newStatus, comment, () => {
          fetchEmployeeRequests(
            pagination.current,
            pagination.pageSize,
            filters
          );
          setShowModal(false);
          setComment("");
          resetProcess();
        });
      } catch (err) {
        // Lỗi đã được hiển thị bằng alert trong hook
      }
    }
  };

  const { renderPaginationItems } = usePagination({
    pagination,
    totalPages,
    totalElements,
    loading,
    onPageChange: handlePageChange,
  });

  const columns = [
    {
      title: "#",
      dataIndex: "index",
      key: "index",
      width: "5%",
      align: "center",
      render: (text, record, index) =>
        (pagination.current - 1) * pagination.pageSize + index + 1,
    },
    {
      title: "Start Date",
      dataIndex: "startDate",
      key: "startDate",
      width: "15%",
      align: "center",
    },
    {
      title: "End Date",
      dataIndex: "endDate",
      key: "endDate",
      width: "15%",
      align: "center",
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      width: "15%",
      align: "center",
    },
    {
      title: "Comment",
      dataIndex: "comment",
      key: "comment",
      width: "20%",
      align: "center",
    },
    {
      title: "Creator",
      dataIndex: "creator",
      key: "creator",
      width: "15%",
      align: "center",
      render: (creator) => creator?.fullName || "N/A",
    },
    {
      title: "Action",
      key: "action",
      width: "15%",
      align: "center",
      render: (record) => (
        <Button
          type="primary"
          icon={<FaEdit />}
          onClick={() => handleShowModal(record)}
          disabled={processLoading}
        />
      ),
    },
  ];

  const tableData = requests.map((request) => ({
    key: request.id,
    id: request.id,
    startDate: request.startDate,
    endDate: request.endDate,
    status: request.status,
    comment: request.comment,
    creator: request.creator,
  }));

  return (
    <StyledContainer>
      <h2>Process Leave Requests</h2>
      <FilterPanel>
        <Input
          placeholder="Filter by Employee Name"
          value={filters.employeeName}
          onChange={(e) =>
            setFilters({ ...filters, employeeName: e.target.value })
          }
          style={{
            width: "200px",
            borderRadius: "6px",
            borderColor: "#2e8b57",
          }}
        />
        <DatePicker
          value={filters.startDate ? new Date(filters.startDate) : null}
          onChange={(date, dateString) =>
            setFilters({ ...filters, startDate: dateString })
          }
          style={{
            width: "200px",
            borderRadius: "6px",
            borderColor: "#2e8b57",
          }}
        />
        <DatePicker
          value={filters.endDate ? new Date(filters.endDate) : null}
          onChange={(date, dateString) =>
            setFilters({ ...filters, endDate: dateString })
          }
          style={{
            width: "200px",
            borderRadius: "6px",
            borderColor: "#2e8b57",
          }}
        />
        <Select
          value={filters.status}
          onChange={(value) => setFilters({ ...filters, status: value })}
          style={{
            width: "200px",
            borderRadius: "6px",
            borderColor: "#2e8b57",
          }}
        >
          <Option value="">All Status</Option>
          <Option value="Pending">Pending</Option>
          <Option value="Approved">Approved</Option>
          <Option value="Rejected">Rejected</Option>
        </Select>
        <StyledButton onClick={handleSearch} disabled={loading}>
          {loading ? <Spin size="small" /> : "Filter"}
        </StyledButton>
      </FilterPanel>
      {error && (
        <p
          style={{
            color: "#ff4500",
            textAlign: "center",
            marginBottom: "16px",
          }}
        >
          Lỗi: {error}
        </p>
      )}
      {processSuccess && (
        <p
          style={{
            color: "#2e8b57",
            textAlign: "center",
            marginBottom: "16px",
            background: "#e0ffe0",
            padding: "10px",
            borderRadius: "5px",
          }}
        >
          Request processed successfully!
        </p>
      )}
      <StyledTable
        columns={columns}
        dataSource={tableData}
        pagination={false}
        scroll={{ x: "max-content", y: "calc(100vh - 300px)" }}
        loading={loading}
        bordered
      />
      <div className="d-flex justify-content-center mt-3">
        <Pagination>{renderPaginationItems()}</Pagination>
      </div>

      <ModalStyled
        title="Update Leave Request Status"
        visible={showModal}
        onCancel={() => setShowModal(false)}
        footer={null}
      >
        <Form layout="vertical">
          <p>
            Change the status for {selectedRequest?.creator?.fullName || "N/A"}
            's leave request:
          </p>
          <Form.Item label="Comment (Optional)">
            <TextArea
              rows={3}
              value={comment}
              onChange={(e) => setComment(e.target.value)}
              placeholder="Enter your comment here..."
              disabled={processLoading}
            />
          </Form.Item>
          <Form.Item>
            <Space>
              <StyledButton
                type="primary"
                onClick={() => handleProcessRequest("Approved")}
                disabled={processLoading}
              >
                {processLoading ? <Spin size="small" /> : "Approve"}
              </StyledButton>
              <StyledButton
                danger
                onClick={() => handleProcessRequest("Rejected")}
                disabled={processLoading}
              >
                {processLoading ? <Spin size="small" /> : "Reject"}
              </StyledButton>
            </Space>
          </Form.Item>
        </Form>
      </ModalStyled>
    </StyledContainer>
  );
};

export default LeaveRequestHandle;
