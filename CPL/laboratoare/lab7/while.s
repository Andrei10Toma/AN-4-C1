.data
new_line:
    .asciiz "\n"

.text
main:
    li $a1, 20
while:
    move $a0, $a1
    li $v0, 1
    syscall
    la $a0, new_line
    li $v0, 4
    syscall
    sub $a1, $a1, 1
    bge $a1, 0, while
end:
    li $v0, 10
    syscall