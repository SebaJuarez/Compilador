package pruebaast.ast;

public class NodoCondicion extends NodoExpresionBooleana {
    private final NodoExpresionBooleana condicion;

    public NodoCondicion(NodoExpresionBooleana condicion) {
        super("COND");
        this.condicion = condicion;
    }

    @Override
    protected String graficar(String idPadre) {
        // Pasa transparente: no agrega nodo extra, delega directamente al hijo
        return condicion.graficar(idPadre);
    }
}
