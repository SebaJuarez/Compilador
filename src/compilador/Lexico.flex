package compilador;
import java_cup.runtime.Symbol;
import java_cup.runtime.ComplexSymbolFactory;
import java_cup.runtime.ComplexSymbolFactory.ComplexSymbol;
import java_cup.runtime.ComplexSymbolFactory.Location;
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
	private ComplexSymbolFactory csf;

	public Lexico(java.io.Reader in, ComplexSymbolFactory sf) {
		this(in);
		this.csf = sf;
	}

	private Symbol symbol(int type) {
		if (csf != null) {
			Location left  = new Location(yyline + 1, yycolumn + 1, yychar);
			Location right = new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
			return csf.newSymbol(sym.terminalNames[type], type, left, right);
		}
		return new Symbol(type);
	}

	private Symbol symbol(int type, Object value) {
		if (csf != null) {
			Location left  = new Location(yyline + 1, yycolumn + 1, yychar);
			Location right = new Location(yyline + 1, yycolumn + yylength(), yychar + yylength());
			return csf.newSymbol(sym.terminalNames[type], type, left, right, value);
		}
		return new Symbol(type, value);
	}

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


	private boolean validate_string(String textString) {
		if (textString.length() > MAX_STRING) {
			throw new RuntimeException("La longitud del lexema "+textString+" excede la esperada");
		}
		return true;
	}

    private boolean validate_int_number(String numberInt) {
		if (Integer.parseInt(numberInt) > MAX_INT || Integer.parseInt(numberInt) < - MAX_INT) {
			throw new RuntimeException("El numero entero del lexema "+numberInt+" excede el esperado");
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
        return symbol(sym.VARIABLE, yytext());
    }
    {NUMENT} {
        validate_int_number(yytext());
        writeSymbolInTable("_"+yytext()+", NUMENT , _ , "+yytext()+" , _ ");
        return symbol(sym.NUMENT, yytext());
    }
    {NUMREAL}  {
        writeSymbolInTable("_"+yytext()+", NUMREAL , _ , "+yytext()+" , _ ");
        return symbol(sym.NUMREAL, yytext());
    }

    {IF}       { return symbol(sym.IF, yytext()); }
    {ELSE}     { return symbol(sym.ELSE, yytext()); }
    {WHILE}    { return symbol(sym.WHILE, yytext()); }
    {DECVAR}   { return symbol(sym.DECVAR, yytext()); }
    {ENDECVAR} { return symbol(sym.ENDECVAR, yytext()); }
    {PROGRAM}  { return symbol(sym.PROGRAM, yytext()); }
    {END}      { return symbol(sym.END, yytext()); }
    {PUT}      { return symbol(sym.PUT, yytext()); }
    {VALSTRING} {
        validate_string(yytext());
        writeSymbolInTable("_"+yytext().substring(1, yytext().length() - 1)+ ", VALSTRING , _ ,"+yytext().substring(1, yytext().length() - 1)+","+ yytext().substring(1, yytext().length() - 1).length());
        return symbol(sym.VALSTRING, yytext());
    }
    {STRING}   { return symbol(sym.STRING, yytext()); }
    {FLOAT}    { return symbol(sym.FLOAT, yytext()); }
    {INT}      { return symbol(sym.INT, yytext()); }
    {CANT}     { return symbol(sym.CANT, yytext()); }

    {OPSUMA}   { return symbol(sym.OPSUMA); }
    {OPRESTA}  { return symbol(sym.OPRESTA); }
    {OPMULTI}  { return symbol(sym.OPMULTI); }
    {OPDIV}    { return symbol(sym.OPDIV); }
    {COMA}     { return symbol(sym.COMA); }
    {PUNTOC}   { return symbol(sym.PUNTOC); }
    {PUNTOPUNTO} { return symbol(sym.PUNTOPUNTO); }
    {OPMOD}    { return symbol(sym.OPMOD); }
    {PAR_A}    { return symbol(sym.PAR_A); }
    {PAR_C}    { return symbol(sym.PAR_C); }
    {LLAVE_A}  { return symbol(sym.LLAVE_A); }
    {LLAVE_C}  { return symbol(sym.LLAVE_C); }
    {CORCH_A}  { return symbol(sym.CORCH_A); }
    {CORCH_C}  { return symbol(sym.CORCH_C); }
    {MENOR_A}  { return symbol(sym.MENOR_A); }
    {MAYOR_A}  { return symbol(sym.MAYOR_A); }
    {IGUAL_A}  { return symbol(sym.IGUAL_A); }
    {MENOR_I}  { return symbol(sym.MENOR_I); }
    {MAYOR_I}  { return symbol(sym.MAYOR_I); }
    {DISTINTO} { return symbol(sym.DISTINTO); }
    {AND}      { return symbol(sym.AND); }
    {OR}       { return symbol(sym.OR); }
    {NOT}      { return symbol(sym.NOT); }
    {ASIGN}    { return symbol(sym.ASIGN); }
    {COMENT_A} { }
    {ESPACIO}  { }
}

[^]  { throw new Error("Caracter no permitido: '" + yytext() + "' en la linea " + (yyline+1) + ", columna " + (yycolumn+1)); }
<<EOF>> { return symbol(sym.EOF); }
