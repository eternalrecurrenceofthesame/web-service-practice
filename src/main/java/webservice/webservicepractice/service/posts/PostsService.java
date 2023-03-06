package webservice.webservicepractice.service.posts;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import webservice.webservicepractice.domain.posts.Posts;
import webservice.webservicepractice.domain.posts.PostsRepository;
import webservice.webservicepractice.web.dto.PostsListResponseDto;
import webservice.webservicepractice.web.dto.PostsResponseDto;
import webservice.webservicepractice.web.dto.PostsSaveRequestDto;
import webservice.webservicepractice.web.dto.PostsUpdateRequestDto;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class PostsService {

    private final PostsRepository postsRepository;

    /**
     * 게시글 저장
     */
    @Transactional
    public Long save(PostsSaveRequestDto requestDto){

        return postsRepository.save(requestDto.toEntity())
                .getId();
    }


    /**
     * 게시글 수정
     *
     * API 예외 처리시 @ExceptionHandler 를 통해 일괄 처리 가능
     */
    @Transactional
    public Long update(Long id, PostsUpdateRequestDto requestDto){
        Posts posts = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        // 도메인 메서드
        posts.update(requestDto.getTitle(), requestDto.getContent());

        return id;

    }

    /**
     * 게시글 조회
     */
    public PostsResponseDto findById(Long id){
        Posts entity = postsRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. id =" + id));

        return new PostsResponseDto(entity);
    }


    /**
     * 게시글 목록 조회
     */
    @Transactional(readOnly=true)
    public List<PostsListResponseDto> findAllDesc(){
        return postsRepository.findAllDesc().stream()
                .map(PostsListResponseDto::new)
                .collect(Collectors.toList());
    }


}
