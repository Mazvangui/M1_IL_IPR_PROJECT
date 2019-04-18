#!/bin/bash
sudo docker rm -vf influxdb
sudo docker run -p 8086:8086 --rm -it --name influxdb -v $(pwd)/data/influxdb:/var/lib/influxdb  influxdb:1.5-alpine
