package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.udn.dut.it2.model.dto.ProcedureDto;
import vn.udn.dut.it2.model.entity.ProcedureCriteria;
import vn.udn.dut.it2.model.entity.ProcedureTicket;
import vn.udn.dut.it2.service.ProcedureService;

import java.util.List;

@RestController
@RequestMapping("/api/procedures")
@RequiredArgsConstructor
@Tag(name = "3. Procedures & Criteria", description = "API Chi tiết Quy trình, Đối soát Tiêu chuẩn và Nộp phản hồi")
public class ProcedureController {

    private final ProcedureService procedureService;

    @GetMapping("/criteria")
    @Operation(summary = "Lấy bảng tiêu chuẩn phân loại của thủ tục (ví dụ: Học bổng KKHT)")
    public ResponseEntity<List<ProcedureCriteria>> getCriteria(
            @RequestParam(defaultValue = "Học bổng Khuyến khích Học tập") String procedureName) {
        return ResponseEntity.ok(procedureService.getCriteriaByProcedure(procedureName));
    }

    @GetMapping("/evaluate/{studentCode}")
    @Operation(summary = "Tự động so khớp hồ sơ điểm số sinh viên với bảng tiêu chuẩn")
    public ResponseEntity<ProcedureDto.EvaluationResult> evaluateEligibility(@PathVariable String studentCode) {
        return ResponseEntity.ok(procedureService.evaluateScholarshipEligibility(studentCode));
    }

    @PostMapping("/tickets")
    @Operation(summary = "Nộp đơn phản hồi/khiếu nại học vụ trực tuyến, sinh mã biên nhận DUT-HB-xxxx")
    public ResponseEntity<ProcedureTicket> createTicket(@RequestBody ProcedureDto.CreateTicketRequest request) {
        return ResponseEntity.ok(procedureService.createTicket(request));
    }

    @GetMapping("/tickets/{studentCode}")
    @Operation(summary = "Danh sách đơn phản hồi đã gửi của sinh viên")
    public ResponseEntity<List<ProcedureTicket>> getTicketsByStudent(@PathVariable String studentCode) {
        return ResponseEntity.ok(procedureService.getTicketsByStudent(studentCode));
    }
}
