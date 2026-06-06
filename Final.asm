include macros2.asm
include number.asm

.MODEL LARGE
.386
.STACK 200h

.DATA
__x dd ?
_cte10 dd 10.0
__y dd ?
_cte20 dd 20.0
__resultado dd ?
_cte15 dd 15.0
_cte2 dd 2.0
_cte1 dd 1.0
__cant dd ?
_cte4 dd 4.0
_cte7 dd 7.0
_cte8 dd 8.0
_cte3 dd 3.0
_cte6 dd 6.0

.CODE
START:
MOV EAX, @DATA
MOV DS, EAX
MOV ES, EAX

FLD _10
FSTP __x

FLD _20
FSTP __y

FLD __x
FLD __y
FADD
FSTP @aux1

FLD @aux1
FSTP __resultado

FLD __resultado
FLD _15
FXCH
FCOMP
FSTSW AX
SAHF
JBE Etiq2

FLD __resultado
FLD _2
FMUL
FSTP @aux2

FLD @aux2
FSTP __resultado

MOV DX, OFFSET __resultado
MOV AH, 9
INT 21h

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
FLD _1
FADD
FSTP @aux3

FLD @aux3
FSTP __x

MOV DX, OFFSET __x
MOV AH, 9
INT 21h

JMP Etiq3
Etiq4:

FLD _0
FSTP ___cant0

FLD _2
FLD _2
FPREM
FSTP @aux4
FSTP ST(0)

FLD @aux4
FLD _0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq6

FLD ___cant0
FLD _1
FADD
FSTP @aux5

FLD @aux5
FSTP ___cant0

Etiq6:

FLD _2
FLD _4
FPREM
FSTP @aux6
FSTP ST(0)

FLD @aux6
FLD _0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq8

FLD ___cant0
FLD _1
FADD
FSTP @aux7

FLD @aux7
FSTP ___cant0

Etiq8:

FLD _2
FLD _7
FPREM
FSTP @aux8
FSTP ST(0)

FLD @aux8
FLD _0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq10

FLD ___cant0
FLD _1
FADD
FSTP @aux9

FLD @aux9
FSTP ___cant0

Etiq10:

FLD _2
FLD _8
FPREM
FSTP @aux10
FSTP ST(0)

FLD @aux10
FLD _0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq12

FLD ___cant0
FLD _1
FADD
FSTP @aux11

FLD @aux11
FSTP ___cant0

Etiq12:

FLD _2
FLD _3
FPREM
FSTP @aux12
FSTP ST(0)

FLD @aux12
FLD _0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq14

FLD ___cant0
FLD _1
FADD
FSTP @aux13

FLD @aux13
FSTP ___cant0

Etiq14:

FLD _2
FLD _6
FPREM
FSTP @aux14
FSTP ST(0)

FLD @aux14
FLD _0
FXCH
FCOMP
FSTSW AX
SAHF
JNE Etiq16

FLD ___cant0
FLD _1
FADD
FSTP @aux15

FLD @aux15
FSTP ___cant0

Etiq16:

FLD ___cant0
FSTP __cant

MOV DX, OFFSET __cant
MOV AH, 9
INT 21h


MOV EAX, 4C00h
INT 21h
END START
