package pruebaast.ast;

public class NodoIgual extends NodoComparacion {

    public NodoIgual(NodoExpresion izquierda, NodoExpresion derecha) {
        super("==", izquierda, derecha);
    }
}
