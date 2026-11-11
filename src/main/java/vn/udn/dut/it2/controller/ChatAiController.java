package vn.udn.dut.it2.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import vn.udn.dut.it2.model.dto.ChatDto;
import vn.udn.dut.it2.service.AiChatService;

@RestController
@RequestMapping("/api/ai-chat")
@RequiredArgsConstructor
@Tag(name = "4. AI Academic Assistant", description = "API Trợ lý học vụ AI (RAG hỏi đáp quy chế DUT)")
public class ChatAiController {

    private final AiChatService aiChatService;

    @PostMapping("/query")
    @Operation(summary = "Gửi câu hỏi học vụ tới trợ lý AI, nhận câu trả lời kèm trích dẫn văn bản pháp quy")
    public ResponseEntity<ChatDto.Response> queryAssistant(@RequestBody ChatDto.Request request) {
        return ResponseEntity.ok(aiChatService.processChat(request));
    }
}
