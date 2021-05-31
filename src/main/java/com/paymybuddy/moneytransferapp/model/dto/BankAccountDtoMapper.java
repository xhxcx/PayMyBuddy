package com.paymybuddy.moneytransferapp.model.dto;

import com.paymybuddy.moneytransferapp.model.BankAccount;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "Spring")
public interface BankAccountDtoMapper {
    BankAccountDtoMapper INSTANCE = Mappers.getMapper(BankAccountDtoMapper.class);

    BankAccountDTO entityToDTO(BankAccount bankAccount);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    BankAccount DTOToEntity(BankAccountDTO bankAccountDTO);

    List<BankAccountDTO> entitiesToDTOs(List<BankAccount> bankAccountList);
}
