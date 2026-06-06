package pruebaast.ast;

public class NodoAsignacion extends NodoSentencia {
    private final NodoIdentificador identificador;
    private final NodoExpresion expresion;

    public NodoAsignacion(NodoIdentificador identificador, NodoExpresion expresion) {
        super(":=");
        this.identificador = identificador;
        this.expresion = expresion;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                identificador.graficar(miId) +
                expresion.graficar(miId);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        ResultadoAssembler expr = expresion.generarAssembler();
        ResultadoAssembler var = identificador.generarAssembler();

        StringBuilder asm = new StringBuilder();
        asm.append(expr.getCodigo());

        asm.append("FLD ").append(expr.getOperando()).append("\n");
        asm.append("FSTP ").append(var.getOperando()).append("\n\n");

        return new ResultadoAssembler(asm.toString(), "");
    }
}
