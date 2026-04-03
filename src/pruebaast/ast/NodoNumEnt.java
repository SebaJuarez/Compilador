package pruebaast.ast;

public class NodoNumEnt extends NodoExpresion {
    private final int valor;

    public NodoNumEnt(int valor) {
        super("NUMENT");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "ENT: " + valor;
    }
}
