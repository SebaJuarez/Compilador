package pruebaast.ast;

public class Nodo implements IGenerarAssembler {
    private final String descripcion;

    public Nodo(String descripcion) {
        this.descripcion = descripcion;
    }

    protected String getIdNodo() {
        return "nodo_" + this.hashCode();
    }

    public String getDescripcionNodo() {
        return descripcion;
    }

    protected String graficar(String idPadre) {
        return String.format("%1$s [label=\"%2$s\"]\n%3$s -- %1$s\n", getIdNodo(), getDescripcionNodo(), idPadre);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        return new ResultadoAssembler("", "");
    }

}