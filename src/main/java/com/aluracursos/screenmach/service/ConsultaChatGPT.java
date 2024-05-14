package com.aluracursos.screenmach.service;

import com.theokanning.openai.completion.CompletionRequest;
import com.theokanning.openai.service.OpenAiService;

public class ConsultaChatGPT {

    public static String obtenerTraduccion(String texto){
        OpenAiService service = new OpenAiService(System.getenv("KEY_CHATGPT"));
        CompletionRequest requisicion = CompletionRequest.builder()
                .model("gpt-3.5-turbo-instruct")
                .prompt("traduce al español el siguiente texto: "+texto)
                .maxTokens(1000)
                .temperature(0.7)
                .build();
        var respuesta = service.createCompletion(requisicion);
        return respuesta.getChoices().get(0).getText();
    }
}
