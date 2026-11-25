
package com.SpringGrok.Grok.service;

import java.io.IOException;

import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ResumeTextExtractionService {

    public String extractText(MultipartFile file) throws IOException {

        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("Please upload a PDF resume.");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null ||
                !fileName.toLowerCase().endsWith(".pdf")) {
            throw new IllegalArgumentException("Only PDF files are allowed.");
        }

        try (PDDocument document = Loader.loadPDF(file.getBytes())) {

            if (document.isEncrypted()) {
                throw new IllegalArgumentException(
                    "Password-protected PDFs are not supported."
                );
            }

            PDFTextStripper stripper = new PDFTextStripper();

            String extractedText = stripper.getText(document);

            if (extractedText == null || extractedText.isBlank()) {
                throw new IllegalArgumentException(
                    "No readable text found in this PDF."
                );
            }

            return extractedText;
        }
    }
}
