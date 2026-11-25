
package com.SpringGrok.Grok.controller;

import java.io.IOException;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.multipart.MultipartFile;

import com.SpringGrok.Grok.request.ResumeRequest;
import com.SpringGrok.Grok.request.ResumeBuilderRequest;

import com.SpringGrok.Grok.response.ResumeAnalysisResponse;
import com.SpringGrok.Grok.response.ResumeBuilderResponse;

import com.SpringGrok.Grok.service.ResumeService;
import com.SpringGrok.Grok.service.ResumeBuilderService;
import com.SpringGrok.Grok.service.ResumePdfService;
import com.SpringGrok.Grok.service.ResumeTextExtractionService;


@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/api/resume")
public class ResumeController {

    private final ResumeService resumeService;
    private final ResumeBuilderService resumeBuilderService;
    private final ResumePdfService resumePdfService;
    private final ResumeTextExtractionService resumeTextExtractionService;

    // Constructor Injection
    public ResumeController(
            ResumeService resumeService,
            ResumeBuilderService resumeBuilderService,
            ResumePdfService resumePdfService,
            ResumeTextExtractionService resumeTextExtractionService) {

        this.resumeService = resumeService;
        this.resumeBuilderService = resumeBuilderService;
        this.resumePdfService = resumePdfService;
        this.resumeTextExtractionService = resumeTextExtractionService;
    }

    // API 1: Analyze resume text using Spring AI
    @PostMapping("/analyze")
    public ResumeAnalysisResponse analyzeResume(
            @RequestBody ResumeRequest request) {

        return resumeService.analyzeResume(request);
    }

    // API 2: Upload PDF and analyze against Job Description
    @PostMapping(
            value = "/analyze-upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<ResumeAnalysisResponse> analyzeUploadedResume(
            @RequestParam("file") MultipartFile file,
            @RequestParam("companyName") String companyName,
            @RequestParam("jobRole") String jobRole,
            @RequestParam("jobDescription") String jobDescription
    ) throws IOException {

        // Step 1: Extract text from uploaded PDF
        String extractedResume =
                resumeTextExtractionService.extractText(file);

        // Step 2: Create ResumeRequest object
        ResumeRequest request = new ResumeRequest();

        request.setResume(extractedResume);
        request.setCompanyName(companyName);
        request.setJobRole(jobRole);
        request.setJobDescription(jobDescription);

        // Step 3: Analyze resume
        ResumeAnalysisResponse result =
                resumeService.analyzeResume(request);

        // Step 4: Return structured response
        return ResponseEntity.ok(result);
    }

    // API 3: Generate resume using AI
    @PostMapping("/builder/generate")
    public ResumeBuilderResponse generateResume(
            @RequestBody ResumeBuilderRequest request) {

        return resumeBuilderService.generateResume(request);
    }

    // API 4: Generate and download resume PDF
    @PostMapping(
            value = "/builder/download",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> downloadResume(
            @RequestBody ResumeBuilderRequest request)
            throws IOException {

        // Step 1: Generate resume using AI
        ResumeBuilderResponse resume =
                resumeBuilderService.generateResume(request);

        // Step 2: Convert generated resume into PDF
        byte[] pdf =
                resumePdfService.generateResumePdf(resume);

        // Step 3: Return downloadable PDF
        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"Resume.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }

    // API 5: Download edited resume PDF
    @PostMapping(
            value = "/builder/download-edited",
            produces = MediaType.APPLICATION_PDF_VALUE
    )
    public ResponseEntity<byte[]> downloadEditedResume(
            @RequestBody ResumeBuilderResponse editedResume)
            throws IOException {

        // Generate PDF using edited resume details
        byte[] pdf =
                resumePdfService.generateResumePdf(editedResume);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"Updated_Resume.pdf\""
                )
                .contentType(MediaType.APPLICATION_PDF)
                .contentLength(pdf.length)
                .body(pdf);
    }
}
