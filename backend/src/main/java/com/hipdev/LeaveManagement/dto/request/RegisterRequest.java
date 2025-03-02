package com.hipdev.LeaveManagement.dto.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class RegisterRequest {
   private String username;
   private String password;
}
