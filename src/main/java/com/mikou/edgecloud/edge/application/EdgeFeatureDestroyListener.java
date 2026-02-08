package com.mikou.edgecloud.edge.application;

import com.mikou.edgecloud.common.events.EdgeFeatureDestroyRequestedEvent;
import com.mikou.edgecloud.edge.domain.service.EdgeFeatureService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
public class EdgeFeatureDestroyListener {

    private final EdgeFeatureService featureService;

    public EdgeFeatureDestroyListener(EdgeFeatureService featureService) {
        this.featureService = featureService;
    }

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void onFeatureDestroyRequested(EdgeFeatureDestroyRequestedEvent event) {
        // 执行销毁逻辑
        featureService.destroyFeature(event.edgeTag(), event.featureType());
    }
}
