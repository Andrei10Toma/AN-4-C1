#include <stdio.h> 
#include <time.h> 
#include <rpc/rpc.h> 

#include "ex2.h" 
#define RMACHINE "rpc.sprc.dfilip.xyz"

int main(int argc, char *argv[]){
    CLIENT *handle;

	handle=clnt_create(
		RMACHINE,		/* numele masinii unde se afla server-ul */
		CHECK_PROG,		/* numele programului disponibil pe server */
		CHECK_VERS,		/* versiunea programului */
		"tcp");			/* tipul conexiunii client-server */

    if(handle == NULL) {
		perror("");
		return -1;
	}
    
    student my_student;
    my_student.grupa = "343C1";
    my_student.nume = "Toma Andrei";

    printf("%s\n", *(grade_1(&my_student, handle)));
}