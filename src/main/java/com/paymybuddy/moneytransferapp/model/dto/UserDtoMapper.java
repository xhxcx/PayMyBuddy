package com.paymybuddy.moneytransferapp.model.dto;

import com.paymybuddy.moneytransferapp.model.UserAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "Spring")
public interface UserDtoMapper {

    UserDtoMapper INSTANCE = Mappers.getMapper(UserDtoMapper.class);

    @Mappings({
            @Mapping(target = "id", ignore = true),
            @Mapping(target = "accountBalance", ignore = true)
    })
    UserAccount userDtoToUser(UserDTO userDTO);
}
