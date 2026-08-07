package com.sebcode.msproducts.complaint.service.impl;

import com.sebcode.msproducts.complaint.dto.request.ComplaintRequestDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintListResponseDTO;
import com.sebcode.msproducts.complaint.dto.response.ComplaintResponseDTO;
import com.sebcode.msproducts.complaint.entity.Complaint;
import com.sebcode.msproducts.complaint.entity.ComplaintStatus;
import com.sebcode.msproducts.complaint.mapper.ComplaintMapper;
import com.sebcode.msproducts.complaint.repository.ComplaintRepository;
import com.sebcode.msproducts.complaint.service.IComplaintService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ComplaintServiceImpl implements IComplaintService {

    private final ComplaintRepository complaintRepository;
    private final ComplaintMapper complaintMapper;
    private final ComplaintNotificationService notificationService;

    @Override
    @Transactional
    public ComplaintResponseDTO createComplaint(ComplaintRequestDTO requestDTO) {
        Complaint complaint = complaintMapper.toEntity(requestDTO);
        complaint.setStatus(ComplaintStatus.PENDIENTE);
        complaint.setNotifiedByEmail(false);
        Complaint saved = complaintRepository.save(complaint);

        boolean notified = notificationService.notify(saved);
        if (notified) {
            saved.setNotifiedByEmail(true);
            saved = complaintRepository.save(saved);
        }

        log.info("Reclamo/queja #{} registrado (tipo: {}, notificado: {})", saved.getId(), saved.getType(), notified);
        return complaintMapper.toResponseDTO(saved);
    }

    @Override
    public Page<ComplaintListResponseDTO> searchComplaints(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return complaintRepository.findAllList(pageable).map(complaintMapper::toListResponseDTO);
    }

}
