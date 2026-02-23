package com.mikou.edgecloud.business.domain;

/**
 * 业务能力接口
 * 各业务模块实现此接口来注册自己的能力
 */
public interface BusinessCapability {
    
    /**
     * 获取能力代码
     */
    String getCode();
    
    /**
     * 获取能力名称
     */
    String getName();
    
    /**
     * 获取能力描述
     */
    String getDescription();
    
    /**
     * 是否启用
     */
    boolean isEnabled();
    
    /**
     * 获取版本号
     */
    String getVersion();
}
