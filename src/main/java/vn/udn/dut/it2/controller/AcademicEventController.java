package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.udn.dut.it2.model.entity.AcademicEvent;
import vn.udn.dut.it2.service.AcademicEventService;

import java.util.List;

@RestController
@RequestMapping("/api/events")
@RequiredArgsConstructor
@Tag(name = "5. Calendar & Deadlines", description = "API Lịch và Bản tin Cá nhân hóa theo Khoa/Lớp")
public class AcademicEventController {

    private final AcademicEventService eventService;

    @GetMapping
    @Operation(summary = "Lấy danh sách sự kiện và hạn chót được lọc theo hồ sơ sinh viên")
    public ResponseEntity<List<AcademicEvent>> getEvents(
            @RequestParam(required = false, defaultValue = "ALL") String eventType,
            @RequestParam(required = false) String faculty,
            @RequestParam(required = false) String className) {
        return ResponseEntity.ok(eventService.getEvents(eventType, faculty, className));
    }
}
