package com.SpringGrok.Grok.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.SpringGrok.Grok.request.ChatRequest;
import com.SpringGrok.Grok.service.ChatService;

@RestController
@RequestMapping("/api/grok")

public class ChatController {
	private final ChatService chatService;

	public ChatController(ChatService chatService) {
		this.chatService = chatService;
	}

	@GetMapping("/chat")
	public String chat(@RequestParam String message) {
		return chatService.askGrok(message);
	}
	@PostMapping("/chat1")
	public String chat(@RequestBody ChatRequest request) {
		return chatService.askGrok(( request).getQuestion());
	}
	

}
