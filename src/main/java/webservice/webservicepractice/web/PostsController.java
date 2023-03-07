package webservice.webservicepractice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import webservice.webservicepractice.service.posts.PostsService;
import webservice.webservicepractice.web.dto.PostsResponseDto;
import webservice.webservicepractice.web.dto.PostsSaveRequestDto;
import webservice.webservicepractice.web.dto.PostsUpdateRequestDto;

@RequiredArgsConstructor
@Controller
public class PostsController {

    private final PostsService postsService;

     /**
     * 게시글 저장
     */

    @GetMapping("/posts/save")
    public String postsSaveForm(Model model){
        model.addAttribute("postsForm", new PostsSaveRequestDto());

        return "createPostsForm";
    }

    @PostMapping("/posts/save")
    public String postsSave(@ModelAttribute PostsSaveRequestDto postsSaveRequestDto){
        postsService.save(postsSaveRequestDto);

        return "redirect:/";
    }


    /**
     * 게시글 수정
     */
    @GetMapping("/posts/edit/{id}")
    public String postEdit(@PathVariable Long id, Model model){
        PostsResponseDto editPost = postsService.findById(id);
        model.addAttribute("editPost", editPost);

        return "editPost";
    }

    @PostMapping("/posts/edit/{id}")
    public String postUpdate(@PathVariable Long id, @ModelAttribute PostsUpdateRequestDto postsUpdateRequestDto){

        postsService.update(id, postsUpdateRequestDto);

        return "redirect:/";
    }

    /**
     * 게시글 삭제
     */
    @GetMapping("/posts/edit/{id}/delete")
    public String postDelete(@PathVariable Long id){
        postsService.delete(id);

        return "redirect:/";
    }








}
