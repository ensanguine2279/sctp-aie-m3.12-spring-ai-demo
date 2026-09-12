package sg.edu.ntu.spring_ai_demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import sg.edu.ntu.spring_ai_demo.service.AiService;

@RestController
public class AiController {

    // Study buddy system prompt properties
    @Value("${spring.ai.demo.study.sysprompt.role}")
    private String studySystemPromptRole;

    @Value("${spring.ai.demo.study.sysprompt.scope}")
    private String studySystemPromptScope;

    @Value("${spring.ai.demo.study.sysprompt.tone}")
    private String studySystemPromptTone;

    @Value("${spring.ai.demo.study.sysprompt.boundaries}")
    private String studySystemPromptBoundaries;

    // Product recommender system prompt properties
    @Value("${spring.ai.demo.product.sysprompt.role}")
    private String pdtSystemPromptRole;

    @Value("${spring.ai.demo.product.sysprompt.scope}")
    private String pdtSystemPromptScope;

    @Value("${spring.ai.demo.product.sysprompt.tone}")
    private String pdtSystemPromptTone;

    @Value("${spring.ai.demo.product.sysprompt.boundaries}")
    private String pdtSystemPromptBoundaries;

    // Recipe suggester system prompt properties
    @Value("${spring.ai.demo.recipe.sysprompt.role}")
    private String recipeSystemPromptRole;

    @Value("${spring.ai.demo.recipe.sysprompt.scope}")
    private String recipeSystemPromptScope;

    @Value("${spring.ai.demo.recipe.sysprompt.tone}")
    private String recipeSystemPromptTone;

    @Value("${spring.ai.demo.recipe.sysprompt.boundaries}")
    private String recipeSystemPromptBoundaries;

    // Interview coach system prompt properties
    @Value("${spring.ai.demo.interview.sysprompt.role}")
    private String interviewSystemPromptRole;

    @Value("${spring.ai.demo.interview.sysprompt.scope}")
    private String interviewSystemPromptScope;

    @Value("${spring.ai.demo.interview.sysprompt.tone}")
    private String interviewSystemPromptTone;

    @Value("${spring.ai.demo.interview.sysprompt.boundaries}")
    private String interviewSystemPromptBoundaries;

    // Summarizer system prompt properties
    @Value("${spring.ai.demo.summarizer.sysprompt.role}")
    private String summarizerSystemPromptRole;

    @Value("${spring.ai.demo.summarizer.sysprompt.scope}")
    private String summarizerSystemPromptScope;

    @Value("${spring.ai.demo.summarizer.sysprompt.tone}")
    private String summarizerSystemPromptTone;

    @Value("${spring.ai.demo.summarizer.sysprompt.boundaries}")
    private String summarizerSystemPromptBoundaries;

    // Chat client is the main interface for interacting with the AI models
    private final ChatClient chatClient;

    // AI service for handling tasks such as saving summaries
    private final AiService aiService;

    public AiController(ChatClient.Builder chatClientBuilder, AiService aiService) {
        this.chatClient = chatClientBuilder.build();
        this.aiService = aiService;
    }

    private String getSystemPrompt(String role, String scope, String tone, String boundaries) {
        return role + " " + scope + " " + tone + " " + boundaries;
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {
        return chatClient.prompt()
                .user(message)
                .call()
                .content();
    }

    @GetMapping("/support")
    public String support(@RequestParam String message) {
        return chatClient.prompt()
                .system("You are a friendly and professional customer support assistant for a CRM software company. " +
                        "You help users with questions about managing customers, contacts, and sales pipelines. " +
                        "Keep your answers concise and practical. " +
                        "If a question is not related to CRM or customer management, politely redirect the user.")
                .user(message)
                .call()
                .content();
    }

    // Product recommender endpoint
    @GetMapping("/product")
    public String recommendProduct(@RequestParam String message) {
        return chatClient.prompt()
                .system(getSystemPrompt(pdtSystemPromptRole, pdtSystemPromptScope, pdtSystemPromptTone,
                        pdtSystemPromptBoundaries))
                .user(message)
                .call()
                .content();
    }

    // Study buddy endpoint
    @GetMapping("/study")
    public String studyBuddy(@RequestParam String message) {
        return chatClient.prompt()
                .system(getSystemPrompt(studySystemPromptRole, studySystemPromptScope, studySystemPromptTone,
                        studySystemPromptBoundaries))
                .user(message)
                .call()
                .content();
    }

    // Recipe suggester endpoint
    @GetMapping("/recipe")
    public String recipeSuggester(@RequestParam String message) {
        return chatClient.prompt()
                .system(getSystemPrompt(recipeSystemPromptRole, recipeSystemPromptScope, recipeSystemPromptTone,
                        recipeSystemPromptBoundaries))
                .user(message)
                .call()
                .content();
    }

    // Interview coach endpoint
    @GetMapping("/interview")
    public String interviewCoach(@RequestParam String message) {
        return chatClient.prompt()
                .system(getSystemPrompt(interviewSystemPromptRole, interviewSystemPromptScope,
                        interviewSystemPromptTone, interviewSystemPromptBoundaries))
                .user(message)
                .call()
                .content();
    }

    // Summarizer endpoint
    @GetMapping("/summarizer")
    public String summarizer(@RequestParam String text) {
        String summary = chatClient.prompt()
                .system(getSystemPrompt(summarizerSystemPromptRole, summarizerSystemPromptScope,
                        summarizerSystemPromptTone, summarizerSystemPromptBoundaries))
                .user(text)
                .call()
                .content();

        // Save the summary to the CSV file using AiService
        aiService.saveSummary(summary);

        return summary;
    }

}