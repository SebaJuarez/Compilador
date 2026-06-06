package pruebaast.ast;

public class NodoMod extends NodoExpresionBinaria {

    public NodoMod(NodoExpresion izquierda, NodoExpresion derecha) {
        super("%", izquierda, derecha);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        ResultadoAssembler izq = super.izquierda.generarAssembler();
        ResultadoAssembler der = super.derecha.generarAssembler();
        String aux = GeneradorAssembler.getNuevaAuxiliar();

        StringBuilder asm = new StringBuilder();
        asm.append(izq.getCodigo()).append(der.getCodigo());

        // FPREM divide ST(0) por ST(1) y deja el resto en ST(0)
        asm.append("FLD ").append(der.getOperando()).append("\n"); // ST(1) = divisor
        asm.append("FLD ").append(izq.getOperando()).append("\n"); // ST(0) = dividendo
        asm.append("FPREM\n");
        asm.append("FSTP ").append(aux).append("\n"); // Guardar el resto
        asm.append("FSTP ST(0)\n\n"); // Limpiar el divisor que quedó en la pila

        return new ResultadoAssembler(asm.toString(), aux);
    }
}
