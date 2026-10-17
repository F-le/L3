	.data
entree: 	.asciz "Entrez un entier: "
petit: 		.asciz "Plus petit que le nombre à découvrir\n"
grand: 		.asciz "Plus grand que le nombre à découvrir\n"
nombre_coups: 	.asciz "Nombre de coups: \n"
a_decouvrir:	.word 42

	.text
	.global __start
	
__start:
	jal main
	li a7, 10
	ecall
	
main:
	la t1, a_decouvrir	
	lw t2, 0(t1)		# nombre a découvrir
	li t3, 0		#compteur de coups

tantque:
	li a7, 4
	la a0, entree
	ecall
		
	li a7, 5
	ecall
	mv t0, a0		#nb saisi dans t0
	
	addi t3, t3, 1
	beq t0, t2, ftq
si:
	bgt t0, t2, sinon
	
alors:
	li a7, 4
	la a0, petit
	ecall
	
	j fsi
	
sinon:	
	li a7, 4
	la a0, grand
	ecall
fsi:
	j tantque
ftq:
	li a7, 4
	la a0, nombre_coups
	ecall
	
	li a7, 1
	mv a0, t3
	ecall
	jr ra