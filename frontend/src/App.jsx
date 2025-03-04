import "./App.css";
import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import Login from "./components/login/Login";
import NavBar from "./components/common/NavBar";
import EmployeeProfile from "./components/profile/ProFile.jsx";
import EmployeeList from "./components/employee/EmployeeList.jsx";
import LeaveRequestList from "./components/leaverequest/LeaveRequestList.jsx";
import "../node_modules/bootstrap/dist/css/bootstrap.min.css";
import "../node_modules/bootstrap/dist/js/bootstrap.bundle.min.js";

function App() {
  return (
    <main className="App">
      <BrowserRouter>
        <NavBar />
        <Routes>
          <Route path="/login" element={<Login />}></Route>
          <Route path="/profile" element={<EmployeeProfile />}></Route>
          <Route path="/employee/list" element={<EmployeeList />}></Route>
          <Route
            path="/leave_request/list"
            element={<LeaveRequestList />}
          ></Route>
        </Routes>
      </BrowserRouter>
    </main>
  );
}

export default App;
