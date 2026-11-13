package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.udn.dut.it2.model.dto.DocDetailDto;

import java.util.List;
import java.util.Map;

/**
 * MOCK: trả về JSON cứng để FE tích hợp trang Detail trước.
 * Sẽ thay bằng Service + Spring Data JPA query DB ở bước sau (giữ nguyên JSON contract).
 */
@RestController
@RequestMapping("/api/docs")
@Tag(name = "6. Docs Detail", description = "API Xem chi tiết bài viết/quy định (step-by-step & link tải biểu mẫu)")
public class DocController {

    private static final String FILE_BASE_URL = "https://example.com/files/";

    private static final Map<Long, DocDetailDto.Response> MOCK_DOCS = Map.of(
            1L, doc(1L,
                    "Hướng dẫn đăng ký xét Học bổng Khuyến khích Học tập",
                    "Quy trình sinh viên đăng ký xét học bổng KKHT theo từng học kỳ tại Trường Đại học Bách khoa - ĐHĐN.",
                    "mau-don-dang-ky-hoc-bong-kkht.docx",
                    List.of("Học bổng", "Biểu mẫu", "Công tác sinh viên"),
                    step(1, "Kiểm tra điều kiện",
                            "Đối chiếu điểm trung bình học kỳ (GPA) và điểm rèn luyện với bảng tiêu chuẩn xét học bổng."),
                    step(2, "Tải và điền biểu mẫu",
                            "Tải biểu mẫu đăng ký ở liên kết bên dưới, điền đầy đủ thông tin cá nhân và kết quả học tập."),
                    step(3, "Xin xác nhận của Khoa",
                            "Nộp biểu mẫu cho Giáo vụ khoa để xác nhận kết quả học tập và rèn luyện."),
                    step(4, "Nộp hồ sơ",
                            "Nộp hồ sơ đã xác nhận tại Phòng Công tác Sinh viên (bộ phận Một cửa) trước hạn quy định."),
                    step(5, "Theo dõi kết quả",
                            "Kết quả xét học bổng được công bố trên cổng thông tin sinh viên sau 10-15 ngày làm việc.")),
            2L, doc(2L,
                    "Thủ tục xin cấp Giấy xác nhận sinh viên",
                    "Dùng để bổ túc hồ sơ vay vốn ngân hàng, tạm hoãn nghĩa vụ quân sự, đăng ký tạm trú hoặc xin thực tập.",
                    "mau-giay-xac-nhan-sinh-vien.docx",
                    List.of("Thủ tục một cửa", "Giấy xác nhận", "Biểu mẫu"),
                    step(1, "Tải biểu mẫu",
                            "Tải mẫu giấy xác nhận sinh viên ở liên kết bên dưới và chọn đúng mục đích xác nhận."),
                    step(2, "Điền thông tin",
                            "Điền họ tên, mã số sinh viên, lớp, khoa và lý do xin xác nhận."),
                    step(3, "Nộp tại bộ phận Một cửa",
                            "Nộp giấy kèm thẻ sinh viên tại bộ phận Một cửa - Phòng Công tác Sinh viên trong giờ hành chính."),
                    step(4, "Nhận kết quả",
                            "Nhận giấy xác nhận đã ký, đóng dấu sau 1-2 ngày làm việc.")),
            3L, doc(3L,
                    "Quy trình đăng ký phúc khảo điểm thi kết thúc học phần",
                    "Áp dụng khi sinh viên cho rằng điểm thi kết thúc học phần chưa phản ánh đúng bài làm.",
                    "mau-don-phuc-khao-diem-thi.docx",
                    List.of("Học vụ", "Phúc khảo", "Điểm thi"),
                    step(1, "Kiểm tra thời hạn",
                            "Đơn phúc khảo chỉ được nhận trong vòng 7 ngày kể từ ngày công bố điểm học phần."),
                    step(2, "Điền đơn phúc khảo",
                            "Tải mẫu đơn, ghi rõ mã học phần, lớp học phần, ngày thi và điểm đã công bố."),
                    step(3, "Nộp đơn và lệ phí",
                            "Nộp đơn tại Văn phòng khoa quản lý học phần và đóng lệ phí phúc khảo theo quy định."),
                    step(4, "Chấm phúc khảo",
                            "Khoa tổ chức chấm lại bài thi bởi hai giảng viên khác với lần chấm đầu."),
                    step(5, "Nhận kết quả",
                            "Điểm sau phúc khảo được cập nhật trên hệ thống sinh viên trong 10 ngày làm việc.")),
            4L, doc(4L,
                    "Thủ tục xin nghỉ học tạm thời và bảo lưu kết quả học tập",
                    "Dành cho sinh viên cần tạm dừng học vì lý do sức khỏe, hoàn cảnh gia đình hoặc nhu cầu cá nhân.",
                    "mau-don-xin-bao-luu.docx",
                    List.of("Học vụ", "Bảo lưu", "Biểu mẫu"),
                    step(1, "Kiểm tra điều kiện",
                            "Sinh viên đã học ít nhất một học kỳ, không bị buộc thôi học hoặc đang bị xem xét kỷ luật."),
                    step(2, "Chuẩn bị hồ sơ",
                            "Điền đơn xin nghỉ học tạm thời, kèm minh chứng (giấy khám sức khỏe, xác nhận của địa phương...)."),
                    step(3, "Xin ý kiến của Khoa",
                            "Trình đơn cho Cố vấn học tập và Trưởng khoa ký xác nhận."),
                    step(4, "Nộp tại Phòng Đào tạo",
                            "Nộp hồ sơ tại Phòng Đào tạo trước khi bắt đầu học kỳ hoặc trong 2 tuần đầu học kỳ."),
                    step(5, "Nhận quyết định",
                            "Nhận Quyết định cho nghỉ học tạm thời sau 5-7 ngày làm việc."),
                    step(6, "Làm thủ tục nhập học lại",
                            "Trước khi hết thời gian bảo lưu, nộp đơn xin tiếp tục học ít nhất 1 tuần trước học kỳ mới.")),
            5L, doc(5L,
                    "Hướng dẫn đăng ký ở Ký túc xá sinh viên",
                    "Quy trình đăng ký chỗ ở nội trú tại Ký túc xá cho sinh viên theo từng năm học.",
                    "mau-don-dang-ky-ky-tuc-xa.docx",
                    List.of("Ký túc xá", "Công tác sinh viên", "Biểu mẫu"),
                    step(1, "Theo dõi thông báo",
                            "Xem thông báo nhận hồ sơ nội trú đầu mỗi năm học trên trang thông tin của Trường."),
                    step(2, "Điền đơn đăng ký",
                            "Tải mẫu đơn, điền thông tin cá nhân và đối tượng ưu tiên (nếu có)."),
                    step(3, "Nộp hồ sơ",
                            "Nộp đơn kèm bản sao thẻ sinh viên, căn cước công dân và giấy tờ ưu tiên tại Ban quản lý Ký túc xá."),
                    step(4, "Đóng phí và nhận phòng",
                            "Sau khi có danh sách xét duyệt, đóng phí nội trú và nhận phòng theo lịch của Ban quản lý."))
    );

    @GetMapping("/{id}")
    @Operation(summary = "[MOCK] Lấy chi tiết bài viết/quy định gồm các bước thực hiện và link tải biểu mẫu (id mẫu: 1-5)")
    public ResponseEntity<DocDetailDto.Response> getDocById(@PathVariable Long id) {
        DocDetailDto.Response doc = MOCK_DOCS.get(id);
        return doc != null ? ResponseEntity.ok(doc) : ResponseEntity.notFound().build();
    }

    private static DocDetailDto.Response doc(Long id, String title, String summary, String fileName,
                                             List<String> tags, DocDetailDto.Step... steps) {
        return DocDetailDto.Response.builder()
                .id(id)
                .title(title)
                .summary(summary)
                .steps(List.of(steps))
                .fileUrl(FILE_BASE_URL + fileName)
                .tags(tags)
                .build();
    }

    private static DocDetailDto.Step step(int order, String title, String description) {
        return DocDetailDto.Step.builder().order(order).title(title).description(description).build();
    }
}
