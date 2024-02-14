;This project was done by:
;Ibraheem Ajaj 1190939
;Mazen Batrawi 1190102

.model small 

.data  

    msg1 db ,0dh,0ah,"please enter number ","$"
     
    numerrormsg db ,0dh,0ah,"The number you entered is not in the range from 0 to 9", "$" 
    
    thanks db ,0dh,0ah,"Thank you for entering all numbers !", "$"
    msg2 db ,": ","$"
    
     
    opmsg db ,0dh,0ah,"Please select the operation you need(v,x,n,d,a,e,s,l,q,r or ? for help)", "$"  
    selvmsg db ,0dh,0ah,"v for average", "$"
    selxmsg db ,0dh,0ah,"x for max", "$"
    selnmsg db ,0dh,0ah,"n for min", "$"  
    seldevmsg db ,0dh,0ah,"e for standard deviation", "$"
    selamsg db ,0dh,0ah,"a to show numbers in ascending order", "$"
    seldmsg db ,0dh,0ah,"d to show numbers in descending order", "$"
    selsmsg db ,0dh,0ah,"s summation of all numbers", "$"
    selallmsg db ,0dh,0ah,"l for all of the above", "$"
    selqmsg db ,0dh,0ah,"q to quit", "$"
    selnewmsg db ,0dh,0ah,"r to enter 6 new numbers", "$"
    selhelp db ,0dh,0ah,"? for help", "$"   
    charerrormsg db ,0dh,0ah,"Unrecognized character" , "$" 
    entersixmsg db ,0dh,0ah,"please enter new six numbers ",0dh,0ah, "$" 
    
    
    wrongCharmsg db ,0dh,0ah,"Unrecognized character, please enter correct one ",0dh,0ah, "$" 
    
    
    printmaxmsg db ,0dh,0ah,"The max number is : ",,"$" 
    printminmsg db ,0dh,0ah,"The min number is : ","$"  
    printsummsg db ,0dh,0ah,"The sum of all numbers is: ", "$"
    printavgmsg db ,0dh,0ah,"The average is: ", "$"
    printdevmsg db ,0dh,0ah,"The standard deviation is: ", "$"
    printascmsg db ,0dh,0ah,"Numbers in ascending order: ", "$"
    printdscmsg db ,0dh,0ah,"Numbers in descending order: ", "$"
    
    newline db ",", "$"  
    
    arr db 6 dup(?)
    
    avg db ?
    sum db ?
    standarddeviation db ?
    char db ?
    
    num db ?
    
    standard db 0
    
    counter db ?
    count equ 9 
 
 
.stack 300

.code 


; The first proccess is to read numbers, so we call the function which reads them.

call read

; When the program finishes readin numbers, either the first time or reading new ones, it will jump here to print the thanks message.
ok:

call printthanks
         
; The printthanks function will jump to the label start
 
start:        

    mov ah,09h           
    lea dx,opmsg    ;Displaying the line which tells the user to enter a charachter for the operation
    int 21h
    
    mov ah,01h
    int 21h         ; Reading the charachter
    mov char, al
    
   
    ; From here to line 121, comparing the charachter with the available options, if it happens to be one of them, the program jumps to the label specified
    ; else, the unrecognized char message is displayed, and it returns to the biggening of the label start. 
    cmp al, 's'
    je funloopsums
    
    
    cmp al, 'v'
    je funloopavgs
    
    cmp al, 'e'
    je funcalcstandarddeviation
    
    cmp al, 'a' 
    je funasort
    
    cmp al, 'd'
    je fundsort
    
    cmp al, 'x'
    je funmaxelement
    
    cmp al, 'n'
    je funminelement
    
    cmp al, 'q'
    je exitcode
    
    cmp al, 'r'
    je readnew
    
    cmp al, '?'
    je funhelp
    
    cmp al, 'l'
    je funall
    
    mov ah,09h    ; if the user entered wrong operation        
    lea dx,wrongCharmsg
    int 21h 
    
   jmp start 


; Each label calls the function that calculates the needed operation then jumps to the label start.
; In case it was the all of the above function, it calls all the specified functions then jumps to the label start.

funloopsums:
    call loopsums
    jmp start        

funloopavgs:
    call loopavgs
    jmp start 
    
funcalcstandarddeviation:
    call calcstandarddeviation
    jmp start 

funasort:
    call asort
    jmp start 
    
fundsort:
    call dsort
   jmp start 
    
funmaxelement:
    call maxelement
    jmp start 
    
funminelement:
    call minelement
    jmp start
    
funhelp:
    call help
    jmp start            

funall:
    call loopsums
    call loopavgs
    call calcstandarddeviation
    call maxelement
    call minelement
    call asort
    call dsort
    jmp start 
    
    
loopsums proc 
    
    mov ah,09h
    lea dx,printsummsg      ; Displaying the message of the sum            
    int 21h

    mov si, offset arr        
    mov si, 0
    mov al, arr[si]
    mov counter, 1
    inc si
    
    for:
        add al,arr[si]
        mov sum, al
        inc counter    ; Calculating the sum in a loop that ends when the counter hits 6, by adding the numbers in the array to the register al
                       ; Whenn the counter hits 6, the procedure moves to the function that seperates the digits of the sum and prints them
        inc si
        cmp counter,6
        jz calcsum
    loop for
    ret

loopsums endp

loopavgs proc        
    mov ah,09h
    lea dx,printavgmsg            
    int 21h

    mov si, offset arr        
    mov si, 0                            ; Calculating the sum for the average as in calculating sum
    mov al, arr[si]
    mov counter, 1
    inc si

    forl:
        add al,arr[si]
        mov sum, al
        inc counter
        inc si
        cmp counter,6
        jz calcavg
    loop forl
    ret
    
loopavgs endp
        
calcsum proc
    aam
        
    mov dl,al
    mov bx,ax        ; Seperating the digits of the sum and printing them using the aam proccess
    mov ah,02h
    
    mov dl,bh 
    add dl,30h
    int 21h 
                 
    
    mov dl,bl
    add dl,30h 
    int 21h
    ret
    
calcsum endp      

 
        
calcavg proc
        
        aam
        mov bx,ax 

        
        mov dl,bh 
        add dl,30h
       
                     
        mov al,sum
        mov ah,0
        mov bl, 6              ; dividing the sum by 6 and seperating the digits of the answer
        div bl
        mov avg, al
        mov dl, avg
        add dl, 30h
        mov ah, 02h
        int 21h
        ret
        
        
calcavg endp
        
loopavgfordev proc

        mov si, offset arr        
        mov si, 0
        mov al, arr[si]
        mov counter, 1
        inc si

        for2:
            add al,arr[si]
            mov sum, al
            inc counter
            inc si
            cmp counter,6
            je nextt
        loop for2
        nextt:
            aam
            mov bx,ax 
    
            
            mov dl,bh                                ; The same proccess for calculating the average, but here it is for the standard deviation
            add dl,30h
           
                         
            mov al,sum
            mov ah,0
            mov bl, 6
            div bl
            mov avg, al
            mov dl, avg
            ret
                
loopavgfordev endp
        
calcstandarddeviation proc
    
    call loopavgfordev
    
    ans:
        mov si, offset arr
        mov si, 0 
        mov counter, 0
        mov cl, 0
        mov standard, 0
        mov al, arr[0]
        sub al, avg
        mov bl, al
        mul bl
        add standard, al
        inc counter
        inc si
        jmp x
        x:
            mov al, arr[si]
            sub al, avg
            mov bl, al                         ; Calculating the (num - avg)^2 in this function
            mov al, bl
            mul bl
            add standard, al
            inc counter
            inc si
            cmp counter,6
            jz nextphase
            loop x
    ret
            
calcstandarddeviation endp

nextphase proc
        mov ah,09h           
        lea dx,printdevmsg
        int 21h
        
        aam
        mov bx,ax 

        
        mov dl,bh 
        add dl,30h
       
                     
        mov al,standard                  ; dividing (sum of (num - avg)^2) by 6
        mov ah,0
        mov bl, 6
        div bl
        mov standard,al
        mov dl, standard
        mov dh, standard
        mov counter,0
        call loopsearch
        ret
        
nextphase endp
        
loopsearch proc
        mov al,counter
        mov bl,counter
        mov bl, al
        mov al, bl
        mul bl                          ; This loop starts from 0, as we know, the part we calculated under the root has an integer root because we are
                                        ; dealing with integers, for example, the square root for 8 is 2 as an integer, so, the loop starts from counter 0 
                                        ; and keeps squaring it untill the value is greater than or equal the part under the square root, if it equals it
                                        ; then we have a number that has an integer square root so we print it as it is, else, we decrement the counter
                                        ; by one and the answer is displayed in the print function
        cmp al,dl
        jz print
        jnle found
        inc counter
        call loopsearch
        ret
                      
loopsearch endp
                
print proc
    
    mov dl, counter
    add dl, 30h
    mov ah, 02h
    int 21h
    ret


print endp 
    

found proc
    sub counter, 1
    jmp print
    ret
    
found endp            


asort proc 
    mov ah,09h
    lea dx,printascmsg            
    int 21h
      
    mov dx, 5
    outerloop:
        mov cx, 5
        lea si, arr


        innerloop:
            mov al, [si]                
            cmp al, [si+1]                ; This function makes the algorithm of bubble sort to sort it in ascending order, to make that happen, we need
                                          ; nested loops, 2 of them. it compares the value of the element in the outer loop to all of the elemnts after
                                          ; it, every time it finds a new element less, it swaps them.
            jl case                      
            xchg al, [si+1]
            mov [si], al                    

            case:
                inc si
                loop innerloop

        dec dx
        jnz outerloop
        call printloop
        ret
        
asort endp 
                
   
dsort proc
    mov ah,09h
    lea dx,printdscmsg            
    int 21h  
    mov dx, 5
    oloop1:
        mov cx, 5
        lea si, arr


        iloop1:
            mov al, [si]                
            cmp al, [si+1]
            jg common1                      
            xchg al, [si+1]                     ; The same proccess as in sorting them in ascending order, but it swaps when it finds an element greater.
            mov [si], al                    

            common1:
                INC si
                loop iloop1

        dec dx
        jnz oloop1
        call printloop
        ret
                
dsort endp        
  
        
                
                
printloop proc
        mov ax, @data
        mov ds, ax 
        
        mov si, offset arr
        mov cx, 6
        
        
        
        printt:
            mov dl, [si]
            add dl, 30h
            mov ah, 02h
            int 21h
            
            cmp cx,1                          ; Printing the array by the loop after sorting it
            je go
            
            mov dl, 2Ch
            mov ah, 02h
            int 21h
            
            
            inc si
            
            loop printt
            
            go:
                ret

            
printloop endp


            

           
maxelement proc
    
        mov ah,09h
        lea dx,printmaxmsg            
        int 21h
        mov si, offset arr
        
        mov al,[si]
        inc si
        
        
        mov counter, 1
         
        maxloop:
            cmp [si], al
            jg newone
            inc si                                   ; We assume that the first element is the greatest element, then we loop on the elements
                                                     ; after it, if we find a greater one, we move it to al, so that al will have the greatest
                                                     ; element in the end and go to printmax to print it
            
            cmp counter, 5
            je printmax
            inc counter
            loop maxloop
            
            newone:
                mov al, [si]
                inc si 
                
                cmp counter,5
                je printmax  
                inc counter
                loop maxloop
                
                
        printmax:
               
            mov dl, al 
            add dl,30h 
            mov ah, 02h
           
            int 21h
            
            ret

            
maxelement endp

minelement proc
    
        mov ah,09h
        lea dx,printminmsg            
        int 21h
        mov si, offset arr
        
        mov al,[si]
        inc si
        
        
        mov counter, 1
         
        minloop:
            cmp [si], al
            jl newonee
            inc si
            
            cmp counter, 5
            je printmin                          ; We assume that the first element is the least element, then we loop on the elements
                                                 ; after it, if we find a less one, we move it to al, so that al will have the least
                                                 ; element in the end and go to printmin to print it
            inc counter
            loop minloop
            
            newonee:
                mov al, [si]
                inc si 
               
                cmp counter,5
                je printmin
                inc counter
                loop minloop                       
        printmin:
               
            mov dl, al 
            add dl,30h 
            mov ah, 02h
           
            int 21h
            
            
            ret
            
minelement endp
            
readnew proc
    mov ax,@data
    mov ds, ax
    
   
    
    mov counter , 0
                                          ; Reading new elements by printing the enter new 6 elements message and calling the read function
    
    mov si, offset arr
     
    mov ah,09h           
    lea dx,entersixmsg
    int 21h   
    
    call read
    ret        
readnew endp

help proc
    
    mov ah,09h
    lea dx,selvmsg            
    int 21h
    
    mov ah,09h
    lea dx,selxmsg            
    int 21h
    
    mov ah,09h
    lea dx,selnmsg            
    int 21h
    
    
    mov ah,09h
    lea dx,seldevmsg            
    int 21h
    
    mov ah,09h
    lea dx,selamsg                            ; Displaying the help messages if the user enters ? when reading an operation
    int 21h
    
    mov ah,09h
    lea dx,seldmsg            
    int 21h
    
    mov ah,09h
    lea dx,selsmsg            
    int 21h
    
    mov ah,09h
    lea dx,selallmsg            
    int 21h
    
    mov ah,09h
    lea dx,selqmsg            
    int 21h
    
    mov ah,09h
    lea dx,selnewmsg            
    int 21h
    
    mov ah,09h
    lea dx,selhelp            
    int 21h
    
    ret
    

    
help endp 

read proc 
    mov ax,@data
    mov ds, ax
    
   
    
    mov counter , 1
    
    mov si, offset arr 
       
   
    top:     
        wrong:          
         mov ah,09h           
         lea dx,msg1
         int 21h
         
         mov al, counter
         add al, 30h
         mov dl, al
         mov ah, 02h
         int 21h
         
         mov ah,09h
         lea dx,msg2            
         int 21h
    
         mov ah, 01h
         int 21h 
         sub al, 30h                                                               
         mov bl, al        
            
            
         mov ah,01h
         int 21h         
         mov cl,al                              ; The read function uses a counter to know when to stop, it reads the number, if the conditions 
                                                ; are satisfied by being between 0 and 9, the number is added to the array, otherwise, it displays
                                                ; that the number entered is wrong, when to counter hits 7, it goes to display the thanks message
            
         cmp cl, 13
         je L2 
            
            
         mov ah,09h           
         lea dx,numerrormsg
         int 21h             
            
                 
         jmp wrong 
                       
         L2:
            
            cmp bl,9
            je next
            jl next
                  
                 
            mov ah,09h           
            lea dx,numerrormsg
            int 21h
                 
              
            jmp wrong
            
            next:
                  cmp bl,0
                  je L1
                  jg L1 
                          
                  mov ah,09h           
                  lea dx,numerrormsg
                  int 21h
                 
                          
                           
                  jmp wrong
                          
                
            L1:
       
                mov [si],bl
                inc si 
                
               ; mov ah,09h           
;                lea dx,newline
;                int 21h 
              
                inc counter   
                cmp counter,7
                je ok
             
             
      loop top
     ret 
read endp                                                                              
 
 
 
printthanks proc
   
    mov ah,09h           
    lea dx,thanks  ; Displayong the thanks message
    int 21h
    ret

printthanks endp

 
 
exitcode:
    
.exit        ; Exiting the program if the user enters q
end
