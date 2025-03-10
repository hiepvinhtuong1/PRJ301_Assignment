import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Login from "./components/login/Login";
import NavBar from "./components/common/NavBar";
import Profile from "./components/profile/ProFile.jsx";
import EmployeeList from "./components/employee/EmployeeList.jsx";
import LeaveRequestList from "./components/leave_request/LeaveRequestList.jsx";
import LeaveRequestUpdate from "./components/leave_request/LeaveReqeustUpdate.jsx";
import LeaveRequestHandle from "./components/leave_request/LeaveRequestHandle.jsx";
import Calendar from "./components/calendar/Calendar.jsx";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../node_modules/bootstrap/dist/js/bootstrap.bundle.min.js";
import { ToastContainer } from "react-toastify";
import "react-toastify/dist/ReactToastify.css";

function App() {
  const isAuthenticated = true;
  return (
    <main className="App">
      <BrowserRouter>
        <ToastContainer />
        {isAuthenticated ? (
          <NavBar />
        ) : // You can render something else or leave it empty if you want nothing shown when not authenticated
        null}
        {/* Rest of your app's content goes here */}
        <Routes>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/profile" element={<Profile />}></Route>
          <Route path="/employee/list" element={<EmployeeList />}></Route>
          <Route
            path="/leave_request/list"
            element={<LeaveRequestList />}
          ></Route>
          s
          <Route
            path="/leave_request/handle"
            element={<LeaveRequestHandle />}
          ></Route>
          <Route
            path="/leave_request/update/:id"
            element={<LeaveRequestUpdate />}
          ></Route>
          <Route path="/calendar/view" element={<Calendar />}></Route>
        </Routes>
      </BrowserRouter>
    </main>
  );
}

export default App;
