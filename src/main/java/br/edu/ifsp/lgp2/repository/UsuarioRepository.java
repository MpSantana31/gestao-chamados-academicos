package br.edu.ifsp.lgp2.repository;

import br.edu.ifsp.lgp2.model.Papel;
import br.edu.ifsp.lgp2.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByLogin(String login);
    boolean existsByLogin(String login);
    long countByPapel(Papel papel);
}
