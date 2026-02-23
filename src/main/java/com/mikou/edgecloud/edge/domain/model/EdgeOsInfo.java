package com.mikou.edgecloud.edge.domain.model;

/**
 * Edge 操作系统信息
 */
public class EdgeOsInfo {
    private String osType;     // Linux, Windows, MacOS
    private String osVersion;  // 22.04, 11, 14.2 等
    private String osArch;     // x86_64, arm64, aarch64 等
    
    public EdgeOsInfo() {
    }
    
    public EdgeOsInfo(String osType, String osVersion, String osArch) {
        this.osType = osType;
        this.osVersion = osVersion;
        this.osArch = osArch;
    }
    
    public String getOsType() {
        return osType;
    }
    
    public EdgeOsInfo setOsType(String osType) {
        this.osType = osType;
        return this;
    }
    
    public String getOsVersion() {
        return osVersion;
    }
    
    public EdgeOsInfo setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }
    
    public String getOsArch() {
        return osArch;
    }
    
    public EdgeOsInfo setOsArch(String osArch) {
        this.osArch = osArch;
        return this;
    }
    
    /**
     * 获取完整的操作系统描述
     */
    public String getFullDescription() {
        StringBuilder sb = new StringBuilder();
        if (osType != null) sb.append(osType);
        if (osVersion != null) sb.append(" ").append(osVersion);
        if (osArch != null) sb.append(" (").append(osArch).append(")");
        return sb.toString().trim();
    }
}
