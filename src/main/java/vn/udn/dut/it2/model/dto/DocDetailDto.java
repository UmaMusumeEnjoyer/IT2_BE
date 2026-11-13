package vn.udn.dut.it2.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

public class DocDetailDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {
        private Long id;
        private String title;
        private String summary;
        private List<Step> steps;

        @JsonProperty("file_url")
        private String fileUrl;

        private List<String> tags;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Step {
        private Integer order;
        private String title;
        private String description;
    }
}
