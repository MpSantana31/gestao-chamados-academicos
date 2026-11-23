package br.edu.ifsp.lgp2.service;

import br.edu.ifsp.lgp2.model.Chamado;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

/** Exportação: recebe os MESMOS registros filtrados pela tela e gera três formatos. */
@Service
public class ExportacaoService {
    private static final String[] COLUNAS={"ID","Titulo","Categoria","Status","Prioridade","Responsavel","Abertura"};
    private String[] linha(Chamado c) {
        return new String[]{String.valueOf(c.getId()), texto(c.getTitulo()),texto(c.getCategoria()),
                enumTexto(c.getStatus()),enumTexto(c.getPrioridade()),texto(c.getResponsavel()),
                c.getDataAbertura()==null?"":c.getDataAbertura().toString()};
    }
    private String texto(String s) {return s==null?"":s;}
    /** Status e prioridade agora são enums: o arquivo recebe o nome do valor. */
    private String enumTexto(Enum<?> valor) {return valor==null?"":valor.name();}

    /** CSV: UTF-8 com BOM para abrir acentos no Excel; ponto e vírgula como separador. */
    public byte[] csv(List<Chamado> itens) {
        StringBuilder sb=new StringBuilder("\uFEFF");
        escreverCsv(sb,COLUNAS);
        for (Chamado c:itens) escreverCsv(sb,linha(c));
        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }
    private void escreverCsv(StringBuilder sb,String[] campos) {
        for (int i=0;i<campos.length;i++) {
            if(i>0) sb.append(';');
            // Aspas duplicadas impedem que separadores e quebras de linha quebrem o CSV.
            String valor=campos[i].replace("\"","\"\"");
            // Previne interpretação de valores como fórmulas por planilhas.
            if (!valor.isEmpty() && "=+-@".indexOf(valor.charAt(0))>=0) valor="'"+valor;
            sb.append('"').append(valor).append('"');
        }
        sb.append("\r\n");
    }

    /** XLSX: Apache POI cria uma pasta de trabalho, uma aba e linhas de células. */
    public byte[] excel(List<Chamado> itens) throws IOException {
        try (Workbook livro=new XSSFWorkbook();ByteArrayOutputStream saida=new ByteArrayOutputStream()) {
            Sheet aba=livro.createSheet("Chamados");
            Font fonte=livro.createFont();fonte.setBold(true);
            CellStyle cabecalho=livro.createCellStyle();cabecalho.setFont(fonte);
            Row primeira=aba.createRow(0);
            for(int col=0;col<COLUNAS.length;col++) {
                Cell celula=primeira.createCell(col);celula.setCellValue(COLUNAS[col]);celula.setCellStyle(cabecalho);
            }
            int numero=1;
            for(Chamado c:itens) {
                Row linha=aba.createRow(numero++);
                String[] valores=linha(c);
                for(int col=0;col<valores.length;col++) {
                    String valor=valores[col];
                    if (!valor.isEmpty() && "=+-@".indexOf(valor.charAt(0))>=0) valor="'"+valor;
                    linha.createCell(col).setCellValue(valor);
                }
            }
            for(int col=0;col<COLUNAS.length;col++) aba.setColumnWidth(col,Math.min(40,Math.max(14,COLUNAS[col].length()+8))*256);
            livro.write(saida);
            return saida.toByteArray();
        }
    }

    /** PDF: PDFBox desenha um relatório paginado com fontes padrão (caracteres simples). */
    public byte[] pdf(List<Chamado> itens) throws IOException {
        try(PDDocument documento=new PDDocument();ByteArrayOutputStream saida=new ByteArrayOutputStream()) {
            PDPage pagina=null;
            PDPageContentStream canvas=null;
            int posicao=0;
            for(int i=-1;i<itens.size();i++) {
                if(canvas==null || posicao>=35) {
                    if(canvas!=null) canvas.close();
                    pagina=new PDPage();documento.addPage(pagina);
                    canvas=new PDPageContentStream(documento,pagina);
                    canvas.beginText();canvas.setFont(PDType1Font.HELVETICA_BOLD,15);
                    canvas.newLineAtOffset(40,760);canvas.showText("Relatorio de Chamados Academicos");canvas.endText();
                    posicao=0;
                }
                String texto=i==-1?"ID | Titulo | Status | Prioridade | Responsavel":formatar(itens.get(i));
                canvas.beginText();canvas.setFont(i==-1?PDType1Font.HELVETICA_BOLD:PDType1Font.HELVETICA,9);
                canvas.newLineAtOffset(40,735-posicao*19);
                canvas.showText(normalizarPdf(texto));canvas.endText();
                posicao++;
            }
            if(canvas!=null) canvas.close();
            documento.save(saida);return saida.toByteArray();
        }
    }
    private String formatar(Chamado c) {
        return c.getId()+" | "+limitar(c.getTitulo(),31)+" | "+enumTexto(c.getStatus())+" | "+enumTexto(c.getPrioridade())+" | "+limitar(c.getResponsavel(),17);
    }
    private String limitar(String t,int tamanho) {t=texto(t);return t.length()>tamanho?t.substring(0,tamanho-3)+"...":t;}
    private String normalizarPdf(String texto) {
        // A fonte Helvetica padrão do PDFBox 2 só oferece caracteres WinAnsi.
        String s=java.text.Normalizer.normalize(texto,java.text.Normalizer.Form.NFD).replaceAll("\\p{M}","");
        return s.replaceAll("[^\\x20-\\x7E]","?");
    }
}
