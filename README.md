# M1_IL_IPR_PROJECT
1. Run mosquitto & rabbitmq & influxdb
2. Run App.java (usine1)
3. Run API.java (usine1 -> mosquitto <- ThreadSend -> JSONSend -> Rabbitmq <- JSONRecv -> addToInfluxdb -> influxdb)
some default variable of API.java :
- EXCHANGE_NAME = "AbdelEX"
- TOPIC_NAME = "/core1/temperature" ...
- dbname = "AbdelDB" for influxdb
4. Run grafana and browse this link (http://localhost:3000)
first connexion in grafana (id:admin psw:admin)
data source -> add data source -> select influxdb
* choose name
* URL = http://localhost:8086
* DATABASE = AbdelDB
* hit Save&Test button
* Now we will create a new dashboard:
* create new dashboard -> choose visiualisation --> choose graph
for exemple you need to follow the progress of /core1/temperature
*general -> choose title.
*queries -> SELECT mean("value") FROM "temperature" WHERE ("core_name" = 'core1') 
                AND $timeFilter GROUP BY time($__interval) fill(null).
*save dashboard and enjoy your progress with awesome graph from grafana
