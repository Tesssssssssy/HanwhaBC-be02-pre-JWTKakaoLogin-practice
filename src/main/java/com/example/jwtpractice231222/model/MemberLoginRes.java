package com.example.jwtpractice231222.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MemberLoginRes {
    Integer id;
    String email;
}
