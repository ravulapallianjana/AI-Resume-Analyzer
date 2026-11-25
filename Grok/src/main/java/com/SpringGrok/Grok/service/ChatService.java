package com.SpringGrok.Grok.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class ChatService {
	private  final ChatClient chatClient;
//	private ChatClient chatClient;
	public ChatService( ChatClient.Builder builder) {
		this.chatClient=builder.build();
	}
	public String askGrok(String question) {
		return chatClient.prompt()
				.user(question)
				.call().
				content();
	}

}
