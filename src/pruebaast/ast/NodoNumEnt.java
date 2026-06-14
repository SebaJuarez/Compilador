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

    @Override
    public ResultadoAssembler generarAssembler() {
        String val = String.valueOf(this.valor).replace("-", "_neg_");
        String nombre = "_cte" + val;

        GeneradorAssembler.declarables.add(nombre + " dd " + this.valor + ".0");

        return new ResultadoAssembler("", nombre);
    }
}
