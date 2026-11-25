
package com.SpringGrok.Grok.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import com.SpringGrok.Grok.request.ResumeBuilderRequest;
import com.SpringGrok.Grok.response.ResumeBuilderResponse;

@Service
public class ResumeBuilderService {

	private final ChatClient chatClient;

	public ResumeBuilderService(ChatClient.Builder builder) {
		this.chatClient = builder.build();
	}

	public ResumeBuilderResponse generateResume(ResumeBuilderRequest request) {

		// Step 1: Prepare AI prompt
		String prompt = """
				You are an expert technical resume writer.

				Analyze the candidate's skills against the job description.

				CANDIDATE SKILLS:
				%s

				JOB ROLE:
				%s

				JOB DESCRIPTION:
				%s

				CANDIDATE EDUCATION:
				%s

				CANDIDATE PROJECTS:
				%s

				CANDIDATE CERTIFICATIONS:
				%s

				STRICT RULES:

				1. Identify skills that match the job requirements.
				2. Identify required skills missing from candidate skills.
				3. Never classify a missing skill as a candidate skill.
				4. Never invent skills, projects or certifications.
				5. Generate a professional summary using only
				   candidate-provided skills and information.
				6. Do not mention missing skills as candidate proficiency.
				7. Return a structured response matching
				   ResumeBuilderResponse.
				""".formatted(request.getSkills(), request.getJobRole(), request.getJobDescription(),
				request.getEducation(), request.getProjects(), request.getCertifications());

		// Step 2: Get structured response from AI
		ResumeBuilderResponse response = chatClient.prompt().user(prompt).call().entity(ResumeBuilderResponse.class);

		// Step 3: Get candidate skills
		List<String> candidateSkills = request.getSkills() == null ? new ArrayList<>() : request.getSkills();

		List<String> verifiedSkills = new ArrayList<>(candidateSkills);

		// Step 4: Validate matched skills
		List<String> matchedSkills = new ArrayList<>();

		if (response.getMatchedSkills() != null) {

			for (String skill : response.getMatchedSkills()) {

				if (containsSkill(candidateSkills, skill) && !containsSkill(matchedSkills, skill)) {

					matchedSkills.add(skill);
				}
			}
		}

		// Step 5: Validate missing skills
		List<String> missingSkills = new ArrayList<>();

		if (response.getMissingSkills() != null) {

			for (String skill : response.getMissingSkills()) {

				if (skill != null && request.getJobDescription() != null
						&& request.getJobDescription().toLowerCase().contains(skill.toLowerCase())
						&& !containsSkill(candidateSkills, skill) && !containsSkill(missingSkills, skill)) {

					missingSkills.add(skill);
				}
			}
		}

		// Step 6: Set skill information
		response.setVerifiedSkills(verifiedSkills);
		response.setMatchedSkills(matchedSkills);
		response.setMissingSkills(missingSkills);
		response.setTechnicalSkills(matchedSkills);

		// Step 7: Generate professional summary
		response.setProfessionalSummary(buildProfessionalSummary(request));

		// Step 8: Preserve candidate information
		response.setProjects(request.getProjects());
		response.setEducation(request.getEducation());
		response.setCertifications(request.getCertifications());
		response.setTargetRole(request.getJobRole());

		// Step 9: Add candidate contact details
		response.setFullName(request.getFullName());
		response.setEmail(request.getEmail());
		response.setPhone(request.getPhone());
		response.setLinkedIn(request.getLinkedIn());
		response.setExperience(request.getExperience());

		// Step 10: Return final response
		return response;
	}

	// Professional summary generation
	private String buildProfessionalSummary(ResumeBuilderRequest request) {

		String skills = request.getSkills() == null ? "" : String.join(", ", request.getSkills());

		String role = request.getJobRole() == null ? "Software Developer" : request.getJobRole();

		return "Recent graduate seeking a " + role + " opportunity. Possesses foundational " + "knowledge of " + skills
				+ ". Interested in applying technical knowledge " + "to software development and contributing "
				+ "to real-world projects.";
	}

	// Case-insensitive skill comparison
	private boolean containsSkill(List<String> skills, String targetSkill) {

		if (skills == null || targetSkill == null) {
			return false;
		}

		return skills.stream().anyMatch(skill -> skill != null && skill.trim().equalsIgnoreCase(targetSkill.trim()));
	}
}
