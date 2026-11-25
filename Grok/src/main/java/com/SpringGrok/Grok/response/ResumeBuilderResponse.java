
package com.SpringGrok.Grok.response;

import java.util.List;

public class ResumeBuilderResponse {

	private String professionalSummary;

	private List<String> verifiedSkills;

	private List<String> matchedSkills;

	private List<String> missingSkills;

	private List<String> technicalSkills;

	private List<String> projects;

	private String education;

	private List<String> certifications;

	private String targetRole;

	private String fullName;
	private String email;
	private String phone;
	private String linkedIn;
	private String experience;

	public String getProfessionalSummary() {
		return professionalSummary;
	}

	public void setProfessionalSummary(String professionalSummary) {
		this.professionalSummary = professionalSummary;
	}

	public List<String> getVerifiedSkills() {
		return verifiedSkills;
	}

	public void setVerifiedSkills(List<String> verifiedSkills) {
		this.verifiedSkills = verifiedSkills;
	}

	public List<String> getMatchedSkills() {
		return matchedSkills;
	}

	public void setMatchedSkills(List<String> matchedSkills) {
		this.matchedSkills = matchedSkills;
	}

	public List<String> getMissingSkills() {
		return missingSkills;
	}

	public void setMissingSkills(List<String> missingSkills) {
		this.missingSkills = missingSkills;
	}

	public List<String> getTechnicalSkills() {
		return technicalSkills;
	}

	public void setTechnicalSkills(List<String> technicalSkills) {
		this.technicalSkills = technicalSkills;
	}

	public List<String> getProjects() {
		return projects;
	}

	public void setProjects(List<String> projects) {
		this.projects = projects;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public List<String> getCertifications() {
		return certifications;
	}

	public void setCertifications(List<String> certifications) {
		this.certifications = certifications;
	}

	public String getTargetRole() {
		return targetRole;
	}

	public void setTargetRole(String targetRole) {
		this.targetRole = targetRole;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLinkedIn() {
		return linkedIn;
	}

	public void setLinkedIn(String linkedIn) {
		this.linkedIn = linkedIn;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}
	
}
