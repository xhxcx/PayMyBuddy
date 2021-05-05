package com.paymybuddy.moneytransferapp.model.dto;

import com.paymybuddy.moneytransferapp.model.Contact;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;


@Mapper(componentModel = "Spring")
public interface ContactDtoMapper {

    ContactDtoMapper INSTANCE = Mappers.getMapper(ContactDtoMapper.class);

    ContactDTO contactToContactDTO(Contact contact);

    @Mappings({
            @Mapping(target = "id", ignore = true),
    })
    Contact contactDtoToContact(ContactDTO contactDTO);
    List<ContactDTO> contactsToContactDTOList(List<Contact> contactList);

}
