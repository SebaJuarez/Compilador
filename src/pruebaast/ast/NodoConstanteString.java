package pruebaast.ast;

public class NodoConstanteString extends NodoExpresion {
    private final String valor;

    public NodoConstanteString(String valor) {
        super("STRING");
        this.valor = valor;
    }

    @Override
    public String getDescripcionNodo() {
        return "STR: " + valor;
    }

    @Override
    public ResultadoAssembler generarAssembler() {

        return new ResultadoAssembler("", "_" + this.valor);
    }
}
