.data
new_line:
    .asciiz "\n"

.text
main:
    li $a1, 20
while_1:
    sub $sp, $sp, 4
    sw $a1, ($sp)
    sub $a1, $a1, 1
    bgt $a1, 0, while_1
while_2:
    lw $a1, ($sp)
    move $a0, $a1
    li $v0, 1
    syscall
    la $a0, new_line
    li $v0, 4
    syscall
    addiu $sp, $sp, 4
    bne $a1, 20, while_2

    li $v0, 10
    syscall