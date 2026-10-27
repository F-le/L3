  pgcd:
    addi sp, sp, -16
    sw ra, 12(sp)
    
    if:
        bnez a1, else
    then:
        # a dans a0
        b endif
    else:
        rem t0, a0, a1
        mv a0, a1
        mv a1, t0
        jal pgcd # le resultat dans a0
    endif:
    
    lw ra, 12(sp)
    addi sp, sp, 16
    jr ra
