package br.edu.ifsp.lgp2;

import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
import br.edu.ifsp.lgp2.service.ChamadoService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestaoChamadosAcademicosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoChamadosAcademicosApplication.class, args);
    }

    /*
     * Cria alguns registros iniciais para facilitar os testes da atividade.
     */
    @Bean
    public CommandLineRunner carregarDadosIniciais(ChamadoService chamadoService) {
        return args -> {
            if (chamadoService.quantidadeChamados() == 0) {
                chamadoService.salvarNovoChamado(new ChamadoCadastroDTO(
                        "Problema no laboratório",
                        "Computador do laboratório não liga.",
                        "Suporte técnico"
                ));

                chamadoService.salvarNovoChamado(new ChamadoCadastroDTO(
                        "Solicitação de declaração",
                        "Preciso de declaração de matrícula.",
                        "Documentos"
                ));

                chamadoService.salvarNovoChamado(new ChamadoCadastroDTO(
                        "Ajuste de matrícula",
                        "Solicito ajuste em unidade curricular.",
                        "Matrícula"
                ));
            }
        };
    }
}
