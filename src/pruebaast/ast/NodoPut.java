package pruebaast.ast;

public class NodoPut extends NodoSentencia {
    private final NodoExpresion expresion;

    public NodoPut(NodoExpresion expresion) {
        super("PUT");
        this.expresion = expresion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                expresion.graficar(miId);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        ResultadoAssembler expr = expresion.generarAssembler();
        StringBuilder asm = new StringBuilder();

        asm.append(expr.getCodigo());
        // Se asume que el operando devuelto por ConstanteString es su nombre en la
        // tabla de datos
        asm.append("MOV DX, OFFSET ").append(expr.getOperando()).append("\n");
        asm.append("MOV AH, 9\n");
        asm.append("INT 21h\n\n");

        return new ResultadoAssembler(asm.toString(), "");
    }
}
