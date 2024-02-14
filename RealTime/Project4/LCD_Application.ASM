   ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;; 

#include p16f877a.inc                ; Include register definition file
TTH1	equ 43h
TH1		equ 44h
HUN1	equ	45h
TEN1	equ	46h
DIG1	equ	47h
NUM1	equ	48h

svnum	equ	49h
TTH2	equ 23h
TH2		equ 24h
HUN2	equ	25h
TEN2	equ	26h
DIG2	equ	27h
temp	equ 22h
RES_MOD	equ 28h
op		equ 29h
Temp	equ	30h

RES1	equ 33h
RES2	equ 34h
RES3	equ	35h
RES4	equ	36h
RES5	equ	37h
RES6	equ	38h
CARRY	equ 39h
divFlag	equ	21h
char1	equ	87h
char2	equ 88h
char3	equ 89h
char4   equ 90h
chsNum   equ 90h

TEMPDIG1	equ 60h
TEMPTEN1	equ 61h
TEMPHUN1	equ 62h
TEMPTH1		equ 63h
TEMPTTH1	equ 64h

RES1T	equ 65h
RES2T	equ 66h
RES3T	equ 67h
RES4T		equ 68h
RES5T	equ 69h

CNT		equ 32h


OPERATION	equ	49h
NUM2	equ	50h
NUMOROP equ 51h
svop equ 54h

TEMP	equ	52h
Result	equ	53h
CURRMax	equ	7dh
TEMPDIG	equ	7eh
Cout equ 7fh
Timer1	EQU	70h		; 1ms count register
TimerX	EQU	71h		; Xms count register
Select	EQU	74h		; Used to set or clear RS bit
OutCod	EQU	75h		; Temp store for output code

RS	EQU	1		; Register select output bit
E	EQU	2		; Enable display input
   ORG	000
   NOP
	
   GOTO	init
   ORG	004
   
	 
	BTFSS Cout,5
	INCF Cout,1
	BCF	INTCON,2	; Reset TMR0 interrupt flag
	RETFIE	

init	ORG	0x0050
	 NOP
	 BANKSEL TRISB
	 MOVLW b'00000010'
	 MOVWF TRISB
	 
	 MOVLW b'00000000'
	 MOVWF TRISC
	 CLRF	TEMPDIG
	 
	 MOVLW b'11000111'
	 BANKSEL OPTION_REG
	 MOVWF OPTION_REG
	 MOVLW	b'10100000'	; INTCON init. code
	 MOVWF	INTCON		; Enable TMR0 interrupt
	 
	 BANKSEL	TRISD
	 MOVLW	b'00000000'
	 MOVWF	TRISD
	 
	 BANKSEL	PORTD
	 
	 CLRF	TTH1
	 CLRF	TH1
	 CLRF	HUN1
	 CLRF	TEN1
	 CLRF	DIG1
	 CLRF	TTH2
	 CLRF	TH2
	 CLRF	HUN2
	 CLRF	TEN2
	 CLRF	DIG2
	 CLRF	NUM1
	 CLRF	OPERATION
	 CLRF	NUM2
	 CLRF	NUMOROP
	 CLRF	TEMP
	 CLRF	Result
	 CLRF	CURRMax
	 CLRF	TEMPDIG
	 CLRF	Cout
	 CLRF	Timer1
	 CLRF	TimerX
	 CLRF	Select	
	 CLRF	 OutCod
	 clrf	RES_MOD
	 CALL 	inid
	 GOTO start
	 
	 
start	NOP	
	;goto div
   CALL	INTRODISPLAY
   ;GET the 3 DIGITS of the first Number
	
   
   CLRF	NUM1
   CLRF	NUM2
	CLRF	TEMPDIG
   CLRF	NUMOROP
   CLRF	Result
   CALL	Get3Digits

   
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   CLRF	TEMPDIG
   MOVLW	2
   MOVWF	CURRMax
   BSF	NUMOROP,0
   CALL	GetDigit
   MOVF	TEMPDIG,W
   MOVWF	OPERATION
   MOVWF svop
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   CLRF	TEMPDIG
   ;GET the 3 DIGITS of the first Number
   CLRF	NUMOROP
   CALL	Get3Digits2
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   BSF	NUMOROP,0
   MOVLW	D'3'
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
	
	movf OPERATION, W
    btfss STATUS, Z
    goto div
    goto add

AFTERCALC
   call clear
   call inid
   call INTRODISPLAY2
   MOVF	Result,W
   MOVWF	PORTC
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   CLRF	NUMOROP

   MOVF	RES6,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES5,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

	MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES4,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES3,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES1,W
;	sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   


Loop56 BTFSS Cout,5
   GOTO Loop56
   MOVLW	D'255'
   CALL	xms
   
   BCF	Select,RS
   MOVLW	0x01
   CALL	send
   MOVLW	0x02
   CALL	send
   
   
   GOTO	start
   
   
;Procedures

table	MOVF	TEMPDIG,W		; Get key count
	ADDWF	PCL,F		; and calculate jump
	RETLW	B'00101011'	; Code for '+'
	RETLW	B'00100101'	; Code for '%'
	RETLW	B'00101111'	; Code for '/'
	RETLW	B'00111101'	;Code for '='
	RETURN

   
GetDigit	
     BANKSEL PORTD
     CALL	DISPDIG
     CLRF	TMR0
     CLRF	Cout
CHECK1	BTFSS  PORTB,1 ;if set PortB[1] GOTO NextNum
   GOTO CHECKTIMER0
   GOTO INCNUM
   
CHECKTIMER0	; if 2 seconds have passed got to the next digit
   BTFSS Cout,5
   GOTO CHECK1
   GOTO GETDIGEND

INCNUM   
   CLRF	TMR0
   CLRF	Cout
   MOVF	CURRMax,W		      ; if >2 return to 0
   SUBWF	TEMPDIG,W
   BTFSC	STATUS,Z
   GOTO	SET0
   GOTO	NORMALINC  
SET0
   CLRF	TEMPDIG
   GOTO DISP
NORMALINC
   INCF	TEMPDIG,1

DISP
   CALL	DISPDIG
CHECK0	BTFSC	PORTB,1
		 GOTO	CHECK0
   GOTO	CHECK1
 
GETDIGEND
    RETURN
 

Get3Digits
   MOVLW	6
   MOVWF	CURRMax
   CLRF	TMR0
   CLRF	Cout
   CALL	GetDigit
   MOVF	TEMPDIG,W
   MOVWF	TTH1
   ;take the first number TENS digit
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   MOVLW	6
   SUBWF	TTH1,W
   BTFSS	STATUS,Z
   GOTO	SETTHEMAXTo9
   MOVLW	5   
   GOTO	BeforeTen1
SETTHEMAXTo9	
   MOVLW	9
BeforeTen1
   MOVWF 	CURRMax
   CLRF		TEMPDIG
   CLRF		TMR0
   CLRF		Cout
   CALL		GetDigit
   MOVF		TEMPDIG,W
   MOVWF	TH1
   ;take the first number digit
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send

   MOVLW	6 ;comparing before
   SUBWF	TTH1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN250
   MOVLW	5  ; comparing me
   SUBWF	TH1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN250
   MOVLW	5 ; if i am max and everything before is max
   GOTO	BeforeDig3
LESSTHAN250	
   MOVLW	9

BeforeDig3
   MOVWF 	CURRMax
   CLRF		TEMPDIG
   CLRF		TMR0
   CLRF		Cout
   CALL		GetDigit
   MOVF		TEMPDIG,W
   MOVWF	HUN1
   ;take the first number digit
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
	MOVLW	6 ;comparing before
   SUBWF	TTH1,W
   BTFSS	STATUS,Z
	GOTO	LESSTHAN2502

   MOVLW	5 ;comparing before
   SUBWF	TH1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN2502

   MOVLW	5  ; comparing me
   SUBWF	HUN1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN2502
   MOVLW	3 ; if i am max and everything before is max
   GOTO	BeforeTen12

LESSTHAN2502
   MOVLW	9

BeforeTen12
   MOVWF 	CURRMax
   CLRF		TEMPDIG
   CLRF		TMR0
   CLRF		Cout
   CALL		GetDigit
   MOVF		TEMPDIG,W
   MOVWF	TEN1
   ;take the first number digit
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
	MOVLW	6
   SUBWF	TTH1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN2503
MOVLW	5
   SUBWF	TH1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN2503

   MOVLW	5
   SUBWF	HUN1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN2503

   MOVLW	3  
   SUBWF	TEN1,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN2503
   MOVLW	5
   GOTO	BeforeDig1
LESSTHAN2503
   MOVLW	9
BeforeDig1
   MOVWF	CURRMax
   CLRF	TEMPDIG
   CLRF	TMR0
   CLRF	Cout
   CALL	GetDigit
   MOVF	TEMPDIG,W
   MOVWF	DIG1
   RETURN

Get3Digits2
   MOVLW	6
   MOVWF	CURRMax
   CLRF	TMR0
   CLRF	Cout
   CALL	GetDigit
   MOVF	TEMPDIG,W
   MOVWF	TTH2
   ;take the first number TENS digit
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   MOVLW	6
   SUBWF	TTH2,W
   BTFSS	STATUS,Z
   GOTO	SETTHEMAXTo922
   MOVLW	5   
   GOTO	BeforeTen122
SETTHEMAXTo922	
   MOVLW	9
BeforeTen122
   MOVWF 	CURRMax
   CLRF		TEMPDIG
   CLRF		TMR0
   CLRF		Cout
   CALL		GetDigit
   MOVF		TEMPDIG,W
   MOVWF	TH2
   ;take the first number digit
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send

   MOVLW	6 ;comparing before
   SUBWF	TTH2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN25022
   MOVLW	5  ; comparing me
   SUBWF	TH2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN25022
   MOVLW	5 ; if i am max and everything before is max
   GOTO	BeforeDig32
LESSTHAN25022
   MOVLW	9

BeforeDig32
   MOVWF 	CURRMax
   CLRF		TEMPDIG
   CLRF		TMR0
   CLRF		Cout
   CALL		GetDigit
   MOVF		TEMPDIG,W
   MOVWF	HUN2
   ;take the first number digit
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
	MOVLW	6 ;comparing before
   SUBWF	TTH2,W
   BTFSS	STATUS,Z
	GOTO	LESSTHAN250222

   MOVLW	5 ;comparing before
   SUBWF	TH2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN250222

   MOVLW	5  ; comparing me
   SUBWF	HUN2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN250222
   MOVLW	3 ; if i am max and everything before is max
   GOTO	BeforeTen1222

LESSTHAN250222
   MOVLW	9

BeforeTen1222
   MOVWF 	CURRMax
   CLRF		TEMPDIG
   CLRF		TMR0
   CLRF		Cout
   CALL		GetDigit
   MOVF		TEMPDIG,W
   MOVWF	TEN2
   ;take the first number digit
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
	MOVLW	6
   SUBWF	TTH2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN25032
MOVLW	5
   SUBWF	TH2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN25032

   MOVLW	5
   SUBWF	HUN2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN25032

   MOVLW	3  
   SUBWF	TEN2,W
   BTFSS	STATUS,Z
   GOTO	LESSTHAN25032
   MOVLW	5
   GOTO	BeforeDig12
LESSTHAN25032
   MOVLW	9
BeforeDig12
   MOVWF	CURRMax
   CLRF	TEMPDIG
   CLRF	TMR0
   CLRF	Cout
   CALL	GetDigit
   MOVF	TEMPDIG,W
   MOVWF	DIG2
   RETURN


   

onems	MOVLW	D'249'		; Count for 1ms delay 
	MOVWF	Timer1		; Load count
loop1	NOP			; Pad for 4 cycle loop
	DECFSZ	Timer1,F		; Count
	GOTO	loop1		; until Z
	RETURN			; and finish

;--------------------------------------------------------------
;	Delay Xms
;	Receives count in W, uses Onems
;--------------------------------------------------------------
xms	MOVWF	TimerX		; Count for X ms
loopX	CALL	onems		; Delay 1ms
	DECFSZ	TimerX,F		; Repeat X times 
	GOTO	loopX		; until Z
	RETURN			; and finish

;--------------------------------------------------------------
;	Generate data/command clock siganl E
;--------------------------------------------------------------
pulseE	
	BANKSEL	PORTD
	BSF	PORTD,E		; Set E high
	CALL	onems		; Delay 1ms
	BCF	PORTD,E		; Reset E low
	CALL	onems		; Delay 1ms
	RETURN			; done

;--------------------------------------------------------------
;	Send a command byte in two nibbles from RB4 - RB7
;	Receives command in W, uses PulseE and Onems
;--------------------------------------------------------------
send	
   BANKSEL	PORTD
	MOVWF	OutCod		; Store output code
	ANDLW	0F0		; Clear low nybble
	MOVWF	PORTD		; Output high nybble
	BTFSC	Select,RS	; Test RS bit
	BSF	PORTD,RS	; and set for data
	CALL	pulseE		; and clock display register
	CALL	onems		; wait 1ms for display

	SWAPF	OutCod,F		; Swap low and high nybbles 
	MOVF	OutCod,W	; Retrieve output code
	ANDLW	0F0		; Clear low nybble
	MOVWF	PORTD		; Output low nybble
	BTFSC	Select,RS	; Test RS bit
	BSF	PORTD,RS	; and set for data
	CALL	pulseE		; and clock display register
	CALL	onems		; wait 1ms for display
	RETURN			; done

;--------------------------------------------------------------
;	Initialise the display
;	Uses Send
;--------------------------------------------------------------
inid	
   BANKSEL	PORTD
	CLRF	PORTD
	MOVLW	D'100'		; Load count for 100ms delay
	CALL	xms		; and wait for display start
	MOVLW	0F0		; Mask for select code
	MOVWF	Select		; High nybble not masked

	MOVLW	0x30		; Load initial nibble
	MOVWF	PORTD		; and output it to display
	CALL	pulseE		; Latch initial code
	MOVLW	D'5'		; Set delay 5ms
	CALL	xms		; and wait
	CALL	pulseE		; Latch initial code again
	CALL	onems		; Wait 1ms
	CALL	pulseE		; Latch initial code again
	BCF	PORTD,4		; Set 4-bit mode
	CALL	pulseE		; Latch it
	
	MOVLW	0x28		; Set 4-bit mode, 2 lines
	CALL	send		; and send code
	MOVLW	0x08		; Switch off display
	CALL	send		; and send code
	MOVLW	0x01		; Code to clear display
	CALL	send		; and send code
	MOVLW	0x06		; Enable cursor auto inc  
	CALL	send		; and send code
	MOVLW	0x80		; Zero display address
	CALL	send		; and send code
	MOVLW	0x0C		; Turn on display  
	CALL	send		; and send code

	RETURN			; Done  
	
clear	MOVLW	01		; code to clear display
	BCF	Select,RS	; Select data mode
	CALL	send		; and send code
	RETURN
		
DISPDIG
   BANKSEL	NUMOROP
   BTFSC	NUMOROP,0
   GOTO	SENDOP
   MOVF	TEMPDIG,W
   ADDLW	0x30
   GOTO	BEFORESEND
SENDOP
   CALL	table
BEFORESEND
   BSF	Select,RS
   CALL	send
   MOVLW	0x10
   BCF	Select,RS
   CALL	send
   RETURN
   
add	
	

	

	CLRF 	RES1
	CLRF 	RES2
	CLRF 	RES3
	CLRF 	RES4
	CLRF 	RES5
	CLRF 	RES6
	CLRF 	CARRY
	MOVLW	0
	MOVWF 	CARRY

	CLRF	W	
	movf 	DIG1, W          ; Move value of a into W register
    addwf 	DIG2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
	;movlw	48
	;sublw	W
    movwf 	RES1            ; Store the final result in z
	movf 	RES1, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes1
	movlw	d'0'
	movwf	CARRY
	goto	getRes2
		
fixRes1
	movlw	d'10'
	subwf	RES1, F
	movlw	d'1'
	movwf	CARRY	
getRes2
	CLRF	W	
	movf 	TEN1, W          ; Move value of a into W register
    addwf 	TEN2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
	;sublw	W
    movwf 	RES2            ; Store the final result in z
	movf 	RES2, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes2
	movlw	d'0'
	movwf	CARRY
	goto	getRes3

fixRes2
	movlw	d'10'
	subwf	RES2, F
	movlw	d'1'
	movwf	CARRY	
getRes3
	CLRF	W	
	movf 	HUN1, W          ; Move value of a into W register
    addwf 	HUN2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
;	sublw	W
    movwf 	RES3            ; Store the final result in z
	movf 	RES3, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes3
	movlw	d'0'
	movwf	CARRY
	goto	getRes4

fixRes3
	movlw	d'10'
	subwf	RES3, F
	movlw	d'1'
	movwf	CARRY	
getRes4
	CLRF	W	
	movf 	TH1, W          ; Move value of a into W register
    addwf 	TH2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
;	sublw	W
    movwf 	RES4           ; Store the final result in z
	movf 	RES4, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes4
	movlw	d'0'
	movwf	CARRY
	goto	getRes5

fixRes4
	movlw	d'10'
	subwf	RES4, F
	movlw	d'1'
	movwf	CARRY	
getRes5
	CLRF	W	
	movf 	TTH1, W          ; Move value of a into W register
    addwf 	TTH2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
;	sublw	W
    movwf 	RES5          ; Store the final result in z
	movf 	RES5, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes5
	movlw	d'0'
	movwf	CARRY
	goto	getRes6

fixRes5
	movlw	d'10'
	subwf	RES5, F
	movlw	d'1'
	movwf	CARRY

getRes6
	movf 	CARRY, W
	movwf 	RES6
	GOTO	AFTERCALC 	


INTRODISPLAY 
   CLRF	NUMOROP
   BSF	Select,RS
   MOVLW d'69' ; Print character "E" Enter Operation
   CALL	send
 
 MOVLW d'110' ; Print character "n"
 CALL	send
 
 MOVLW d'116' ; Print character "t"
 CALL	send
 
 MOVLW d'101' ; Print character "e"
 CALL	send
 
 MOVLW d'114' ; Print character "r"
 CALL	send
 
 MOVLW d'0' ; Print caracter " "
 CALL	send
 
 MOVLW d'79' ; Print caracter "O"
 CALL	send
 
 MOVLW d'112' ; Print character "p"
 CALL	send

 MOVLW d'101' ; Print character "e"
 CALL	send
 
 MOVLW d'114' ; Print character "r"
 CALL	send
 
 MOVLW d'97' ; Print character "a"
 CALL	send 
 
 MOVLW d'116' ; Print character "t"
 CALL	send
 
 MOVLW d'105' ; Print character "i"
 CALL	send
 
 MOVLW d'111' ; Print character "o"
 CALL	send
 
 MOVLW d'110' ; Print character "n"
 CALL	send
 
 BCF	Select,RS
 MOVLW	0xC0
 CALL	send
 
   RETURN

INTRODISPLAY2 

   CLRF	NUMOROP
   BSF	Select,RS

   MOVLW d'82' ; Print character "R" Enter Operation
   CALL	send
 
 
 MOVLW d'69' ; Print character "E"
 CALL	send
 
 MOVLW d'83' ; Print character "S"
 CALL	send
 
 MOVLW d'85' ; Print character "U"
 CALL	send
 
 MOVLW d'76' ; Print character "L"
 CALL	send
 
 MOVLW d'84' ; Print caracter "T"
 CALL	send
 
 MOVLW d'58' ; Print caracter ":"
 CALL	send
 
 BCF	Select,RS
 MOVLW	0xC0
 CALL	send

BSF	Select,RS

MOVLW d'61' ; Print caracter "="
 CALL	send

   
   
   MOVF	RES6,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES5,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

	MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES4,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES3,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES1,W
;	sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
   MOVLW D'3000'		; Load count for 100ms delay
   CALL	xms		; and wait for display start
 CALL	loopX
goto INTRODISPLAY3
 
   RETURN

CHECKZERO
	movf	DIG2, W
	btfss	STATUS, Z
	RETURN
	movf	TEN2, W
	btfss	STATUS, Z
	RETURN
	movf	HUN2, W
	btfss	STATUS, Z
	RETURN
	movf	TH2, W
	btfss	STATUS, Z
	RETURN
	movf	TTH2, W
	btfss	STATUS, Z
	RETURN
	goto	MATH_ERROR

div
	call 	CHECKZERO
	MOVLW	0x01
	MOVWF 	divFlag
	clrf	RES1
	clrf	RES2
	clrf	RES3
	clrf	RES4
	clrf	RES5
	clrf	RES6
	clrf	RES1T
	clrf	RES2T
	clrf	RES3T
	clrf	RES4T
	clrf	RES5T

	MOVF	DIG1,W
	MOVWF	TEMPDIG1
	MOVF	TEN1,W
	MOVWF	TEMPTEN1
	MOVF	HUN1,W
	MOVWF	TEMPHUN1
	MOVF	TH1,W
	MOVWF	TEMPTH1
	MOVF	TTH1,W
	MOVWF	TEMPTTH1

	DivLoop:
		BSF		STATUS,C			; Negative detect flag	
		MOVF	TTH2,W		; get first number
		SUBWF	TEMPTTH1,W		; subtract second
		MOVWF	RES5T		; and store result
		BTFSS	STATUS,C			; answer negative?
		GOTO	exitDiv				; yes, minus result
	
		BSF		STATUS,C			; Negative detect flag		
		MOVF	TH2,W			; get first number
		SUBWF	TEMPTH1,W			; subtract second
		MOVWF	RES4T			; and store result
		BTFSS	STATUS,C			; answer negative?
		GOTO	takeFromTenThoDiv	; yes, minus result
		GOTO	subHunDiv

takeFromTenThoDiv:
	MOVF	RES5T,W		; 
	BTFSC	STATUS,Z			; check if Z
	GOTO	exitDiv
	DECF  	RES5
   	MOVF	TEMPTH1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	TH2,W		; get first number
	SUBWF	Temp,W			; subtract second
	MOVWF	RES4T		; and store result
	
subHunDiv:
	BSF		STATUS,C			; Negative detect flag		
	MOVF	HUN2,W			; get first number
	SUBWF	TEMPHUN1,W			; subtract second
	MOVWF	RES3T			; and store result
	BTFSS	STATUS,C			; answer negative?
	GOTO	takeFromThoDiv		; yes, minus result
	GOTO 	subTenDiv

takeFromThoDiv:
	MOVF	RES4T,W		; 
	BTFSC	STATUS,Z	; check if Z
	GOTO	takeFromTenTho2Div
	DECF  	RES4T
   	MOVF	TEMPHUN1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	HUN2,W		; get first number
	SUBWF	Temp,W		; subtract second
	MOVWF	RES3T		; and store result
	GOTO	subTenDiv

takeFromTenTho2Div:
	MOVF	RES5T,W		; 
	BTFSC	STATUS,Z	; check if Z
	GOTO	exitDiv
	DECF  	RES5T
   	MOVLW	D'9'
	MOVWF	RES4T		; and store result
	MOVF	TEMPHUN1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	HUN2,W		; get first number
	SUBWF	Temp,W		; subtract second
	MOVWF	RES3T		; and store result

	
subTenDiv:	
	BSF		STATUS,C			; Negative detect flag		
	MOVF	TEN2,W			; get first number
	SUBWF	TEMPTEN1,W			; subtract second
	MOVWF	RES2T			; and store result
	
	BTFSS	STATUS,C			; answer negative?
	GOTO	takeFromHunDiv		; yes, minus result
	GOTO	subUnitDiv

takeFromHunDiv:
   	MOVF	RES3T,W		; 
	BTFSC	STATUS,Z	; check if Z
	GOTO	takeFromTho2Div
	DECF  	RES3T
   	MOVF	TEMPTEN1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	TEN2,W		; get first number
	SUBWF	Temp,W		; subtract second
	MOVWF	RES2T		; and store result
	GOTO	subUnitDiv
	
takeFromTho2Div:
	MOVF	RES4T,W		; 
	BTFSC	STATUS,Z	; check if Z
	GOTO	takeFromTenTho3Div
	DECF  	RES4T
	MOVLW	D'9'
	MOVWF	RES3T
   	MOVF	TEMPTEN1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	TEN2,W		; get first number
	SUBWF	Temp,W		; subtract second
	MOVWF	RES2T		; and store result
	GOTO	subUnitDiv

takeFromTenTho3Div:
	MOVF	RES5T,W		; 
	BTFSC	STATUS,Z	; check if Z
	GOTO	exitDiv
	DECF  	RES5T
   	MOVLW	D'9'
	MOVWF	RES4T		; and store result
	MOVLW	D'9'
	MOVWF	RES3T		; and store result
	MOVF	TEMPTEN1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	TEN2,W		; get first number
	SUBWF	Temp,W		; subtract second
	MOVWF	RES2T		; and store result
	
subUnitDiv:
	BSF		STATUS,C		; Negative detect flag		
	MOVF	DIG2,W		; get first number
	SUBWF	TEMPDIG1,W		; subtract second
	MOVWF	RES1T		; and store 

	BTFSS	STATUS,C		; answer negative?
	GOTO	takeFromTenDiv		; yes, minus result
	GOTO	subDoneDiv

takeFromTenDiv:
   	MOVF	RES2T,W		; 
	BTFSC	STATUS,Z			; check if Z
	GOTO	takeFromHun2Div
	DECF  	RES2T
   	MOVF	TEMPDIG1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	DIG2,W			; get first number
	SUBWF	Temp,W				; subtract second
	MOVWF	RES1T			; and store result
	GOTO	subDoneDiv
	
takeFromHun2Div:
   	MOVF	RES3T,W		; 
	BTFSC	STATUS,Z		; check if Z
	GOTO	takeFromTho3Div
	DECF  	RES3T
	MOVLW	D'9'
	MOVWF	RES2T	
   	MOVF	TEMPDIG1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	DIG2,W		; get first number
	SUBWF	Temp,W			; subtract second
	MOVWF	RES1T		; and store result
	GOTO	subDoneDiv
	
takeFromTho3Div:
	MOVF	RES4T,W		; 
	BTFSC	STATUS,Z		; check if Z
	GOTO	takeFromTenTho4Div
	DECF  	RES4T
	MOVLW	D'9'
	MOVWF	RES3T
	MOVLW	D'9'
	MOVWF	RES2T
	
    MOVF	TEMPDIG1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	DIG2,W		; get first number
	SUBWF	Temp,W		; subtract second
	MOVWF	RES1T		; and store result
	GOTO	subDoneDiv

takeFromTenTho4Div:
	MOVF	RES5T,W		; 
	BTFSC	STATUS,Z	; check if Z
	GOTO	exitDiv
	DECF  	RES5T
   	MOVLW	D'9'
	MOVWF	RES4T		; and store result
	MOVLW	D'9'
	MOVWF	RES3T		; and store result
	MOVLW	D'9'
	MOVWF	RES2T		; and store result     	
	MOVF	TEMPDIG1,W
	ADDLW 	D'10'
	MOVWF	Temp
	MOVF	DIG2,W		; get first number
	SUBWF	Temp,W		; subtract second
	MOVWF	RES1T		; and store result
	
subDoneDiv:
	MOVF	RES1T,W
	MOVWF	TEMPDIG1
	MOVF	RES2T,W
	MOVWF	TEMPTEN1
	MOVF	RES3T,W
	MOVWF	TEMPHUN1
	MOVF	RES4T,W
	MOVWF	TEMPTH1
	MOVF	RES5T,W
	MOVWF	TEMPTTH1
	

	INCF 	RES1
	BSF		STATUS,C			; Negative detect flag
	MOVLW 	D'10'
	SUBWF  	RES1,W
	BTFSS	STATUS,C		; answer positive?
	GOTO 	DivLoop
	GOTO 	addTenDiv
	

addTenDiv:
	MOVWF 	RES1
	INCF 	RES2
	BSF		STATUS,C			; Negative detect flag
	MOVLW 	D'10'
	SUBWF  	RES2,W
	BTFSS	STATUS,C		; answer positive?
	GOTO 	DivLoop
	GOTO 	addHunDiv


addHunDiv:
	MOVWF 	RES2
	INCF 	RES3
	BSF		STATUS,C			; Negative detect flag
	MOVLW 	D'10'
	SUBWF  	RES3,W
	BTFSS	STATUS,C		; answer positive?
	GOTO 	DivLoop
	GOTO 	addThoDiv


addThoDiv:
	MOVWF 	RES3
	INCF 	RES4
	BSF		STATUS,C			; Negative detect flag
	MOVLW 	D'10'
	SUBWF  	RES4,W
	BTFSS	STATUS,C		; answer positive?
	GOTO 	DivLoop
	GOTO 	addTenThoDiv


addTenThoDiv:
	MOVWF 	RES4
	INCF 	RES5
	GOTO 	DivLoop

exitDiv:
	movf 	OPERATION, W
	sublw	d'1'
    btfss STATUS, Z
    goto AFTERCALC
	
	MOVLW	0
	MOVWF 	CARRY

	CLRF	W	
	movf 	RES1T, W          ; Move value of a into W register
    addwf 	DIG2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
	;movlw	48
	;sublw	W
    movwf 	RES1T            ; Store the final result in z
	movf 	RES1T, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes11
	movlw	d'0'
	movwf	CARRY
	goto	getRes22
		
fixRes11
	movlw	d'10'
	subwf	RES1T, F
	movlw	d'1'
	movwf	CARRY	
getRes22
	CLRF	W	
	movf 	RES2T, W          ; Move value of a into W register
    addwf 	TEN2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
	;sublw	W
    movwf 	RES2T            ; Store the final result in z
	movf 	RES2T, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes22
	movlw	d'0'
	movwf	CARRY
	goto	getRes33

fixRes22
	movlw	d'10'
	subwf	RES2T, F
	movlw	d'1'
	movwf	CARRY	
getRes33
	CLRF	W	
	movf 	RES3T, W          ; Move value of a into W register
    addwf 	HUN2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
;	sublw	W
    movwf 	RES3T            ; Store the final result in z
	movf 	RES3T, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes33
	movlw	d'0'
	movwf	CARRY
	goto	getRes44

fixRes33
	movlw	d'10'
	subwf	RES3T, F
	movlw	d'1'
	movwf	CARRY	
getRes44
	CLRF	W	
	movf 	RES4T, W          ; Move value of a into W register
    addwf 	TH2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
;	sublw	W
    movwf 	RES4T          ; Store the final result in z
	movf 	RES4T, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes44
	movlw	d'0'
	movwf	CARRY
	goto	getRes55

fixRes44
	movlw	d'10'
	subwf	RES4T, F
	movlw	d'1'
	movwf	CARRY	
getRes55
	CLRF	W	
	movf 	RES5T, W          ; Move value of a into W register
    addwf 	TTH2, W         ; Add b to W register
    addwf 	CARRY, W         ; Add c to W register
;	movlw	48
;	sublw	W
    movwf 	RES5T          ; Store the final result in z
	movf 	RES5T, W
	sublw	d'9'
	btfss	STATUS, C
	goto 	fixRes55
	movlw	d'0'
	movwf	CARRY
	goto	getRes66

fixRes55
	movlw	d'10'
	subwf	RES5T, F
	movlw	d'1'
	movwf	CARRY

getRes66
	movlw	0
	movwf	RES6
	movf	RES5T, W
	movwf	RES5
	movf	RES4T, W
	movwf	RES4
	movf	RES3T, W
	movwf	RES3
	movf	RES2T, W
	movwf	RES2
	movf	RES1T, W
	movwf	RES1
	GOTO AFTERCALC

INTRODISPLAY3
CALL	inid

   CLRF	NUMOROP
   BSF	Select,RS

 MOVLW d'75' ; Print character "K"
 CALL	send
 
 MOVLW d'69' ; Print character "E"
 CALL	send
 
 MOVLW d'69' ; Print character "E"
 CALL	send
 
 MOVLW d'80' ; Print character "P"
 CALL	send
 
 MOVLW d'63' ; Print character "?"
 CALL	send
 
 MOVLW d'32' ; Print character " "
 CALL	send
 
 MOVLW d'91' ; Print caracter "["
 CALL	send
 
 MOVLW d'49' ; Print caracter "1"
 CALL	send
 
 MOVLW d'58' ; Print caracter ":"
 CALL	send
 
 MOVLW d'89' ; Print caracter "Y"
 CALL	send
 
 MOVLW d'44' ; Print caracter ","
 CALL	send
 
 MOVLW d'32' ; Print caracter " "
 CALL	send
 
 MOVLW d'50' ; Print caracter "2"
 CALL	send
 
 MOVLW d'58' ; Print caracter ":"
 CALL	send
 
 MOVLW d'78' ; Print caracter "N"
 CALL	send
 
 MOVLW d'93' ; Print caracter "]"
 CALL	send
 
 BCF	Select,RS
 MOVLW	0xC0
 CALL	send

	CLRF	TEMPDIG
	movlw	1
	movwf	TEMPDIG
   CLRF	NUMOROP
  
   MOVLW	2
   MOVWF	CURRMax
   CLRF	TMR0
   CLRF	Cout
   CALL	GetDigit2
	movf	TEMPDIG, W
   movwf char1
    movlw 2
    movwf char2

    ; Compare char1 and char2
    movf char1, W    ; Move char1 to the W (working) register
    xorwf char2, W   ; XOR char2 with the value in the W register
    
    bz 	startagain      
    bnz savenum
    
    
    
    
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   
 ; CALL	send
 
 
 
   ; movlw 2
    
 
 
BSF	Select,RS
   CLRF	NUMOROP



 
   RETURN



startagain
	call clear
   CALL	inid
   call	start
   

savenum
	call clear
   CALL	inid
   
   CLRF	NUMOROP
   BSF	Select,RS
   
   
       MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	TTH1,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	TH1,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	HUN1,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	TEN1,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	DIG1,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
 
 
  
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   CLRF	TEMPDIG
   MOVLW	2
   MOVWF	CURRMax
   BSF	NUMOROP,0
   CALL	GetDigit
   MOVF	TEMPDIG,W
   MOVWF	OPERATION
    MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   CLRF	TEMPDIG
   ;GET the 3 DIGITS of the first Number
   CLRF	NUMOROP

   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	TTH2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	TH2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	HUN2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	TEN2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
      MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	DIG2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
   
   
   
   
   
   BCF	Select,RS
 MOVLW	0xC0
 CALL	send

BSF	Select,RS

MOVLW d'61' ; Print caracter "="
 CALL	send
 
 
 
 
 
 

  
    movlw 0
    movwf char4

    ; Compare char1 and char2
    movf char4, W    ; Move char1 to the W (working) register
    xorwf OPERATION, W   ; XOR char2 with the value in the W register
    
    bz add     
    bnz div 

 

 
 
 
   
    MOVF	Result,W
   MOVWF	PORTC
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
   CLRF	NUMOROP

   MOVF	RES6,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES5,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

	MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES4,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES3,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG

MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES2,W
	;sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   MOVLW	0X14 ;SHIFT	Cursor By one to right
   BCF	Select,RS
   CALL	send
    MOVF	RES1,W
;	sublw 48
   MOVWF	TEMPDIG
   CALL	DISPDIG
   
   
   
   CLRF	TMR0
   CLRF	Cout

   
Loop57 BTFSS Cout,5
   GOTO Loop57
   MOVLW	D'255'
   CALL	xms
   
   BCF	Select,RS
   MOVLW	0x01
   CALL	send
   MOVLW	0x02
   CALL	send
   
   
 BCF	Select,RS
 MOVLW	0xC0
 CALL	send


   MOVLW D'3000'		; Load count for 100ms delay
   CALL	xms		; and wait for display start
  CALL	loopX

 
      GOTO	INTRODISPLAY3
      
      
      
      
      
      
      
      
rrr: 
      movlw 0
    movwf char4

    ; Compare char1 and char2
    movf char4, W    ; Move char1 to the W (working) register
    xorwf OPERATION, W   ; XOR char2 with the value in the W register
    
    bz add 
    bnz add 
    return
      
      
   MATH_ERROR	
      CALL 	clear
      CALL	inid

      CLRF	NUMOROP
      BSF	Select,RS

    MOVLW d'77' ; Print character "M"
    CALL	send
    
    MOVLW d'65' ; Print character "A"
    CALL	send
    
    MOVLW d'84' ; Print character "T"
    CALL	send
    
    MOVLW d'72' ; Print character "H"
    CALL	send
    
    MOVLW d'32' ; Print character " "
    CALL	send
    
    MOVLW d'69' ; Print character "E"
    CALL	send
    
    MOVLW d'82' ; Print caracter "R"
    CALL	send
    
    MOVLW d'82' ; Print caracter "R"
    CALL	send
    
    MOVLW d'79' ; Print caracter "O"
    CALL	send
    
    MOVLW d'82' ; Print caracter "R"
    CALL	send
    
    
    BCF		Select,RS
    MOVLW	0xC0
    CALL	send
    
    MOVLW 	D'2000'		; Load count for 100ms delay
    CALL	xms		; and wait for display start
    CALL	loopX
    GOTO 	init   
      
GetDigit2	
     BANKSEL PORTD
     CALL	DISPDIG2
     CLRF	TMR0
     CLRF	Cout
CHECK12	BTFSS  PORTB,1 ;if set PortB[1] GOTO NextNum
   GOTO CHECKTIMER02
   GOTO INCNUM2
   
CHECKTIMER02	; if 2 seconds have passed got to the next digit
   BTFSS Cout,5
   GOTO CHECK12
   GOTO GETDIGEND2

INCNUM2   
   CLRF	TMR0
   CLRF	Cout
   MOVF	CURRMax,W		      ; if >2 return to 1
   SUBWF	TEMPDIG,W
   BTFSC	STATUS,Z
   GOTO	SET02
   GOTO	NORMALINC2 
SET02
   CLRF	TEMPDIG
	movlw	1
	movwf	TEMPDIG
   GOTO DISP2
NORMALINC2
   INCF	TEMPDIG,1

DISP2
   CALL	DISPDIG2
CHECK02	BTFSC	PORTB,1
		 GOTO	CHECK02
   GOTO	CHECK12
 
GETDIGEND2
    RETURN		
DISPDIG2
   BANKSEL	NUMOROP
   BTFSC	NUMOROP,0
   GOTO	SENDOP2
   MOVF	TEMPDIG,W
   ADDLW	0x30
   GOTO	BEFORESEND2
SENDOP2
   CALL	table
BEFORESEND2
   BSF	Select,RS
   CALL	send
   MOVLW	0x10
   BCF	Select,RS
   CALL	send
   RETURN     
      
   END