package pruebaast.ast;

public class NodoMod extends NodoExpresionBinaria {

    public NodoMod(NodoExpresion izquierda, NodoExpresion derecha) {
        super("%", izquierda, derecha);
    }
}
