package UMC.WithYou.feature.post.controller.dto;

import lombok.Getter;

public class CommentRequest {

    @Getter
    public static class WriteRequestDTO {
        private String content;
    }
    @Getter
    public static class EditRequestDTO {
        private String content;
    }
}
