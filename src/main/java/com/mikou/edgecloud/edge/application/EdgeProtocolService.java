package com.mikou.edgecloud.edge.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.EdgeProtocolDto;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EdgeProtocolService {
    EdgeProtocolDto createProtocol(UUID edgeTag, String protocol, Integer ipId,
                                   Integer port, Integer portRangeStart, Integer portRangeEnd);
    void destroyProtocol(Integer id);
    Page<EdgeProtocolDto> listProtocolsByEdgeTag(UUID edgeTag, Pageable pageable);
}