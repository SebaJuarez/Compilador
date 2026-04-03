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
}
