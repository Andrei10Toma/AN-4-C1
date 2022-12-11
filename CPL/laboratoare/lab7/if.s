.data
string_one:
    .asciiz "1\n"
string_two:
    .asciiz "2\n"

string_large:
    .asciiz "Large Value\n"
string_small:
    .asciiz "Small Value\n"

.text
main:
    li $v0, 4
    la $a0, string_one
    syscall
    li $v1, 13
    ble $v1, 64, small
big:
    li $v0, 4
    la $a0, string_large
    syscall
    j end
small:
    li $v0, 4
    la $a0, string_small
    syscall
end:
    li $v0, 4
    la $a0, string_two
    syscall
    li $v0, 10
    syscall