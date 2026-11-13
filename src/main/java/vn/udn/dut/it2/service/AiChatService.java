package vn.udn.dut.it2.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;
import org.springframework.web.server.ResponseStatusException;
import vn.udn.dut.it2.config.AiProperties;
import vn.udn.dut.it2.model.dto.ChatDto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AiChatService {

    private static final List<String> DEFAULT_SUGGESTIONS = Arrays.asList(
            "Tiêu chuẩn học bổng loại Giỏi là bao nhiêu?",
            "Điều kiện số tín chỉ tối thiểu để xét học bổng?",
            "Thủ tục xin cấp giấy xác nhận sinh viên trực tuyến?",
            "Hạn chót rút bớt học phần kỳ này là khi nào?"
    );

    private final AiProperties aiProperties;
    private final RestClient aiRestClient;
    private final ObjectMapper objectMapper;

    public ChatDto.Response processChat(ChatDto.Request request) {
        if ("mock".equalsIgnoreCase(aiProperties.getProvider())) {
            return mockResponse(request.getMessage());
        }

        if (!"gemini".equalsIgnoreCase(aiProperties.getProvider())) {
            throw new ResponseStatusException(
                    HttpStatus.SERVICE_UNAVAILABLE,
                    "Unsupported AI provider: " + aiProperties.getProvider());
        }
        if (aiProperties.getApiKey() == null || aiProperties.getApiKey().isBlank()) {
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "AI_API_KEY is not configured");
        }

        JsonNode retrieval = retrieveContext(request.getMessage());
        return generateAnswer(request, retrieval);
    }

    private JsonNode retrieveContext(String message) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("query", message);
        payload.put("topK", aiProperties.getRetrievalTopK());
        payload.put("top_k", aiProperties.getRetrievalTopK());

        try {
            JsonNode response = aiRestClient.post()
                    .uri(aiProperties.getRetrievalBaseUrl() + aiProperties.getRetrievalPath())
                    .body(payload)
                    .retrieve()
                    .body(JsonNode.class);
            if (response == null || response.isNull()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Retrieval service returned an empty response");
            }
            return response;
        } catch (RestClientException exception) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_GATEWAY, "Could not retrieve context from the AI search service", exception);
        }
    }

    private ChatDto.Response generateAnswer(ChatDto.Request request, JsonNode retrieval) {
        String context = retrieval.has("context")
                ? retrieval.get("context").asText()
                : retrieval.toString();
        String systemPrompt = aiProperties.getSystemPrompt();
        if (systemPrompt == null || systemPrompt.isBlank()) {
            systemPrompt = "Answer only from the supplied context and return valid JSON.";
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("systemInstruction", Map.of("parts", List.of(Map.of("text", systemPrompt))));
        body.put("contents", List.of(Map.of(
                "role", "user",
                "parts", List.of(Map.of("text", "CONTEXT:\n" + context + "\n\nQUESTION:\n" + request.getMessage()))
        )));
        body.put("generationConfig", Map.of(
                "temperature", 0.1,
                "maxOutputTokens", aiProperties.getMaxOutputTokens(),
                "responseMimeType", "application/json",
                "responseSchema", responseSchema()
        ));

        try {
            JsonNode response = aiRestClient.post()
                    .uri(aiProperties.getGeminiBaseUrl() + "/v1beta/models/"
                            + aiProperties.getModel() + ":generateContent?key=" + aiProperties.getApiKey())
                    .body(body)
                    .retrieve()
                    .body(JsonNode.class);
            String json = response == null
                    ? ""
                    : response.path("candidates").path(0).path("content").path("parts").path(0).path("text").asText();
            if (json.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini returned no answer");
            }
            return parseResponse(json);
        } catch (RestClientException exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini request failed", exception);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Gemini returned invalid JSON", exception);
        }
    }

    private ChatDto.Response parseResponse(String json) throws Exception {
        String normalized = json.trim();
        if (normalized.startsWith("```")) {
            normalized = normalized.replaceFirst("^```(?:json)?\\s*", "").replaceFirst("\\s*```$", "");
        }
        ChatDto.Response response = objectMapper.readValue(normalized, ChatDto.Response.class);
        if (response.getAnswer() == null || response.getAnswer().isBlank()) {
            throw new IllegalArgumentException("AI response is missing answer");
        }
        if (response.getSources() == null) {
            response.setSources(new ArrayList<>());
        }
        if (response.getSuggestions() == null) {
            response.setSuggestions(DEFAULT_SUGGESTIONS);
        }
        return response;
    }

    private Map<String, Object> responseSchema() {
        Map<String, Object> source = new LinkedHashMap<>();
        source.put("type", "OBJECT");
        source.put("properties", Map.of(
                "documentNumber", Map.of("type", "STRING"),
                "documentTitle", Map.of("type", "STRING"),
                "clause", Map.of("type", "STRING"),
                "excerpt", Map.of("type", "STRING"),
                "pageNumber", Map.of("type", "INTEGER"),
                "pdfUrl", Map.of("type", "STRING")
        ));
        source.put("required", List.of("documentNumber", "documentTitle", "clause", "excerpt", "pdfUrl"));

        Map<String, Object> schema = new LinkedHashMap<>();
        schema.put("type", "OBJECT");
        schema.put("properties", Map.of(
                "answer", Map.of("type", "STRING"),
                "sources", Map.of("type", "ARRAY", "items", source),
                "suggestions", Map.of("type", "ARRAY", "items", Map.of("type", "STRING"))
        ));
        schema.put("required", List.of("answer", "sources"));
        return schema;
    }

    private ChatDto.Response mockResponse(String message) {
        String lowerMessage = message == null ? "" : message.toLowerCase();
        List<ChatDto.Citation> sources = new ArrayList<>();
        String answer = "Trợ lý học vụ DUT đang chạy ở chế độ mock. Hãy cấu hình AI_PROVIDER=gemini "
                + "và AI_API_KEY để bật truy xuất văn bản và Gemini.";

        if (lowerMessage.contains("học bổng") || lowerMessage.contains("giỏi")) {
            answer = "Theo dữ liệu mẫu, tiêu chuẩn học bổng loại Giỏi là điểm học tập từ 8.00 "
                    + "đến 8.99 và điểm rèn luyện từ 80 điểm trở lên.";
            sources.add(ChatDto.Citation.builder()
                    .documentNumber("1414/QĐ-ĐHBK")
                    .documentTitle("Quy định xét cấp HBKKHT tại Trường ĐHBK-ĐHĐN")
                    .clause("Điều 4, Khoản 2")
                    .excerpt("Mức học bổng loại Giỏi căn cứ vào kết quả học tập và rèn luyện.")
                    .pdfUrl("/tai-lieu-riel/QD_1414_DHBK.pdf")
                    .build());
        }

        return ChatDto.Response.builder()
                .answer(answer)
                .sources(sources)
                .suggestions(DEFAULT_SUGGESTIONS)
                .build();
    }
}
