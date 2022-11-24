/*
 * This is sample code generated by rpcgen.
 * These are only templates and you can use them
 * as a guideline for developing your own functions.
 */

#include "oauth.h"
#include <iostream>
#include <string>
#include <fstream>
#include <vector>
#include <sstream>
#include <unordered_map>

using namespace std;

CLIENT *
oauth_prog_1(char *host)
{
	CLIENT *clnt;

	clnt = clnt_create (host, OAUTH_PROG, OUATH_VERS, "udp");
	if (clnt == NULL) {
		clnt_pcreateerror (host);
		exit (1);
	}

	return clnt;
}

void split_str(string &str, char delim, vector<string> &out ) {  
	std::stringstream s(str);
	std::string s2;
	while (getline(s, s2, delim))
		out.push_back(s2);
}

unordered_map<string, string> user_id_access_token;
unordered_map<string, int> user_id_ops;

int
main (int argc, char *argv[])
{
	char *host;

	setbuf(stdout, NULL);

	if (argc < 2) {
		printf ("usage: %s server_host\n", argv[0]);
		exit (1);
	}
	host = argv[1];
	CLIENT *clnt = oauth_prog_1 (host);
	char *operations_file = argv[2];
	ifstream fin(operations_file);
	string line;
	while (getline(fin, line)) {
		vector<string> split_line;
		split_str(line, ',', split_line);
		if (strcmp(split_line[1].c_str(), "REQUEST") == 0) {
			request_authorization_information request_info;
			memcpy(request_info.user_id, split_line[0].c_str(), 16);
			char *authorization = *(request_authorization_1(&request_info, clnt));
			if (strcmp(authorization, "USER_NOT_FOUND") != 0) {
				approve_request_token_information approve_token_info;
				memcpy(approve_token_info.access_token, authorization, 16);
				approve_request_token_1(&approve_token_info, clnt);

				request_access_token_information request_access_info;
				request_access_info.auto_refresh = stoi(split_line[2]);
				memcpy(request_access_info.authorization_token, authorization, 16);
				memcpy(request_access_info.user_id, split_line[0].c_str(), 16);
				request_access_token_response *access_response = request_access_token_1(&request_access_info, clnt);
				if (strcmp(access_response->access_token, "REQUEST_DENIED") != 0) {
					if (request_access_info.auto_refresh == 0) {
						cout << authorization << " -> " << access_response->access_token << endl;
					} else {
						cout << authorization << " -> " << access_response->access_token << "," << access_response->refresh_token << endl;
						user_id_ops[split_line[0]] = access_response->valability;
					}
					user_id_access_token[split_line[0]] = access_response->access_token;
				} else {
					cout << "REQUEST_DENIED" << endl;
				}
			} else {
				cout << "USER_NOT_FOUND" << endl;
			}
		} else {
			cout << split_line[0] << " : " << user_id_ops[split_line[0]] << endl;
			if (user_id_ops.find(split_line[0]) != user_id_ops.end() && user_id_ops[split_line[0]] == 0) {
				request_access_token_information request_access_info;
				request_access_info.auto_refresh = 2;
				memcpy(request_access_info.authorization_token, user_id_access_token[split_line[0]].c_str(), 16);
				request_access_token_response *refresh_response = request_access_token_1(&request_access_info, clnt);
				user_id_ops[split_line[0]] = refresh_response->valability;
				user_id_access_token[split_line[0]] = refresh_response->access_token;
			}
			validate_delegated_action_information delegate_action_info;
			memcpy(delegate_action_info.access_token, user_id_access_token[split_line[0]].c_str(), 16);
			memcpy(delegate_action_info.operation, split_line[1].c_str(), 8);
			delegate_action_info.resource = (char *)calloc(split_line[2].length() + 1, sizeof(char));
			memcpy(delegate_action_info.resource, split_line[2].c_str(), split_line[2].length() + 1);
			char *response = *(validate_delegated_action_1(&delegate_action_info, clnt));
			if (strcmp(response, "TOKEN_EXPIRED") == 0) {
				user_id_access_token.erase(split_line[0]);
			}
			if (user_id_ops.find(split_line[0]) != user_id_ops.end())
				user_id_ops[split_line[0]]--;
			cout << response << endl;
		}
	}
	fin.close();

	exit (0);
}