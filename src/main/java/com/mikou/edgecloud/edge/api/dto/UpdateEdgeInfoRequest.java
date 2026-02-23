package com.mikou.edgecloud.edge.api.dto;

/**
 * 更新 Edge 硬件和系统信息的请求
 */
public class UpdateEdgeInfoRequest {
    private Integer cpuCores;
    private String cpuModel;
    private Long totalMemory;  // 字节
    private String osType;
    private String osVersion;
    private String osArch;
    private String edgeVersion;

    public Integer getCpuCores() {
        return cpuCores;
    }

    public UpdateEdgeInfoRequest setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
        return this;
    }

    public String getCpuModel() {
        return cpuModel;
    }

    public UpdateEdgeInfoRequest setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
        return this;
    }

    public Long getTotalMemory() {
        return totalMemory;
    }

    public UpdateEdgeInfoRequest setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
        return this;
    }

    public String getOsType() {
        return osType;
    }

    public UpdateEdgeInfoRequest setOsType(String osType) {
        this.osType = osType;
        return this;
    }

    public String getOsVersion() {
        return osVersion;
    }

    public UpdateEdgeInfoRequest setOsVersion(String osVersion) {
        this.osVersion = osVersion;
        return this;
    }

    public String getOsArch() {
        return osArch;
    }

    public UpdateEdgeInfoRequest setOsArch(String osArch) {
        this.osArch = osArch;
        return this;
    }

    public String getEdgeVersion() {
        return edgeVersion;
    }

    public UpdateEdgeInfoRequest setEdgeVersion(String edgeVersion) {
        this.edgeVersion = edgeVersion;
        return this;
    }
}
