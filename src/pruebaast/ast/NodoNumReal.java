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

    @Override
    public ResultadoAssembler generarAssembler() {
        String val = String.valueOf(this.valor).replace(".", "_").replace("-", "_neg_");
        String nombre = "_cte" + val;
        GeneradorAssembler.declarables.add(nombre + " dd " + this.valor);
        return new ResultadoAssembler("", nombre);
    }
}
