# Compiladores 2026 — Guía de instalación en Eclipse
## Requisitos previos
- JDK 8 o superior
- Eclipse IDE for Java Developers
- JFlex 1.6.x (jar)
- CUP v0.11b (java-cup-11b.jar + java-cup-11b-runtime.jar)

---

## Descargar los JARs necesarios

Necesitás DOS archivos JAR de CUP y UNO de JFlex:

| Archivo                    | Para qué sirve                        |
|----------------------------|---------------------------------------|
| java-cup-11b.jar           | Compilar el .cup → genera parser.java |
| java-cup-11b-runtime.jar   | Runtime (se agrega al proyecto)       |
| jflex-full-1.6.1.jar       | Compilar el .flex → genera Lexico.java|

Guardá los 3 jars en la carpeta `lib/` de este proyecto.

---

## Estructura de carpetas

```
compiladores2026/
├── src/
│   ├── compilador/          ← paquete compilador
│   │   ├── Lexico.flex      ← léxico (fuente)
│   │   ├── sintactico.cup   ← sintáctico (fuente)
│   │   ├── Lexico.java      ← generado por JFlex
│   │   ├── parser.java      ← generado por CUP
│   │   └── sym.java         ← generado por CUP
│   ├── pruebaast/
│   │   └── ast/
│   │       ├── Nodo.java
│   │       ├── NodoAnd.java
│   │       ├── NodoAsignacion.java
│   │       ├── NodoCant.java
│   │       ├── NodoCiclo.java
│   │       ├── NodoComparacion.java
│   │       ├── NodoCondicion.java
│   │       ├── NodoConstante.java
│   │       ├── NodoConstanteString.java
│   │       ├── NodoDistinto.java
│   │       ├── NodoDivision.java
│   │       ├── NodoExpresion.java
│   │       ├── NodoExpresionBinaria.java
│   │       ├── NodoExpresionBooleana.java
│   │       ├── NodoIdentificador.java
│   │       ├── NodoIf.java
│   │       ├── NodoIgual.java
│   │       ├── NodoMayor.java
│   │       ├── NodoMayorIgual.java
│   │       ├── NodoMenor.java
│   │       ├── NodoMenorIgual.java
│   │       ├── NodoMod.java
│   │       ├── NodoMultiplicacion.java
│   │       ├── NodoNot.java
│   │       ├── NodoNumEnt.java
│   │       ├── NodoNumReal.java
│   │       ├── NodoOr.java
│   │       ├── NodoPrograma.java
│   │       ├── NodoPut.java
│   │       ├── NodoResta.java
│   │       ├── NodoSentencia.java
│   │       └── NodoSuma.java
│   └── gui/
│       └── VentanaCompilador.java
├── lib/
│   ├── java-cup-11b.jar
│   ├── java-cup-11b-runtime.jar
│   └── jflex-full-1.6.1.jar
└── programa_prueba.txt
```

---

## Agregar los JARs al Build Path

1. Click derecho sobre el proyecto → `Build Path → Configure Build Path`
2. Pestaña **Libraries** → `Add External JARs...`
3. Seleccioná los 3 JARs de la carpeta `lib/`
4. Click **Apply and Close**

---

## Generar Lexico.java, parser.java y sym.java

### Generar con JFlex (Lexico.java)

Abrí una terminal (cmd / PowerShell en Windows) y ejecutá:

```bash
# Windows (ajustá la ruta a tu JDK y al jar)
java -jar lib\jflex-full-1.6.1.jar src\compilador\Lexico.flex

# Linux/Mac
java -jar lib/jflex-full-1.6.1.jar src/compilador/Lexico.flex
```

Esto genera `src/compilador/Lexico.java` automáticamente.

### Generar con CUP (parser.java + sym.java)

```bash
# Windows
java -jar lib\java-cup-11b.jar -parser parser -symbols sym -destdir src\compilador src\compilador\sintactico.cup

# Linux/Mac
java -jar lib/java-cup-11b.jar -parser parser -symbols sym -destdir src/compilador src/compilador/sintactico.cup
```

Esto genera `src/compilador/parser.java` y `src/compilador/sym.java`.

### Refrescar Eclipse

Click derecho sobre el proyecto → **Refresh** (F5).
Los nuevos archivos van a aparecer. Si hay errores de compilación en
`Lexico.java` sobre el package, asegurate que la primera línea diga:
```java
package compilador;
```

---

## Verificar que compile

En Eclipse, el proyecto no debería mostrar errores en rojo.
Si aparece alguno, revisá:
- Que los 3 JARs estén en el Build Path
- Que todos los .java de `pruebaast/ast/` estén copiados
- Que `VentanaCompilador.java` esté en el paquete `gui`

---

## Ejecutar la aplicación

1. Click derecho sobre `VentanaCompilador.java`
2. `Run As → Java Application`
3. Se abre la ventana del compilador

---

## Usar la aplicación

1. Click en **📂 Seleccionar** → elegí `programa_prueba.txt`
2. Click en **▶ Ejecutar**
3. La consola muestra la traza del análisis (las reglas que se aplicaron)
4. El panel derecho muestra el árbol en formato **Graphviz DOT**
5. Para verlo visualmente: copiá el contenido y pegalo en:
   👉 https://dreampuf.github.io/GraphvizOnline/

---

## TIPS IMPORTANTES

### Si JFlex genera el archivo en otro lugar:
Movelo manualmente a `src/compilador/` y verificá que el package sea `compilador`.

### Si CUP da error de "can't open file":
Ejecutá el comando desde la raíz del proyecto (donde está la carpeta `src/`).

### El archivo programa_prueba.txt tiene este programa de ejemplo:
```
program
    _x = 10
    _y = 20
    _resultado = _x + _y
    if (_resultado > 15) {
        _resultado = _resultado * 2
        put(_resultado)
    }
    while (_x < _y) {
        _x = _x + 1
        put(_x)
    }
    _cant = cant[2, 4, 7, 8, 3, 6]
    put(_cant)
end
```

### Variables en este lenguaje:
- Deben comenzar con `_` seguido de una letra: `_x`, `_miVar`, `_resultado`
- Palabras reservadas: `program`, `end`, `if`, `else`, `while`, `put`, `cant`
- Operadores lógicos: `&` (AND), `||` (OR), `!!` (NOT)
- Distinto: `<>`, Igual: `==`
- Comentarios: `/$  esto es un comentario  $/`

---

## Resultado esperado en el panel AST

El panel derecho mostrará algo como:

```
graph G {
nodo_programa [label="Programa"]
nodo_12345 [label=":="]
nodo_programa -- nodo_12345
nodo_67890 [label="ID: _x"]
nodo_12345 -- nodo_67890
...
}
```

Copiá eso en https://dreampuf.github.io/GraphvizOnline/ y verás el árbol graficado.
