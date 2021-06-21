.data
	QuestionStr: .asciiz "How old are you(in years)?"
	AnswerStr: .asciiz "You have been alive for these many days: " 
	invalidStr: .asciiz "Input must be greater than or equal to zero."
	
.text
main: 	
	##prompt for input 
	la $a0,QuestionStr
	li $v0,4
	syscall
	li $v0,5
	syscall
 	##save input into t0
	add $t0,$v0,$zero
	li $t1,0
	##print 0 if input is 0
	beqz $t0,printAns
	bltz $t0,invalid
	jal multiply
	j printAns
	
multiply: 
	subi $t0,$t0,1
	addi $t1,$t1,365
	bnez $t0,multiply
	jr $ra 

printAns: 
	la $a0,AnswerStr
	li $v0,4
	syscall
	add $a0,$t1,$zero
	li $v0,1
	syscall 
	li $v0,10
	syscall
	
invalid: 
	la $a0,invalidStr
	li $v0,4
	syscall 
	li $v0,10
	syscall 
	