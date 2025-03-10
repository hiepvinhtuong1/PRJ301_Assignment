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
import { FaEye, FaEdit, FaTrash, FaPlus } from "react-icons/fa";
import useLeaveRequests from "../../hooks/useLeaveRequests";
import useCreateLeaveRequest from "../../hooks/useCreateLeaveRequest";
import useUpdateRequest from "../../hooks/useUpdateLeaveRequest";
import useDeleteRequest from "../../hooks/useDeleteLeaveRequest";
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

const AddButton = styled(Button)`
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: #3cb371;
  border: none;
  position: fixed;
  bottom: 20px;
  right: 20px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
  &:hover {
    background: #2e8b57;
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

const LeaveRequestList = () => {
  const navigate = useNavigate();
  const location = useLocation();

  const getParamsFromUrl = () => {
    const params = new URLSearchParams(location.search);
    return {
      filters: {
        searchText: params.get("searchText") || "",
        startDate: params.get("startDate") || "",
        endDate: params.get("endDate") || "",
        status: params.get("status") || "",
      },
      pagination: {
        current: parseInt(params.get("page")) || 1,
        pageSize: parseInt(params.get("pageSize")) || 10,
      },
      id: params.get("id") || null,
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
    fetchLeaveRequests,
  } = useLeaveRequests();

  const updateUrl = (newFilters, newPagination, id = null) => {
    const params = new URLSearchParams();
    if (newFilters.searchText) params.set("searchText", newFilters.searchText);
    if (newFilters.startDate) params.set("startDate", newFilters.startDate);
    if (newFilters.endDate) params.set("endDate", newFilters.endDate);
    if (newFilters.status) params.set("status", newFilters.status);
    params.set("page", newPagination.current);
    params.set("pageSize", newPagination.pageSize);
    if (id) params.set("id", id);

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
    fetchLeaveRequests(
      urlFilters,
      urlPagination.current,
      urlPagination.pageSize
    );
  }, [location.search, fetchLeaveRequests]);

  const handleSearch = () => {
    const newPagination = { ...pagination, current: 1 };
    setPagination(newPagination);
    updateUrl(filters, newPagination);
  };

  const handlePageChange = (newPage) => {
    const newPagination = {
      current: newPage,
      pageSize: pagination.pageSize,
    };
    setPagination(newPagination);
    updateUrl(filters, newPagination);
  };

  const [showModal, setShowModal] = useState(false);
  const [modalContent, setModalContent] = useState({ title: "", content: "" });
  const handleShowModal = (title, content) => {
    setModalContent({ title, content });
    setShowModal(true);
  };

  const [showCreateModal, setShowCreateModal] = useState(false);
  const [showUpdateModal, setShowUpdateModal] = useState(false);
  const [editId, setEditId] = useState(null);

  const {
    createLeaveRequest,
    handleChange: handleCreateChange,
    submitCreateLeaveRequest,
    loading: createLoading,
    error: createError,
  } = useCreateLeaveRequest();

  const {
    updateLeaveRequest,
    handleChange: handleUpdateChange,
    updateRequest,
    loading: updateLoading,
    error: updateError,
    success: updateSuccess,
  } = useUpdateRequest();

  const { deleteRequest, loading: deleteLoading } = useDeleteRequest();

  const handleCreateSubmit = () => {
    submitCreateLeaveRequest(() => {
      setShowCreateModal(false);
      const newPagination = { ...pagination, current: 1 };
      setPagination(newPagination);
      updateUrl(filters, newPagination);
      fetchLeaveRequests(filters, 1, newPagination.pageSize);
    });
  };

  const handleUpdateSubmit = () => {
    updateRequest(editId, () => {
      setShowUpdateModal(false);
      const newPagination = { ...pagination, current: 1 };
      setPagination(newPagination);
      updateUrl(filters, newPagination, editId);
      fetchLeaveRequests(filters, 1, newPagination.pageSize);
      setEditId(null);
    });
  };

  const handleEditClick = (request) => {
    setEditId(request?.id);
    updateLeaveRequest.title = request.title;
    updateLeaveRequest.reason = request.reason;
    updateLeaveRequest.startDate = request.startDate;
    updateLeaveRequest.endDate = request.endDate;
    setShowUpdateModal(true);
  };

  const handleDeleteClick = (id) => {
    Modal.confirm({
      title: "Are you sure you want to delete this leave request?",
      onOk: () => {
        deleteRequest(id, () => {
          const newPagination = { ...pagination };
          setPagination(newPagination);
          updateUrl(filters, newPagination);
          fetchLeaveRequests(
            filters,
            pagination.current,
            newPagination.pageSize
          );
        });
      },
    });
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
      title: "Title",
      dataIndex: "title",
      key: "title",
      width: "10%",
      align: "center",
    },
    {
      title: "Start Date",
      dataIndex: "startDate",
      key: "startDate",
      width: "10%",
      align: "center",
    },
    {
      title: "End Date",
      dataIndex: "endDate",
      key: "endDate",
      width: "10%",
      align: "center",
    },
    {
      title: "Status",
      dataIndex: "status",
      key: "status",
      width: "10%",
      align: "center",
    },
    {
      title: "Comment",
      dataIndex: "comment",
      key: "comment",
      width: "15%",
      align: "center",
    },
    {
      title: "Processor",
      dataIndex: "processor",
      key: "processor",
      width: "10%",
      align: "center",
      render: (processor) => processor?.fullName || "N/A",
    },
    {
      title: "Reason",
      key: "reason",
      width: "15%",
      align: "center",
      render: (record) => (
        <Button
          type="link"
          onClick={() => handleShowModal("Reason Details", record.reason)}
        >
          <FaEye /> View Reason
        </Button>
      ),
    },
    {
      title: "Actions",
      key: "actions",
      width: "15%",
      align: "center",
      render: (record) => (
        <Space>
          <Button
            type="primary"
            icon={<FaEdit />}
            onClick={() => handleEditClick(record)}
          />
          <Button
            danger
            icon={<FaTrash />}
            onClick={() => handleDeleteClick(record.id)}
            disabled={deleteLoading}
          />
        </Space>
      ),
    },
  ];

  const tableData = requests.map((request) => ({
    key: request.id,
    id: request.id,
    title: request.title,
    startDate: request.startDate,
    endDate: request.endDate,
    status: request.status,
    comment: request.comment,
    processor: request.processor,
    reason: request.reason,
  }));

  return (
    <StyledContainer>
      <h2>Leave Request List</h2>
      <FilterPanel>
        <Input
          placeholder="Search..."
          value={filters.searchText}
          onChange={(e) =>
            setFilters({ ...filters, searchText: e.target.value })
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
          <Option value="Approved">Approved</Option>
          <Option value="Pending">Pending</Option>
          <Option value="Rejected">Rejected</Option>
        </Select>
        <StyledButton onClick={handleSearch} disabled={loading}>
          {loading ? <Spin size="small" /> : "Search"}
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

      <AddButton
        onClick={() => setShowCreateModal(true)}
        disabled={createLoading}
      >
        <FaPlus size={30} />
      </AddButton>

      <ModalStyled
        title={modalContent.title}
        visible={showModal}
        onCancel={() => setShowModal(false)}
        footer={[
          <Button key="close" onClick={() => setShowModal(false)}>
            Close
          </Button>,
        ]}
      >
        <div
          style={{
            maxHeight: "300px",
            overflowY: "auto",
            wordWrap: "break-word",
            padding: "10px",
            background: "#f9f9f9",
            borderRadius: "5px",
          }}
        >
          {modalContent.content}
        </div>
      </ModalStyled>

      <ModalStyled
        title="Request Leave"
        visible={showCreateModal}
        onCancel={() => setShowCreateModal(false)}
        footer={null}
      >
        <Form onFinish={handleCreateSubmit}>
          <Form.Item
            label="Title"
            name="title"
            rules={[{ required: true, message: "Please enter a title" }]}
          >
            <Input
              name="title"
              value={createLeaveRequest.title}
              onChange={(e) => handleCreateChange({ target: e.target })}
              disabled={createLoading}
            />
          </Form.Item>
          <Form.Item
            label="Start Date"
            name="startDate"
            rules={[{ required: true, message: "Please select a start date" }]}
          >
            <DatePicker
              name="startDate"
              value={
                createLeaveRequest.startDate
                  ? new Date(createLeaveRequest.startDate)
                  : null
              }
              onChange={(date, dateString) =>
                handleCreateChange({
                  target: { name: "startDate", value: dateString },
                })
              }
              style={{ width: "100%" }}
              disabled={createLoading}
            />
          </Form.Item>
          <Form.Item
            label="End Date"
            name="endDate"
            rules={[{ required: true, message: "Please select an end date" }]}
          >
            <DatePicker
              name="endDate"
              value={
                createLeaveRequest.endDate
                  ? new Date(createLeaveRequest.endDate)
                  : null
              }
              onChange={(date, dateString) =>
                handleCreateChange({
                  target: { name: "endDate", value: dateString },
                })
              }
              style={{ width: "100%" }}
              disabled={createLoading}
            />
          </Form.Item>
          <Form.Item
            label="Reason"
            name="reason"
            rules={[{ required: true, message: "Please enter a reason" }]}
          >
            <TextArea
              name="reason"
              value={createLeaveRequest.reason}
              onChange={(e) => handleCreateChange({ target: e.target })}
              disabled={createLoading}
              rows={4}
            />
          </Form.Item>
          <Form.Item>
            <Space>
              <StyledButton
                type="primary"
                htmlType="submit"
                disabled={createLoading}
              >
                {createLoading ? "Submitting..." : "Submit"}
              </StyledButton>
              <Button
                onClick={() => setShowCreateModal(false)}
                disabled={createLoading}
              >
                Cancel
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </ModalStyled>

      <ModalStyled
        title="Edit Leave Request"
        visible={showUpdateModal}
        onCancel={() => setShowUpdateModal(false)}
        footer={null}
      >
        <Form onFinish={handleUpdateSubmit}>
          <Form.Item
            label="Title"
            name="title"
            rules={[{ required: true, message: "Please enter a title" }]}
            initialValue={updateLeaveRequest.title}
          >
            <Input
              name="title"
              value={updateLeaveRequest.title}
              onChange={(e) => handleUpdateChange({ target: e.target })}
              disabled={updateLoading}
            />
          </Form.Item>
          <Form.Item
            label="Start Date"
            name="startDate"
            rules={[{ required: true, message: "Please select a start date" }]}
            initialValue={
              updateLeaveRequest.startDate
                ? new Date(updateLeaveRequest.startDate)
                : null
            }
          >
            <DatePicker
              name="startDate"
              value={
                updateLeaveRequest.startDate
                  ? new Date(updateLeaveRequest.startDate)
                  : null
              }
              onChange={(date, dateString) =>
                handleUpdateChange({
                  target: { name: "startDate", value: dateString },
                })
              }
              style={{ width: "100%" }}
              disabled={updateLoading}
            />
          </Form.Item>
          <Form.Item
            label="End Date"
            name="endDate"
            rules={[{ required: true, message: "Please select an end date" }]}
            initialValue={
              updateLeaveRequest.endDate
                ? new Date(updateLeaveRequest.endDate)
                : null
            }
          >
            <DatePicker
              name="endDate"
              value={
                updateLeaveRequest.endDate
                  ? new Date(updateLeaveRequest.endDate)
                  : null
              }
              onChange={(date, dateString) =>
                handleUpdateChange({
                  target: { name: "endDate", value: dateString },
                })
              }
              style={{ width: "100%" }}
              disabled={updateLoading}
            />
          </Form.Item>
          <Form.Item
            label="Reason"
            name="reason"
            rules={[{ required: true, message: "Please enter a reason" }]}
            initialValue={updateLeaveRequest.reason}
          >
            <TextArea
              name="reason"
              value={updateLeaveRequest.reason}
              onChange={(e) => handleUpdateChange({ target: e.target })}
              disabled={updateLoading}
              rows={4}
            />
          </Form.Item>
          <Form.Item>
            <Space>
              <StyledButton
                type="primary"
                htmlType="submit"
                disabled={updateLoading}
              >
                {updateLoading ? "Updating..." : "Update"}
              </StyledButton>
              <Button
                onClick={() => setShowUpdateModal(false)}
                disabled={updateLoading}
              >
                Cancel
              </Button>
            </Space>
          </Form.Item>
        </Form>
      </ModalStyled>
    </StyledContainer>
  );
};

export default LeaveRequestList;
