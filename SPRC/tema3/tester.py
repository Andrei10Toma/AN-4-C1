import paho.mqtt.client as mqtt
import json
import time

client: mqtt.Client = mqtt.Client()

def on_connect(client, userdata, flags, rc):
    print('Connected with result code ' + str(rc))
    client.subscribe('#')

def on_message(client, userdata, msg):
    return

client.on_connect = on_connect
client.on_message = on_message

client.connect('localhost', 1883)
client.loop_start()
jsonBody = [
    {
        "BAT" : 99 ,
        "HUMID" : 40 ,
        "PRJ" : "SPRC" ,
        "TMP" : 25.3 ,
        "status" : "OK" ,
    },
    {
        "BAT" : 90 ,
        "HUMID" : 42 ,
        "PRJ" : "SPRC" ,
        "TMP" : 24.2 ,
        "status" : "OK" ,
    },
    {
        "BAT" : 86 ,
        "HUMID" : 37 ,
        "PRJ" : "SPRC" ,
        "TMP" : 20.3 ,
        "status" : "OK" ,
    },
    {
        "Alarm " : 0 ,
        "AQI" : 12 ,
        "RSSI " : 25
    },
    {
        "Alarm " : 3 ,
        "AQI" : 15 ,
        "RSSI " : 31
    },
    {
        "Alarm " : 2 ,
        "AQI" : 20 ,
        "RSSI " : 29
    }
]

while True:
    client.publish('UPB/RPi_1', json.dumps(jsonBody[0]))
    time.sleep(2)
    client.publish('UPB/RPi_1', json.dumps(jsonBody[1]))
    time.sleep(2)
    client.publish('UPB/RPi_1', json.dumps(jsonBody[2]))
    time.sleep(2)

    client.publish('Dorinel/Zeus', json.dumps(jsonBody[3]))
    time.sleep(2)
    client.publish('Dorinel/Zeus', json.dumps(jsonBody[4]))
    time.sleep(2)
    client.publish('Dorinel/Zeus', json.dumps(jsonBody[5]))
