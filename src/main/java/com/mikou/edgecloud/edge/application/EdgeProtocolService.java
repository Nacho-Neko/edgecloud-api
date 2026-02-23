package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.EdgeProtocolDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EdgeProtocolService {
    /**
     * @param port      单端口，与 portRange 互斥
     * @param portRange 端口范围字符串 "start-end"，与 port 互斥
     */
    EdgeProtocolDto createProtocol(UUID edgeTag, String protocol, Integer ipId,
                                   Integer port, String portRange);
    void destroyProtocol(Integer id);
    Page<EdgeProtocolDto> listProtocolsByEdgeTag(UUID edgeTag, Pageable pageable);
}