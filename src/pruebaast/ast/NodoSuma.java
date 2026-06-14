package pruebaast.ast;

public class NodoSuma extends NodoExpresionBinaria {

    public NodoSuma(NodoExpresion izquierda, NodoExpresion derecha) {
        super("+", izquierda, derecha);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        // 1. Obtener código de los hijos
        ResultadoAssembler izq = super.izquierda.generarAssembler();
        ResultadoAssembler der = super.derecha.generarAssembler();

        // 2. Pedir una variable auxiliar nueva
        String aux = GeneradorAssembler.getNuevaAuxiliar();

        // 3. Armar las instrucciones
        StringBuilder codigo = new StringBuilder();
        codigo.append(izq.getCodigo()); // Código previo del hijo izquierdo (si lo hay)
        codigo.append(der.getCodigo()); // Código previo del hijo derecho (si lo hay)

        // Instrucciones del coprocesador para sumar
        codigo.append("FLD ").append(izq.getOperando()).append("\n");
        codigo.append("FLD ").append(der.getOperando()).append("\n");
        codigo.append("FADD\n");
        codigo.append("FSTP ").append(aux).append("\n\n"); // Guardar en el auxiliar y sacar de la pila

        // IMPORTANTE: Deberás registrar 'aux' en tu Tabla de Símbolos en este punto
        // para que luego se imprima en el .DATA

        return new ResultadoAssembler(codigo.toString(), aux);
    }
}