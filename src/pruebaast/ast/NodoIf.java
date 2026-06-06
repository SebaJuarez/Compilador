package pruebaast.ast;

import java.util.List;

public class NodoIf extends NodoSentencia {
    private final NodoExpresionBooleana condicion;
    private final List<NodoSentencia> sentenciasThen;
    private final List<NodoSentencia> sentenciasElse;

    public NodoIf(NodoExpresionBooleana condicion, List<NodoSentencia> sentenciasThen,
            List<NodoSentencia> sentenciasElse) {
        super("IF");
        this.condicion = condicion;
        this.sentenciasThen = sentenciasThen;
        this.sentenciasElse = sentenciasElse;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        StringBuilder resultado = new StringBuilder();

        // Grafica el nodo IF
        resultado.append(super.graficar(idPadre));

        // Grafica la condición colgando directamente del nodo IF
        resultado.append(condicion.graficar(miId));

        // Agrega un nodo ficticio THEN colgando del nodo IF
        Nodo nodoThen = new Nodo("Then");
        resultado.append(nodoThen.graficar(miId));

        // Grafica las sentencias asociadas al "then" colgando del nodo ficticio THEN
        String idNodoThen = nodoThen.getIdNodo();
        for (NodoSentencia sentencia : sentenciasThen) {
            resultado.append(sentencia.graficar(idNodoThen));
        }

        // Si hay sentencias asociadas al "else"...
        if (sentenciasElse != null) {
            // Agrega un nodo ficticio "ELSE" colgando del nodo IF
            Nodo nodoElse = new Nodo("Else");
            resultado.append(nodoElse.graficar(miId));

            // Grafica las sentencias asociadas al "else" colgando del nodo ficticio ELSE
            String idNodoElse = nodoElse.getIdNodo();
            for (NodoSentencia sentencia : sentenciasElse) {
                resultado.append(sentencia.graficar(idNodoElse));
            }
        }

        return resultado.toString();
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        StringBuilder asm = new StringBuilder();
        ResultadoAssembler cond = condicion.generarAssembler();

        String etiqElse = GeneradorAssembler.getNuevaEtiqueta();
        String etiqEnd = GeneradorAssembler.getNuevaEtiqueta();

        asm.append(cond.getCodigo());

        // Si la condición es falsa, salta al ELSE (o al final si no hay ELSE)
        String etiquetaSalto = (sentenciasElse != null && !sentenciasElse.isEmpty()) ? etiqElse : etiqEnd;
        asm.append(cond.getOperando()).append(" ").append(etiquetaSalto).append("\n\n");

        // Bloque THEN
        for (NodoSentencia s : sentenciasThen) {
            asm.append(s.generarAssembler().getCodigo());
        }

        if (sentenciasElse != null && !sentenciasElse.isEmpty()) {
            asm.append("JMP ").append(etiqEnd).append("\n"); // Salta el bloque else
            asm.append(etiqElse).append(":\n");
            // Bloque ELSE
            for (NodoSentencia s : sentenciasElse) {
                asm.append(s.generarAssembler().getCodigo());
            }
        }

        asm.append(etiqEnd).append(":\n\n");

        return new ResultadoAssembler(asm.toString(), "");
    }
}
