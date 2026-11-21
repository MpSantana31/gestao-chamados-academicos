package br.edu.ifsp.lgp2;

import br.edu.ifsp.lgp2.service.ConsultaService;
import br.edu.ifsp.lgp2.repository.ChamadoRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

/** Teste de integração: o contexto Spring carrega e as consultas JPA funcionam. */
@SpringBootTest(properties={"spring.datasource.url=jdbc:h2:mem:teste;DB_CLOSE_DELAY=-1","spring.jpa.hibernate.ddl-auto=create-drop"})
class GestaoChamadosAcademicosApplicationTests {
    @Autowired ConsultaService consultas;
    @Autowired ChamadoRepository repository;
    @Test void indicadoresSaoCoerentes(){assertEquals(repository.count(),consultas.indicadores().total());}
}
