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
}
