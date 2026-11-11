package vn.udn.dut.it2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import vn.udn.dut.it2.model.dto.ProcedureDto;
import vn.udn.dut.it2.model.entity.ProcedureCriteria;
import vn.udn.dut.it2.model.entity.ProcedureTicket;
import vn.udn.dut.it2.model.entity.Student;
import vn.udn.dut.it2.repository.ProcedureCriteriaRepository;
import vn.udn.dut.it2.repository.ProcedureTicketRepository;
import vn.udn.dut.it2.repository.StudentRepository;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProcedureService {

    private final ProcedureCriteriaRepository criteriaRepository;
    private final ProcedureTicketRepository ticketRepository;
    private final StudentRepository studentRepository;

    public List<ProcedureCriteria> getCriteriaByProcedure(String procedureName) {
        return criteriaRepository.findByProcedureName(procedureName);
    }

    public ProcedureDto.EvaluationResult evaluateScholarshipEligibility(String studentCode) {
        Student student = studentRepository.findByStudentCode(studentCode)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy sinh viên có mã: " + studentCode));

        BigDecimal gpa = student.getGpa() != null ? student.getGpa() : BigDecimal.ZERO;
        int drl = student.getTrainingPoint() != null ? student.getTrainingPoint() : 0;

        String level = "KHONG_DU_DIEU_KIEN";
        String message = "Chưa đạt tiêu chuẩn xét học bổng khuyến khích học tập theo QĐ 1414/QĐ-ĐHBK.";

        if (gpa.compareTo(new BigDecimal("9.00")) >= 0 && drl >= 90) {
            level = "LOAI_XUAT_SAC";
            message = "Đạt tiêu chuẩn Học bổng Loại Xuất sắc (ĐTBCHK >= 9.00, ĐRL >= 90).";
        } else if (gpa.compareTo(new BigDecimal("8.00")) >= 0 && drl >= 80) {
            level = "LOAI_GIOI";
            message = "Đạt tiêu chuẩn Học bổng Loại Giỏi (ĐTBCHK 8.00 - 8.99, ĐRL >= 80).";
        } else if (gpa.compareTo(new BigDecimal("7.00")) >= 0 && drl >= 65) {
            level = "LOAI_KHA";
            message = "Đạt tiêu chuẩn Học bổng Loại Khá (ĐTBCHK 7.00 - 7.99, ĐRL >= 65).";
        }

        List<String> checklistSteps = Arrays.asList(
                "Kiểm tra điểm số và điều kiện học phần ban đầu (Đã hoàn thành)",
                "Đối soát danh sách công bố dự kiến của Khoa/Phòng Đào tạo",
                "Nộp phản hồi/khiếu nại nếu có sai lệch điểm TBCHK hoặc ĐRL",
                "Nhận thông báo quyết định cấp học bổng chính thức"
        );

        return ProcedureDto.EvaluationResult.builder()
                .studentCode(student.getStudentCode())
                .fullName(student.getFullName())
                .gpa(gpa)
                .trainingPoint(drl)
                .eligibleLevel(level)
                .statusMessage(message)
                .checklistSteps(checklistSteps)
                .build();
    }

    @Transactional
    public ProcedureTicket createTicket(ProcedureDto.CreateTicketRequest request) {
        String ticketCode = "DUT-HB-" + (int)(Math.random() * 90000 + 10000);
        ProcedureTicket ticket = ProcedureTicket.builder()
                .ticketCode(ticketCode)
                .studentCode(request.getStudentCode())
                .procedureName(request.getProcedureName())
                .reason(request.getReason())
                .status("PENDING")
                .build();

        return ticketRepository.save(ticket);
    }

    public List<ProcedureTicket> getTicketsByStudent(String studentCode) {
        return ticketRepository.findByStudentCode(studentCode);
    }
}
