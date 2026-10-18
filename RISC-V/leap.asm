	.data
invite_str:	.asciz	"Année ? "
bissextile:	.asciz "Bissextile"
pas_bissextile:	.asciz "Pas bissextile"

	.text
	.global __start
	
__start:
	jal main
	li a7, 10
	ecall

main:
	li a7, 4
	la a0, invite_str
	ecall
	
	li a7, 5
	ecall
	mv t0, a0
	
if1:	li t1, 100
	rem t1, t0, t1
	bnez t1, else1
then1:
if3: 
	li t1, 400
	rem t1, t0, t1
	bnez t1, else3
then3:
	la a0, bissextile
	li a7, 4
	ecall
	b endif3
else3:
	la a0, pas_bissextile
	li a7, 4
	ecall
endif3:
	b endif1
else1:
if2:
	li t1, 4
	rem t1, t0, t1
	bnez t1, else2
then2:
	la a0, bissextile
	li a7, 4
	ecall
	b endif2
else2:
	la a0, pas_bissextile
	li a7, 4
	ecall
endif2:
endif1:
	jr ra
