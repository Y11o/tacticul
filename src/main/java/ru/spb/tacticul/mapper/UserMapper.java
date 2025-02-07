package ru.spb.tacticul.mapper;

import ru.spb.tacticul.dto.UserDTO;
import ru.spb.tacticul.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);
}
