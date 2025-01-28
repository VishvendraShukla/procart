package com.vishvendra.procart.service.audit;

import com.vishvendra.procart.entities.Audit;
import com.vishvendra.procart.model.AuditLogsDTO;
import com.vishvendra.procart.model.PageResultResponse;
import org.springframework.data.domain.PageRequest;

public interface AuditService {

  void createAudit(Audit audit);

  PageResultResponse<AuditLogsDTO> getAuditLogs(PageRequest pageRequest);

}
