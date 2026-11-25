
package com.SpringGrok.Grok.service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;

import org.springframework.stereotype.Service;

import com.SpringGrok.Grok.response.ResumeBuilderResponse;

@Service
public class ResumePdfService {

    private static final float MARGIN = 50;

    private static final PDType1Font BOLD_FONT =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);

    private static final PDType1Font REGULAR_FONT =
            new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    public byte[] generateResumePdf(
            ResumeBuilderResponse resume) throws IOException {

        try (PDDocument document = new PDDocument();
             ByteArrayOutputStream output =
                     new ByteArrayOutputStream()) {

            PdfLayout layout = new PdfLayout(document);

            // Candidate name
            layout.writeText(
                    safe(resume.getFullName()),
                    BOLD_FONT, 22);

            // Contact information
            String contact = safe(resume.getEmail())
                    + " | " + safe(resume.getPhone());

            layout.writeText(contact, REGULAR_FONT, 10);

            if (hasText(resume.getLinkedIn())) {
                layout.writeText(
                        safe(resume.getLinkedIn()),
                        REGULAR_FONT, 10);
            }

            layout.addDivider();

            // Professional Summary
            addSection(layout, "PROFESSIONAL SUMMARY");

            if (hasText(resume.getProfessionalSummary())) {
                layout.writeParagraph(
                        resume.getProfessionalSummary(),
                        REGULAR_FONT, 10);
            }

            // Technical Skills
            if (hasItems(resume.getTechnicalSkills())) {
                addSection(layout, "TECHNICAL SKILLS");

                layout.writeParagraph(
                        String.join(", ", resume.getTechnicalSkills()),
                        REGULAR_FONT, 10);
            }

            // Experience
            if (hasText(resume.getExperience())) {
                addSection(layout, "EXPERIENCE");

                layout.writeParagraph(
                        resume.getExperience(),
                        REGULAR_FONT, 10);
            }

            // Projects
            if (hasItems(resume.getProjects())) {
                addSection(layout, "PROJECTS");

                for (String project : resume.getProjects()) {
                    if (hasText(project)) {
                        layout.writeParagraph(
                                "- " + project,
                                REGULAR_FONT, 10);
                    }
                }
            }

            // Education
            if (hasText(resume.getEducation())) {
                addSection(layout, "EDUCATION");

                layout.writeParagraph(
                        resume.getEducation(),
                        REGULAR_FONT, 10);
            }

            // Certifications - display only when available
            if (hasItems(resume.getCertifications())) {
                addSection(layout, "CERTIFICATIONS");

                for (String certification :
                        resume.getCertifications()) {

                    if (hasText(certification)) {
                        layout.writeParagraph(
                                "- " + certification,
                                REGULAR_FONT, 10);
                    }
                }
            }

            layout.close();

            document.save(output);

            return output.toByteArray();
        }
    }

    private void addSection(
            PdfLayout layout,
            String heading) throws IOException {

        layout.writeHeading(heading);
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private boolean hasItems(List<String> items) {
        return items != null
                && items.stream().anyMatch(this::hasText);
    }

    private String safe(String value) {
        if (value == null) {
            return "";
        }

        // Standard PDF fonts support a limited character set.
        return value.replaceAll("[^\\x20-\\x7E]", " ");
    }

    // Helper class responsible for PDF page layout
    private static class PdfLayout {

        private final PDDocument document;

        private PDPageContentStream content;

        private float y;

        private final float pageWidth =
                PDRectangle.A4.getWidth();

        private final float usableWidth =
                pageWidth - (2 * MARGIN);

        PdfLayout(PDDocument document) throws IOException {
            this.document = document;
            createPage();
        }

        private void createPage() throws IOException {

            PDPage page = new PDPage(PDRectangle.A4);

            document.addPage(page);

            content = new PDPageContentStream(document, page);

            y = page.getMediaBox().getHeight() - MARGIN;
        }

        private void checkSpace(float required)
                throws IOException {

            if (y - required < MARGIN) {

                content.close();

                createPage();
            }
        }

        void writeText(
                String text,
                PDType1Font font,
                float size) throws IOException {

            if (text == null || text.isBlank()) {
                return;
            }

            checkSpace(size + 8);

            content.beginText();

            content.setFont(font, size);

            content.setNonStrokingColor(0.12f, 0.16f, 0.23f);

            content.newLineAtOffset(MARGIN, y);

            content.showText(text);

            content.endText();

            y -= size + 8;
        }

        void writeParagraph(
                String paragraph,
                PDType1Font font,
                float size) throws IOException {

            if (paragraph == null || paragraph.isBlank()) {
                return;
            }

            String[] words = safeText(paragraph).split("\\s+");

            StringBuilder line = new StringBuilder();

            for (String word : words) {

                String testLine = line.length() == 0
                        ? word
                        : line + " " + word;

                float textWidth =
                        font.getStringWidth(testLine)
                        / 1000 * size;

                if (textWidth > usableWidth
                        && line.length() > 0) {

                    writeText(line.toString(), font, size);

                    line = new StringBuilder(word);

                } else {

                    line = new StringBuilder(testLine);
                }
            }

            if (line.length() > 0) {
                writeText(line.toString(), font, size);
            }

            y -= 3;
        }

        void writeHeading(String heading) throws IOException {

            checkSpace(35);

            y -= 8;

            writeText(heading, BOLD_FONT, 12);

            content.setStrokingColor(0.27f, 0.39f, 0.59f);

            content.setLineWidth(0.8f);

            content.moveTo(MARGIN, y + 3);

            content.lineTo(pageWidth - MARGIN, y + 3);

            content.stroke();

            y -= 8;
        }

        void addDivider() throws IOException {

            checkSpace(15);

            content.setStrokingColor(0.27f, 0.39f, 0.59f);

            content.setLineWidth(1);

            content.moveTo(MARGIN, y);

            content.lineTo(pageWidth - MARGIN, y);

            content.stroke();

            y -= 15;
        }

        private String safeText(String value) {

            return value.replaceAll("[^\\x20-\\x7E]", " ");
        }

        void close() throws IOException {

            if (content != null) {
                content.close();
            }
        }
    }
}
