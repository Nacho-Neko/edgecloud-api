package com.mikou.edgecloud.common.events;


import java.util.List;
import java.util.UUID;

/**
 * 领域事件：释放 NIC IP
 *
 * allocationType: 资源类型，例如 "EOP"
 * resourceTag:    资源唯一标识，例如 eopTag（UUID/Tag）
 * ipIds:          可选；为空或空列表表示释放该资源占用的全部 IP；否则释放指定 IP
 */
public record ReleasedNicIPEvent(String allocationType, UUID resourceTag, List<Integer> ipIds) {} ;