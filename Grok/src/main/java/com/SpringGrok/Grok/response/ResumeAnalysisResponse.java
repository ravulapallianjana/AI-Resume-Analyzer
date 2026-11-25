
package com.SpringGrok.Grok.response;

import java.util.List;

public class ResumeAnalysisResponse {

    private String companyName;
    private String jobRole;
    private double matchPercentage;

    private List<String> totalRequiredSkills;
    private List<String> matchedSkills;
    private List<String> missingSkills;

    private String recommendation;

    // Uploaded resume details
    private ResumeDetails resumeDetails;

    // Company Name
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Job Role
    public String getJobRole() {
        return jobRole;
    }

    public void setJobRole(String jobRole) {
        this.jobRole = jobRole;
    }

    // Match Percentage
    public double getMatchPercentage() {
        return matchPercentage;
    }

    public void setMatchPercentage(double matchPercentage) {
        this.matchPercentage = matchPercentage;
    }

    // Total Required Skills
    public List<String> getTotalRequiredSkills() {
        return totalRequiredSkills;
    }

    public void setTotalRequiredSkills(List<String> totalRequiredSkills) {
        this.totalRequiredSkills = totalRequiredSkills;
    }

    // Matched Skills
    public List<String> getMatchedSkills() {
        return matchedSkills;
    }

    public void setMatchedSkills(List<String> matchedSkills) {
        this.matchedSkills = matchedSkills;
    }

    // Missing Skills
    public List<String> getMissingSkills() {
        return missingSkills;
    }

    public void setMissingSkills(List<String> missingSkills) {
        this.missingSkills = missingSkills;
    }

    // Recommendation
    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    // Resume Details
    public ResumeDetails getResumeDetails() {
        return resumeDetails;
    }

    public void setResumeDetails(ResumeDetails resumeDetails) {
        this.resumeDetails = resumeDetails;
    }
}
