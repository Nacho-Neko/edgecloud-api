package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.EdgeTransferDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EdgeTransferService {
    EdgeTransferDto createTransfer(UUID senderEdgeTag, Integer sendIpId,
                                   Integer targetProtocolId, String hostName, String remark);

    /** pairKey 作为对外操作句柄，不暴露数据库 id */
    void deleteTransfer(UUID pairKey);

    Page<EdgeTransferDto> listTransfersByEdgeTag(UUID edgeTag, Pageable pageable);
}