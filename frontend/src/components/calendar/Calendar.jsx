import React, { useState, useEffect } from "react";
import {
  Table,
  Select,
  DatePicker,
  Button,
  Space,
  Card,
  Spin,
  Empty,
} from "antd";
import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek";
import useGetCalendar from "../../hooks/useGetCalendar";
import styled from "styled-components";
dayjs.extend(isoWeek);

const { Option } = Select;
const { RangePicker } = DatePicker;

// Styled components với tông màu mới dựa trên timesheet
const StyledCard = styled(Card)`
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
  background: #ffffff;
  overflow: hidden;
  border: 1px solid #cccccc;
  height: calc(100vh - 80px); // Trừ chiều cao NavBar (56px) và padding (24px)

  .ant-card-body {
    height: calc(100% - 60px); // Trừ chiều cao của header (khoảng 60px)
    overflow: auto; // Cho phép cuộn nếu nội dung vượt quá
    padding: 0; // Xóa padding mặc định để tối ưu không gian
  }
`;

const HeaderTitle = styled.h2`
  color: #2e8b57;
  font-weight: 600;
  margin-bottom: 24px;
  text-align: center;
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
    max-width: 100px;
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
    max-width: 100px;
  }
`;

const ControlPanel = styled(Space)`
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

// Hàm tổng hợp dữ liệu với trạng thái Absent/Attend
const aggregateData = (type, startDate, endDate, calendarData) => {
  let groupedData = {};
  calendarData.forEach((employee) => {
    groupedData[employee.employeeName] = {};
  });

  let current = dayjs(startDate);
  while (current.isBefore(endDate) || current.isSame(endDate, type)) {
    let periodKey =
      type === "week"
        ? `Tuần ${current.isoWeek()}`
        : type === "month"
        ? `Tháng ${current.month() + 1}`
        : type === "year"
        ? `Năm ${current.year()}`
        : current.format("DD/MM");

    calendarData.forEach((employee) => {
      let workDays = employee.leaveDays
        .map((leaveDay) => dayjs(leaveDay, "YYYY-MM-DD"))
        .filter((leaveDay) => {
          return type === "week"
            ? leaveDay.isoWeek() === current.isoWeek()
            : type === "month"
            ? leaveDay.month() === current.month()
            : type === "year"
            ? leaveDay.year() === current.year()
            : leaveDay.isSame(current, "day");
        }).length;

      let totalDays =
        type === "week"
          ? 7
          : type === "month"
          ? current.daysInMonth()
          : type === "year"
          ? 365
          : 1;

      let workingDays = totalDays - workDays;
      groupedData[employee.employeeName][periodKey] = (
        <span>
          {workDays === totalDays ? (
            <span
              style={{
                color: "#ff4500",
                fontSize: "16px",
                fontWeight: "bold",
              }}
            >
              Absent
            </span>
          ) : workDays === 0 ? (
            <span
              style={{
                color: "#2e8b57",
                fontSize: "16px",
                fontWeight: "bold",
              }}
            >
              Attend
            </span>
          ) : (
            <>
              <span style={{ color: "#2e8b57" }}>{workingDays}</span>
              <span style={{ color: "#666666" }}>/{totalDays}</span>
            </>
          )}
        </span>
      );
    });
    current = current.add(1, type);
  }
  return groupedData;
};

const CalendarAgenda = () => {
  const [viewType, setViewType] = useState("day");
  const [dateRange, setDateRange] = useState([]);
  const [filteredData, setFilteredData] = useState({});

  const { calendarData, loading, error, fetchCalendar } = useGetCalendar();

  useEffect(() => {
    if (dateRange.length === 2) {
      fetchCalendar(dayjs(dateRange[0]), dayjs(dateRange[1]));
    }
  }, [dateRange, fetchCalendar]);

  useEffect(() => {
    if (dateRange.length === 2 && calendarData.length > 0) {
      const aggregatedData = aggregateData(
        viewType,
        dayjs(dateRange[0]),
        dayjs(dateRange[1]),
        calendarData
      );
      setFilteredData(aggregatedData);
    }
  }, [viewType, dateRange, calendarData]);

  const columns = [
    {
      title: "Nhân sự",
      dataIndex: "name",
      key: "name",
      width: 120,
      align: "center",
      fixed: "left",
      render: (text) => (
        <span style={{ fontWeight: 500, color: "#333333" }}>{text}</span>
      ),
    },
    ...Object.keys(filteredData[calendarData[0]?.employeeName] || {}).map(
      (date) => ({
        title: (
          <span
            style={{
              maxWidth: "100px",
              display: "block",
              overflow: "hidden",
              textOverflow: "ellipsis",
              color: "#333333",
            }}
          >
            {date.length > 10 ? `${date.substring(0, 10)}...` : date}
          </span>
        ),
        dataIndex: date,
        key: date,
        width: 100,
        align: "center",
      })
    ),
  ];

  const tableData = calendarData.map((employee) => ({
    key: employee.id,
    name: employee.employeeName,
    ...filteredData[employee.employeeName],
  }));

  return (
    <div style={{ padding: "24px", background: "#f5f5f5", minHeight: "100vh" }}>
      <StyledCard>
        <HeaderTitle>Quản lý chấm công</HeaderTitle>

        <ControlPanel>
          <RangePicker
            onChange={(dates) =>
              setDateRange(dates ? dates.map((date) => date.toISOString()) : [])
            }
            size="large"
            style={{
              width: "300px",
              borderRadius: "6px",
              borderColor: "#2e8b57",
            }}
            placeholder={["Ngày bắt đầu", "Ngày kết thúc"]}
          />
          <Select
            value={viewType}
            onChange={(value) => setViewType(value)}
            size="large"
            style={{
              width: "150px",
              borderRadius: "6px",
              borderColor: "#2e8b57",
            }}
          >
            <Option value="day">Theo ngày</Option>
            <Option value="week">Theo tuần</Option>
            <Option value="month">Theo tháng</Option>
            <Option value="year">Theo năm</Option>
          </Select>
          <StyledButton
            type="primary"
            size="large"
            onClick={() => {
              if (dateRange.length === 2)
                fetchCalendar(dayjs(dateRange[0]), dayjs(dateRange[1]));
            }}
            disabled={loading || dateRange.length !== 2}
            icon={loading ? <Spin size="small" /> : null}
          >
            {loading ? "Đang tải..." : "Tìm kiếm"}
          </StyledButton>
        </ControlPanel>

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

        {calendarData.length === 0 && !loading && !error && (
          <Empty
            description="Không có dữ liệu chấm công"
            style={{ margin: "40px 0", color: "#666666" }}
          />
        )}

        {calendarData.length > 0 && (
          <StyledTable
            columns={columns}
            dataSource={tableData}
            pagination={{
              pageSize: 10,
              showSizeChanger: true,
              pageSizeOptions: ["10", "20", "50"],
            }}
            scroll={{ x: "max-content", y: "calc(100vh - 300px)" }} // Điều chỉnh chiều cao của bảng
            loading={loading}
            bordered
          />
        )}
      </StyledCard>
    </div>
  );
};

export default CalendarAgenda;
