package br.edu.ifsp.lgp2.service;

import br.edu.ifsp.lgp2.dto.ChamadoAtualizacaoStatusDTO;
import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
import br.edu.ifsp.lgp2.factory.ChamadoFactory;
import br.edu.ifsp.lgp2.model.Chamado;
import br.edu.ifsp.lgp2.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

/*
 * Service centraliza as operações e regras do sistema.
 */
@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;

    public ChamadoService(ChamadoRepository chamadoRepository) {
        this.chamadoRepository = chamadoRepository;
    }

    public List<Chamado> listarTodos() {
        return chamadoRepository.findAll();
    }

    public List<Chamado> filtrarPorStatus(String status) {
        if (status == null || status.isBlank()) {
            return listarTodos();
        }

        return chamadoRepository.findByStatus(status);
    }

    public Chamado buscarPorId(Long id) {
        return chamadoRepository.findById(id).orElse(null);
    }

    /** Optional explicita ao Controller REST a possibilidade de não encontrar o recurso. */
    public java.util.Optional<Chamado> buscarOpcional(Long id) {
        return chamadoRepository.findById(id);
    }

    public boolean existe(Long id) {
        return chamadoRepository.existsById(id);
    }

    public long quantidadeChamados() {
        return chamadoRepository.count();
    }

    /*
     * Cadastro usando DTO.
     * O DTO traz apenas os dados que vieram do formulário.
     */
    public Chamado salvarNovoChamado(ChamadoCadastroDTO dto) {

        Chamado chamado = new Chamado();

        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setCategoria(dto.getCategoria());

        chamado = ChamadoFactory.criarChamado(chamado);

        return chamadoRepository.save(chamado);
    }

    /*
     * Edição geral do chamado.
     */
    public Chamado atualizarChamado(Chamado chamado) {
        // Recupera a entidade persistida, preservando a data de abertura original.
        Chamado salvo = chamadoRepository.findById(chamado.getId()).orElse(null);
        if (salvo == null) return null;
        salvo.setTitulo(chamado.getTitulo());
        salvo.setDescricao(chamado.getDescricao());
        salvo.setCategoria(chamado.getCategoria());
        salvo.setStatus(chamado.getStatus());
        salvo.setPrioridade(chamado.getPrioridade());
        salvo.setResponsavel(chamado.getResponsavel());
        salvo.setObservacaoAtendimento(chamado.getObservacaoAtendimento());
        salvo.setDataAtualizacao(LocalDate.now());
        return chamadoRepository.save(salvo);
    }

    /*
     * GABARITO DA ATIVIDADE:
     *
     * Atualiza apenas status e observação de atendimento.
     * O DTO protege os demais campos da Entity.
     */
    public Chamado atualizarStatus(ChamadoAtualizacaoStatusDTO dto) {

        Chamado chamado = chamadoRepository.findById(dto.getId()).orElse(null);

        if (chamado == null) {
            return null;
        }

        chamado.setStatus(dto.getStatus());
        chamado.setObservacaoAtendimento(dto.getObservacaoAtendimento());
        chamado.setDataAtualizacao(LocalDate.now());

        return chamadoRepository.save(chamado);
    }

    public void excluir(Long id) {
        chamadoRepository.deleteById(id);
    }
}
