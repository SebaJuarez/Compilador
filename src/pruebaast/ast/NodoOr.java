package pruebaast.ast;

public class NodoOr extends NodoExpresionBooleana {
    private final NodoExpresionBooleana izquierda;
    private final NodoExpresionBooleana derecha;

    public NodoOr(NodoExpresionBooleana izquierda, NodoExpresionBooleana derecha) {
        super("OR");
        this.izquierda = izquierda;
        this.derecha = derecha;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        return super.graficar(idPadre) +
                izquierda.graficar(miId) +
                derecha.graficar(miId);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        ResultadoAssembler izq = this.izquierda.generarAssembler();
        ResultadoAssembler der = this.derecha.generarAssembler();

        StringBuilder asm = new StringBuilder();
        String auxBool = GeneradorAssembler.getNuevaAuxiliar();
        String etiqVerdadero = GeneradorAssembler.getNuevaEtiqueta();
        String etiqFallo = GeneradorAssembler.getNuevaEtiqueta();
        String etiqFinOr = GeneradorAssembler.getNuevaEtiqueta();

        // 1. Asumimos FALSO (0) por defecto
        asm.append("MOV ").append(auxBool).append(", 0\n");

        // 2. Evaluamos lado izquierdo. OJO: Tenemos que invertir el salto que devuelve
        // para saber si ACERTÓ.
        asm.append(izq.getCodigo());
        String saltoAcertoIzq = invertirSalto(izq.getOperando()); // Deberás crear el método invertirSalto (como en
                                                                  // NodoNot)
        asm.append(saltoAcertoIzq).append(" ").append(etiqVerdadero).append("\n");

        // 3. Evaluamos lado derecho (si la izquierda falló, llega acá)
        asm.append(der.getCodigo());
        String saltoAcertoDer = invertirSalto(der.getOperando());
        asm.append(saltoAcertoDer).append(" ").append(etiqVerdadero).append("\n");

        // 4. Si llegó acá, ninguna pasó. Salta al final manteniendo el 0
        asm.append("JMP ").append(etiqFinOr).append("\n");

        // 5. Etiqueta de Verdadero
        asm.append(etiqVerdadero).append(":\n");
        asm.append("MOV ").append(auxBool).append(", 1\n");

        asm.append(etiqFinOr).append(":\n");

        // Comparamos el resultado final
        asm.append("CMP ").append(auxBool).append(", 1\n");

        // Devuelve "JNE" (Salta si No es Igual a 1) para que el IF/WHILE sepa qué hacer
        return new ResultadoAssembler(asm.toString(), "JNE");
    }

    // Método utilitario necesario en la misma clase:
    private String invertirSalto(String salto) {
        switch (salto) {
            case "JE":
                return "JNE";
            case "JNE":
                return "JE";
            case "JA":
                return "JBE";
            case "JAE":
                return "JB";
            case "JB":
                return "JAE";
            case "JBE":
                return "JA";
            default:
                return salto;
        }
    }

}
