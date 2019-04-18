#!/bin/bash
mkdir -p data/grafana
sudo docker rm -vf grafana
sudo docker run -e GF_AUTH_ANONYMOUS_ENABLED=true --net host --user $(id -u):$(id -g) --rm  -it -v $(pwd)/data/grafana:/var/lib/grafana -p 3000:3000 grafana/grafana