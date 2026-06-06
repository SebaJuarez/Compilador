package pruebaast.ast;

public class ResultadoAssembler {
    private String codigo;   // Las instrucciones ASM generadas
    private String operando; // El nombre de la variable o @aux donde quedó el resultado

    public ResultadoAssembler(String codigo, String operando) {
        this.codigo = codigo;
        this.operando = operando;
    }

    public String getCodigo() { return codigo; }
    public String getOperando() { return operando; }
}
