package UMC.WithYou.feature.post.service;


import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.member.service.MemberService;
import UMC.WithYou.feature.post.domain.Comment;
import UMC.WithYou.feature.post.domain.Post;
import UMC.WithYou.feature.post.repository.CommentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final PostService postService;

    public CommentService(CommentRepository commentRepository, MemberService memberService, PostService postService) {
        this.commentRepository = commentRepository;
        this.memberService = memberService;
        this.postService = postService;
    }

    public Comment writeComment(Member member, Long postId, String content) {
        Post post = postService.findPostById(postId);
        Comment comment = new Comment(post, member, content);

        return commentRepository.save(comment);
    }

    public void deleteComment(Member member, Long commentId) {
        Comment comment = this.findCommentById(commentId);
        this.validateCommentOwnership(member, comment);

        commentRepository.delete(comment);
    }

    public Comment editComment(Member member, Long commentId, String content) {
        Comment comment = this.findCommentById(commentId);
        this.validateCommentOwnership(member, comment);

        comment.setContent(content);
        return comment;
    }

    private void validateCommentOwnership(Member member, Comment comment){
        if (!member.isSameId(comment.getMember().getId())) {
            throw new CommonErrorHandler(ErrorStatus.UNAUTHORIZED_ACCESS_TO_COMMENT);
        }

    }

    public Comment findCommentById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(
                ()->new CommonErrorHandler(ErrorStatus.COMMENT_NOT_FOUND)
        );
    }
}
