/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#ifndef _EX2_H_RPCGEN
#define _EX2_H_RPCGEN

#include <rpc/rpc.h>


#ifdef __cplusplus
extern "C" {
#endif


struct student {
	char *nume;
	char *grupa;
};
typedef struct student student;

#define CHECK_PROG 0x31234567
#define CHECK_VERS 1

#if defined(__STDC__) || defined(__cplusplus)
#define GRADE 1
extern  char ** grade_1(student *, CLIENT *);
extern  char ** grade_1_svc(student *, struct svc_req *);
extern int check_prog_1_freeresult (SVCXPRT *, xdrproc_t, caddr_t);

#else /* K&R C */
#define GRADE 1
extern  char ** grade_1();
extern  char ** grade_1_svc();
extern int check_prog_1_freeresult ();
#endif /* K&R C */

/* the xdr functions */

#if defined(__STDC__) || defined(__cplusplus)
extern  bool_t xdr_student (XDR *, student*);

#else /* K&R C */
extern bool_t xdr_student ();

#endif /* K&R C */

#ifdef __cplusplus
}
#endif

#endif /* !_EX2_H_RPCGEN */
