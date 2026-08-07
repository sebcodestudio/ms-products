package com.sebcode.msproducts.complaint.service;

import com.sebcode.msproducts.complaint.dto.request.ComplaintRequestDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintListResponseDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintResponseDTO;
import org.springframework.data.domain.Page;

public interface IComplaintService {

    ComplaintResponseDTO createComplaint(ComplaintRequestDTO requestDTO);

    Page<ComplaintListResponseDTO> searchComplaints(int page, int size);

}
