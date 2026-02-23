package com.mikou.edgecloud.business.eop.application;

import com.mikou.edgecloud.edge.api.dto.EdgeTransferDto;

import java.util.List;
import java.util.UUID;

/** 委托接口，实际逻辑由 EdgeTransferService 承载 */
public interface EopTransferService {
    EdgeTransferDto createTransfer(UUID senderEdgeTag, UUID targetEdgeTag,
                                   Integer sendIpId, Integer receiveIpId,
                                   String protocol, String hostName, String remark);
    void deleteTransfer(UUID pairKey);
    List<EdgeTransferDto> listTransfersByEdgeTag(UUID edgeTag);
}