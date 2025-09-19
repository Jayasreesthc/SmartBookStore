package com.bookStore.controller;

import com.bookStore.entity.ContactRequest;
import com.bookStore.service.ContactRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ContactController {
    @Autowired
    private ContactRequestService service;

    @PostMapping("/contact")
    public String saveContact(@ModelAttribute ContactRequest request, RedirectAttributes redirectAttributes) {
        service.save(request);
        redirectAttributes.addFlashAttribute("success", "Your message has been sent successfully!");
        return "redirect:/home";  // back to contact page
    }

    // Admin view
    @GetMapping("/admin/contacts")
    public String viewAllContacts(Model model) {
        model.addAttribute("contacts", service.getAllRequests());
        return "admin_contacts";
    }
}
