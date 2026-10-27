	.data
N:		.word 100
premier: 	.space 101

	.text
	.global __start

__start:
	jal main
	li a7, 10
	ecall

#Initialisation: par défaut, tous les entiers sauf 0 et 1 sont considérés premiers.
main:
	la t0, premier 			# t0 = adresse du tableau premier
	la a1, N
	lw t1, 0(a1)			# t1 = N
	
	#store byte: 1 octet | short halfword: 2 | store word: 4
	sb zero, 0(t0) 			#écrit 0 à l'adresse t0, donc dans premier[0].
	sb zero, 1(t0) 			#écrit 0 à l'adresse t0 + 1, donc dans premier[1]
	li t2, 2			# i = 0
	li t3, 1			# t3 = true
	
pour1:
	bgtu t2, t1, fpour1		# Si i > N, sortir de la boucle
faire1:
	add t4, t0, t2			#adresse de premier[i]
	sb t3, 0(t4)			# premier[i] = true
	addi t2, t2, 1			# i++
	j pour1				# Retour au test
	
fpour1:
 	# Suite du programme
 	
# Criblage
	li t2, 2			# i=2
    	srli t5, t1, 1          	# t5 = N / 2

pour2:
    	bgtu t2, t5, fpour2     	# Si i > N/2, sortir

faire2:
    	add t6, t0, t2          	# Adresse de premier[i]
    	lbu t3, 0(t6)            	# t3 = premier[i]

si1:
    	beqz t3, fsi1            	# Si premier[i] == false, sauter le criblage

alors1:
    	mul t4, t2, t2           	# j = i * i

pour2_1:
    	bgtu t4, t1, fpour2_1   	# Si j > N, sortir de la boucle intérieure

faire2_1:
    	add t6, t0, t4           	# Adresse de premier[j]
    	sb zero, 0(t6)           	# premier[j] = false
    	add t4, t4, t2           	# j += i
    	j pour2_1                	# Retour au test de la boucle intérieure

fpour2_1:

fsi1:
	addi t2, t2, 1           	# i++
    	j pour2                  	# Retour au test de la boucle extérieure

fpour2:
	# Suite du programme
	
#Affichage
	li t2, 2			# i = 2
	
pour3:
	bgtu t2, t1, fpour3		# Si i > N, sortir
faire3:	
	add t6, t0, t2			# Adresse de premier[i]
	lbu t3, 0(t6)			# t3 = premier[i]
si2:
	beqz t3, fsi2			# Si premier[i] == false, ne pas afficher

alors2:	
	mv a0, t2               	# afficher i
    	li a7, 1               	 	# Appel système : afficher un entier
    	ecall

    	li a0, ' '              	# afficher un espace, ou 32
    	li a7, 11               	# Appel système : afficher un caractère
    	ecall

fsi2:
    	addi t2, t2, 1          	# i++
    	j pour3                 	# Itération suivante

fpour3:
    	li a0, '\n'             	# saut de ligne, ou 10
    	li a7, 11               	# Appel système : afficher un caractère
    	ecall 
    	jr ra				# retour à __start, qui termine