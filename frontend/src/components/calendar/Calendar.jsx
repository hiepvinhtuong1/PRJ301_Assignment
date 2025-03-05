import React, { useState } from "react";
import { Table, Select, DatePicker, Button } from "antd";
import dayjs from "dayjs";
import isoWeek from "dayjs/plugin/isoWeek";
dayjs.extend(isoWeek);

const { Option } = Select;
const { RangePicker } = DatePicker;

const employees = ["Mr A", "Mr B", "Mr C", "Mr D"];
const rawData = {
  "Mr A": {
    "1/1": "Đi làm",
    "2/1": "Đi làm",
    "3/1": "Đi làm",
    "4/1": "Đi làm",
    "5/1": "Đi làm",
    "6/1": "Đi làm",
    "7/1": "Đi làm",
    "8/1": "Đi làm",
    "9/1": "Đi làm",
  },
  "Mr B": {
    "1/1": "Đi làm",
    "2/1": "Đi làm",
    "3/1": "Nghỉ",
    "4/1": "Nghỉ",
    "5/1": "Đi làm",
    "6/1": "Đi làm",
    "7/1": "Đi làm",
    "8/1": "Đi làm",
    "9/1": "Đi làm",
  },
  "Mr C": {
    "1/1": "Đi làm",
    "2/1": "Đi làm",
    "3/1": "Đi làm",
    "4/1": "Đi làm",
    "5/1": "Đi làm",
    "6/1": "Nghỉ",
    "7/1": "Đi làm",
    "8/1": "Đi làm",
    "9/1": "Đi làm",
  },
  "Mr D": {
    "1/1": "Đi làm",
    "2/1": "Nghỉ",
    "3/1": "Nghỉ",
    "4/1": "Đi làm",
    "5/1": "Đi làm",
    "6/1": "Đi làm",
    "7/1": "Đi làm",
    "8/1": "Đi làm",
    "9/1": "Đi làm",
  },
};

const aggregateData = (type, startDate, endDate) => {
  let groupedData = {};
  employees.forEach((employee) => {
    groupedData[employee] = {};
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

    employees.forEach((employee) => {
      let workDays = Object.entries(rawData[employee])
        .filter(([date]) => {
          let checkDate = dayjs(date, "D/M");
          return type === "week"
            ? checkDate.isoWeek() === current.isoWeek()
            : type === "month"
            ? checkDate.month() === current.month()
            : type === "year"
            ? checkDate.year() === current.year()
            : checkDate.isSame(current, "day");
        })
        .filter(([_, status]) => status === "Đi làm").length;

      let totalDays =
        type === "week"
          ? 7
          : type === "month"
          ? current.daysInMonth()
          : type === "year"
          ? 365
          : 1;

      groupedData[employee][periodKey] = `${workDays}/${totalDays}`;
    });
    current = current.add(1, type);
  }
  return groupedData;
};

const CalendarAgenda = () => {
  const [viewType, setViewType] = useState("day");
  const [dateRange, setDateRange] = useState([]);
  const [filteredData, setFilteredData] = useState(rawData);

  const handleSearch = () => {
    if (dateRange.length === 2) {
      setFilteredData(
        aggregateData(viewType, dayjs(dateRange[0]), dayjs(dateRange[1]))
      );
    }
  };

  const columns = [
    {
      title: "Nhân sự",
      dataIndex: "name",
      key: "name",
    },
    ...Object.keys(filteredData[employees[0]] || {}).map((date) => ({
      title: date,
      dataIndex: date,
      key: date,
    })),
  ];

  const tableData = employees.map((employee) => {
    return { key: employee, name: employee, ...filteredData[employee] };
  });

  return (
    <div>
      <RangePicker
        onChange={(dates) =>
          setDateRange(dates ? dates.map((date) => date.toISOString()) : [])
        }
      />
      <Select value={viewType} onChange={(value) => setViewType(value)}>
        <Option value="day">Ngày</Option>
        <Option value="week">Tuần</Option>
        <Option value="month">Tháng</Option>
        <Option value="year">Năm</Option>
      </Select>
      <Button onClick={handleSearch}>Tìm kiếm</Button>
      <Table columns={columns} dataSource={tableData} pagination={false} />
    </div>
  );
};

export default CalendarAgenda;
