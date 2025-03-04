import { useState } from "react";
import "bootstrap/dist/css/bootstrap.min.css";

const Calendar = () => {
  const [searchText, setSearchText] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [filteredEmployees, setFilteredEmployees] = useState([]);
  const [daysOfWeek, setDaysOfWeek] = useState([]);

  // Employee data (true = working, false = leave)
  const employees = [
    {
      name: "Mr A",
      status: [true, true, true, true, true, true, true],
    },
    {
      name: "Mr B",
      status: [true, true, false, true, false, true, true],
    },
    {
      name: "Mr C",
      status: [false, true, true, true, true, false, true],
    },
    {
      name: "Mr D",
      status: [true, false, true, true, true, true, false],
    },
    {
      name: "Mr A",
      status: [true, true, true, true, true, true, true],
    },
    {
      name: "Mr B",
      status: [true, true, false, true, false, true, true],
    },
    {
      name: "Mr C",
      status: [false, true, true, true, true, false, true],
    },
    {
      name: "Mr D",
      status: [true, false, true, true, true, true, false],
    },
    {
      name: "Mr A",
      status: [true, true, true, true, true, true, true],
    },
    {
      name: "Mr B",
      status: [true, true, false, true, false, true, true],
    },
    {
      name: "Mr C",
      status: [false, true, true, true, true, false, true],
    },
    {
      name: "Mr D",
      status: [true, false, true, true, true, true, false],
    },
  ];

  // Function to generate days of the week based on start and end date
  const generateDaysOfWeek = (start, end) => {
    const startDate = new Date(start);
    const endDate = new Date(end);
    let days = [];

    while (startDate <= endDate) {
      days.push(
        startDate.toLocaleDateString("en-US", {
          weekday: "short",
          day: "2-digit",
          month: "2-digit",
        })
      );
      startDate.setDate(startDate.getDate() + 1); // Move to the next day
    }

    return days;
  };

  const handleSubmit = () => {
    // Filter employees based on search text
    const filtered = employees.filter((employee) =>
      employee.name.toLowerCase().includes(searchText.toLowerCase())
    );
    setFilteredEmployees(filtered);

    // Generate the days of the week based on selected date range
    if (startDate && endDate) {
      setDaysOfWeek(generateDaysOfWeek(startDate, endDate));
    }
  };

  return (
    <div className="container mt-4">
      <h2>Employee Attendance Calendar</h2>

      {/* Filters for Search, Start Date, and End Date */}
      <div className="row mb-3">
        <div className="col-md-3">
          <input
            type="text"
            className="form-control"
            placeholder="Search by employee name"
            onChange={(e) => setSearchText(e.target.value)}
          />
        </div>
        <div className="col-md-3">
          <input
            type="date"
            className="form-control"
            value={startDate}
            onChange={(e) => setStartDate(e.target.value)}
          />
        </div>
        <div className="col-md-3">
          <input
            type="date"
            className="form-control"
            value={endDate}
            onChange={(e) => setEndDate(e.target.value)}
          />
        </div>
        <div className="col-md-3">
          <button className="btn btn-primary" onClick={handleSubmit}>
            Submit
          </button>
        </div>
      </div>

      {/* Calendar Table (responsive) */}
      <div className="table-responsive">
        <table className="table table-bordered text-center">
          <thead className="thead-dark">
            <tr>
              <th>Nhân sự</th>
              {daysOfWeek.map((day, index) => (
                <th key={index}>{day}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {filteredEmployees.length > 0 ? (
              filteredEmployees.map((employee, rowIndex) => (
                <tr key={rowIndex}>
                  <td>{employee.name}</td>
                  {employee.status
                    .slice(0, daysOfWeek.length)
                    .map((isWorking, colIndex) => (
                      <td
                        key={colIndex}
                        style={{
                          backgroundColor: isWorking ? "green" : "red",
                          color: "white",
                          textAlign: "center",
                        }}
                      ></td> // No text here, only colors
                    ))}
                </tr>
              ))
            ) : (
              <tr>
                <td colSpan={daysOfWeek.length + 1}>No data available</td>
              </tr>
            )}
          </tbody>
        </table>
      </div>
    </div>
  );
};

export default Calendar;
