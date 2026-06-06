package pruebaast.ast;

public class NodoNot extends NodoExpresionBooleana {
    private final NodoExpresionBooleana izquierda;

    public NodoNot(NodoExpresionBooleana izquierda) {
        super("NOT");
        this.izquierda = izquierda;

    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                izquierda.graficar(miId);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        ResultadoAssembler cond = izquierda.generarAssembler(); // Devuelve el código y el salto
        String saltoOriginal = cond.getOperando();
        String saltoInvertido = "";

        // Invertimos la lógica del salto
        switch (saltoOriginal) {
            case "JE":
                saltoInvertido = "JNE";
                break;
            case "JNE":
                saltoInvertido = "JE";
                break;
            case "JA":
                saltoInvertido = "JBE";
                break; // > pasa a <=
            case "JAE":
                saltoInvertido = "JB";
                break; // >= pasa a <
            case "JB":
                saltoInvertido = "JAE";
                break; // < pasa a >=
            case "JBE":
                saltoInvertido = "JA";
                break; // <= pasa a >
            default:
                saltoInvertido = saltoOriginal;
        }

        // Retornamos el mismo código evaluador, pero con la instrucción de salto
        // cambiada
        return new ResultadoAssembler(cond.getCodigo(), saltoInvertido);
    }

}
