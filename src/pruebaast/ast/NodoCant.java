package pruebaast.ast;

import java.util.List;

public class NodoCant extends NodoExpresion {
    private final List<NodoSentencia> sentencias;

    public NodoCant(List<NodoSentencia> sentencias) {
        super("CANT");
        this.sentencias = sentencias;
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = this.getIdNodo();
        StringBuilder resultado = new StringBuilder();
        resultado.append(super.graficar(idPadre));

        Nodo nodoCuerpo = new Nodo("Lista");
        resultado.append(nodoCuerpo.graficar(miId));
        String idCuerpo = nodoCuerpo.getIdNodo();

        for (NodoSentencia s : sentencias) {
            resultado.append(s.graficar(idCuerpo));
        }

        return resultado.toString();
    }
}
