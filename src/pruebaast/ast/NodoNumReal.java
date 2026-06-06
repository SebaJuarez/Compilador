package pruebaast.ast;

public class NodoNumReal extends NodoExpresion {
    private final float valor;

    public NodoNumReal(float valor) {
        super("NUMREAL");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "REAL: " + valor;
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        // Reemplaza el punto decimal por un guión bajo para que sea una etiqueta ASM
        // válida
        String val = String.valueOf(this.valor).replace(".", "_").replace("-", "_neg_");
        return new ResultadoAssembler("", "_" + val);
    }
}
