package UMC.WithYou.feature.post.controller;

import UMC.WithYou.common.annotation.AuthorizedMember;
import UMC.WithYou.common.apiPayload.WithUResponse;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.post.controller.dto.PostRequest.*;
import UMC.WithYou.feature.post.controller.dto.PostResponse.*;
import UMC.WithYou.feature.post.domain.Post;
import UMC.WithYou.feature.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@RestController
public class PostController {
    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }

    @Operation(summary = "게시글 작성")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT token", required = true, schema = @Schema(type = "String"), in = ParameterIn.HEADER),
            @Parameter(name = "member", hidden = true),
            @Parameter( name = "travelId" , description = "여행 Id", required = true, schema = @Schema(type = "Long"))
    })
    @PostMapping("api/v1/travels/{travelId}/posts")
    public WithUResponse<PublishResponse> publishPost(
            @AuthorizedMember Member member,
            @PathVariable("travelId") Long travelId,
            @RequestPart PublishRequestDTO request,
            @RequestPart List<MultipartFile> mediaList
        ){
        String text = request.getText();

        Long postId = postService.createPost(member, travelId, text, mediaList);
        return WithUResponse.onSuccess(new PublishResponse(postId));
    }


    @Operation(summary = "여행 로그에 해당하는 모든 포스트 조회")
    @Parameters({
            @Parameter( name = "travelId" , description = "여행 Id", required = true, schema = @Schema(type = "Long"))
    })
    @GetMapping("api/v1/travels/{travelId}/posts")
    public WithUResponse<List<ThumbnailResponseDTO>> getPostThumbnails(@PathVariable("travelId") Long travelId){
        List<Post> posts = postService.getScrapedPosts(travelId);

        return WithUResponse.onSuccess(posts.stream()
                .map(p -> new ThumbnailResponseDTO(p))
                .toList()
        );
    }

    @Operation(summary = "게시글 단건 조회")
    @Parameters({
            @Parameter( name = "travelId" , description = "여행 Id", required = true, schema = @Schema(type = "Long")),
            @Parameter( name = "postId" , description = "게시글 Id", required = true, schema = @Schema(type = "Long"))
    })
    @GetMapping("api/v1/posts/{postId}")
    public WithUResponse<PostResponseDTO> getPost(
            @PathVariable("postId") Long postId
    ){

        Post post = postService.getPost(postId);
        return WithUResponse.onSuccess(new PostResponseDTO(post));
    }


    @Operation(summary = "게시글 삭제")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT token", required = true, schema = @Schema(type = "String"), in = ParameterIn.HEADER),
            @Parameter(name = "member", hidden = true),
            @Parameter( name = "postId" , description = "게시글 Id", required = true, schema = @Schema(type = "Long"))
    })
    @DeleteMapping("api/v1/posts/{postId}")
    public WithUResponse<DeletionResponseDTO> deletePost(
            @AuthorizedMember Member member,
            @PathVariable("postId") Long postId
    ){
        postService.deletePost(member, postId);

        return WithUResponse.onSuccess(new DeletionResponseDTO(postId));
    }


    @Operation(summary = "게시글 수정")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT token", required = true, schema = @Schema(type = "String"), in = ParameterIn.HEADER),
            @Parameter(name = "member", hidden = true),
            @Parameter( name = "postId" , description = "게시글 Id", required = true, schema = @Schema(type = "Long"))
    })

    @PatchMapping("api/v1/posts/{postId}")
    public WithUResponse<EditResponseDTO> editPost(
            @AuthorizedMember Member member,
            @PathVariable("postId") Long postId,
            @RequestBody EditRequestDTO request
    ){
        String text = request.getText();
        Map<Long, Integer> newPositions = request.getNewPositions();
        Post post = postService.editPost(member, postId, text, newPositions);
        return WithUResponse.onSuccess(new EditResponseDTO(post.getId()));
    }



    @Operation(summary = "스크랩")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT token", required = true, schema = @Schema(type = "String"), in = ParameterIn.HEADER),
            @Parameter(name = "member", hidden = true),
            @Parameter( name = "postId" , description = "게시글 Id", required = true, schema = @Schema(type = "Long"))
    })
    @PostMapping("api/v1/posts/{postId}")
    public WithUResponse<ScrapeResponseDTO> scrapPost(
            @AuthorizedMember Member member,
            @PathVariable("postId") Long postId
    ){
        Boolean isScraped = postService.toggleScrap(member, postId);
        return WithUResponse.onSuccess(new ScrapeResponseDTO(postId, isScraped));
    }

    @Operation(summary = "회원이 스크랩한 모든 게시글 조회")
    @Parameters({
            @Parameter(name = "Authorization", description = "JWT token", required = true, schema = @Schema(type = "String"), in = ParameterIn.HEADER),
            @Parameter(name = "member", hidden = true),
    })
    @GetMapping("api/v1/posts")
    public WithUResponse<List<ThumbnailResponseDTO>> getScrapedPosts(
            @AuthorizedMember Member member
    ){
        List<Post> posts = postService.getScrapedPosts(member);

        return WithUResponse.onSuccess(
                posts.stream()
                        .map(p -> new ThumbnailResponseDTO(p))
                        .toList()
        );
    }



}
