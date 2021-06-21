.data
	firstName: .asciiz "Please enter your first name.\n"
	lastName: .asciiz "Please enter your last name.\n"
	answer: .asciiz "You entered:"
	comma: .asciiz ","
	buffer: .space 40
	limit: .word 19      
.text 
main:
	
	## t7=limit +1 
	lw $t7,limit
	addi $t7,$t7,1
	add $t9,$zero,$t7
	##prompt for first name and get 
	la $a1,firstName
	jal printString
	jal GETS 
	add $t4,$zero,$v0 
	
	li $t9,0
	la $a1,lastName 
	jal printString 
	jal GETS 
	add $t4,$t4,$v0    ##total num 
	
	la $a1,answer
	jal printString
	
	beqz $t4,exit  ##exit if nothing entered 
	jal PUTS
	add $sp,$sp,$t7
exit:
	li $v0,10
	syscall 

GETS: 
	move $t6,$ra
	sub $sp,$sp,$t7
	
	
	li $t0,0  ## $t0 is counter/number of char read 
	lw $t2,limit  ## t2 holds the limit 
	GetsLoop: 
		jal GETCHAR 
		beq $v0,10,endGets ##check for enter/CR 
		sb $v0,($sp)
		sb $v0,buffer($t9)	
		addi $t0,$t0,1
		addi $sp,$sp,1
		addi $t9,$t9,1
		blt $t0,$t2,GetsLoop
		endGets: 
			move $t3,$t0
			fillZeros:
			sb $zero,($sp)
			sb $zero,buffer($t9)	
			addi $t0,$t0,1
			addi $sp,$sp,1
			addi $t9,$t9,1
			ble $t0,$t2,fillZeros
	
	sub $sp,$sp,$t7
	move $ra,$t6
	move $v0,$t3
	jr $ra 
	
	
PUTS: 
	move $t3,$ra
	li $t0,0
	la $t2,buffer
	loop1: 
		lb $a0,($t2)
		jal PUTCHAR
		addi $t2,$t2,1
		bnez $a0,loop1
	la $t2,buffer($t7)
	loop2: 
		lb $a0,($t2)
		jal PUTCHAR
		addi $t2,$t2,1
		bnez $a0,loop2
		
	move $ra,$t3
	jr $ra 
printString: 
	move $t0,$ra
	
	loop: 
		lb $a0,($a1)
		jal PUTCHAR
		addi $a1,$a1,1
		bnez $a0,loop
		
	move $ra,$t0
	jr $ra 



GETCHAR: 
	lui $a3,0xffff
	CkReady: 
		lw $t1,0($a3)
		andi $t1,$t1,0x1
		beqz $t1,CkReady
		lw $v0,4($a3)
		jr $ra

PUTCHAR:
	lui $a3,0xffff
	XReady:
		lw $t1,8($a3)
		andi $t1,$t1,0x1
		beqz $t1,XReady 
		sw $a0,12($a3)
		jr $ra