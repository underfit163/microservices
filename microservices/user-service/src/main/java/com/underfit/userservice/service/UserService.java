package com.underfit.userservice.service;

import com.underfit.userservice.dto.ResponseUserDto;
import com.underfit.userservice.dto.UserDto;
import com.underfit.userservice.entity.User;
import com.underfit.userservice.feignclient.CompanyServiceFeignClient;
import com.underfit.userservice.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final CompanyServiceFeignClient companyServiceFeignClient;

    public List<UserDto> getAllUsers() {
        return userRepository
                .findAll()
                .stream()
                .map(UserDto::toDto)
                .peek(userDto -> {
                    if (userDto.getCompanyId() != null)
                        userDto.setCompanyName(Objects.requireNonNull(companyServiceFeignClient
                                .getCompanyById(userDto.getCompanyId()).getBody()).getCompanyName());
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public Long createUser(UserDto userDto) {
        if (userDto.getCompanyId() != null) {
            Boolean isExist = companyServiceFeignClient.existCompanyById(userDto.getCompanyId()).getBody();
            if (Boolean.FALSE.equals(isExist)) {
                throw new EntityNotFoundException(
                        "Компании с идентификатором %s не существует".formatted(userDto.getCompanyId())
                );
            }
        }
        userDto.setEnabled(true);
        User user = userRepository.save(userDto.toEntity());
        log.info("Create user: " + userDto);
        return user.getId();
    }

    @Transactional
    public void updateEnabledUser(Long id, Boolean enabled) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Пользователя с идентификатором %s не существует"
                        .formatted(id)));
        user.setEnabled(enabled);
        userRepository.save(user);
        log.info("Status user %s changed: %s".formatted(id, enabled));
    }

    @Transactional
    public UserDto updateUser(UserDto userDto) {
        Boolean isExist = companyServiceFeignClient.existCompanyById(userDto.getCompanyId()).getBody();
        if (Boolean.FALSE.equals(isExist)) {
            throw new EntityNotFoundException(
                    "Компании с идентификатором %s не существует".formatted(userDto.getCompanyId())
            );
        }
        User user = userRepository.findById(userDto.getId()).orElseThrow(
                () -> new EntityNotFoundException("Пользователя с идентификатором %s не существует"
                        .formatted(userDto.getId())));
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new EntityNotFoundException(
                    "Пользователя с идентификатором %s деактивирован".formatted(user.getId()));
        }
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setCompanyId(userDto.getCompanyId());

        userRepository.save(user);

        BeanUtils.copyProperties(user, userDto);
        userDto.setCompanyName(Objects.requireNonNull(companyServiceFeignClient
                .getCompanyById(userDto.getCompanyId()).getBody()).getCompanyName());
        log.info("Update user: " + userDto);
        return userDto;
    }

    public Boolean existUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Пользователя с идентификатором %s не существует"
                        .formatted(id)));
        if (Boolean.FALSE.equals(user.getEnabled())) {
            throw new EntityNotFoundException(
                    "Пользователя с идентификатором %s деактивирован".formatted(id));
        }
        return true;
    }

    public ResponseUserDto getUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Пользователя с идентификатором %s не существует"
                        .formatted(id)));
        if (Boolean.FALSE.equals(user.getEnabled()))
            throw new EntityNotFoundException(
                    "Пользователя с идентификатором %s деактивирован".formatted(id));
        ResponseUserDto responseUserDto = new ResponseUserDto();
        responseUserDto.setUserId(user.getId());
        responseUserDto.setUserName(user.getName());
        return responseUserDto;
    }
}