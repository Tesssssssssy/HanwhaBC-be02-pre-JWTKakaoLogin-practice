package com.example.jwtpractice231222.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberLoginReq {
    String email;
    String password;
}
