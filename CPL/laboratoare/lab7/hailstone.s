.data
comma:
    .asciiz ", "

.text
main:
    li $a1, 12
    li $t1, 2
    li $t2, 3
    move $a0, $a1
    li $v0, 1
    syscall
while:
    div $a1, $t1
    mfhi $a2
    beq $a2, 0, if_branch
    j else_branch
if_branch:
    mflo $a1
    j end_if
else_branch:
    mult $a1, $t2
    mflo $a1
    add $a1, $a1, 1
end_if:
    li $v0, 4
    la $a0, comma
    syscall
    move $a0, $a1
    li $v0, 1
    syscall
    bne $a1, 1, while
    li $v0, 10
    syscall