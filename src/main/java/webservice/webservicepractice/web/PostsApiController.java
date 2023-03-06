package webservice.webservicepractice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import webservice.webservicepractice.service.posts.PostsService;
import webservice.webservicepractice.web.dto.PostsResponseDto;
import webservice.webservicepractice.web.dto.PostsSaveRequestDto;
import webservice.webservicepractice.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@RestController
public class PostsApiController {

    private final PostsService postsService;

    /**
     * 게시글 저장
     */
    @PostMapping("/api/v1/posts")
    public Long save(@RequestBody PostsSaveRequestDto requestDto){
        return postsService.save(requestDto);
    }



    /**
     * 게시글 수정
     * put : 리소스를 대체, 해당 리소스가 없으면 생성
     */
    @PutMapping("/api/v1/posts/{id}")
    public Long update(@PathVariable Long id, @RequestBody PostsUpdateRequestDto requestDto){
        return postsService.update(id, requestDto);
    }


    /**
     * 게시글 조회
     */
    @GetMapping("/api/v1/posts/{id}")
    public PostsResponseDto findById(@PathVariable Long id){
        return postsService.findById(id);
    }





}
