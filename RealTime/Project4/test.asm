	PROCESSOR 16F877A
	__CONFIG	0x3771

	#INCLUDE "P16F877A.INC"
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

letter	EQU	30	; current letter
pos		EQU	31	; value for currsor position
sp		EQU	32	; variable to store space ascii
ms		EQU	33	; time counter

CBLOCK	34 ; array to store the entered string
	one
	two
	three
	four
	five
	mod
	cur
ENDC

readEnd	EQU	39
calc1	EQU 3B
calc2	EQU 3C

; PROGRAM BEGINS ;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;

		ORG		0	
		NOP			
		goto	init
	
init:
END