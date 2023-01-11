import paho.mqtt.client as mqtt
from influxdb import InfluxDBClient
import json
import os
from datetime import datetime

client: mqtt.Client = mqtt.Client()
db_client: InfluxDBClient = InfluxDBClient('influxdb', 8086, retries=10)


def on_connect(client, userdata, flags, rc):
    print('Connected with result code ' + str(rc))
    client.subscribe('#')


def log_message(s: str):
    if os.getenv('DEBUG_DATA_FLOW') != 'true':
        return
    print(s)


def on_message(client, userdata, msg):
    payload: dict = json.loads(msg.payload)
    split_topic = msg.topic.split('/')
    location = split_topic[0]
    station = split_topic[1]

    if not payload:
        return None
    log_message(f"Received a message by topic [{msg.topic}]")
    if 'timestamp' in payload:
        log_message(f"Data timestamp is: {payload['timestamp']}")
    else:
        log_message(f"Data timestamp is NOW")

    fields = {}
    for key, value in payload.items():
        if isinstance(value, (int, float)):
            log_message(f"{location}.{station}.{key} {value}")
            fields[key] = value

    db_data = []
    current_time = payload['timestamp'] if 'timestamp' in payload else datetime.now()

    for key, value in fields.items():
        db_data.append({
            'measurement': f"{location}.{station}.{key}",
            'tags': {
                'station': station,
                'location': location,
            },
            'time': current_time,
            'fields': {
                'value': value
            },
        })

    db_client.write_points(db_data)


if __name__ == '__main__':
    # create the database
    if not {'name': 'sprc3'} in db_client.get_list_database():
        db_client.create_database('sprc3')
    db_client.switch_database('sprc3')

    client.on_connect = on_connect
    client.on_message = on_message

    client.connect('mqtt_broker', 1883)
    client.loop_forever()
