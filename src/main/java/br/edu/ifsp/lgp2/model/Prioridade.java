package br.edu.ifsp.lgp2.model;

/*
 * Enum que restringe os valores aceitos em Chamado.prioridade.
 * A prioridade inicial continua sendo definida pela ChamadoFactory a partir
 * da categoria, mas passa a ter um conjunto fechado de valores possíveis.
 */
public enum Prioridade {
    BAIXA,
    MEDIA,
    ALTA,
    URGENTE
}
