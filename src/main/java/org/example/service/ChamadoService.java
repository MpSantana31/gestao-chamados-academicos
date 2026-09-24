package org.example.service;

import java.util.List;
import java.util.ArrayList;

import org.example.model.Categoria;
import org.example.model.Chamado;
import org.example.model.Usuario;

public class ChamadoService {
    List<Chamado> chamados = new ArrayList<>();

    public void abrirChamado(int id, String titulo, String descricao, String status, Usuario requerente, Categoria categoria) {
        Chamado chamado1 = new Chamado(id, titulo, descricao, status, requerente, categoria);
        chamados.add(chamado1);
    }

    public List<Chamado> listarChamados() {
        return  chamados;
    }

    public void atualizarStatusChamado(int id, String novoStatus) {
        for (Chamado c : chamados) {
            if (c.getId() == id) {
                c.setStatus(novoStatus);
                return;
            }
        }
    }


}
