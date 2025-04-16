package UMC.WithYou.feature.post.service;

import UMC.WithYou.common.apiPayload.code.status.ErrorStatus;
import UMC.WithYou.common.apiPayload.exception.handler.CommonErrorHandler;
import UMC.WithYou.feature.member.domain.Member;
import UMC.WithYou.feature.post.domain.Post;
import UMC.WithYou.feature.post.domain.PostMedia;
import UMC.WithYou.feature.post.domain.ScrapedPost;
import UMC.WithYou.feature.post.repository.PostMediaRepository;
import UMC.WithYou.feature.post.repository.PostRepository;
import UMC.WithYou.feature.post.repository.ScrapedPostRepository;
import UMC.WithYou.feature.travel.domain.Travel;
import UMC.WithYou.feature.travel.service.TravelService;
import UMC.WithYou.infra.s3.S3Service;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final TravelService travelService;
    private final ScrapedPostRepository scrapedPostRepository;
    private final S3Service s3Service;
    private final PostMediaRepository postMediaRepository;


    public Long createPost(Member member, Long travelId, String text, List<MultipartFile> mediaList){

        Travel travel = travelService.findTravelById(travelId);

        if (mediaList.isEmpty() || mediaList.size() > 10){
            throw new CommonErrorHandler(ErrorStatus.INVALID_MEDIA_COUNT);
        }

        Post post = new Post(member, travel, text);
        List<PostMedia> postMediaList = new ArrayList<>();

        for (int sequence = 0; sequence < mediaList.size(); sequence++){
            String fileName = s3Service.createFileName(mediaList.get(sequence).getOriginalFilename());
            String url = s3Service.uploadMedia(mediaList.get(sequence), fileName);
            postMediaList.add(new PostMedia(post, sequence, url, fileName));
        }

        post.setPostMediaList(postMediaList);

        postRepository.save(post);
        return post.getId();
    }

    public List<Post> getScrapedPosts(Long travelId){
        travelService.findTravelById(travelId);

        return postRepository.findByTravelId(travelId);
    }

    public Post getPost(Long postId){
        return this.findPostById(postId);
    }

    public void deletePost(Member member, Long postId){
        Post post = this.findPostById(postId);
        this.validatePostOwnership(member, post);

        for (PostMedia postMedia : post.getPostMediaList()){
            s3Service.deleteFile(postMedia.getFineName());
        }

        postRepository.delete(post);
    }


    public Post editPost(Member member, Long postId, String text, Map<Long, Integer> newPositions){
        Post post = this.findPostById(postId);
        this.validatePostOwnership(member, post);

        List<PostMedia> postMediaList = post.getPostMediaList();

        for (int i = 0; i < postMediaList.size();){
            PostMedia postMedia = postMediaList.get(i);
            Long mediaId = postMedia.getId();
            int newPosition = newPositions.get(mediaId);

            if (newPosition == -1){
                postMediaRepository.delete(postMedia);
                postMediaList.remove(i);
                System.out.println(postMediaList.size());
                s3Service.deleteFile(postMedia.getFineName());
            }
            else{
                postMedia.setPosition(newPosition);
                i++;
            }
        }
        post.edit(text);
        return post;
    }

    public List<Post> getScrapedPosts(Member member) {
        List<ScrapedPost> scrapedPosts = scrapedPostRepository.findScrapedPostsByMember(member);
        List<Post> posts = new ArrayList<>();
        for (ScrapedPost scrapedPost: scrapedPosts){
            posts.add(scrapedPost.getPost());
        }
        return posts;
    }

    public Boolean toggleScrap(Member member, Long postId) {
        Post post = this.findPostById(postId);


        Optional<ScrapedPost> existingScrapedPost = scrapedPostRepository.findScrapedPostByMemberAndPost(member, post);
        if (existingScrapedPost.isPresent()){
            scrapedPostRepository.delete(existingScrapedPost.get());
            return false;
        }

        ScrapedPost scrapedPost = new ScrapedPost(post, member);
        scrapedPostRepository.save(scrapedPost);
        return true;
    }








    public Post findPostById(Long postId){
        return postRepository.findById(postId).orElseThrow(
                ()->new CommonErrorHandler(ErrorStatus.POST_NOT_FOUND)
        );
    }

    private void validatePostOwnership(Member member, Post post) {
        if (!member.isSameId(post.getMember().getId())){
            throw new CommonErrorHandler(ErrorStatus.UNAUTHORIZED_ACCESS_TO_POST);
        }
    }
}
