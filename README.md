# Compiladores 2026 — TP Integrador

Compilador completo con análisis léxico, sintáctico, generación de AST y generación de código assembler (TASM). La aplicación genera un archivo `.asm` a partir del código fuente y permite compilarlo a `.exe` directamente desde la GUI.

---

## Requisitos

| Herramienta | Versión | Para qué |
|---|---|---|
| JDK | 8 o superior | Correr la aplicación |
| Eclipse IDE | cualquiera | Desarrollo |
| JFlex | 1.6.x | Regenerar el léxico (opcional) |
| CUP | v0.11b | Regenerar el parser (opcional) |
| DOSBox | 0.74-3 | Compilar el `.asm` a `.exe` |

> **Lexico.java**, **parser.java** y **sym.java** ya están generados y commiteados. Solo necesitás regenerarlos si modificás `Lexico.flex` o `sintactico.cup`.

> **TASM.EXE** y **TLINK.EXE** ya están en `tools/tasm/` del proyecto. Solo necesitás instalar DOSBox.

---

## Instalación

### 1. Clonar el repositorio

```bash
git clone <url-del-repo>
cd Compilador
```

### 2. Importar en Eclipse

1. `File → Import → Existing Projects into Workspace`
2. Seleccioná la carpeta `Compilador`
3. Click **Finish**

### 3. Agregar los JARs al Build Path

Los JARs ya están en `lib/`. Solo hay que registrarlos:

1. Click derecho sobre el proyecto → `Build Path → Configure Build Path`
2. Pestaña **Libraries** → `Add External JARs...`
3. Seleccioná los 3 archivos de la carpeta `lib/`:
   - `java-cup-11b.jar`
   - `java-cup-11b-runtime.jar`
   - `jflex-full-1.6.1.jar`
4. Click **Apply and Close**

### 4. Instalar DOSBox

DOSBox es necesario para compilar el `.asm` a `.exe`. TASM es un ensamblador de 16 bits que no corre directamente en Windows 10 de 64 bits.

Descargalo de: **https://www.dosbox.com** → versión 0.74-3

Una vez instalado, el botón **⚙ Compilar .exe** de la GUI lo detecta automáticamente. Si no lo encontrás, la aplicación muestra un cartel con instrucciones.

---

## Estructura del proyecto

```
Compilador/
├── src/
│   ├── compilador/
│   │   ├── Lexico.flex          ← léxico (fuente JFlex)
│   │   ├── sintactico.cup       ← gramática (fuente CUP)
│   │   ├── Lexico.java          ← generado por JFlex
│   │   ├── parser.java          ← generado por CUP
│   │   └── sym.java             ← generado por CUP
│   ├── pruebaast/ast/
│   │   ├── Nodo.java
│   │   ├── NodoPrograma.java    ← genera la estructura completa del .asm
│   │   ├── NodoAsignacion.java
│   │   ├── NodoCiclo.java       ← WHILE
│   │   ├── NodoIf.java          ← IF / ELSE
│   │   ├── NodoPut.java         ← PUT (salida)
│   │   ├── NodoSuma/Resta/Multiplicacion/Division/Mod.java
│   │   ├── NodoMenor/Mayor/Igual/Distinto/...java  ← comparaciones
│   │   ├── NodoAnd/Or/Not.java  ← operadores lógicos
│   │   ├── NodoNumEnt.java      ← constante entera
│   │   ├── NodoNumReal.java     ← constante real
│   │   ├── NodoIdentificador.java
│   │   ├── NodoConstanteString.java
│   │   ├── GeneradorAssembler.java   ← maneja auxiliares y etiquetas
│   │   ├── IGenerarAssembler.java    ← interface del generador
│   │   └── ResultadoAssembler.java   ← encapsula código + operando
│   └── gui/
│       └── VentanaCompilador.java
├── lib/
│   ├── java-cup-11b.jar
│   ├── java-cup-11b-runtime.jar
│   └── jflex-full-1.6.1.jar
├── tools/
│   └── tasm/
│       ├── TASM.EXE             ← Turbo Assembler
│       └── TLINK.EXE            ← Turbo Linker
├── pruebas/
│   ├── programa_prueba.txt
│   ├── programa_if.txt
│   ├── cant_diferentes_casos.txt
│   └── cant_en_if.txt
├── macros2.asm                  ← macros de display y entrada
├── number.asm                   ← macro DisplayFloat
├── Final.asm                    ← salida del compilador (se sobreescribe)
├── Final.obj                    ← generado por TASM
├── Final.exe                    ← ejecutable final
└── ts.txt                       ← tabla de símbolos
```

---

## Ejecutar la aplicación

1. Click derecho sobre `VentanaCompilador.java`
2. `Run As → Java Application`

---

## Usar la aplicación

### Compilar a AST + Assembler

1. Click en **📂 Seleccionar** → elegí un archivo `.txt` con el programa fuente
2. Click en **▶ Ejecutar**
3. La consola muestra la traza del análisis (reglas aplicadas)
4. El panel derecho muestra el árbol AST en formato Graphviz DOT
5. En la raíz del proyecto se genera/actualiza `Final.asm`

### Compilar a ejecutable (.exe)

1. Primero ejecutar el paso anterior para tener `Final.asm`
2. Click en **⚙ Compilar .exe**
3. La aplicación usa DOSBox + TASM automáticamente
4. Si DOSBox no está instalado → aparece un cartel con instrucciones
5. Si `tools/tasm/TASM.EXE` no existe → aparece un cartel indicando dónde copiar los archivos
6. Al finalizar, `Final.exe` queda en la raíz del proyecto

### Ver el árbol AST visualmente

Copiá el contenido del panel AST y pegalo en:
👉 https://dreampuf.github.io/GraphvizOnline/

---

## El lenguaje

### Estructura básica

```
program
    <sentencias>
end
```

### Variables

- Deben comenzar con `_` seguido de una letra: `_x`, `_resultado`, `_miVar`
- Se declaran implícitamente al usarse

### Tipos de datos

| Tipo | Ejemplo |
|---|---|
| Entero | `10`, `42` |
| Real | `3.14`, `1.5` |
| String | `"hola mundo"` |

### Sentencias

```
_x = 10                      ← asignación
_y = _x + 5                  ← expresión aritmética
put(_x)                      ← salida por pantalla
put("hola")                  ← salida de string

if (_x > 0) {                ← condicional
    put(_x)
}

if (_x > 0) {                ← condicional con else
    put(_x)
} else {
    put(_y)
}

while (_x < 10) {            ← ciclo
    _x = _x + 1
}

_c = cant[2, 4, 6, 8]       ← cantidad de pares en la lista
```

### Operadores aritméticos

| Operador | Operación |
|---|---|
| `+` | Suma |
| `-` | Resta |
| `*` | Multiplicación |
| `/` | División |
| `%` | Módulo |

### Operadores de comparación

| Operador | Significado |
|---|---|
| `==` | Igual |
| `<>` | Distinto |
| `<` | Menor |
| `>` | Mayor |
| `<=` | Menor o igual |
| `>=` | Mayor o igual |

### Operadores lógicos

| Operador | Significado |
|---|---|
| `&` | AND |
| `\|\|` | OR |
| `!!` | NOT |

### CANT

`cant[lista]` cuenta cuántos números pares hay en la lista:

```
_c = cant[2, 4, 7, 8, 3, 6]    ← resultado: 4
_c = cant[]                      ← resultado: 0
```

### Comentarios

```
/$ esto es un comentario $/
```

---

## Regenerar el léxico y el parser (solo si modificás los fuentes)

### JFlex → Lexico.java

```bash
# Desde la raíz del proyecto
java -jar lib/java-cup-11b.jar -parser parser -symbols sym -destdir src/compilador src/compilador/sintactico.cup
```

### CUP → parser.java + sym.java

```bash
java -jar lib/java-cup-11b.jar -parser parser -symbols sym -destdir src/compilador src/compilador/sintactico.cup
```

Después de regenerar: click derecho sobre el proyecto en Eclipse → **Refresh (F5)**.

Si `Lexico.java` tiene error de package, verificá que la primera línea sea:
```java
package compilador;
```

---

## Cómo funciona el generador de assembler

El compilador genera código TASM para el coprocesador matemático x87 (`.MODEL LARGE`, `.386`).

- Todas las variables y constantes se declaran en el segmento `.DATA` como `dd` (doble palabra, 32 bits)
- Las operaciones aritméticas usan instrucciones FPU: `FLD`, `FADD`, `FSUB`, `FMUL`, `FDIV`, `FPREM`, `FSTP`
- Las comparaciones usan `FCOMP` + `FSTSW AX` + `SAHF` + saltos condicionales
- Los saltos se generan automáticamente con etiquetas (`Etiq1`, `Etiq2`, ...)
- Las variables temporales se generan automáticamente (`_@aux1`, `_@aux2`, ...)
- La salida de números usa la macro `DisplayFloat` de `number.asm`
- La salida de strings usa la macro `displayString` de `macros2.asm`

---

## Troubleshooting

**El proyecto tiene errores rojos en Eclipse**
→ Verificá que los 3 JARs estén en el Build Path (ver sección de instalación)

**"Error al leer la Tabla de Símbolos"**
→ Asegurate de ejecutar la aplicación desde Eclipse con el directorio de trabajo en la raíz del proyecto (es el comportamiento por defecto)

**El botón Compilar .exe no hace nada o da error**
→ Verificá que DOSBox esté instalado. Si instalaste DOSBox pero sigue sin funcionar, revisá que esté en `C:\Program Files (x86)\DOSBox-0.74-3\`

**TASM falla durante el ensamblado**
→ Abrí `Final.asm` manualmente y revisá si hay algún símbolo no declarado en `.DATA`

**CUP da "can't open file"**
→ Ejecutá el comando desde la raíz del proyecto (donde está la carpeta `src/`)
