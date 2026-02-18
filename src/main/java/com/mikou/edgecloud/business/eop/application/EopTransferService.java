package com.mikou.edgecloud.business.eop.application;

import com.mikou.edgecloud.business.eop.api.dto.EopTransferDto;

import java.util.List;

public interface EopTransferService {
    EopTransferDto createTransfer(String senderEopTag, String targetEopTag, Integer sendIpId, Integer receiveIpId, String protocol, String hostName, String remark);
    void deleteTransfer(Integer id);
    List<EopTransferDto> listTransfersByEopTag(String eopTag);
}
