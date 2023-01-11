#!/bin/bash

export SPRC_DVP=$(pwd)
[ -d "$SPRC_DVP/influxdb_data" ] || mkdir "$SPRC_DVP/influxdb_data"
[ -d "$SPRC_DVP/grafana_data" ] || mkdir "$SPRC_DVP/grafana_data"

chmod o+w grafana_data

docker build src -t mqttadapter
docker stack deploy -c stack.yml sprc3
