package pruebaast.ast;

public class NodoMultiplicacion extends NodoExpresionBinaria {

    public NodoMultiplicacion(NodoExpresion izquierda, NodoExpresion derecha) {
        super("*", izquierda, derecha);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        ResultadoAssembler izq = super.izquierda.generarAssembler();
        ResultadoAssembler der = super.derecha.generarAssembler();
        String aux = GeneradorAssembler.getNuevaAuxiliar();

        StringBuilder asm = new StringBuilder();
        asm.append(izq.getCodigo()).append(der.getCodigo());

        asm.append("FLD ").append(izq.getOperando()).append("\n");
        asm.append("FLD ").append(der.getOperando()).append("\n");
        asm.append("FMUL\n");
        asm.append("FSTP ").append(aux).append("\n\n");

        return new ResultadoAssembler(asm.toString(), aux);
    }
}
