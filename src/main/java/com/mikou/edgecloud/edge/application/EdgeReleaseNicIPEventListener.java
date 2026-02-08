package com.mikou.edgecloud.edge.application;

import com.mikou.edgecloud.common.events.ReleasedNicIPEvent;
import com.mikou.edgecloud.edge.domain.service.IpAllocationService;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeNicIpEntity;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EdgeReleaseNicIPEventListener {

    private final IpAllocationService ipAllocationService;

    public EdgeReleaseNicIPEventListener(IpAllocationService ipAllocationService) {
        this.ipAllocationService = ipAllocationService;
    }

    /**
     * AFTER_COMMIT：只有当“发布事件的那一方”的事务提交成功后才处理，
     * 避免出现“业务回滚了但事件已经处理”的尴尬。
     */
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void on(ReleasedNicIPEvent event) {
        if (event == null || event.allocationType() == null || event.resourceTag() == null) {
            return;
        }

        List<EdgeNicIpEntity> allocatedIps =
                ipAllocationService.findAllocatedIps(event.allocationType(), event.resourceTag());

        for (EdgeNicIpEntity ip : allocatedIps) {
            // 幂等：如果重复消费事件，releaseIp 也应该是安全的（你实现里找不到就 false）
            ipAllocationService.releaseIp(ip.getId());
        }
    }
}