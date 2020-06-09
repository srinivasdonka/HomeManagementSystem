package com.homemanagement.repositories;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

import com.homemanagement.domain.MongoApproval;
public interface ApprovalRepositoryBase {
    boolean updateOrCreate(Collection<MongoApproval> mongoApprovals);

    boolean updateExpiresAt(LocalDateTime now, MongoApproval mongoApproval);

    boolean deleteByUserIdAndClientIdAndScope(MongoApproval mongoApproval);

    List<MongoApproval> findByUserIdAndClientId(String userId, String clientId);
}
