package br.edu.ifsp.lgp2.service;

import br.edu.ifsp.lgp2.dto.UsuarioCadastroDTO;
import br.edu.ifsp.lgp2.dto.UsuarioEdicaoDTO;
import br.edu.ifsp.lgp2.model.Papel;
import br.edu.ifsp.lgp2.model.Usuario;
import br.edu.ifsp.lgp2.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElse(null);
    }

    public Usuario buscarPorLogin(String login) {
        return usuarioRepository.findByLogin(login).orElse(null);
    }

    public Usuario salvarNovo(UsuarioCadastroDTO dto) {
        if (dto.getLogin() == null || dto.getLogin().isBlank()
                || dto.getNome() == null || dto.getNome().isBlank()
                || dto.getSenha() == null || dto.getSenha().isBlank()
                || dto.getPapel() == null) {
            return null;
        }
        if (usuarioRepository.existsByLogin(dto.getLogin())) {
            return null;
        }
        return usuarioRepository.save(new Usuario(
                dto.getLogin(), dto.getNome(), dto.getSenha(), dto.getPapel()));
    }

    public Usuario atualizar(UsuarioEdicaoDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getId()).orElse(null);
        if (usuario == null
                || dto.getLogin() == null || dto.getLogin().isBlank()
                || dto.getNome() == null || dto.getNome().isBlank()
                || dto.getPapel() == null) {
            return null;
        }
        Optional<Usuario> outro = usuarioRepository.findByLogin(dto.getLogin());
        if (outro.isPresent() && !outro.get().getId().equals(usuario.getId())) {
            return null;
        }
        usuario.setLogin(dto.getLogin());
        usuario.setNome(dto.getNome());
        usuario.setPapel(dto.getPapel());
        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            usuario.setSenha(dto.getSenha());
        }
        return usuarioRepository.save(usuario);
    }

    public boolean excluir(Long id, String loginAtual) {
        Usuario usuario = usuarioRepository.findById(id).orElse(null);
        if (usuario == null || usuario.getLogin().equals(loginAtual)) {
            return false;
        }
        if (usuario.getPapel() == Papel.ADMIN && usuarioRepository.countByPapel(Papel.ADMIN) <= 1) {
            return false;
        }
        usuarioRepository.delete(usuario);
        return true;
    }
}
