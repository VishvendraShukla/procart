package com.vishvendra.procart.repository;

import com.vishvendra.procart.entities.Audit;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditEventRepository extends BaseRepository<Audit> {

}
