/*
 * Please do not edit this file.
 * It was generated using rpcgen.
 */

#include "oauth.h"

bool_t
xdr_request_authorization_information (XDR *xdrs, request_authorization_information *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->user_id, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_request_access_token_information (XDR *xdrs, request_access_token_information *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->user_id, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	 if (!xdr_vector (xdrs, (char *)objp->authorization_token, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->auto_refresh))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_request_access_token_response (XDR *xdrs, request_access_token_response *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->access_token, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	 if (!xdr_vector (xdrs, (char *)objp->refresh_token, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->valability))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_validate_delegated_action_information (XDR *xdrs, validate_delegated_action_information *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->access_token, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	 if (!xdr_string (xdrs, &objp->resource, ~0))
		 return FALSE;
	 if (!xdr_vector (xdrs, (char *)objp->operation, 8,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_approve_request_token_information (XDR *xdrs, approve_request_token_information *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_vector (xdrs, (char *)objp->access_token, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	 if (!xdr_int (xdrs, &objp->sign))
		 return FALSE;
	return TRUE;
}

bool_t
xdr_approve_entry (XDR *xdrs, approve_entry *objp)
{
	register int32_t *buf;

	int i;
	 if (!xdr_string (xdrs, &objp->resource, ~0))
		 return FALSE;
	 if (!xdr_vector (xdrs, (char *)objp->permissions, 16,
		sizeof (char), (xdrproc_t) xdr_char))
		 return FALSE;
	return TRUE;
}
