package vn.udn.dut.it2.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.udn.dut.it2.model.dto.ChatDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AiChatService {

    public ChatDto.Response processChat(ChatDto.Request request) {
        String msg = request.getMessage() != null ? request.getMessage().toLowerCase() : "";

        List<ChatDto.Citation> citations = new ArrayList<>();
        List<String> suggestions = Arrays.asList(
                "Tiêu chuẩn học bổng loại Giỏi là bao nhiêu?",
                "Điều kiện số tín chỉ tối thiểu để xét học bổng?",
                "Thủ tục xin cấp giấy xác nhận sinh viên trực tuyến?",
                "Hạn chót rút bớt học phần kỳ này là khi nào?"
        );

        String answer;

        if (msg.contains("học bổng") || msg.contains("xuất sắc") || msg.contains("giỏi") || msg.contains("khá") || msg.contains("điểm rèn luyện")) {
            answer = "Theo Quyết định số **1414/QĐ-ĐHBK** của Trường Đại học Bách khoa – ĐHĐN, tiêu chuẩn xét cấp Học bổng Khuyến khích Học tập gồm 3 mức:\n\n" +
                    "- **Loại Xuất sắc:** Điểm TBCHK lần 1 $\\ge 9.00$ và Điểm rèn luyện (ĐRL) $\\ge 90$ điểm.\n" +
                    "- **Loại Giỏi:** Điểm TBCHK lần 1 từ $8.00 - 8.99$ và ĐRL $\\ge 80$ điểm.\n" +
                    "- **Loại Khá:** Điểm TBCHK lần 1 từ $7.00 - 7.99$ và ĐRL $\\ge 65$ điểm.\n\n" +
                    "*Lưu ý điều kiện loại trừ:* Sinh viên phải đăng ký đủ số tín chỉ tối thiểu theo quy chế (từ 15 tín chỉ/kỳ, trừ kỳ cuối), không có học phần nào thi lần đầu dưới điểm D (hoặc dưới điểm F đối với môn điều kiện), và không bị kỷ luật từ mức khiển trách trở lên trong kỳ học.";

            citations.add(ChatDto.Citation.builder()
                    .documentNumber("1414/QĐ-ĐHBK")
                    .documentTitle("Quy định xét cấp HBKKHT tại Trường ĐHBK-ĐHĐN")
                    .clause("Điều 4, Khoản 2")
                    .excerpt("Mức học bổng loại Xuất sắc, Giỏi, Khá căn cứ vào kết quả học tập và rèn luyện lần thi thứ nhất...")
                    .pdfUrl("/tai-lieu-riel/QD_1414_DHBK.pdf")
                    .build());
        } else if (msg.contains("tín chỉ tối thiểu") || msg.contains("số tín chỉ")) {
            answer = "Theo quy chế đào tạo tín chỉ và hướng dẫn xét học vụ:\n\n" +
                    "- Sinh viên hệ chính quy cần tích lũy tối thiểu **15 tín chỉ** trong một học kỳ chính để đủ điều kiện xét học bổng khuyến khích học tập.\n" +
                    "- Đối với học kỳ cuối khóa (học kỳ tốt nghiệp), số tín chỉ tối thiểu tính theo số lượng học phần/đồ án tốt nghiệp quy định trong khung CTĐT.";

            citations.add(ChatDto.Citation.builder()
                    .documentNumber("1414/QĐ-ĐHBK")
                    .documentTitle("Quy định xét cấp HBKKHT tại Trường ĐHBK-ĐHĐN")
                    .clause("Điều 3, Khoản 1")
                    .excerpt("Sinh viên phải tích lũy đủ số tín chỉ tối thiểu theo quy chế đào tạo tín chỉ hiện hành...")
                    .pdfUrl("/tai-lieu-riel/QD_1414_DHBK.pdf")
                    .build());
        } else if (msg.contains("giấy xác nhận") || msg.contains("nghĩa vụ quân sự") || msg.contains("thủ tục")) {
            answer = "Để đăng ký cấp **Giấy xác nhận sinh viên** hoặc **Giấy tạm hoãn NVQS**:\n\n" +
                    "1. Đăng nhập vào Cổng dịch vụ trực tuyến sinh viên: [sv.dut.udn.vn](https://sv.dut.udn.vn/).\n" +
                    "2. Vào mục **Thủ tục Một cửa** -> **Đăng ký giấy tờ xác nhận**.\n" +
                    "3. Chọn loại giấy cần cấp và gửi yêu cầu. Phòng Công tác Sinh viên sẽ xử lý trong vòng 01 - 02 ngày làm việc và thông báo nhận qua email trường.";
        } else {
            answer = "Trợ lý học vụ DUT đã ghi nhận câu hỏi của bạn. Hệ thống đang đối soát dữ liệu với kho văn bản quy chế đào tạo và chính sách sinh viên của Nhà trường. Bạn có thể tham khảo các câu hỏi thường gặp bên dưới hoặc chọn trực tiếp mục tra cứu quy chế.";
        }

        return ChatDto.Response.builder()
                .answer(answer)
                .citations(citations)
                .suggestions(suggestions)
                .build();
    }
}
