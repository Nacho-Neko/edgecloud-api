package com.mikou.edgecloud.edge.infrastructure.persistence.entity;

import com.mikou.edgecloud.edge.domain.enums.IpStatus;
import org.junit.jupiter.api.Test;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.junit.jupiter.api.Assertions.*;

class EdgeNicIpEntityTest {

    @Test
    void testPrivateIpNotNull() {
        EdgeNicIpEntity entity = new EdgeNicIpEntity();
        assertThrows(IllegalArgumentException.class, () -> entity.setPrivateIp(null));
        assertThrows(IllegalArgumentException.class, () -> entity.setPrivateIp(""));
        assertThrows(IllegalArgumentException.class, () -> entity.setPrivateIp("  "));
    }

    @Test
    void testPrivateIpFormat() {
        EdgeNicIpEntity entity = new EdgeNicIpEntity();
        assertThrows(IllegalArgumentException.class, () -> entity.setPrivateIp("invalid-ip"));
        entity.setPrivateIp("192.168.1.1");
        assertEquals("192.168.1.1", entity.getPrivateIp());
    }

    @Test
    void testPrivateIpAddress() throws UnknownHostException {
        EdgeNicIpEntity entity = new EdgeNicIpEntity();
        InetAddress addr = InetAddress.getByName("10.0.0.1");
        entity.setPrivateIpAddress(addr);
        assertEquals("10.0.0.1", entity.getPrivateIp());
        assertEquals(addr, entity.getPrivateIpAddress());
    }

    @Test
    void testIpStatusEnumValue() {
        assertEquals(1, IpStatus.ACTIVE.getValue());
        assertEquals(0, IpStatus.REMOVED.getValue());
    }
}
