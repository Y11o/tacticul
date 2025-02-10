package ru.spb.tacticul.mapper;

import org.mapstruct.Mapping;
import ru.spb.tacticul.dto.UserDTO;
import ru.spb.tacticul.dto.UserCreateDTO;
import ru.spb.tacticul.model.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserDTO userToUserDTO(User user);
    User userDTOToUser(UserDTO userDTO);

    @Mapping(target = "id", ignore = true)
    User userCreateDTOToUser(UserCreateDTO userCreateDTO);
}
