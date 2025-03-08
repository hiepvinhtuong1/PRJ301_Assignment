    package com.hipdev.LeaveManagement.dto.request.leave_request;


    import com.fasterxml.jackson.annotation.JsonInclude;
    import lombok.Data;

    import java.time.LocalDate;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public class UpdateLeaveRequest {
        private String title;
        private String reason;
        private LocalDate startDate;
        private LocalDate endDate;
        private String status;
    }

