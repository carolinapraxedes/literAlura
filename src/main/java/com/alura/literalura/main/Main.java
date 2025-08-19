package com.alura.literalura.main;

import com.alura.literalura.Models.Autor;
import com.alura.literalura.Models.DadosLivros;
import com.alura.literalura.Services.ConsumoAPI;
import com.alura.literalura.Services.ConversorJSONobject;
import org.springframework.boot.CommandLineRunner;

import java.io.IOException;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class Main  {
    private static final String API_URL = "https://gutendex.com/books/?search=";
    Scanner leitura = new Scanner(System.in);
    ConsumoAPI consumo = new ConsumoAPI();
    ConversorJSONobject conversor = new ConversorJSONobject();

    String json = consumo.obterDados(API_URL);

    // Converte o JSON para o record 'DadosBusca'
    DadosLivros dados = conversor.obterDados(json, DadosLivros.class);


    public void exibirMenu() {
        var opcao = -1;

        while(opcao!=0){
            System.out.println("======== Menu ========");
            System.out.println("1 - Consultar todos os livros");
            System.out.println("2 - Buscar livro por titulo");
            System.out.println("3 - Buscar livro por idioma");


            System.out.println("4 - Consultar autores");
            System.out.println("0 - Sair");

            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    consultarLivros();
                    break;
                case 2:
                    buscarLivroPorTitulo();
                    break;
                case 3:
                    buscarLivroPorIdioma();
                    break;
                case 4:
                    consultarAutores();
                    break;
                case 0:
                    System.out.println("Saindo do programa...");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }


        }

        leitura.close();
    }

    private void buscarLivroPorIdioma() {
        System.out.println("Digite o idioma para buscar livros:");
        System.out.println("  en - Inglês");
        System.out.println("  pt - Português");
        System.out.println("  es - Espanhol");
        System.out.println("  fr - Francês");

        var idiomaBusca = leitura.nextLine();

        var url = "https://gutendex.com/books/?languages=" + idiomaBusca;
        json = consumo.obterDados(url);

        dados = conversor.obterDados(json, DadosLivros.class);

        if (dados != null && dados.resultado() != null && !dados.resultado().isEmpty()) {
            System.out.println("--- Livros encontrados no idioma '" + idiomaBusca + "': ---");
            dados.resultado().forEach(System.out::println);
        } else {
            System.out.println("Nenhum livro encontrado no idioma especificado.");
        }

    }

    private void buscarLivroPorTitulo() {
        System.out.println("Digite o título do livro para buscar:");
        var tituloBusca = leitura.nextLine();
        var tituloFormatado = tituloBusca.replace(" ", "+");

        var url = "https://gutendex.com/books/?search=" + tituloFormatado;

        // Fazer a requisição para a API
        json = consumo.obterDados(url);

        // Converter o JSON para o record 'DadosBusca'
        dados = conversor.obterDados(json, DadosLivros.class);
        if (dados != null && dados.resultado() != null && !dados.resultado().isEmpty()) {
            System.out.println("--- Livros encontrados para o título '" + tituloBusca + "': ---");
            dados.resultado().forEach(System.out::println);
        } else {
            System.out.println("Nenhum livro encontrado para o título '" + tituloBusca + "'.");
        }

    }

    private void consultarAutores() {
        System.out.println("Consultando autores...");
        if (dados != null && dados.resultado() != null) {
            Set<Autor> autoresUnicos = dados.resultado().stream()
                    .filter(livro -> livro.autores() != null)
                    .flatMap(livro -> livro.autores().stream())
                    .collect(Collectors.toSet());

            if (!autoresUnicos.isEmpty()) {
                System.out.println("--- Autores encontrados: ---");
                autoresUnicos.forEach(System.out::println);
            } else {
                System.out.println("Nenhum autor encontrado.");
            }
        } else {
            System.out.println("Nenhum livro encontrado para extrair os autores.");
        }




    }

    private void consultarLivros() {
        System.out.println("Consultando livros...");


        //Verifica se a lista de resultados não está vazia e exibe
        if (dados != null && dados.resultado() != null) {
            System.out.println("--- Livros encontrados: ---");
            System.out.println("Quantidades de livros: "+ dados.qtdLivros());
            dados.resultado().forEach(System.out::println);
        } else {
            System.out.println("Nenhum livro encontrado.");
        }
    }
}
