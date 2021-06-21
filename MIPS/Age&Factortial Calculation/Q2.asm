.data
	QuestionStr: .asciiz "Enter an integer greater than or equal to zero"
	InputStr: .asciiz "Your input: "
	OutputStr: .asciiz "The factorial is : " 
	invalidStr: .asciiz "The value you entered is less than zero. This program only works with values greater than or equal to zero."
	newLine: .asciiz "\n"
	redo: .asciiz "Calculate another factorial?(Y/N)"
	
.text
main: 	
	##prompt for input 
	la $a0,newLine
	li $v0,4
	syscall 
	la $a0,QuestionStr
	li $v0,4
	syscall
	li $v0,5
	syscall
 	##save input into $a0
	add $a0,$v0,$zero
	##check invalid input
	bltz $a0,invalid
	##calculate and print result 
	jal Fact
	jal printAns
	
	##ask for a redo
	la $a0,redo
	li $v0,4
	syscall 
	li $v0,12
	syscall  
	beq $v0,89,main
	
	li $v0,10
	syscall 
	
	
	
Fact:
	subi $sp,$sp,8
	sw $ra,4($sp)
	sw $a0,0($sp)
	addi $t3,$zero,1
	slt $t0,$a0,$t3
	beq $t0,$zero,L1
	addi $v0,$zero,1
	addi $sp,$sp,8
	jr $ra
	
L1: 
	subi $a0,$a0,1
	jal Fact
	lw $a0,0($sp)
	lw $ra,4($sp)
	addi $sp,$sp,8
	mul $v0,$a0,$v0
	jr $ra
	
printAns: 
	##$t0 has input, $t1 has output 
	add $t0,$a0,$zero
	add $t1,$v0,$zero
	##show input
	la $a0,InputStr
	li $v0,4
	syscall 
	add $a0,$t0,$zero
	li $v0,1
	syscall
	##new line
	la $a0,newLine
	li $v0,4
	syscall 
	#show output
	la $a0,OutputStr
	li $v0,4
	syscall 
	add $a0,$t1,$zero
	li $v0,1
	syscall 
	#new line 
	la $a0,newLine
	li $v0,4
	syscall 
	jr $ra
	
invalid: 
	la $a0,invalidStr
	li $v0,4
	syscall 
	li $v0,10
	syscall 
	
