package br.edu.ifsp.lgp2.service;

import br.edu.ifsp.lgp2.dto.ChamadoAtualizacaoStatusDTO;
import br.edu.ifsp.lgp2.dto.ChamadoCadastroDTO;
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

    public boolean existe(Long id, String login, Papel papel) {
        return buscarPorId(id, login, papel) != null;
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

    public Chamado atualizarChamado(Chamado dados, String login, Papel papel) {
        Chamado salvo = buscarPorId(dados.getId(), login, papel);
        if (salvo == null) {
            return null;
        }
        salvo.setTitulo(dados.getTitulo());
        salvo.setDescricao(dados.getDescricao());
        salvo.setCategoria(dados.getCategoria());
        salvo.setStatus(dados.getStatus());
        salvo.setPrioridade(dados.getPrioridade());
        salvo.setResponsavel(dados.getResponsavel());
        salvo.setObservacaoAtendimento(dados.getObservacaoAtendimento());
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
