package com.app.learningspringai.rest;

/**
 * Simple abstraction over the underlying AI chat client so the controller can be
 * tested without invoking the real OpenAI API.
 */
public interface AiService {

    /**
     * Ask the AI model a question.
     * @param prompt user prompt (non-empty)
     * @param model optional model name (nullable or blank = default)
     * @return AI textual answer
     */
    String ask(String prompt, String model);
}

