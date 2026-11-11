package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.udn.dut.it2.model.entity.Student;
import vn.udn.dut.it2.service.StudentService;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
@Tag(name = "1. Student Profile", description = "API Hồ sơ học tập cá nhân hóa sinh viên DUT")
public class StudentController {

    private final StudentService studentService;

    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin học tập của sinh viên hiện tại (Academic Status Strip)")
    public ResponseEntity<Student> getCurrentStudent(@RequestParam(required = false, defaultValue = "102230001") String studentCode) {
        return studentService.getStudentProfile(studentCode)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.ok(studentService.getDefaultMockStudent()));
    }

    @GetMapping("/{studentCode}")
    @Operation(summary = "Lấy thông tin sinh viên theo mã số sinh viên")
    public ResponseEntity<Student> getStudentByCode(@PathVariable String studentCode) {
        return studentService.getStudentProfile(studentCode)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
