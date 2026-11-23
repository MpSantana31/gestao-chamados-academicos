package br.edu.ifsp.lgp2.model;

/*
 * Enum que restringe os valores aceitos em Chamado.status.
 *
 * Antes o status era uma String livre, então qualquer texto enviado pela API
 * era aceito e gravado no banco. Com o enum, um valor inválido é recusado na
 * conversão (formulário ou JSON) antes de chegar à camada de persistência.
 */
public enum StatusChamado {
    ABERTO,
    EM_ANALISE,
    CONCLUIDO,
    CANCELADO
}
