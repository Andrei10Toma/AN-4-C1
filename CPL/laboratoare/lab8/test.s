.data
    myvar: .word 0

    myvar2: .word 0

.text
    myfunc:
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4
        	li $a0 4
        lw $ra 4($sp)
        addiu $sp $sp 28
        lw $fp 0($sp)
        jr $ra

    avg:
        move $fp $sp
        sw $ra 0($sp)
        addiu $sp $sp -4
            lw $a0, 4($fp)

        	sw $a0 0($sp)
        	addiu $sp $sp -4
            lw $a0, 8($fp)

        	lw $t1 4($sp)
        	add $a0 $t1 $a0
        	addiu $sp $sp 4		
            sw $a0 0($sp)
            addiu $sp $sp -4
        	li $a0 2
            lw $t1 4($sp)
            div $a0 $t1 $a0
            addiu $sp $sp 4    
        lw $ra 4($sp)
        addiu $sp $sp 16
        lw $fp 0($sp)
        jr $ra

main:
	li $a0 4
    neg $a0 $a0
	li $a0 1
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 2
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 3
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		# 1+2+3
	li $a0 5
	sw $a0 0($sp)
	addiu $sp $sp -4
	li $a0 6
	lw $t1 4($sp)
	add $a0 $t1 $a0
	addiu $sp $sp 4		# 5+6
	li $a0 2
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 2
    lw $t1 4($sp)
    mul $a0 $t1 $a0
    addiu $sp $sp 4    # 2*2
	li $a0 84
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 2
    lw $t1 4($sp)
    div $a0 $t1 $a0
    addiu $sp $sp 4    # 84/2
	li $a0 1
    bgtz $a0, if_1
    if_1:
        	li $a0 5
        b fi_1
    else_1:
        	li $a0 7
    fi_1:
	li $a0 0
    bgtz $a0, if_2
    if_2:
        	li $a0 7
        b fi_2
    else_2:
        	li $a0 5
    fi_2:
sw $fp 0($sp)
	li $a0 5
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 4
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 3
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 2
    sw $a0 0($sp)
    addiu $sp $sp -4
	li $a0 1
    sw $a0 0($sp)
    addiu $sp $sp -4

    jal myfunc
	li $a0 5
    sw $a0, myvar
    lw $a0, myvar
    sw $a0, myvar2
sw $fp 0($sp)
    lw $a0, myvar2
    sw $a0 0($sp)
    addiu $sp $sp -4
    lw $a0, myvar
    sw $a0 0($sp)
    addiu $sp $sp -4

    jal avg
	li $v0, 10	
	syscall		#exit