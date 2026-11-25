
package com.SpringGrok.Grok.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.SpringGrok.Grok.response.ResumeDetails;

@Service
public class ResumeDetailsExtractionService {

    private final ChatClient chatClient;

    public ResumeDetailsExtractionService(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    public ResumeDetails extractResumeDetails(String resumeText) {

        String prompt = """
                You are an expert resume parser.

                Your task is to extract complete and accurate
                information from the candidate's original resume.

                ORIGINAL RESUME:
                ----------------
                %s
                ----------------

                EXTRACTION RULES:

                1. PERSONAL DETAILS:
                   Extract full name, email, phone and LinkedIn URL.

                2. PROFESSIONAL SUMMARY:
                   Extract the complete professional summary.
                   Do not shorten or rewrite it.

                3. EDUCATION:
                   Extract complete education details including
                   degree, college, university, year and CGPA
                   wherever available.

                4. EXPERIENCE:
                   Extract complete work experience or internship
                   details, including company, role, duration,
                   responsibilities and achievements.
                   Do not create experience for a fresher.

                5. TECHNICAL SKILLS:
                   Extract all technical skills mentioned in
                   the original resume.
                   Do not omit any skill.

                6. PROJECTS - VERY IMPORTANT:
                   Extract every project mentioned in the resume.
                   Do not return only project names or titles.

                   For each project, include all available details:
                   - Project title
                   - Project description
                   - Technologies and tools used
                   - Features implemented
                   - Responsibilities and contributions
                   - Database details
                   - Important implementation details

                   Preserve the original meaning and details.
                   Do not summarize a complete project into
                   a single short title.

                   Each project must be a separate string
                   inside the projects JSON array.

                   If the original resume contains only a title
                   without a description, return only that title.
                   Never invent missing project information.

                7. CERTIFICATIONS:
                   Extract all certification names and related
                   details mentioned in the resume.

                8. ACCURACY:
                   Extract only information present in the
                   original resume.
                   Never invent personal details, skills,
                   projects, experience or certifications.

                9. MISSING INFORMATION:
                   Use null for unavailable string fields.
                   Use empty arrays for unavailable list fields.

                10. OUTPUT:
                    Return valid JSON only.
                    Use exactly these field names:
                    fullName, email, phone, linkedIn,
                    professionalSummary, education, experience,
                    targetRole, technicalSkills, projects,
                    certifications.

                REQUIRED JSON STRUCTURE:

                {
                  "fullName": null,
                  "email": null,
                  "phone": null,
                  "linkedIn": null,
                  "professionalSummary": null,
                  "education": null,
                  "experience": null,
                  "targetRole": null,
                  "technicalSkills": [],
                  "projects": [],
                  "certifications": []
                }
                """.formatted(resumeText);

        return chatClient.prompt()
                .user(prompt)
                .call()
                .entity(ResumeDetails.class);
    }
}
