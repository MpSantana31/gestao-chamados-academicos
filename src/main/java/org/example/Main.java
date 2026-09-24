package org.example;

import org.example.model.Categoria;
import org.example.model.Chamado;
import org.example.model.Usuario;
import org.example.service.ChamadoService;

public class Main {
    public static void main(String[] args) {
        Usuario usuario1 = new Usuario(1, "Marcos", "marcos@email.com", "Aluno");
        Usuario usuario2 = new Usuario(2, "Ana", "ana@email.com", "Professora");

        Categoria cat1 = new Categoria(1, "Suporte", "Problemas de suporte técnico");
        Categoria cat2 = new Categoria(2, "Bug", "Erros no sistema");

        ChamadoService service = new ChamadoService();

        service.abrirChamado(1, "Chamado 1", "Problema com login", "Aberto", usuario1, cat1);
        service.abrirChamado(2, "Chamado 2", "Erro na tela inicial", "Aberto", usuario2, cat2);

        System.out.println("=== Chamados Abertos ===");
        for (Chamado c : service.listarChamados()) {
            System.out.println("ID: " + c.getId() + " | Titulo: " + c.getTitulo() +
                    " | Status: " + c.getStatus() + " | Requerente: " + c.getRequerente().getNome() +
                    " | Categoria: " + c.getCategoria().getNome());
        }

        service.atualizarStatusChamado(1, "Em Andamento");
        service.atualizarStatusChamado(2, "Resolvido");

        System.out.println("\n=== Chamados Após Alteração de Status ===");
        for (Chamado c : service.listarChamados()) {
            System.out.println("ID: " + c.getId() + " | Titulo: " + c.getTitulo() +
                    " | Status: " + c.getStatus() + " | Requerente: " + c.getRequerente().getNome());
        }
    }
}