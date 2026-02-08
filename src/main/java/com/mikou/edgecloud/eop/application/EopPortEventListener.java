package com.mikou.edgecloud.eop.application;

import com.mikou.edgecloud.eop.domain.enums.EopOccupierType;
import com.mikou.edgecloud.eop.domain.events.PortApplyRequestedEvent;
import com.mikou.edgecloud.eop.domain.events.PortReleaseRequestedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 端口领域事件监听器
 */
@Component
public class EopPortEventListener {

    private final EopPortService eopPortService;

    public EopPortEventListener(EopPortService eopPortService) {
        this.eopPortService = eopPortService;
    }

    @EventListener
    public void handlePortApplyRequest(PortApplyRequestedEvent event) {
        eopPortService.executeOccupyPort(
                event.ipId(),
                event.port(),
                event.occupierType(),
                event.occupierId()
        );
    }

    @EventListener
    public void handlePortReleaseRequest(PortReleaseRequestedEvent event) {
        if (event.releaseAllByOccupier()) {
            eopPortService.executeReleaseAllPortsByOccupier(
                    event.occupierType(),
                    event.occupierId()
            );
        } else {
            eopPortService.executeReleasePort(
                    event.ipId(),
                    event.port()
            );
        }
    }
}
