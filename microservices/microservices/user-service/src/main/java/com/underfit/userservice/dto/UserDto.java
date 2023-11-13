package com.underfit.userservice.dto;

import com.underfit.userservice.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * DTO for {@link com.underfit.userservice.entity.User}
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserDto implements Serializable {
    private Long id;
    private String name;
    private String login;
    private String password;
    private String email;
    private Boolean enabled;
    private Long companyId;
    private String companyName;

    public User toEntity() {
        User user = new User();
        BeanUtils.copyProperties(this, user);
        return user;
    }

    public static UserDto toDto(User user) {
        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        return userDto;
    }
}