package com.vishvendra.procart.service.audit;

import com.vishvendra.procart.entities.Audit;
import com.vishvendra.procart.repository.AuditEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service("auditService")
@RequiredArgsConstructor
public class AuditServiceImpl implements AuditService {

  private final AuditEventRepository auditEventRepository;

  @Override
  public void createAudit(Audit audit) {
    auditEventRepository.save(audit);
  }
}
