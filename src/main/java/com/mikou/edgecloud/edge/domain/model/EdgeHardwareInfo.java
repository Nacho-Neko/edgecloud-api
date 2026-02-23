package com.mikou.edgecloud.edge.domain.model;

/**
 * Edge 硬件信息
 */
public class EdgeHardwareInfo {
    private Integer cpuCores;
    private String cpuModel;
    private Long totalMemory;  // 字节
    
    public EdgeHardwareInfo() {
    }
    
    public EdgeHardwareInfo(Integer cpuCores, String cpuModel, Long totalMemory) {
        this.cpuCores = cpuCores;
        this.cpuModel = cpuModel;
        this.totalMemory = totalMemory;
    }
    
    public Integer getCpuCores() {
        return cpuCores;
    }
    
    public EdgeHardwareInfo setCpuCores(Integer cpuCores) {
        this.cpuCores = cpuCores;
        return this;
    }
    
    public String getCpuModel() {
        return cpuModel;
    }
    
    public EdgeHardwareInfo setCpuModel(String cpuModel) {
        this.cpuModel = cpuModel;
        return this;
    }
    
    public Long getTotalMemory() {
        return totalMemory;
    }
    
    public EdgeHardwareInfo setTotalMemory(Long totalMemory) {
        this.totalMemory = totalMemory;
        return this;
    }
    
    /**
     * 获取总内存（GB）
     */
    public double getTotalMemoryGB() {
        return totalMemory != null ? totalMemory / (1024.0 * 1024.0 * 1024.0) : 0;
    }
}
