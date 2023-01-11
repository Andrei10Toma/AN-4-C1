# Tema 3 SPRC (Docker Swarm)

## Organizare

- config - directorul in care se va afla fisierul de configurare pentru
mosquitto (mosquitto.conf).
- grafana_data - volumul utilizat de grafana pentru persistenta
dashboard-urilor.
- influxdb_data - volumul utilizat de influxdb pentru persistenta
datelor aflate in baza de date.
- src - contine codul `python` pentru adaptor si Dockerfile-ul pentru
a construi imaginea.
- run.sh - script de lansare a Docker Swarm-ului.
- stack.yml - descrierea stack-ului Docker.
- tester.py - un scurt script de testare care trimite date catre catre
broker, implicit catre adaptorul creat.

## Detalii de implementare *Broker*

- Imagine folosita: https://hub.docker.com/_/eclipse-mosquitto.
- Brokerul este expus pe portul `1883`.
- Volumele create sunt:
    - `${SPRC_DVP}/mosquitto_log` pentru logurile de la mosquitto.
    - `${SPRC_DVP}/broker_data` pentru persistenta brokerilor.
- Am folosit aceasta imagine, deoarece implementeaza o versiune a
protocolului `mqtt` mai mare de 3.0 si pentru ca a fost facuta la
laborator.

## Detalii de implementare *TSDB*
- Imaginea folosita: https://hub.docker.com/_/influxdb.
- Baza de date este expusa pe portul `8086`.
- Volumele create sunt:
    - `${SPRC_DVP}/influxdb_data` pentru persistenta datelor din baza
    de date
- Am folosit aceasta imagine, deoarece aceasta a fost specificata in
enunt si am gasit un tutorial pentru ea.

## Detalii de implementare *Adapter*
- Imaginea este build-uita in script inainte ca Swarm-ul sa fie pornit
cu ajutorul comenzii `docker build`.
- Conectarea si primirea mesajelor sunt event-based.
- La momentul conectarii la baza de date, se printeaza un mesaj, iar
clientul se aboneaza pe toate topicurile (`#`).
- La primirea mesajului se parseaza json-ul primit, si se pastreaza din
acesta doar valorile numerice, de asemenea se genereaza un timestamp daca
acesta nu este dat printe field-urile json-ului. Se parcurg toate
field-urile numerice si, la final, se scriu toate valorile in baza de
date.
- Mesajul va avea in measurement ca serie de timp
`{location}.{station}.{key}` pentru ca afisarea in Grafana sa fie
corespunzatoare.

## Detalii de implementare *Grafana*
- Imaginea folosita: https://hub.docker.com/r/grafana/grafana
- Portul pe care este expus este `80`.
- Volumele create sunt:
    - `${SPRC_DVP}/grafana_data` pentru persistenta
    dashboard-urilor.
- Conecatera am facut-o din interfata web oferita de Grafana prin a
ma conecta la `http://influxdb:8086`.
- Am creat cele 2 dashboard-uri cerute in enunt:
    - UPB IoT Data - selectorul l-am facut sa inceapa cu `UPB.`
    measurement, iar pentru nume am pus ca Time Series $1.$2
    ({station}.{key}).
    - Battery Dashboard - selectorul l-am facut sa extraga toate
    fieldurile `BAT` iar ca Time Series am pus $1, adica statia. Pentru
    am create cate un quey pentru fiecare valoare ceruta, iar la final
    le-am dat join.

## Detalii de implementare *Tester* (Extra)
- Un client care se conecteaza si el la broker si trimite mesaje pentru
ca baza de date sa fie populata.

## Rulare
- `./run.sh` - pentru build de imagine si lansare Swarm
