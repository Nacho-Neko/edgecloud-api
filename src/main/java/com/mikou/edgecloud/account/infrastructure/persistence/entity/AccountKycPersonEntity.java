package com.mikou.edgecloud.account.infrastructure.persistence.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.UUID;

@TableName("account_kyc_person")
public class AccountKycPersonEntity {

    @TableId("kyc_id")
    private UUID kycId;

    @TableField("real_name_plain")
    private String realNamePlain;

    @TableField("id_number_plain")
    private String idNumberPlain;

    @TableField("id_number_masked")
    private String idNumberMasked;

    public UUID getKycId() {
        return kycId;
    }

    public AccountKycPersonEntity setKycId(UUID kycId) {
        this.kycId = kycId;
        return this;
    }

    public String getRealNamePlain() {
        return realNamePlain;
    }

    public AccountKycPersonEntity setRealNamePlain(String realNamePlain) {
        this.realNamePlain = realNamePlain;
        return this;
    }

    public String getIdNumberPlain() {
        return idNumberPlain;
    }

    public AccountKycPersonEntity setIdNumberPlain(String idNumberPlain) {
        this.idNumberPlain = idNumberPlain;
        return this;
    }

    public String getIdNumberMasked() {
        return idNumberMasked;
    }

    public AccountKycPersonEntity setIdNumberMasked(String idNumberMasked) {
        this.idNumberMasked = idNumberMasked;
        return this;
    }
}
