package com.SpringGrok.Grok.request;

import lombok.Data;

@Data
public class ChatRequest {
	private String question ;

	public String getQuestion() {
		return question;
	}
}
