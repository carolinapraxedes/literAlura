package com.alura.literalura.Models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;


@JsonIgnoreProperties(ignoreUnknown = true)
public record DadosLivros(@JsonAlias("results") List<Livro> resultado,
                          @JsonAlias("count") Integer qtdLivros) {
}
