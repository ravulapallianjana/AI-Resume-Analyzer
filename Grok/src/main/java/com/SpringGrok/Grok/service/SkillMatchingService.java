
package com.SpringGrok.Grok.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.stereotype.Service;

@Service
public class SkillMatchingService {

    public List<String> findMatchedSkills(
            List<String> requiredSkills,
            List<String> resumeSkills) {

        List<String> matchedSkills = new ArrayList<>();

        for (String required : requiredSkills) {

            for (String candidate : resumeSkills) {

                if (normalize(required).equals(normalize(candidate))) {

                    matchedSkills.add(required);
                    break;
                }
            }
        }

        return matchedSkills;
    }

    public List<String> findMissingSkills(
            List<String> requiredSkills,
            List<String> matchedSkills) {

        List<String> missingSkills = new ArrayList<>();

        for (String required : requiredSkills) {

            boolean found = matchedSkills.stream()
                    .anyMatch(skill ->
                            normalize(skill).equals(normalize(required)));

            if (!found) {
                missingSkills.add(required);
            }
        }

        return missingSkills;
    }

    public double calculateMatchPercentage(
            int matchedCount,
            int totalRequiredCount) {

        if (totalRequiredCount == 0) {
            return 0.0;
        }

        return Math.round(
                (matchedCount * 10000.0) / totalRequiredCount
        ) / 100.0;
    }

    private String normalize(String skill) {

        return skill == null
                ? ""
                : skill.trim().toLowerCase(Locale.ROOT);
    }
}
