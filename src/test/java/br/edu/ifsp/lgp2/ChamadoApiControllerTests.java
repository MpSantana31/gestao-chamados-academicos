package br.edu.ifsp.lgp2;

import br.edu.ifsp.lgp2.model.Papel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:teste-papeis;DB_CLOSE_DELAY=-1",
        "spring.jpa.hibernate.ddl-auto=create-drop"
})
@AutoConfigureMockMvc
@Transactional
class ChamadoApiControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void adminVeChamadosDoSeed() throws Exception {
        mockMvc.perform(get("/api/chamados")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].requerente.senha").doesNotExist());
    }

    @Test
    void usuarioNaoVeChamadosDoAdmin() throws Exception {
        mockMvc.perform(get("/api/chamados")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }

    @Test
    void usuarioCriaChamadoEPassaAVerApenasOSeu() throws Exception {
        mockMvc.perform(post("/api/chamados")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Chamado do aluno\",\"descricao\":\"Teste\",\"categoria\":\"Documentos\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/chamados")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].titulo").value("Chamado do aluno"));
    }

    @Test
    void usuarioNaoPodeExcluirNemAlterarStatus() throws Exception {
        mockMvc.perform(delete("/api/chamados/1")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO))
                .andExpect(status().isForbidden());

        mockMvc.perform(put("/api/chamados/1/status")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CONCLUIDO\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void usuarioNaoAcessaRelatoriosNemIndicadores() throws Exception {
        mockMvc.perform(get("/relatorios")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/api/chamados/indicadores")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO))
                .andExpect(status().isForbidden());
    }

    @Test
    void adminPodeExcluirChamado() throws Exception {
        mockMvc.perform(delete("/api/chamados/1")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN))
                .andExpect(status().isNoContent());
    }

    @Test
    void adminAtualizaChamadoPelaApi() throws Exception {
        mockMvc.perform(put("/api/chamados/1")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Titulo atualizado\",\"status\":\"EM_ANALISE\",\"prioridade\":\"URGENTE\",\"responsavel\":\"Equipe de TI\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo atualizado"))
                .andExpect(jsonPath("$.status").value("EM_ANALISE"))
                .andExpect(jsonPath("$.prioridade").value("URGENTE"));

        mockMvc.perform(get("/api/chamados/1")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.titulo").value("Titulo atualizado"));
    }

    @Test
    void atualizacaoParcialMantemCamposObrigatorios() throws Exception {
        mockMvc.perform(put("/api/chamados/1")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"CONCLUIDO\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CONCLUIDO"))
                .andExpect(jsonPath("$.titulo").value("Problema no laboratório"))
                .andExpect(jsonPath("$.responsavel").value("Equipe de TI"));
    }

    @Test
    void usuarioNaoPodeAtualizarChamado() throws Exception {
        mockMvc.perform(put("/api/chamados/1")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Tentativa\"}"))
                .andExpect(status().isForbidden());
    }

    @Test
    void atualizarChamadoInexistenteRetorna404() throws Exception {
        mockMvc.perform(put("/api/chamados/9999")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"titulo\":\"Nao existe\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void statusInvalidoERecusado() throws Exception {
        mockMvc.perform(put("/api/chamados/1/status")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"BANANA\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void loginComUsuarioPersistido() throws Exception {
        mockMvc.perform(post("/login")
                        .param("usuario", "admin")
                        .param("senha", "admin123"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(post("/login")
                        .param("usuario", "admin")
                        .param("senha", "errada"))
                .andExpect(status().isOk());
    }

    @Test
    void consoleH2NaoEstaDisponivel() throws Exception {
        mockMvc.perform(get("/h2-console")
                        .sessionAttr("usuario", "aluno")
                        .sessionAttr("papel", Papel.USUARIO))
                .andExpect(status().isNotFound());
    }

    @Test
    void adminPodeCadastrarUsuario() throws Exception {
        mockMvc.perform(post("/usuarios/salvar")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN)
                        .param("login", "professor")
                        .param("nome", "Professor Teste")
                        .param("senha", "prof123")
                        .param("papel", "USUARIO"))
                .andExpect(status().is3xxRedirection());

        mockMvc.perform(get("/usuarios")
                        .sessionAttr("usuario", "admin")
                        .sessionAttr("papel", Papel.ADMIN))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("usuarios"));
    }
}
