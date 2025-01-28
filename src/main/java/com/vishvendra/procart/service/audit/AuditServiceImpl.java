package com.vishvendra.procart.service.audit;

import com.vishvendra.procart.entities.Audit;
import com.vishvendra.procart.mapper.AuditMapper;
import com.vishvendra.procart.model.AuditLogsDTO;
import com.vishvendra.procart.model.PageResultResponse;
import com.vishvendra.procart.repository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service("auditService")
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

  private final AuditEventRepository auditEventRepository;
  private final AuditMapper auditMapper;

  @Override
  public void createAudit(Audit audit) {
    auditEventRepository.save(audit);
  }

  @Override
  public PageResultResponse<AuditLogsDTO> getAuditLogs(PageRequest pageRequest) {
    PageRequest sortedPageRequest = PageRequest.of(
        pageRequest.getPageNumber(),
        pageRequest.getPageSize(),
        Sort.by(Sort.Direction.DESC, "createdAt")
    );
    Page<Audit> auditLogs = auditEventRepository.findAll(sortedPageRequest);
    return mapToDTOList(auditLogs);
  }

  private PageResultResponse<AuditLogsDTO> mapToDTOList(Page<Audit> auditLogs) {
    return PageResultResponse.of(
        auditLogs.map(auditMapper::toDTO).toList(),
        auditLogs.getSize(),
        auditLogs.getTotalElements(),
        auditLogs.getNumber(),
        auditLogs.isLast());
  }
}
