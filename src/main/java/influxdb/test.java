package influxdb;

import java.util.concurrent.TimeUnit;

import org.influxdb.BatchOptions;
import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;

public class test {

	public static void main(String[] args) {
		InfluxDB influxDB = InfluxDBFactory.connect("http://localhost:8086");
		String dbName = "aTimeSeries";
		influxDB.createDatabase(dbName);
		influxDB.setDatabase(dbName);
		String rpName = "aRetentionPolicy";
		influxDB.createRetentionPolicy(rpName, dbName, "30d", "30m", 2, true);
		influxDB.setRetentionPolicy(rpName);

		influxDB.enableBatch(BatchOptions.DEFAULTS);

		influxDB.write(Point.measurement("cpu")
		    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
		    .addField("idle", 90L)
		    .addField("user", 9L)
		    .addField("system", 1L)
		    .build());

		influxDB.write(Point.measurement("disk")
		    .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
		    .addField("used", 80L)
		    .addField("free", 1L)
		    .build());

		Query query = new Query("SELECT idle FROM cpu", dbName);
		influxDB.query(query);
		//influxDB.dropRetentionPolicy(rpName, dbName);
		//influxDB.deleteDatabase(dbName);
		influxDB.close();
	}

}
