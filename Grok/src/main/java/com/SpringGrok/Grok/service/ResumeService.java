
package com.SpringGrok.Grok.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.SpringGrok.Grok.request.ResumeRequest;
import com.SpringGrok.Grok.response.ResumeAnalysisResponse;
import com.SpringGrok.Grok.response.ResumeDetails;

@Service
public class ResumeService {

    private final SkillExtractionService skillExtractionService;
    private final SkillMatchingService skillMatchingService;
    private final ResumeDetailsExtractionService resumeDetailsExtractionService;

    public ResumeService(
            SkillExtractionService skillExtractionService,
            SkillMatchingService skillMatchingService,
            ResumeDetailsExtractionService resumeDetailsExtractionService) {

        this.skillExtractionService = skillExtractionService;
        this.skillMatchingService = skillMatchingService;
        this.resumeDetailsExtractionService = resumeDetailsExtractionService;
    }

    public ResumeAnalysisResponse analyzeResume(ResumeRequest request) {

        long totalStart = System.nanoTime();

        // CALL 1: Extract skills from Job Description
        long jdStart = System.nanoTime();

        List<String> requiredSkills =
                skillExtractionService.extractSkills(
                        request.getJobDescription());

        printTime("Job Description skill extraction", jdStart);

        // CALL 2: Extract all resume details.
        // This also gives us technicalSkills.
        long resumeStart = System.nanoTime();

        ResumeDetails resumeDetails =
                resumeDetailsExtractionService.extractResumeDetails(
                        request.getResume());

        printTime("Resume details extraction", resumeStart);

        // Reuse extracted skills. Do NOT call AI again for resume skills.
        List<String> resumeSkills =
                resumeDetails.getTechnicalSkills() == null
                        ? List.of()
                        : resumeDetails.getTechnicalSkills();

        // Java performs matching locally
        List<String> matchedSkills =
                skillMatchingService.findMatchedSkills(
                        requiredSkills, resumeSkills);

        List<String> missingSkills =
                skillMatchingService.findMissingSkills(
                        requiredSkills, matchedSkills);

        double percentage =
                skillMatchingService.calculateMatchPercentage(
                        matchedSkills.size(),
                        requiredSkills.size());

        // Generate recommendations using Java.
        // No additional AI call is needed.
        String recommendation =
                generateRecommendation(missingSkills);

        ResumeAnalysisResponse response =
                new ResumeAnalysisResponse();

        response.setCompanyName(request.getCompanyName());
        response.setJobRole(request.getJobRole());
        response.setMatchPercentage(percentage);
        response.setTotalRequiredSkills(requiredSkills);
        response.setMatchedSkills(matchedSkills);
        response.setMissingSkills(missingSkills);
        response.setRecommendation(recommendation);
        response.setResumeDetails(resumeDetails);

        printTime("TOTAL ANALYSIS", totalStart);

        return response;
    }

    private String generateRecommendation(List<String> missingSkills) {

        if (missingSkills == null || missingSkills.isEmpty()) {
            return "Your resume covers all identified required skills. "
                    + "Continue improving your practical knowledge "
                    + "through real-world projects.";
        }

        return "Your resume does not currently show the following "
                + "required skills: "
                + String.join(", ", missingSkills)
                + ". Focus on learning these skills and demonstrating "
                + "them through practical projects. Add them to your "
                + "resume only after gaining the relevant experience.";
    }

    private void printTime(String operation, long startTime) {

        long elapsedMillis =
                (System.nanoTime() - startTime) / 1_000_000;

        System.out.println(
                operation + " took " + elapsedMillis + " ms"
        );
    }
}
