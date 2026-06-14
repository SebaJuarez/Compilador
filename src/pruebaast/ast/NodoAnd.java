package pruebaast.ast;

public class NodoAnd extends NodoExpresionBooleana {

    private final NodoExpresionBooleana izquierda;
    private final NodoExpresionBooleana derecha;

    public NodoAnd(NodoExpresionBooleana izquierda, NodoExpresionBooleana derecha) {
        super("AND");
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

        // Evaluamos la izquierda. Si falla, el salto deberá ir a la etiqueta de salida
        // (Else o Fin)
        // Para hacer esto limpio en un diseño simple de generador sin pasar etiquetas
        // por parámetro:
        // Haremos que devuelva un salto "JE" sobre una bandera que consolidamos.

        String auxBool = GeneradorAssembler.getNuevaAuxiliar();
        String etiqFallo = GeneradorAssembler.getNuevaEtiqueta();
        String etiqFinAnd = GeneradorAssembler.getNuevaEtiqueta();

        // 1. Asumimos que es VERDADERO (1)
        asm.append("MOV ").append(auxBool).append(", 1\n");

        // 2. Evaluamos lado izquierdo
        asm.append(izq.getCodigo());
        asm.append(izq.getOperando()).append(" ").append(etiqFallo).append("\n"); // Si no cumple izq, salta a fallo

        // 3. Evaluamos lado derecho
        asm.append(der.getCodigo());
        asm.append(der.getOperando()).append(" ").append(etiqFallo).append("\n"); // Si no cumple der, salta a fallo

        // 4. Si llega acá, ambas se cumplieron
        asm.append("JMP ").append(etiqFinAnd).append("\n");

        // 5. Etiqueta de fallo: seteamos la variable en 0 (FALSO)
        asm.append(etiqFallo).append(":\n");
        asm.append("MOV ").append(auxBool).append(", 0\n");

        asm.append(etiqFinAnd).append(":\n");

        // Comparamos el resultado final para devolver un salto compatible con
        // NodoIf/NodoCiclo
        asm.append("CMP ").append(auxBool).append(", 1\n");

        // Si NO es igual a 1 (es decir, falló), devuelve el salto "JNE"
        return new ResultadoAssembler(asm.toString(), "JNE");
    }

}
