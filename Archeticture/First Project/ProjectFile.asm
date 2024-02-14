# 1190102 - Mazen Batrawi
# 1191448 - Shahd AbuDaghash
################# Data segment #####################
.data
choice: .space 1024
inputFile: .space 1024
outputFile: .space 1024
inputText: .space 3072
text_without_filtering_white_spaces: .space 4096 # allocate space for the resulting string
text_with_filtering_white_spaces: .space 4096 # allocate space for the resulting string
maxLength: .word 0 # variable to store maximum length
encrypted_string: .space 4096 # allocate space for the resulting string
decrypted_string: .space 4096 # allocate space for the resulting string
finalResult: .space 4096
newline: .asciiz "\n"
options: .asciiz "Please select your option:\n( e ) for encryption\n( d ) for decryption\n"
your_choice: .asciiz "Your choice is: "
wrong_choice: .asciiz "\nEntered option is not from the list. Try again.\n"
enter_name_of_plain_file: .asciiz "\nPlease input the name of the plain text file: "
enter_name_of_cipher_file: .asciiz "\nPlease input the name of the cipher text file: "
enter_name_of_output_file: .asciiz "\nPlease enter the output file name: "
file_content_string_text: .asciiz "\nThe content of the file is:\n"
file_content_filtered_string_text: .asciiz "\nThe content of the file after filtering is:\n"
maximum_text: .asciiz "\nThe maximum word length (The key) is: "
enc_text: .asciiz "\n\nThe encrypted text is:\n"
dec_text: .asciiz "\n\nThe decrypted text is:\n"
successfully_saved_text: .asciiz "\nThe text is successfully saved in the file.\n"

################# Code segment #####################
.text

main:

# ******************* Menu *******************

# display the available options for the user to choose from (encryption/decryption)
	li $v0, 4 # 4 is the system call for printing on console
	la $a0, options
	syscall
	
choose_option:
	# Ask the user for their selection
	li $v0, 4 
	la $a0, your_choice
	syscall
	
	# read a character from console
	li $v0, 12
	syscall
	move $t0, $v0
	
	# check if the selection is encryption or decryption
	beq $t0, 'e', do_encryption # user's choise is encryption (e)
	beq $t0, 'd', do_decryption # user's choise is decryption (d)
	
	# else: display that it's a wrong option
	li $v0, 4 
	la $a0, wrong_choice
	syscall
	j choose_option # Read the user's choice again
	
# *************************************************

# ******************* Encryption *******************

# This function performs the encryption process for a file chosen by the user
do_encryption:
	# ask the user to enter the name of the input file
	li $v0, 4
	la $a0, enter_name_of_plain_file
	syscall
	la $a0, inputFile
	jal read_filename # Reading the plaintext filename
	jal read_file_content # read the content of the plaintext file
	jal filter_the_file # filter the file (remove special characters and numbers, and convert letters to lowercase
	la $t6, text_with_filtering_white_spaces
	jal getMaxLength # get the key: the max length of words
	jal encrypt # perform the encryption for the saved text
	# ask the user to enter the output file to save the text to
	li $v0, 4
	la $a0, enter_name_of_output_file
	syscall
	la $a0, outputFile
	jal read_filename # read the filename of the output file
	la $a0, encrypted_string
	jal copy_to_result
	j write_file_content # write content to output file
	
# *************************************************	

# ******************* Decryption *******************

# This function performs the decryption process for a cipher file chosen by the user
do_decryption:
	# ask the user to enter the name of the input file
	li $v0, 4
	la $a0, enter_name_of_cipher_file
	syscall
	la $a0, inputFile
	jal read_filename # Reading the ciphertext filename
	jal read_file_content # read the content of the ciphertext file
	la $t6, inputText
	jal getMaxLength # get the key: the max length of words
	jal decrypt # perform the decryption for the saved text
	# ask the user to enter the output file to save the text to
	li $v0, 4
	la $a0, enter_name_of_output_file
	syscall
	la $a0, outputFile
	jal read_filename # read the filename of the output file
	la $a0, decrypted_string
	jal copy_to_result
	j write_file_content # write content to output file
	
# *************************************************	

# ******************* File Operations *******************

read_filename:
	li $a1, 4096 # maximum number of characters to read
	li $v0, 8 # syscall code for reading a string
	syscall
	
loop_read_filename: 
	lb $t0, 0($a0)
	beq $t0, 10, done_reading_file_name # The user entered (enter) '\n'
	addi $a0, $a0, 1
	j loop_read_filename
		
done_reading_file_name:
	li $t0, 0
	sb $t0, 0($a0)
	jr $ra

read_output_filename:
	li $v0, 8 # system call for read string
	la $a0, outputFile # address of buffer
	li $a1, 1024 # size of buffer
	syscall # read string
	jr $ra
	
read_file_content:
	# Open file for reading
	li   $v0, 13          # 13 is the system call for opening a file
	la   $a0, inputFile    # store inputFile address in a0
	li   $a1, 0           # flag for reading
	li   $a2, 0           # mode is ignored
	syscall               # open a file 
	move $s0, $v0         # save the file descriptor  

	# reading from file just opened
	li   $v0, 14        # 14 is the system call for reading from a file
	move $a0, $s0       # access the opened file through the file descriptor
	la   $a1, inputText    # address of buffer from which to read
	li   $a2,  3072     # hardcoded buffer length (every 1024 is 1 KB) => 3 KB
	syscall             # read from file
	
	# Close the file
	li $v0, 16 # 16 is the system call for closing a file
	move $a0, $s0 # access the file by using the file descriptor
	syscall # close the file
	
	#Printing the content of the file
 	li $v0, 4 # 4 is the system call for printing on console
	la $a0, file_content_string_text # load address of "The content of the file is:\n"
	syscall
 	li $v0, 4
	la $a0, inputText # load address of the buffer (contains the text of the file)
	syscall
 	li $v0, 4
	la $a0, newline # load address of newLine "\n\n"
	syscall
	
	jr $ra

write_file_content:
	# Print on file
	li $v0, 13 # system call for open file
	la $a0, outputFile # address of filename
	li $a1, 1 # flags for writing and creating
	li $a2, 0666 # mode for new file
	syscall # open the file
	move $s0, $v0 # save the file descriptor in $s0

	# Write to the file
	li $v0, 15 # system call for write to file
	move $a0, $s0 # file descriptor
	la $a1, finalResult # address of buffer
	li $a2, 3072 # size of buffer
	syscall # write to the file

	# Close the file
	li $v0, 16 # system call for close file
	move $a0, $s0 # file descriptor
	syscall # close the file
	
	li $v0, 4 # 4 is the system call for printing on console
	la $a0, successfully_saved_text
	syscall
	
 	j exit
 	 

copy_to_result:
	la $a1, finalResult
	
loop_store_result:
	lb $t1, 0($a0)
	sb $t1, 0($a1)
	
	addi $a0, $a0, 1
	addi $a1, $a1, 1
	
	beq $t1, 0, end_store_result_loop
	j loop_store_result

end_store_result_loop:
	jr $ra
	
# *************************************************	

# ******************* Filtering the file *******************
filter_the_file:
	# Remove non-alphabetic characters and convert to lowercase
	#load variables to registers to use in loop_removeNonAlpha_convertToLowerCase
	la $a0, inputText
	la $a1, text_without_filtering_white_spaces

loop_removeNonAlpha_convertToLowerCase: # remove everything but alphabet, space, and newLine

   	lb $t0, 0($a0) # load the current char from inputText
   	beq $t0, 0, endLoop_removeNonAlpha_convertToLowerCase # branch to endLoop if end of string has been reached
   
   	li $t3, 32 # 32 is the ascii code for [[space]]
   	beq $t0, $t3, store # branch to store space char
   	li $t3, 10 # 10 is the ascii code for [[newLine]]
   	beq $t0, $t3, store # branch to store newLine char
    
   	# check if char is from a-z or A-Z
   	blt $t0, 'A', skip # characters with ascii less than 'A' are not alphabet
   	bgt $t0, 'z', skip # characters with ascii greater than 'z' are not alphabet
   	bge $t0, 'a', store # store char (a-z)
   	ble $t0, 'Z', lowercase # convert letter to lowercase (A-Z)
   	j skip

store: # store char in text_without_filtering_white_spaces
	sb $t0, 0($a1)
   	addi $a1, $a1, 1 #increment index for text_without_filtering_white_spaces
   	addi $a0, $a0, 1 #increment index for inputText
   	j loop_removeNonAlpha_convertToLowerCase
   	
lowercase: # convert char from uppercase to lowercase
   	addi $t0, $t0, 32
   	j store

skip: #increment index for inputText
   	addi $a0, $a0, 1
   	j loop_removeNonAlpha_convertToLowerCase


endLoop_removeNonAlpha_convertToLowerCase: # jump here after loop_removeNonAlpha_convertToLowerCase is done

	# Remove extra spaces in file
	# set up loop variables to use in loop_removeExtraSpaces
	la $a0, text_without_filtering_white_spaces 
	la $a1, text_with_filtering_white_spaces 
	li $t3, 1 # space flag

loop_removeExtraSpaces: # remove extra spaces in file content

   	lb $t0, 0($a0) # load the current char from text_without_filtering_white_spaces
   	beq $t0, 0, endLoop_removeExtraSpaces # branch to endLoop_removeExtraSpaces if end of string has been reached
   	
   	beq $t0, 10, process_newLine_or_space # process char when newLine

   	# check if char is a space
   	li $t4, 32 # 32 is the ascii code for [[space]]
   	bne $t0, $t4, process_not_space # branch to process_not_space if char is not a space

    # check if preceding char was a space
    beq $t3, 1, skip2 # branch to skip if preceding char was NOT a space
    j process_newLine_or_space # process char 


process_not_space: # store char and reset space flag to 0
    sb $t0, 0($a1) #store char in text_with_filtering_white_spaces
    addi $a1, $a1, 1 # increment index for text_with_filtering_white_spaces
   	addi $a0, $a0, 1 #increment index for text_without_filtering_white_spaces
   	li $t3, 0 # clear the flag for preceding space
   	j loop_removeExtraSpaces

process_newLine_or_space: # store char and set space flag to 1
	sb $t0, 0($a1) # store char in text_with_filtering_white_spaces
   	addi $a1, $a1, 1 # increment index for text_with_filtering_white_spaces
   	addi $a0, $a0, 1 #increment index for text_without_filtering_white_spaces
   	li $t3, 1 # set the flag for preceding space
   	j loop_removeExtraSpaces

skip2: # increment index for text_without_filtering_white_spaces
    addi $a0, $a0, 1
    j loop_removeExtraSpaces

endLoop_removeExtraSpaces: # jump here after loop_removeExtraSpaces is done

	# print the file content after filtering it
 	li $v0, 4
	la $a0, file_content_filtered_string_text
	syscall
 	li $v0, 4
	la $a0, text_with_filtering_white_spaces
	syscall
 	li $v0, 4
	la $a0, newline
	syscall
    
    	jr $ra

# *************************************************	

# ******************* Get Max Length *******************

getMaxLength:
	# get the max word length
	move $t1, $t6
	li $t6, 0 # currentLength
	li $t7, 0 # maxLength

loop_getMaxLength:
    lb $t0, 0($t1) # load the current char from text_with_filtering_white_spaces
    beq $t0, 0, endLoop_getMaxLength # branch to endLoop_getMaxLength if end of string has been reached
    
    addi $t1, $t1, 1 # increment the index
    
    beq $t0, 32, resetLength # reset the currentLength if char is space
    beq $t0, 10, resetLength # reset the currentLength if char is newLine
    
    addi $t6, $t6, 1 # increment the currentLength by 1
    blt $t6, $t7, loop_getMaxLength # Go to next char if currentLength is less than maxLength
    move $t7, $t6 # update maxLength to currentLength
    j loop_getMaxLength
    
resetLength:
    	move $t6, $zero # reset currentLength
    	j loop_getMaxLength
    
endLoop_getMaxLength: # jump here after loop_getMaxLength is done

	# print the max length
    li $v0, 4 
    la $a0, maximum_text
    syscall
    li $v0, 1 # 1 is the system call for printing an integer
	move $a0, $t7
	syscall
    	
    li $t0, 26 # The number of lowercase alphabet 
	# get the maxLength % 26
	div $t7, $t0
	mfhi $t7
	sw $t7, maxLength
	
	jr $ra

# *************************************************	

# ******************* Encryption operations *******************

encrypt:		
	# Perform encryption
	la $a0, text_with_filtering_white_spaces
	la $a1, encrypted_string

loop_encrypt: # do the encryption operation
	lb $t0, 0($a0) # load the current char from text_with_filtering_white_spaces
	beq $t0, 0, endLoop_encrypt # branch to endLoop_encrypt if end of string has been reached
	
	addi $a0, $a0, 1 # increment the index
	
	beq $t0, 32, store_encrypted # store space
	beq $t0, 10, store_encrypted # store newLine
	
	# add the key to the character
	sw $t7, maxLength
	add $t0, $t0, $t7
	
	# fix the case when the encrypted letter becomes larger than 'z'
	bgt $t0, 'z', fix_encryption
	j store_encrypted
	
store_encrypted: # store the character in the encrypted string
	sb $t0, 0($a1)
	addi $a1, $a1, 1
	j loop_encrypt

fix_encryption: # get the letter back to be between a-z by subtracting 26
	sub $t0, $t0, 26
	j store_encrypted

endLoop_encrypt: # jump here after loop_encrypt is done

	# print the encrypted string
    li $v0, 4
    la $a0, enc_text
    syscall
    li $v0, 4
    la $a0, encrypted_string
    syscall
    li $v0, 4
   	la $a0, newline
    syscall
	jr $ra

# *************************************************	

# ******************* Encryption operations *******************

decrypt:
	# Perform decryption
	la $a0, inputText
	la $a1, decrypted_string

loop_decrypt: # do the encryption operation
	lb $t0, 0($a0) # load the current char from enc
	beq $t0, 0, endLoop_decrypt # branch to endLoop_encrypt if end of string has been reached
	
	addi $a0, $a0, 1 # increment the index
	
	beq $t0, 32, store_decryption # store space
	beq $t0, 10, store_decryption #store newLine
	
	# subtract key from char
	sw $t7, maxLength 
	sub $t0, $t0, $t7
	
	# fix the case when the encrypted letter becomes less than 'a'
	blt $t0, 'a', fix_decryption
	j store_decryption
	
store_decryption: # store the character in the decrypted string
	sb $t0, 0($a1)
	addi $a1, $a1, 1
	j loop_decrypt
	
fix_decryption: # get the letter back to be between a-z by adding 26
	add $t0, $t0, 26
	j store_decryption

endLoop_decrypt: # jump here after loop_decrypt is done

	# print the decrypted string
    li $v0, 4
    la $a0, dec_text
    syscall
    li $v0, 4
    la $a0, decrypted_string
    syscall
    li $v0, 4
   	la $a0, newline
    syscall
	jr $ra

# *************************************************	  

# exit the program
exit:
	li $v0, 10 # system call for exiting the program
	syscall # exit the program
