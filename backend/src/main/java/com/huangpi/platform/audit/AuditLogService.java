package com.huangpi.platform.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.huangpi.platform.domain.AuditLogEntity;
import com.huangpi.platform.repository.AuditLogRepository;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    public AuditLogService(AuditLogRepository auditLogRepository, ObjectMapper objectMapper) {
        this.auditLogRepository = auditLogRepository;
        this.objectMapper = objectMapper;
    }

    @Transactional
    public void record(Long actorUserId, String action, String targetType, Long targetId, Map<String, Object> detail) {
        AuditLogEntity log = new AuditLogEntity();
        log.setActorUserId(actorUserId);
        log.setAction(action);
        log.setTargetType(targetType);
        log.setTargetId(targetId);
        try {
            log.setDetailJson(objectMapper.writeValueAsString(detail));
        } catch (JsonProcessingException exception) {
            log.setDetailJson("{}");
        }
        auditLogRepository.save(log);
    }
}
