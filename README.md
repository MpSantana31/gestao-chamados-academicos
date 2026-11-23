# Sistema de Gestão de Chamados Acadêmicos

Projeto final de **Linguagem de Programação 2** — IFSP, Bacharelado em Sistemas de Informação.
Spring Boot 3.3.5 · Java 17 · Thymeleaf · JPA/H2 · API REST · exportação CSV, PDF e Excel.

**Integrante:** Marcos P Santana

## Como executar

Com JDK 17 instalado:

```bash
./mvnw spring-boot:run
```

Abra <http://localhost:8080>. O banco H2 é criado em `./dados/` no primeiro start, já com os usuários e três chamados de exemplo (apague a pasta para zerar).

**Usuários:** `admin` / `admin123` (ADMIN) e `aluno` / `aluno123` (USUARIO).

## Funcionalidades

- Login e logout com sessão; usuário comum vê só os próprios chamados, admin vê todos.
- Cadastro de chamado (título, descrição, categoria) — status, prioridade e responsável definidos pela `ChamadoFactory`.
- Listagem com filtros (busca, status, categoria, prioridade, período), detalhes, edição, exclusão e atualização de status com observação de atendimento.
- Indicadores (totais por status, prioridade alta/urgente, percentual concluído) e relatório com os mesmos filtros.
- Exportação em CSV, PDF e Excel a partir da lista filtrada.
- Gestão de usuários (admin).
- Mensagens de sucesso e erro nas operações.

## API REST

Mesma sessão do login web (`/login` primeiro); sem sessão → `401`, escrita só para ADMIN → `403`.

- `GET /api/chamados` — lista (respeita papel e filtros)
- `GET /api/chamados/{id}` — 200 / 404
- `POST /api/chamados` — 201 / 400
- `PUT /api/chamados/{id}` — atualiza o chamado: 200 / 400 / 403 / 404
- `PUT /api/chamados/{id}/status` — status + observação: 200 / 400 / 403 / 404
- `DELETE /api/chamados/{id}` — 204 / 403 / 404
- `GET /api/chamados/indicadores` — 200 / 403

```bash
curl -c c.txt -d "usuario=admin&senha=admin123" http://localhost:8080/login
curl -b c.txt http://localhost:8080/api/chamados
```

Status: `ABERTO`, `EM_ANALISE`, `CONCLUIDO`, `CANCELADO`. Prioridade: `BAIXA`, `MEDIA`, `ALTA`, `URGENTE`.

## Relatório e exportação

Como admin, abra **relatórios**, aplique um filtro e use **Baixar CSV / PDF / Excel**. O mesmo arquivo sai em `GET /relatorios/exportar/{csv|pdf|excel}` com os parâmetros do filtro (`status`, `categoria`, `prioridade`, `busca`, `inicio`, `fim`).

O `RelatorioController` recebe o `FiltroChamadoDTO`, o `ConsultaService` monta uma `Specification` (um predicado por filtro, combinados com AND), a lista resultante é a mesma da tela e o `ExportacaoService` gera o arquivo.

## Organização

`controller` (web e `@RestController`) · `service` (regras, filtros, exportação) · `repository` (Spring Data JPA) · `model` (entidades e enums) · `dto` · `factory` · `config` (sessão e rotas de admin). Interface web e API compartilham os mesmos Services, Repository, Entities e DTOs.

## Testes

```bash
./mvnw test
```

15 testes com `MockMvc`: seed, cadastro, isolamento por usuário, bloqueio de rotas/verbos para não-admin, atualização total e parcial, exclusão, 404, status inválido e login.

## Refatorações

1. Status e prioridade eram `String` livre (a API gravava `{"status":"BANANA"}`); viraram os enums `StatusChamado` e `Prioridade`.
2. A edição colava a entidade no formulário (`@ModelAttribute Chamado`); passou a usar `ChamadoEdicaoDTO`, compartilhado entre web e API.
3. Faltava o `PUT /api/chamados/{id}` exigido no enunciado (respondia 405); implementado no `ChamadoService`.
4. Testes usavam o mesmo banco sem rollback; `@Transactional` garante isolamento.
