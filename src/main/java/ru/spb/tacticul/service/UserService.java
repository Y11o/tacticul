package ru.spb.tacticul.service;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validator;
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
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final Validator validator;

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

    public UserDTO create(UserDTO userDTO) {
        User user = userMapper.userDTOToUser(userDTO);
        validateUser(user);
        return userMapper.userToUserDTO(userRepository.save(user));
    }

    public UserDTO update(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(existingUser -> {
                    if (userDTO.login() != null) {
                        existingUser.setLogin(userDTO.login());
                    }
                    if (userDTO.email() != null) {
                        existingUser.setEmail(userDTO.email());
                    }
                    validateUser(existingUser);
                    return userMapper.userToUserDTO(userRepository.save(existingUser));
                })
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
    }

    public void delete(Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Пользователь", id);
        }
        userRepository.deleteById(id);
    }

    private void validateUser(User user) {
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }
}
