package com.bookStore.controller;

import com.bookStore.entity.Client;
import com.bookStore.service.MyBookListService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MyBookListController {

    @Autowired
    private MyBookListService myBookService;

    @RequestMapping("/deleteMyList/{id}")
    public String deleteMyList(@PathVariable("id")int id, HttpSession session,
                               RedirectAttributes redirectAttributes){
        Client client = (Client) session.getAttribute("loggedInClient");
        if (client == null) {
            return "redirect:/Login_client";
        }
        myBookService.deleteFromMyBooks(id, client.getId());
        redirectAttributes.addFlashAttribute("message", "Book removed from MyBooks");
        return "redirect:/my_books";
    }
}
