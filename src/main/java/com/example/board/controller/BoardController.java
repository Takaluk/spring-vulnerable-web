package com.example.board.controller;

import com.example.board.model.Post;
import com.example.board.model.User;
import com.example.board.service.BoardService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class BoardController {
    private final BoardService boardService;

    // Home page
    @GetMapping("/")
    public String home(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        model.addAttribute("user", user);
        return "home";
    }

    // Board page for a specific department
    @GetMapping("/board/{department}")
    public String board(@PathVariable String department, HttpSession session, Model model) {
//        User user = (User) session.getAttribute("user");
//        if (user == null || (!user.getDepartment().equals(department) && !user.getRole().equals("부장"))) {
//            return "error";
//        }

        List<Post> posts = boardService.getPostsByDepartment(department);
        model.addAttribute("posts", posts);
        model.addAttribute("department", department);
        return "board";
    }

    // Page to create a new post
    @GetMapping("/board/{department}/post/new")
    public String newPostPage(@PathVariable String department, Model model) {
        model.addAttribute("department", department);
        return "post_form";
    }

    // Handle form submission to create a new post
    @PostMapping("/board/{department}/post")
    public String createPost(@PathVariable String department, Post post, HttpSession session) throws UnsupportedEncodingException {
        // Set department for the post
        post.setDepartment(department);

        // Optionally, associate the post with the logged-in user
        User user = (User) session.getAttribute("user");
        if (user != null) {
            post.setAuthor(user.getUsername()); // Assuming Post has an author field
        }

        // Save the post
        boardService.savePost(post);

        // Redirect back to the department's board, ensuring the department name is URL-encoded
        String encodedDepartment = URLEncoder.encode(department, "UTF-8");
        return "redirect:/board/" + encodedDepartment;
    }

    // Handle deleting a post
    @PostMapping("/board/{department}/post/{id}/delete")
    public String deletePost(@PathVariable String department, @PathVariable Long id, @RequestParam String password) throws UnsupportedEncodingException {
        boardService.getPostById(id).ifPresent(post -> {
            if (post.getPassword().equals(password)) {
                boardService.deletePost(id);
            }
        });

        // URL-encode the department name in case it contains special characters
        String encodedDepartment = URLEncoder.encode(department, "UTF-8");
        return "redirect:/board/" + encodedDepartment;
    }
}
