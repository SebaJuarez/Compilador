package pruebaast.ast;

public class NodoMenorIgual extends NodoComparacion {

    public NodoMenorIgual(NodoExpresion izquierda, NodoExpresion derecha) {
        super("<=", izquierda, derecha);
    }

    @Override
    public ResultadoAssembler generarAssembler() {
        ResultadoAssembler izq = super.izquierda.generarAssembler();
        ResultadoAssembler der = super.derecha.generarAssembler();

        StringBuilder asm = new StringBuilder();
        asm.append(izq.getCodigo()).append(der.getCodigo());

        asm.append("FLD ").append(izq.getOperando()).append("\n");
        asm.append("FLD ").append(der.getOperando()).append("\n");
        asm.append("FXCH\n");
        asm.append("FCOMP\n");
        asm.append("FSTSW AX\n");
        asm.append("SAHF\n");

        return new ResultadoAssembler(asm.toString(), "JA");
    }
}
