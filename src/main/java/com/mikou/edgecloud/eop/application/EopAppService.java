package com.mikou.edgecloud.eop.application;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.mikou.edgecloud.edge.api.dto.EdgeListQuery;
import com.mikou.edgecloud.eop.api.dto.EopAppDto;
import com.mikou.edgecloud.eop.api.dto.EopBoundDto;
import com.mikou.edgecloud.eop.domain.model.EopSettings;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface EopAppService {
    Page<EopAppDto> listApps(EdgeListQuery query, Pageable pageable);
    EopAppDto updateSettings(UUID eopTag, EopSettings settings);
    void createProtocol(UUID eopTag, String protocol, EopSettings.ProtocolListener listener);
    void destroyProtocol(UUID eopTag, String protocol, Integer port);
    Page<EopBoundDto> listBoundsByEopTag(UUID eopTag, Pageable pageable);
    void syncApps();
}
