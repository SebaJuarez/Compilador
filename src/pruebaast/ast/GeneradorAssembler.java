package pruebaast.ast;

import java.util.LinkedHashSet;
import java.util.Set;

public class GeneradorAssembler {
    private static int contadorAux = 1;
    public static Set<String> declarables = new LinkedHashSet<>();
    private static int contadorEtiquetas = 1;

    public static String getNuevaAuxiliar() {
        return "@aux" + (contadorAux++);
    }

    // Opcional: método para resetear el contador entre compilaciones
    public static void reset() {
        contadorAux = 1;
    }

    public static String getNuevaEtiqueta() {
        return "Etiq" + (contadorEtiquetas++);
    }
}