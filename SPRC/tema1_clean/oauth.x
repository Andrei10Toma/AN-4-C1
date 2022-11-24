/*
 * the information that is sent to the server to request the
 * authorization_token
 * user_id - the id of the user that makes the request
 */
struct request_authorization_information {
    char user_id[16];
};

/*
 * the information that is sent to the server to generate the access
 * token
 * user_id - of the user
 * authorization_token - the token generated by the previous
 * authorization request
 * auto_refresh - flags for the user that will use the automatic
 * refresh of the access token
 */
struct request_access_token_information {
    char user_id[16];
    char authorization_token[16];
    int auto_refresh;
};

/*
 * the information sent back to the client for the access token request
 * access_token - the generated access token
 * refresh_token - the generated refresh token if the user set the
 * auto_refresh flag from the access token request
 * valability - the number of operations available for the access token 
 */
struct request_access_token_response {
    char access_token[16];
    char refresh_token[16];
    int valability;
};

/*
 * the information sent to the server when the user wants to execute
 * an operation on a resource from the server
 * access_token - the access token
 * resource - the name of the resource
 * operation - one of the 5 permitted operations (RIMDX)
 */
struct validate_delegated_action_information {
    char access_token[16];
    string resource<>;
    char operation[8];
};

/*
 * the information sent to the server to approve the request token
 * and assign the set of approvals in the user data base
 * access_token - authorization token to check if it can be signed
 * sign - flag that marks the token is signed
 */
struct approve_request_token_information {
    char access_token[16];
    int sign;
};

/*
 * entry of a resource
 * resource - name of the resource
 * permission - set of permissions (RIMDX)
 */
struct approve_entry {
    string resource<>;
    char permissions[16];
};

program OAUTH_PROG {
    version OUATH_VERS {
        /* generate the authorization token for the given user id */
        string request_authorization(request_authorization_information) = 1;
        
        /* generate the access token for the given data */
        request_access_token_response request_access_token(request_access_token_information) = 2;

        /* check if the operation that will be executed on the resource is permitted */
        string validate_delegated_action(validate_delegated_action_information) = 3;

        /* sign the authorization token */
        approve_request_token_information approve_request_token(approve_request_token_information) = 4;
    } = 1;
} = 123456789;