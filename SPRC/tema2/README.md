# Tema 2 SPRC

Pentru implementarea temei am ales sa folosesc MongoDB pentru baza de
date, Mongo Express pentru utilitarul de gestiune al bazei de date si
python impreuna cu flask si pymongo pentru implementarea api-ului care
se conecteaza la baza de date.

Precizari:
- Portul pe care ruleaza aplicatia cu rest api-ul este 80 in cotainer
si este expus prin portul 6000 deci in postamn variabila port ar
trebui setata pe 6000.
- Interfata expusa de mongo express se poate accesa dintr-un browser pe
localhost portul 1808 (portul expus, in container este pornit pe portul
8081).
- Credentialele de autentificare pentru Mongo Express sunt mexpress,
mexpress.

Rulare:
- Se schimba directorul de lucru intr-un director in care se afla
continutul arhivei.
- Se ruleaza `docker compose up`.