package br.edu.ifsp.lgp2.repository;

import br.edu.ifsp.lgp2.model.Chamado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/** Repository: abstrai o acesso ao banco H2. O Spring Data implementa os métodos JPA. */
public interface ChamadoRepository extends JpaRepository<Chamado, Long>, JpaSpecificationExecutor<Chamado> {
}
