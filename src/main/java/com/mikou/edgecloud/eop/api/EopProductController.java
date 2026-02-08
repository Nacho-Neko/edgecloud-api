package com.mikou.edgecloud.eop.api;

import com.mikou.edgecloud.eop.api.dto.CreateEopProductRequest;
import com.mikou.edgecloud.eop.api.dto.EopProductDto;
import com.mikou.edgecloud.eop.api.dto.UpdateEopProductRequest;
import com.mikou.edgecloud.eop.application.EopProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/eop/products")
@Tag(name = "EopProduct")
public class EopProductController {

    private final EopProductService productService;

    public EopProductController(EopProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    @Operation(summary = "创建 EOP 产品")
    public ResponseEntity<UUID> create(@RequestBody CreateEopProductRequest req) {
        UUID tag = productService.create(req);
        return ResponseEntity.ok(tag);
    }

    @PutMapping("/{tag}")
    @Operation(summary = "修改 EOP 产品")
    public ResponseEntity<Void> update(@PathVariable String tag, @RequestBody UpdateEopProductRequest req) {
        productService.updateByTag(tag, req);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{tag}")
    @Operation(summary = "删除 EOP 产品（硬删除）")
    public ResponseEntity<Void> delete(@PathVariable String tag) {
        productService.deleteByTag(tag);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "获取所有产品列表")
    public ResponseEntity<List<EopProductDto>> listAll() {
        return ResponseEntity.ok(productService.listAll());
    }
}