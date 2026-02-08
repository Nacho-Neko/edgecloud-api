package com.mikou.edgecloud.account.api.admin.dto;

import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountKycEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountKycPersonEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.entity.AccountSensitiveAccessAuditEntity;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountKycMapper;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountKycPersonMapper;
import com.mikou.edgecloud.account.infrastructure.persistence.mapper.AccountSensitiveAccessAuditMapper;
import java.time.Instant;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminKycService {

    private final AccountKycMapper accountKycMapper;
    private final AccountKycPersonMapper accountKycPersonMapper;
    private final AccountSensitiveAccessAuditMapper auditMapper;

    public AdminKycService(
            AccountKycMapper accountKycMapper,
            AccountKycPersonMapper accountKycPersonMapper,
            AccountSensitiveAccessAuditMapper auditMapper
    ) {
        this.accountKycMapper = accountKycMapper;
        this.accountKycPersonMapper = accountKycPersonMapper;
        this.auditMapper = auditMapper;
    }

    @Transactional(readOnly = true)
    public AdminKycMaskedDto getMaskedPerson(UUID kycId) {
        AccountKycEntity kyc = accountKycMapper.selectById(kycId);
        if (kyc == null) {
            throw new IllegalArgumentException("KYC not found: " + kycId);
        }

        AccountKycPersonEntity person = accountKycPersonMapper.selectById(kycId);

        AdminKycMaskedDto dto = new AdminKycMaskedDto()
                .setKycId(kyc.getId())
                .setAccountId(kyc.getAccountId())
                .setProvider(kyc.getProvider())
                .setKycType(kyc.getKycType())
                .setStatus(kyc.getStatus())
                .setSubmittedAt(kyc.getSubmittedAt())
                .setVerifiedAt(kyc.getVerifiedAt());

        if (person != null) {
            dto.setIdNumberMasked(person.getIdNumberMasked());
            dto.setRealNameMasked(maskName(person.getRealNamePlain()));
        }
        return dto;
    }

    @Transactional
    public AdminKycRevealedDto revealPerson(UUID kycId, UUID adminId) {
        AccountKycEntity kyc = accountKycMapper.selectById(kycId);
        if (kyc == null) {
            throw new IllegalArgumentException("KYC not found: " + kycId);
        }

        AccountKycPersonEntity person = accountKycPersonMapper.selectById(kycId);
        if (person == null) {
            throw new IllegalArgumentException("KYC person detail not found: " + kycId);
        }

        // Lightweight audit: record who revealed what.
        auditMapper.insert(new AccountSensitiveAccessAuditEntity()
                .setAdminId(adminId)
                .setKycId(kycId)
                .setAction("REVEAL_KYC_PERSON")
                .setCreatedAt(Instant.now())
        );

        return new AdminKycRevealedDto()
                .setKycId(kyc.getId())
                .setAccountId(kyc.getAccountId())
                .setProvider(kyc.getProvider())
                .setKycType(kyc.getKycType())
                .setStatus(kyc.getStatus())
                .setRealNamePlain(person.getRealNamePlain())
                .setIdNumberPlain(person.getIdNumberPlain())
                .setSubmittedAt(kyc.getSubmittedAt())
                .setVerifiedAt(kyc.getVerifiedAt());
    }

    private static String maskName(String realNamePlain) {
        if (realNamePlain == null || realNamePlain.isBlank()) {
            return null;
        }
        if (realNamePlain.length() == 1) {
            return "*";
        }
        return realNamePlain.charAt(0) + "*";
    }
}
