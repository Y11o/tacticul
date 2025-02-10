package ru.spb.tacticul.service;

import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.spb.tacticul.dto.UserCreateDTO;
import ru.spb.tacticul.dto.UserDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.UserMapper;
import ru.spb.tacticul.model.User;
import ru.spb.tacticul.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;
    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() {
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::userToUserDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
    }

    @Transactional
    public UserDTO create(UserCreateDTO userCreateDTO) {
        log.info("Создание нового пользователя: {}", userCreateDTO.login());

        if (userRepository.findByLogin(userCreateDTO.login()).isPresent()) {
            throw new IllegalArgumentException("Пользователь с таким логином уже существует");
        }

        User user = userMapper.userCreateDTOToUser(userCreateDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        validateUser(user);
        user = userRepository.save(user);
        log.info("Пользователь {} успешно создан", user.getLogin());

        return userMapper.userToUserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO userDTO) {
        log.info("Обновление пользователя с ID: {}", id);

        return userRepository.findById(id)
                .map(existingUser -> {
                    if (userDTO.login() != null && !userDTO.login().isEmpty()) {
                        existingUser.setLogin(userDTO.login());
                    }
                    if (userDTO.email() != null && !userDTO.email().isEmpty()) {
                        existingUser.setEmail(userDTO.email());
                    }
                    validateUser(existingUser);
                    userRepository.save(existingUser);
                    log.info("Пользователь с ID {} обновлён", id);
                    return userMapper.userToUserDTO(existingUser);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
    }

    @Transactional
    public void delete(Long id) {
        log.info("Удаление пользователя с ID: {}", id);
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь", id);
        }
        userRepository.deleteById(id);
        log.info("Пользователь с ID {} удалён", id);
    }

    private void validateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}

