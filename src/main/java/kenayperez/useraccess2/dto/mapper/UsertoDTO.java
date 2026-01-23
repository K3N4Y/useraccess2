package kenayperez.useraccess2.dto.mapper;

import kenayperez.useraccess2.dto.UserDto;
import kenayperez.useraccess2.entities.UserEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UsertoDTO {

    UsertoDTO INSTANCE = Mappers.getMapper(UsertoDTO.class);

    @Mapping(source = "username", target = "name")
    UserDto toUserDto(UserEntity userEntity);

    @Mapping(source = "name", target = "username")
    @Mapping(target = "passwordHash", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "roles", ignore = true)
    UserEntity toUserEntity(UserDto userDto);
}
