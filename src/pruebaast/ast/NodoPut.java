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

        // Agregamos el código previo (si es una suma, etc.)
        if (expr.getCodigo() != null) {
            asm.append(expr.getCodigo());
        }

        if (this.expresion instanceof NodoConstanteString) {
            // Imprime el string usando la macro de macros2.asm
            asm.append("displayString ").append(expr.getOperando()).append("\n");
        } else {
            // Imprime el número usando la macro de number.asm (con 2 decimales)
            asm.append("DisplayFloat ").append(expr.getOperando()).append(", 2\n");
        }

        // SIEMPRE metemos un salto de línea al final del PUT para que quede prolijo
        asm.append("newLine 1\n\n");

        return new ResultadoAssembler(asm.toString(), "");
    }
}
