package com.mikou.edgecloud.common.spi;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mikou.edgecloud.edge.infrastructure.persistence.entity.EdgeEntity;
import com.mikou.edgecloud.edge.infrastructure.persistence.mapper.EdgeMapper;
import com.mikou.edgecloud.eop.domain.enums.EopDirection;
import com.mikou.edgecloud.eop.domain.events.EopNotifyMessage;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopAppEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.entity.EopBoundEntity;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopAppMapper;
import com.mikou.edgecloud.eop.domain.infrastructure.persistence.mapper.EopBoundMapper;
import com.mikou.edgecloud.eop.domain.model.EopBoundParams;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class NatsClientTest {

    @Autowired
    private NatsClient natsClient;

    @Autowired
    private EopBoundMapper eopBoundMapper;

    @Autowired
    private EopAppMapper eopAppMapper;

    @Autowired
    private EdgeMapper edgeMapper;

    private static final String TEST_BOUND_TAG = "2c969b84-6574-4693-a037-cba1921f9899";

    @Test
    public void testPublishMessage() {
        // 构造一个模拟消息
        EopNotifyMessage message = new EopNotifyMessage(
                EopNotifyMessage.ACTION_CREATE_BOUND,
                UUID.randomUUID(), // edgeTag
                "Test Data Content"
        );

        // 发送消息到 subject: eop.notify
        // 运行此测试前请确保 NATS 服务器 (59.37.173.154:4222) 是可以访问的
        System.out.println("[DEBUG_LOG] Sending NATS message...");
        natsClient.publish("eop.notify", message);
        System.out.println("[DEBUG_LOG] Message published successfully.");
        
        // 由于 publish 是异步且无返回值的，这里通常配合断言或手动观察 NATS 监控
    }

    @Test
    public void testCreateBound() {
        // 1. 从数据库查询真实的 Bound 数据
        EopBoundEntity entity = eopBoundMapper.selectOne(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getTag, TEST_BOUND_TAG));
        
        assertNotNull(entity, "数据库中找不到 Tag 为 " + TEST_BOUND_TAG + " 的 Bound 数据");

        // 2. 获取真实的 EdgeTag
        EopAppEntity app = eopAppMapper.selectById(entity.getEopId());
        assertNotNull(app, "找不到关联的 EopApp");
        
        EdgeEntity edge = edgeMapper.selectById(app.getEdgeId());
        assertNotNull(edge, "找不到关联的 Edge");

        // 3. 构造消息
        EopNotifyMessage message = new EopNotifyMessage(
                EopNotifyMessage.ACTION_CREATE_BOUND,
                edge.getEdgeTag(),
                entity
        );

        System.out.println("[DEBUG_LOG] Sending CREATE_BOUND (Inbound) message using real data...");
        natsClient.publish("eop.notify", message);
        System.out.println("[DEBUG_LOG] CREATE_BOUND message published. EdgeTag: " + edge.getEdgeTag());
    }

    @Test
    public void testDeleteBound() {
        // 1. 从数据库查询真实的 Bound 数据
        EopBoundEntity entity = eopBoundMapper.selectOne(new LambdaQueryWrapper<EopBoundEntity>()
                .eq(EopBoundEntity::getTag, TEST_BOUND_TAG));
        
        assertNotNull(entity, "数据库中找不到 Tag 为 " + TEST_BOUND_TAG + " 的 Bound 数据");

        // 2. 获取真实的 EdgeTag
        EopAppEntity app = eopAppMapper.selectById(entity.getEopId());
        assertNotNull(app, "找不到关联的 EopApp");
        
        EdgeEntity edge = edgeMapper.selectById(app.getEdgeId());
        assertNotNull(edge, "找不到关联 of Edge");

        // 3. 构造消息
        EopNotifyMessage message = new EopNotifyMessage(
                EopNotifyMessage.ACTION_DELETE_BOUND,
                edge.getEdgeTag(),
                entity
        );

        System.out.println("[DEBUG_LOG] Sending DELETE_BOUND message using real data...");
        natsClient.publish("eop.notify", message);
        System.out.println("[DEBUG_LOG] DELETE_BOUND message published. EdgeTag: " + edge.getEdgeTag());
    }
}
