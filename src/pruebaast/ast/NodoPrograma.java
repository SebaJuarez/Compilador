package pruebaast.ast;

import java.util.List;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class NodoPrograma extends Nodo {
    private final List<NodoSentencia> sentencias;

    public NodoPrograma(List<NodoSentencia> sentencias) {
        super("PGM");
        this.sentencias = sentencias;
    }

    public String graficar() {
        return this.graficar(null);
    }

    @Override
    protected String graficar(String idPadre) {
        final String miId = "nodo_programa";

        StringBuilder resultado = new StringBuilder();
        resultado.append("graph G {\n");

        resultado.append(miId + " [label=\"Programa\"]\n");
        for (NodoSentencia sentencia : this.sentencias) {
            resultado.append(sentencia.graficar(miId));
        }

        resultado.append("}");

        return resultado.toString();
    }

    @Override
    public ResultadoAssembler generarAssembler() {

        // PASO 1: Recorrer las sentencias PRIMERO.
        // Esto hace que los nodos se ejecuten y vayan llenando la lista
        // GeneradorAssembler.declarables con los @aux que necesiten.
        StringBuilder codigoSentencias = new StringBuilder();
        for (NodoSentencia sentencia : this.sentencias) {
            ResultadoAssembler res = sentencia.generarAssembler();
            if (res != null && res.getCodigo() != null) {
                codigoSentencias.append(res.getCodigo());
            }
        }

        // PASO 2: Ahora sí armamos el archivo desde el principio
        StringBuilder asm = new StringBuilder();

        asm.append("include macros2.asm\n");
        asm.append("include number.asm\n\n");

        asm.append(".MODEL LARGE\n");
        asm.append(".386\n");
        asm.append(".STACK 200h\n\n");

        // PASO 3: Sección de Datos (.DATA)
        asm.append(".DATA\n");

        // A) Leer ts.txt y meterlo en 'declarables' para evitar duplicados
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader("ts.txt"))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                if (linea.trim().isEmpty())
                    continue;

                String[] partes = linea.split(",", 5);
                if (partes.length >= 4) {
                    String nombre = partes[0].trim();
                    String tipo = partes[1].trim();

                    switch (tipo) {
                        case "VARIABLE":
                            GeneradorAssembler.declarables.add(nombre + " dd ?");
                            break;
                        case "NUMENT":
                            String valorEnt = partes[3].trim();
                            GeneradorAssembler.declarables.add(nombre + " dd " + valorEnt + ".0");
                            break;
                        case "NUMREAL":
                            String valorReal = partes[3].trim();
                            GeneradorAssembler.declarables.add(nombre + " dd " + valorReal);
                            break;
                        case "STRING":
                        case "VALSTRING":
                            String valorStr = partes[3].trim();
                            GeneradorAssembler.declarables
                                    .add(nombre + " db \"" + valorStr + "\", '$', " + valorStr.length() + " dup(?)");
                            break;
                    }
                }
            }
        } catch (java.io.IOException e) {
            System.out.println("Error al leer la Tabla de Simbolos: " + e.getMessage());
        }

        // B) Escribir TODO junto (Variables del TS + Auxiliares).
        // Al ser un Set, no habrá NINGÚN duplicado.
        for (String declaracion : GeneradorAssembler.declarables) {
            asm.append(declaracion).append("\n");
        }
        asm.append("\n");

        // PASO 4: Sección de Código (.CODE)
        asm.append(".CODE\n");
        asm.append("START:\n");
        asm.append("MOV EAX, @DATA\n");
        asm.append("MOV DS, EAX\n");
        asm.append("MOV ES, EAX\n\n");

        // Pegamos las instrucciones que habíamos guardado en el PASO 1
        asm.append(codigoSentencias.toString());

        // Fin del programa
        asm.append("\nMOV EAX, 4C00h\n");
        asm.append("INT 21h\n");
        asm.append("END START\n");

        return new ResultadoAssembler(asm.toString(), "");
    }
}