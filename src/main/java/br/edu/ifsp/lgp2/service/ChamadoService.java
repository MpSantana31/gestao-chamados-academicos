package br.edu.ifsp.lgp2.service;

import br.edu.ifsp.lgp2.dto.ChamadoAtualizacaoStatusDTO;
import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
import br.edu.ifsp.lgp2.dto.ChamadoEdicaoDTO;
import br.edu.ifsp.lgp2.factory.ChamadoFactory;
import br.edu.ifsp.lgp2.model.Chamado;
import br.edu.ifsp.lgp2.model.Papel;
import br.edu.ifsp.lgp2.model.Usuario;
import br.edu.ifsp.lgp2.repository.ChamadoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class ChamadoService {

    private final ChamadoRepository chamadoRepository;
    private final UsuarioService usuarioService;

    public ChamadoService(ChamadoRepository chamadoRepository, UsuarioService usuarioService) {
        this.chamadoRepository = chamadoRepository;
        this.usuarioService = usuarioService;
    }

    public Chamado buscarPorId(Long id, String login, Papel papel) {
        return chamadoRepository.findById(id)
                .filter(chamado -> acessivel(chamado, login, papel))
                .orElse(null);
    }

    public Optional<Chamado> buscarOpcional(Long id, String login, Papel papel) {
        return chamadoRepository.findById(id)
                .filter(chamado -> acessivel(chamado, login, papel));
    }

    public long quantidadeChamados() {
        return chamadoRepository.count();
    }

    public Chamado salvarNovoChamado(ChamadoCadastroDTO dto, String login) {
        Usuario requerente = usuarioService.buscarPorLogin(login);
        if (requerente == null) {
            return null;
        }
        Chamado chamado = new Chamado();
        chamado.setTitulo(dto.getTitulo());
        chamado.setDescricao(dto.getDescricao());
        chamado.setCategoria(dto.getCategoria());
        chamado.setRequerente(requerente);
        chamado = ChamadoFactory.criarChamado(chamado);
        return chamadoRepository.save(chamado);
    }

    public Chamado atualizarChamado(ChamadoEdicaoDTO dto, String login, Papel papel) {
        Chamado salvo = buscarPorId(dto.getId(), login, papel);
        if (salvo == null) {
            return null;
        }
        // Os campos obrigatórios do banco só são sobrescritos quando vieram no DTO,
        // assim uma atualização parcial pela API não apaga o que já estava gravado.
        if (dto.getTitulo() != null) {
            salvo.setTitulo(dto.getTitulo());
        }
        if (dto.getDescricao() != null) {
            salvo.setDescricao(dto.getDescricao());
        }
        if (dto.getCategoria() != null) {
            salvo.setCategoria(dto.getCategoria());
        }
        if (dto.getStatus() != null) {
            salvo.setStatus(dto.getStatus());
        }
        if (dto.getPrioridade() != null) {
            salvo.setPrioridade(dto.getPrioridade());
        }
        if (dto.getResponsavel() != null) {
            salvo.setResponsavel(dto.getResponsavel());
        }
        salvo.setObservacaoAtendimento(dto.getObservacaoAtendimento());
        salvo.setDataAtualizacao(LocalDate.now());
        return chamadoRepository.save(salvo);
    }

    public Chamado atualizarStatus(ChamadoAtualizacaoStatusDTO dto, String login, Papel papel) {
        Chamado chamado = buscarPorId(dto.getId(), login, papel);
        if (chamado == null) {
            return null;
        }
        chamado.setStatus(dto.getStatus());
        chamado.setObservacaoAtendimento(dto.getObservacaoAtendimento());
        chamado.setDataAtualizacao(LocalDate.now());
        return chamadoRepository.save(chamado);
    }

    public boolean excluir(Long id, String login, Papel papel) {
        Chamado chamado = buscarPorId(id, login, papel);
        if (chamado == null) {
            return false;
        }
        chamadoRepository.delete(chamado);
        return true;
    }

    private boolean acessivel(Chamado chamado, String login, Papel papel) {
        return papel == Papel.ADMIN
                || (login != null && chamado.getRequerente() != null
                && login.equals(chamado.getRequerente().getLogin()));
    }
}
