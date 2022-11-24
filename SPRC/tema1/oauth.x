struct request_authorization_information {
    char user_id[16];
};

struct request_access_token_information {
    char user_id[16];
    char authorization_token[16];
    int auto_refresh;
};

struct request_access_token_response {
    char access_token[16];
    char refresh_token[16];
    int valability;
};

struct validate_delegated_action_information {
    char access_token[16];
    string resource<>;
    char operation[8];
};

struct approve_request_token_information {
    char access_token[16];
    int sign;
};

struct approve_entry {
    string resource<>;
    char permissions[16];
};

program OAUTH_PROG {
    version OUATH_VERS {
        string request_authorization(request_authorization_information) = 1;
        request_access_token_response request_access_token(request_access_token_information) = 2;
        string validate_delegated_action(validate_delegated_action_information) = 3;
        approve_request_token_information approve_request_token(approve_request_token_information) = 4;
    } = 1;
} = 123456789;