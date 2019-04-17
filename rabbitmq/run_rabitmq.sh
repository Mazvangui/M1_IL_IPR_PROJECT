#!/bin/bash
# NOM D'UTILISATEUR :
export RMQ_USER="admin"

# MOT DE PASSE :
export RMQ_PASSWORD="admin"

# VHOST :
export RMQ_VHOST="ipr"

export PORT_SERVER=8088
export PORT_WEB=8089

echo "adresse serveur : amqp://${RMQ_USER}:${RMQ_PASSWORD}@localhost:${PORT_SERVER}/${RMQ_VHOST}"
echo "adresse admin : http://${RMQ_USER}:${RMQ_PASSWORD}@localhost:${PORT_WEB}/"

sudo docker rm -vf rbmqtp
sudo docker run --rm -it --hostname my-rabbit --name rbmqtp -p ${PORT_WEB}:15672 -p ${PORT_SERVER}:5672 -e RABBITMQ_DEFAULT_VHOST=${RMQ_VHOST} -e RABBITMQ_DEFAULT_USER=${RMQ_USER} -e RABBITMQ_DEFAULT_PASS=${RMQ_PASSWORD} rabbitmq:3-management
