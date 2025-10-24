package com.app.learningspringai.rest;

/**
 * Request body for chat endpoint.
 */
public record ChatRequest(String prompt, String model) { }


