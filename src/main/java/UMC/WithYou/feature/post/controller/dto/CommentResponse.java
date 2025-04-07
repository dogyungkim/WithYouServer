package UMC.WithYou.feature.post.controller.dto;

import UMC.WithYou.feature.post.domain.Comment;
import lombok.Getter;

public class CommentResponse {
    @Getter
    public static class WriteResponseDTO {
        private Long postId;
        private Long memberId;
        private Long commentId;
        private String content;

        public WriteResponseDTO(Comment comment){
            this.postId = comment.getPost().getId();
            this.memberId = comment.getMember().getId();
            this.commentId = comment.getId();
            this.content = comment.getContent();
        }
    }

    @Getter
    public static class DeletionResponseDTO{
        private Long commentId;
        public DeletionResponseDTO( Long commentId){
            this.commentId = commentId;
        }
    }

    @Getter
    public static class EditResponseDTO {
        private Long postId;
        private Long commentId;
        private String content;
        public EditResponseDTO(Comment comment){
            this.postId = comment.getPost().getId();
            this.commentId = comment.getId();
            this.content = comment.getContent();
        }
    }

}


