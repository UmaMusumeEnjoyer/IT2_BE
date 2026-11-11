package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.udn.dut.it2.model.entity.Regulation;
import vn.udn.dut.it2.service.RegulationService;

import java.util.List;

@RestController
@RequestMapping("/api/regulations")
@RequiredArgsConstructor
@Tag(name = "2. Regulations & Legal Docs", description = "API Tra cứu và Danh mục Quy chế Pháp quy")
public class RegulationController {

    private final RegulationService regulationService;

    @GetMapping
    @Operation(summary = "Tìm kiếm và lọc danh mục văn bản quy chế")
    public ResponseEntity<List<Regulation>> searchRegulations(
            @RequestParam(required = false, defaultValue = "ALL") String category,
            @RequestParam(required = false) String keyword) {
        return ResponseEntity.ok(regulationService.search(category, keyword));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Xem chi tiết một văn bản quy chuẩn (phục vụ Document Viewer)")
    public ResponseEntity<Regulation> getRegulationById(@PathVariable Long id) {
        return regulationService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
