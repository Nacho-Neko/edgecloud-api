package com.mikou.edgecloud.account.api.admin;

import com.mikou.edgecloud.account.api.admin.dto.AdminKycMaskedDto;
import com.mikou.edgecloud.account.api.admin.dto.AdminKycRevealedDto;
import com.mikou.edgecloud.account.api.admin.dto.AdminKycService;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/kyc")
public class AdminKycController {

    private final AdminKycService adminKycService;

    public AdminKycController(AdminKycService adminKycService) {
        this.adminKycService = adminKycService;
    }

    @GetMapping("/person/{kycId}")
    public AdminKycMaskedDto getPersonMasked(@PathVariable("kycId") UUID kycId) {
        return adminKycService.getMaskedPerson(kycId);
    }

    @PostMapping("/person/{kycId}/reveal")
    public AdminKycRevealedDto revealPerson(
            @PathVariable("kycId") UUID kycId,
            Authentication authentication
    ) {
        String name = (authentication != null ? authentication.getName() : null);
        UUID adminId = null;
        try {
            if (name != null) {
                adminId = UUID.fromString(name);
            }
        } catch (IllegalArgumentException e) {
            // fallback or error
        }
        return adminKycService.revealPerson(kycId, adminId);
    }
}
