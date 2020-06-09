package com.homemanagement.security;

import static java.util.Objects.isNull;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.security.oauth2.provider.approval.Approval;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.stereotype.Component;

import com.homemanagement.domain.MongoApproval;
import com.homemanagement.repositories.ApprovalRepository;
import com.homemanagement.utils.HomeManagementUtil;

@Component
public class MongoApprovalStore implements ApprovalStore {

    private final ApprovalRepository approvalRepository;

    private boolean handleRevocationsAsExpiry = false;

    public MongoApprovalStore(final ApprovalRepository mongoApprovalRepository) {
        this.approvalRepository = mongoApprovalRepository;
    }

    @Override
    public boolean addApprovals(final Collection<Approval> approvals) {
        final Collection<MongoApproval> mongoApprovals = transformToMongoApproval(approvals);

        return approvalRepository.updateOrCreate(mongoApprovals);
    }

    @Override
    public boolean revokeApprovals(final Collection<Approval> approvals) {
        boolean success = true;

        final Collection<MongoApproval> mongoApprovals = transformToMongoApproval(approvals);

        for (final MongoApproval mongoApproval : mongoApprovals) {
            if (handleRevocationsAsExpiry) {
                final boolean updateResult = approvalRepository.updateExpiresAt(LocalDateTime.now(), mongoApproval);
                if (!updateResult) {
                    success = false;
                }

            }
            else {
                final boolean deleteResult = approvalRepository.deleteByUserIdAndClientIdAndScope(mongoApproval);

                if (!deleteResult) {
                    success = false;
                }
            }
        }
        return success;
    }

    @Override
    public Collection<Approval> getApprovals(final String userId,
                                             final String clientId) {
        final List<MongoApproval> mongoApprovals = approvalRepository.findByUserIdAndClientId(userId, clientId);
        return transformToApprovals(mongoApprovals);
    }

    private List<Approval> transformToApprovals(final List<MongoApproval> mongoApprovals) {
        return mongoApprovals.stream().map(mongoApproval -> new Approval(mongoApproval.getUserId(),
                mongoApproval.getClientId(),
                mongoApproval.getScope(),
                Date.from(mongoApproval.getExpiresAt().atZone(ZoneId.systemDefault()).toInstant()),
                mongoApproval.getStatus(),
                Date.from(mongoApproval.getLastUpdatedAt().atZone(ZoneId.systemDefault()).toInstant())))
                .collect(Collectors.toList());
    }

    private List<MongoApproval> transformToMongoApproval(final Collection<Approval> approvals) {
        return approvals.stream().map(approval -> new MongoApproval(UUID.randomUUID().toString(),
                approval.getUserId(),
                approval.getClientId(),
                approval.getScope(),
                isNull(approval.getStatus()) ? Approval.ApprovalStatus.APPROVED: approval.getStatus(),
                		HomeManagementUtil.convertTolocalDateTimeFrom(approval.getExpiresAt()),
                		HomeManagementUtil.convertTolocalDateTimeFrom(approval.getLastUpdatedAt()))).collect(Collectors.toList());
    }

    public void setHandleRevocationsAsExpiry(boolean handleRevocationsAsExpiry) {
        this.handleRevocationsAsExpiry = handleRevocationsAsExpiry;
    }
}
