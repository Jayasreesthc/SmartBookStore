package com.bookStore.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;

@Controller
public class pdfController {

    private final Path pdfStorage = Paths.get("D:Java/SpringBoot/BookStore/uploads");

    public pdfController() throws IOException {
        if (!Files.exists(pdfStorage)) {
            Files.createDirectories(pdfStorage);
        }
    }

    // Admin upload page
    @GetMapping("/upload-pdf")
    public String uploadPage() {
        return "viewPdf"; // Thymeleaf template
    }

    @PostMapping("/upload-pdf")
    public String uploadPdf(@RequestParam("file") MultipartFile file, Model model) throws IOException {
        if (!file.isEmpty() && file.getOriginalFilename().endsWith(".pdf")) {
            Path target = pdfStorage.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
            model.addAttribute("message", "Uploaded successfully: " + file.getOriginalFilename());
        } else {
            model.addAttribute("message", "Only PDF files are allowed!");
        }
        var pdfList = Files.list(pdfStorage)
                .map(path -> {
                    try {
                        return new PdfInfo(
                                path.getFileName().toString(),
                                Files.size(path) / 1024   // size in KB
                        );
                    } catch (IOException e) {
                        return new PdfInfo(path.getFileName().toString(), 0L);
                    }
                })
                .toList();

        model.addAttribute("pdfs", pdfList);

        return "viewPdf";
    }

    // User page: list PDFs
    @GetMapping("/pdf-list")
    public String listPdfs(Model model) throws IOException {
        // List files with their size in KB
        var pdfList = Files.list(pdfStorage)
                .map(path -> {
                    try {
                        return new PdfInfo(
                                path.getFileName().toString(),
                                Files.size(path) / 1024   // size in KB
                        );
                    } catch (IOException e) {
                        return new PdfInfo(path.getFileName().toString(), 0L);
                    }
                })
                .toList();

        model.addAttribute("pdfs", pdfList);
        return "viewPdf";
    }


    @GetMapping("/pdf-clist")
    public String listCPdfs(Model model) throws IOException {
        // List files with their size in KB
        var pdfList = Files.list(pdfStorage)
                .map(path -> {
                    try {
                        return new PdfInfo(
                                path.getFileName().toString(),
                                Files.size(path) / 1024   // size in KB
                        );
                    } catch (IOException e) {
                        return new PdfInfo(path.getFileName().toString(), 0L);
                    }
                })
                .toList();

        model.addAttribute("pdfs", pdfList);
        return "viewcpdf";
    }

    // Download endpoint
    @GetMapping("/download-pdf")
    public ResponseEntity<Resource> downloadPdf(@RequestParam("file") String filename) throws MalformedURLException {
        Path filePath = pdfStorage.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }

    // DTO for PDF info
    public static class PdfInfo {
        private String name;
        private long sizeKb;

        public PdfInfo(String name, long sizeKb) {
            this.name = name;
            this.sizeKb = sizeKb;
        }

        public String getName() { return name; }
        public long getSizeKb() { return sizeKb; }
    }

    @GetMapping("/view-pdf")
    public ResponseEntity<Resource> viewPdf(@RequestParam("file") String filename) throws MalformedURLException {
        Path filePath = pdfStorage.resolve(filename).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (!resource.exists() || !resource.isReadable()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @GetMapping("/delete-pdf")
    public String deletePdf(@RequestParam("file") String filename) throws IOException {
        Path filePath = pdfStorage.resolve(filename).normalize();
        Files.deleteIfExists(filePath);
        return "redirect:/pdf-list"; // refresh the list after delete
    }



}
