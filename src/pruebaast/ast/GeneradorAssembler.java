package pruebaast.ast;

import java.util.LinkedHashSet;
import java.util.Set;

public class GeneradorAssembler {
    private static int contadorAux = 1;
    public static Set<String> declarables = new LinkedHashSet<>();
    private static int contadorEtiquetas = 1;

    public static String getNuevaAuxiliar() {
        // Le agregamos el guion bajo al principio según la teoría de la cátedra
        String aux = "_@aux" + (contadorAux++);

        // Lo anotamos para que NodoPrograma lo imprima en el .DATA
        declarables.add(aux + " dd ?");

        return aux;
    }

    // Opcional: método para resetear el contador entre compilaciones
    public static void reset() {
        contadorAux = 1;
        contadorEtiquetas = 1;
        declarables.clear();
    }

    public static String getNuevaEtiqueta() {
        return "Etiq" + (contadorEtiquetas++);
    }
}