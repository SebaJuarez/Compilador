package compilador;
import java_cup.runtime.Symbol;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
%%

%cup
%public
%class Lexico
%line
%column
%char

%init{
	try{
		String dir = System.getProperty("user.dir");
		String path = dir + "/ts.txt";
		f = new File(path);
		bw = new BufferedWriter(new FileWriter(f,false));
		simbolsList = new ArrayList<>();
	}catch (IOException e){
		e.printStackTrace();
	}
%init}


%{
	BufferedWriter bw;
	File f;
	ArrayList<String> simbolsList;
	
	public void writeSymbolInTable(String s) throws IOException{
		if(!simbolsList.contains(s.split(",")[0])){
			bw.write(s);
			bw.newLine();
			bw.flush();
			simbolsList.add(s.split(",")[0]);
		}
	}
	
	public String s = "";
	final int MAX_STRING = 30;
    final int MAX_INT = 32767;
	

	private boolean validate_string(String textString) throws Exception {
		if (textString.length() > MAX_STRING) {
			throw new Exception("La longitud del lexema "+textString+" excede la esperada");
		}
		return true;
	}

    private boolean validate_int_number(String numberInt) throws Exception {
		if (Integer.parseInt(numberInt) > MAX_INT || Integer.parseInt(numberInt) < - MAX_INT) {
			throw new Exception("El numero entero del lexema "+numberInt+" excede el esperado");
		}
		return true;
	}
%}

AND = "&"
OR = "||"
NOT = "!!"
OPMOD = "%"
MAYOR_A = ">"
MAYOR_I = ">="
MENOR_A = "<"
MENOR_I = "<="
DISTINTO = "<>"
IGUAL_A = "=="
OPSUMA = "+"
OPRESTA = "-"
OPMULTI = "*"
OPDIV = "/"
PAR_A = "("
PAR_C = ")"
LLAVE_A = "{"
LLAVE_C = "}"
CORCH_A = "["
CORCH_C = "]"
COMA = ","
PUNTOC = ";"
PUNTOPUNTO = ":"
ASIGN = "="

LETRA = [a-zA-Z]
DGT = [0-9]
ESPACIO = [\t | \f | " " | \r|\n]
VALSTRING = "\"" [^\"\n\r]* "\""
NUMENT = {DGT}+
NUMREAL = ({DGT}"."{DGT}+) | ({DGT}+"."{DGT}*) | ({DGT}*"."{DGT}+)
VARIABLE = "_"{LETRA} ({LETRA} | {DGT})*
COMENT_A = "/$"~"$/"

WHILE = while | WHILE
IF = if | IF | If
ELSE = else | ELSE
DECVAR = decvar | DECVAR
ENDECVAR = enddecvar | ENDECVAR
PROGRAM = PROGRAM | program
END = end | END
PUT = put | PUT
STRING = string | STRING
FLOAT = float | FLOAT
INT = int | INT
CANT = cant | CANT

%%

<YYINITIAL> {
    
    {VARIABLE}   {
        writeSymbolInTable(yytext() + ",VARIABLE,,_,_");
        return new Symbol(sym.VARIABLE, yytext());
    }
    {NUMENT} {
        validate_int_number(yytext());
        writeSymbolInTable("_"+yytext()+", NUMENT , _ , "+yytext()+" , _ ");
        return new Symbol(sym.NUMENT, yytext());
    }
    {NUMREAL}  {
        writeSymbolInTable("_"+yytext()+", NUMREAL , _ , "+yytext()+" , _ ");
        return new Symbol(sym.NUMREAL, yytext());
    }       
    
    {IF}       { return new Symbol(sym.IF, yytext()); }
    {ELSE}     { return new Symbol(sym.ELSE, yytext()); }
    {WHILE}    { return new Symbol(sym.WHILE, yytext()); }
    {DECVAR}   { return new Symbol(sym.DECVAR, yytext()); }
    {ENDECVAR} { return new Symbol(sym.ENDECVAR, yytext()); }
    {PROGRAM}  { return new Symbol(sym.PROGRAM, yytext()); }
    {END}      { return new Symbol(sym.END, yytext()); }
    {PUT}      { return new Symbol(sym.PUT, yytext()); }
    {VALSTRING} {
        validate_string(yytext());
        writeSymbolInTable("_"+yytext().substring(1, yytext().length() - 1)+ ", VALSTRING , _ ,"+yytext().substring(1, yytext().length() - 1)+","+ yytext().substring(1, yytext().length() - 1).length());
        return new Symbol(sym.VALSTRING, yytext());
    }
    {STRING}   { return new Symbol(sym.STRING, yytext()); }
    {FLOAT}    { return new Symbol(sym.FLOAT, yytext()); }
    {INT}      { return new Symbol(sym.INT, yytext()); }
    {CANT}     { return new Symbol(sym.CANT, yytext()); }

    {OPSUMA}   { return new Symbol(sym.OPSUMA, yytext()); }
    {OPRESTA}  { return new Symbol(sym.OPRESTA, yytext()); }
    {OPMULTI}  { return new Symbol(sym.OPMULTI, yytext()); }
    {OPDIV}    { return new Symbol(sym.OPDIV, yytext()); }
    {COMA}     { return new Symbol(sym.COMA, yytext()); }
    {PUNTOC}   { return new Symbol(sym.PUNTOC, yytext()); }
    {PUNTOPUNTO} { return new Symbol(sym.PUNTOPUNTO, yytext()); }
    {OPMOD}    { return new Symbol(sym.OPMOD, yytext()); }
    {PAR_A}    { return new Symbol(sym.PAR_A, yytext()); }
    {PAR_C}    { return new Symbol(sym.PAR_C, yytext()); }
    {LLAVE_A}  { return new Symbol(sym.LLAVE_A, yytext()); }
    {LLAVE_C}  { return new Symbol(sym.LLAVE_C, yytext()); }
    {CORCH_A}  { return new Symbol(sym.CORCH_A, yytext()); }
    {CORCH_C}  { return new Symbol(sym.CORCH_C, yytext()); }
    {MENOR_A}  { return new Symbol(sym.MENOR_A, yytext()); }
    {MAYOR_A}  { return new Symbol(sym.MAYOR_A, yytext()); }
    {IGUAL_A}  { return new Symbol(sym.IGUAL_A, yytext()); }
    {MENOR_I}  { return new Symbol(sym.MENOR_I, yytext()); }
    {MAYOR_I}  { return new Symbol(sym.MAYOR_I, yytext()); }
    {DISTINTO} { return new Symbol(sym.DISTINTO, yytext()); }
    {AND}      { return new Symbol(sym.AND, yytext()); }
    {OR}       { return new Symbol(sym.OR, yytext()); }
    {NOT}      { return new Symbol(sym.NOT, yytext()); }
    {ASIGN}    { return new Symbol(sym.ASIGN, yytext()); }
    {COMENT_A} { }
    {ESPACIO}  { }
}

[^]  { throw new Error("Caracter no permitido: '" + yytext() + "' en la linea " + (yyline+1) + ", columna " + (yycolumn+1)); }
<<EOF>> { return new Symbol(sym.EOF); }
