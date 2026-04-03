package pruebaast.ast;

public class NodoMayor extends NodoComparacion {

    public NodoMayor(NodoExpresion izquierda, NodoExpresion derecha) {
        super(">", izquierda, derecha);
    }
}
