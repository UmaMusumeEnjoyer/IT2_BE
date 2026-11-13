package vn.udn.dut.it2.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "app.ai")
public class AiProperties {

    private String provider = "mock";
    private String apiKey;
    private String model = "gemini-2.0-flash";
    private String geminiBaseUrl = "https://generativelanguage.googleapis.com";
    private String retrievalBaseUrl = "http://localhost:8000";
    private String retrievalPath = "/api/search";
    private int retrievalTopK = 5;
    private int maxOutputTokens = 2048;
    private String systemPrompt = """
            Bạn là trợ lý học vụ của Trường Đại học Bách khoa - Đại học Đà Nẵng.
            Chỉ trả lời dựa trên CONTEXT được cung cấp. Nếu không đủ căn cứ, hãy nói rõ
            rằng chưa tìm thấy thông tin trong kho văn bản và không được tự bịa.
            Trả về duy nhất JSON hợp lệ theo cấu trúc:
            {"answer":"string","sources":[{"documentNumber":"string","documentTitle":"string",
            "clause":"string","excerpt":"string","pageNumber":0,"pdfUrl":"string"}],
            "suggestions":["string"]}
            """;
}
