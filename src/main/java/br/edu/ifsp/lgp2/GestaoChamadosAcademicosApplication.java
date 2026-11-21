package br.edu.ifsp.lgp2;

import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
import br.edu.ifsp.lgp2.dto.UsuarioCadastroDTO;
import br.edu.ifsp.lgp2.model.Papel;
import br.edu.ifsp.lgp2.service.ChamadoService;
import br.edu.ifsp.lgp2.service.UsuarioService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GestaoChamadosAcademicosApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestaoChamadosAcademicosApplication.class, args);
    }

    @Bean
    public CommandLineRunner carregarDadosIniciais(ChamadoService chamadoService,
                                                   UsuarioService usuarioService) {
        return args -> {
            if (usuarioService.buscarPorLogin("admin") == null) {
                usuarioService.salvarNovo(new UsuarioCadastroDTO(
                        "admin", "Administrador", "admin123", Papel.ADMIN));
            }
            if (usuarioService.buscarPorLogin("aluno") == null) {
                usuarioService.salvarNovo(new UsuarioCadastroDTO(
                        "aluno", "Aluno de demonstração", "aluno123", Papel.USUARIO));
            }

            if (chamadoService.quantidadeChamados() == 0) {
                chamadoService.salvarNovoChamado(new ChamadoCadastroDTO(
                        "Problema no laboratório",
                        "Computador do laboratório não liga.",
                        "Suporte técnico"), "admin");

                chamadoService.salvarNovoChamado(new ChamadoCadastroDTO(
                        "Solicitação de declaração",
                        "Preciso de declaração de matrícula.",
                        "Documentos"), "admin");

                chamadoService.salvarNovoChamado(new ChamadoCadastroDTO(
                        "Ajuste de matrícula",
                        "Solicito ajuste em unidade curricular.",
                        "Matrícula"), "admin");
            }
        };
    }
}
