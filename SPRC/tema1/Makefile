build:
	g++ -c oauth_xdr.c
	g++ -o client oauth_client.cpp oauth_clnt.c oauth_xdr.o -lnsl -Wall
	g++ -o server oauth_server.cpp oauth_svc.cpp oauth_xdr.o -lnsl -Wall

build_rpc:
	rpcgen -Ca oauth.x

backup:
	cp -r . ../backup

clean:
	rm -f client server oauth.h oauth_clnt.c oauth_svc.c oauth_client.c oauth_server.c Makefile.oauth oauth_xdr.*
