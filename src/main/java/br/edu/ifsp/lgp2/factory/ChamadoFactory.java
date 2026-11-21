package br.edu.ifsp.lgp2.factory;

import br.edu.ifsp.lgp2.model.Chamado;

import java.time.LocalDate;

/*
 * Factory centraliza regras de criação de chamados novos.
 */
public class ChamadoFactory {

    public static Chamado criarChamado(Chamado chamado) {

        chamado.setStatus("ABERTO");
        chamado.setDataAbertura(LocalDate.now());
        chamado.setDataAtualizacao(LocalDate.now());

        if ("Suporte técnico".equals(chamado.getCategoria())) {
            chamado.setPrioridade("ALTA");
            chamado.setResponsavel("Equipe de TI");

        } else if ("Documentos".equals(chamado.getCategoria())) {
            chamado.setPrioridade("MEDIA");
            chamado.setResponsavel("Secretaria Acadêmica");

        } else if ("Matrícula".equals(chamado.getCategoria())) {
            chamado.setPrioridade("ALTA");
            chamado.setResponsavel("Coordenação de Curso");

        } else {
            chamado.setPrioridade("BAIXA");
            chamado.setResponsavel("Atendimento Geral");
        }

        return chamado;
    }
}
