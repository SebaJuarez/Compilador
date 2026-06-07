include macros2.asm
include number.asm

.MODEL LARGE
.386
.STACK 200h

.DATA
___cant0 dd ?
_cte10 dd 10.0
_cte20 dd 20.0
_@aux1 dd ?
_cte15 dd 15.0
_cte2 dd 2.0
_@aux2 dd ?
_cte1 dd 1.0
_@aux3 dd ?
_cte0 dd 0.0
_@aux4 dd ?
_@aux5 dd ?
_cte4 dd 4.0
_@aux6 dd ?
_@aux7 dd ?
_cte7 dd 7.0
_@aux8 dd ?
_@aux9 dd ?
_cte8 dd 8.0
_@aux10 dd ?
_@aux11 dd ?
_cte3 dd 3.0
_@aux12 dd ?
_@aux13 dd ?
_cte6 dd 6.0
_@aux14 dd ?
_@aux15 dd ?
__x dd ?
__y dd ?
__resultado dd ?
__cant dd ?

.CODE
START:
MOV EAX, @DATA
MOV DS, EAX
MOV ES, EAX

FLD _cte10
FSTP __x

FLD _cte20
FSTP __y

FLD __x
FLD __y
FADD
FSTP _@aux1

FLD _@aux1
FSTP __resultado

FLD __resultado
FLD _cte15
FXCH
FCOMP
FSTSW AX
SAHF
JBE Etiq2

FLD __resultado
FLD _cte2
FMUL
FSTP _@aux2

FLD _@aux2
FSTP __resultado

DisplayFloat __resultado, 2
newLine 1

Etiq2:

Etiq3:
FLD __x
FLD __y
FXCH
FCOMP
FSTSW AX
SAHF
JAE Etiq4

FLD __x
FLD _cte1
FADD
FSTP _@aux3

FLD _@aux3
FSTP __x

DisplayFloat __x, 2
newLine 1

JMP Etiq3
Etiq4:

FLD _cte0
FSTP ___cant0

FLD _cte2
FLD _cte2
FPREM
FSTP _@aux4
FSTP ST(0)

FLD _@aux4
FLD _cte0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq6

FLD ___cant0
FLD _cte1
FADD
FSTP _@aux5

FLD _@aux5
FSTP ___cant0

Etiq6:

FLD _cte2
FLD _cte4
FPREM
FSTP _@aux6
FSTP ST(0)

FLD _@aux6
FLD _cte0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq8

FLD ___cant0
FLD _cte1
FADD
FSTP _@aux7

FLD _@aux7
FSTP ___cant0

Etiq8:

FLD _cte2
FLD _cte7
FPREM
FSTP _@aux8
FSTP ST(0)

FLD _@aux8
FLD _cte0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq10

FLD ___cant0
FLD _cte1
FADD
FSTP _@aux9

FLD _@aux9
FSTP ___cant0

Etiq10:

FLD _cte2
FLD _cte8
FPREM
FSTP _@aux10
FSTP ST(0)

FLD _@aux10
FLD _cte0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq12

FLD ___cant0
FLD _cte1
FADD
FSTP _@aux11

FLD _@aux11
FSTP ___cant0

Etiq12:

FLD _cte2
FLD _cte3
FPREM
FSTP _@aux12
FSTP ST(0)

FLD _@aux12
FLD _cte0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq14

FLD ___cant0
FLD _cte1
FADD
FSTP _@aux13

FLD _@aux13
FSTP ___cant0

Etiq14:

FLD _cte2
FLD _cte6
FPREM
FSTP _@aux14
FSTP ST(0)

FLD _@aux14
FLD _cte0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq16

FLD ___cant0
FLD _cte1
FADD
FSTP _@aux15

FLD _@aux15
FSTP ___cant0

Etiq16:

FLD ___cant0
FSTP __cant

DisplayFloat __cant, 2
newLine 1


MOV EAX, 4C00h
INT 21h
END START
