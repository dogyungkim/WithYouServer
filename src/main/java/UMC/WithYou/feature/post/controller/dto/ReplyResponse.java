package UMC.WithYou.feature.post.controller.dto;

import UMC.WithYou.feature.post.domain.Reply;
import lombok.AllArgsConstructor;
import lombok.Getter;


public class ReplyResponse {


    @Getter
    public static class WriteResponseDTO{
        private Long commentId;
        private Long replyId;
        private String content;

        public WriteResponseDTO(Reply reply) {
            this.commentId = reply.getComment().getId();
            this.replyId = reply.getId();
            this.content = reply.getContent();
        }
    }

    @Getter
    @AllArgsConstructor
    public static class DeletionResponseDTO{
        private Long replyId;
    }

    @Getter
    @AllArgsConstructor
    public static class EditResponseDTO{
        private Long replyId;
    }
}
