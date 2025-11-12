package com.app.learningspringai.model;

/**
 * Request body for chat endpoint.
 */
public record ChatRequest(String prompt, String model) { }


