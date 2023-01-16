package com.yuechu.dto;

import com.yuechu.entity.User;
import lombok.Data;

@Data
public class UserDto extends User {
    private String code;
}
