package com.sebcode.msproducts.complaint.mapper;

import com.sebcode.msproducts.complaint.dto.request.ComplaintRequestDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintListResponseDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintResponseDTO;
import com.sebcode.msproducts.complaint.entity.Complaint;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ComplaintMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "notifiedByEmail", ignore = true)
    @Mapping(target = "state", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    @Mapping(target = "deleteAt", ignore = true)
    @Mapping(target = "createdUser", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updateUser", ignore = true)
    @Mapping(target = "updateAt", ignore = true)
    Complaint toEntity(ComplaintRequestDTO dto);

    ComplaintResponseDTO toResponseDTO(Complaint complaint);

    ComplaintListResponseDTO toListResponseDTO(Complaint complaint);

}
