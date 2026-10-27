main:
	li a0, 48
	li a1, 18
	jal ra, pgcd
	li a7, 1
	ecall
	li a7, 10
	ecall

pgcd:
tq:
	beq a1, zero, ftq
	rem t0, a0, a1
	mv a0, a1
	mv a1, t0
	j tq
ftq:
	jr ra