package pruebaast.ast;

public class NodoMenorIgual extends NodoComparacion {

    public NodoMenorIgual(NodoExpresion izquierda, NodoExpresion derecha) {
        super("<=", izquierda, derecha);
    }
}
