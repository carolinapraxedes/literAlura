package com.alura.literalura.Services;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ConversorJSONobject {
    private ObjectMapper mapper = new ObjectMapper();

    public <T> T obterDados(String json, Class<T> classe) {
        try {
            return mapper.readValue(json, classe);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao converter JSON: " + e.getMessage(), e);
        }
    }
}
