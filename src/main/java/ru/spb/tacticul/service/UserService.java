package ru.spb.tacticul.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import ru.spb.tacticul.dto.UserDTO;
import ru.spb.tacticul.exception.ResourceNotFoundException;
import ru.spb.tacticul.mapper.UserMapper;
import ru.spb.tacticul.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    private final BCryptPasswordEncoder passwordEncoder;

    public List<UserDTO> getAll() {
        log.info("Получение всех записей 'Пользователь'");
        return userRepository.findAll().stream()
                .map(userMapper::userToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getById(Long id) {
        log.info("Поиск записи 'Пользователь' по ID: {}", id);
        return userRepository.findById(id)
                .map(userMapper::userToUserDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Пользователь", id));
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
                    if (userDTO.password() != null && !userDTO.password().isEmpty()){
                        existingUser.setPassword(passwordEncoder.encode(existingUser.getPassword()));
                    }
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
}
