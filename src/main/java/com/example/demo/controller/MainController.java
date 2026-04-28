package com.example.demo.controller;


import com.example.demo.model.Book;
import com.example.demo.model.User;
import com.example.demo.repository.BookRepo;
import com.example.demo.repository.UserRepo;
import org.springframework.ui.Model;
import jakarta.servlet.http.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class MainController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    BookRepo bookRepo;

    // show login page
    @GetMapping("/")
    public String loginPage() {
        return "login";
    }

    // handle login
    @PostMapping("/login")
    public void login(HttpServletRequest request, HttpServletResponse response) throws Exception {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        User user = userRepo.findByUsernameAndPassword(username, password);

        if (user != null) {
            HttpSession session = request.getSession();
            session.setAttribute("user", username);

            response.sendRedirect("/addBook");
        } else {
            response.getWriter().println("Invalid login");
        }
    }

    // show add book page
    @GetMapping("/addBook")
    public String addBookPage(HttpSession session, HttpServletResponse response, Model model) throws Exception {

        if (session.getAttribute("user") == null) {
            response.sendRedirect("/");
            return null;
        }
        model.addAttribute("name", session.getAttribute("user"));
        return "addBook";
    }

    // handle add book
    @PostMapping("/addBook")
    public void saveBook(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws Exception {

        if (session.getAttribute("user") == null) {
            response.sendRedirect("/");
            return;
        }

        String title = request.getParameter("title");
        String author = request.getParameter("author");

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);

        bookRepo.save(book);

        response.getWriter().println("Book Added Successfully");
    }
}