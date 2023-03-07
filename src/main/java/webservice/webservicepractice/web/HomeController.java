package webservice.webservicepractice.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import webservice.webservicepractice.service.posts.PostsService;
import webservice.webservicepractice.web.dto.PostsSaveRequestDto;

@RequiredArgsConstructor
@Controller
public class HomeController {

    private final PostsService postsService;

    @GetMapping("/")
    public String home(Model model){
        model.addAttribute("posts", postsService.findAllDesc());
        return "home";
    }


}
