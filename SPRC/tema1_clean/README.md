# Tema 1 SPRC (Toma Andrei 343C1)

## Precizari
- Am modificat expected out-urile din checker adaugand o
linie noua;
- Fisierle *readme* le-am modificat sa contina doar valoarea
default de initializare a numarului de operatii permise pana
la expirarea tokenului;
- Sa **nu** se ruleze rpcgen ca o sa strice sursele;
- In `check.sh` am modificat astfel incat sa fie afisat
continutul fisierelor *readme* ca ultim parametru al
serverului dat din linia de comanda;
- Explicatii ale temei se gasesc in fisierele `.cpp` si
fisierul `.x` prin comentarii.
- Intregul enunt al temei este implementat.

## Rulare
- Se compileaza sursele:<br>
`make build`
- Se copiaza in folderul tests executabilele server si
client:<br>
`cp server client tests`
- ne mutam in folderul tests si se ruleaza checkerul:<br>
`cd tests`<br>
`./check.sh all`
