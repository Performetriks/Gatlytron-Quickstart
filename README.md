
# Gatlytron Quickstart

Gatlytron is a little framework that assists you in creating Gatling tests and provides some additional features like reporting.
This project is a starting project that gives you a working base setup you can build your tests upon.
Clone it to your disk and open it in your IDE to get started.

# Known Limitations
Before you try Gatlytron and find out it doesn't work for your case, here some known limitations:
* Gatlytron is made for Gatling version v3.12.0 and higher. For using Gatlytron with older Gatling versions, see the respective section at the end of this manual.
* Gatlytron can only run one simulation class at the time. It cannot start multiple simulations in parallel.
* The simulation name is not auto detected and has to be set manually using `Gatlytron.setSimulationName(this.getClass().getSimpleName());`. This name will be used for Gatlytron reports and can be customized. It will not be used for the Gatling standard reports(they will still use the class name).


# Creating and Running Tests
Example code that uses gatling can be found in this repository under `src/main/java/` in the package `com.performetriks.gatlytron.test`.

To create a Scenario, create a class that extends `GatlytronScenario.java`:

``` java
public class SampleScenario extends GatlytronScenario {
  
    public SampleScenario() {
        super("MyScenario");
      
        this
        	  //.debug(TestGlobals.DEBUG) // default obtained from Gatlytron.isDebu();
            .feederBuilder(TestGlobals.getDataFeeder())
            .scenarioSteps(
                exec(
						http("callURL").get("")
                )
        );
    }
}
```

In your simulation, you can add your test scenarios using the `.build*()` methods.
Using these methods you can easily create Standard load test with proper load distribution, just run a scenario once or just get a plain scenario and add your `inject*()` definition yourself:

``` java

public class SimulationExample extends Simulation {
 
    private static final Duration TEST_DURATION = Duration.ofSeconds(15);

    {
    	setUp(
                new SampleScenario().buildStandardLoad(10, 600, 0, 2)
              , new SampleScenario().buildRunOnce()
              , new SampleScenario().buildScenario(0).injectOpen(...)
           ).protocols(TestGlobals.getProtocol())
            .maxDuration(TEST_DURATION)
           ;

    }
    
    @Override
    public void after() {
		Gatlytron.terminate();
    }
}
```


## Executing a Simulation
If you define multiple simulations in your project, to run a specific simulation define the parameter "-Dgatling.simulationClass":

```
# maven
mvn clean verify "-Dgatling.conf.file=gatling.conf" "-Dgatling.simulationClass=com.performetriks.gatlytron.test.simulation.SimulationCheckDebug"

# gradle - by default, results will be written to ./taget directory
./gradlew clean verify "-Dgatling.simulationClass=com.performetriks.gatlytron.test.simulation.SimulationCheckDebug"
```

## Logging
Gatlytron provides some methods to set log levels for logback in code and other logging options. This helps keeping all the config in one place instead of having it distributed in code and config files.
Keep in mind that extensive logging can affect the execution of your tests. Only enable what you really need.

``` java
Gatlytron.setDebug(false); // common debug flag, can be accessed with Gatlytron.isDebug()
Gatlytron.setLogLevelRoot(Level.INFO);
Gatlytron.setLogLevel(Level.INFO, "com.performetriks.gatlytron");

Gatlytron.setRawDataToSysout(false);
Gatlytron.setRawDataLogPath( DIR_RESULTS + "/gatlytron-raw.log" );
```

# Reporting
Gatlytron comes with an built-in data receiver, which is used to get the real time data and send it to a data store or otherwise process it through classes implementing the GatlytronReporter interface(feel free to extend this one yourself).

The fields available in the reported data are as follows:

* **time:** Time in epoch milliseconds.
* **type:** The type of the record: REQ = Request / USR = User Counts.
* **simulation:** Name of the simulation.
* **scenario:** Name of the scenario.
* **metric:** The name of the metric.
* **code:** The response code of the request, will be 000 if unknown, for example in case of connection errors.

* **ok_count, ok_min, ok_max, ok_mean, ok_stdev, ok_p50, ok_p75, ok_p95, ok_p99:** Fields that countain the metrics for request in status "ok".
* **ko_count, ko_min, ko_max, ko_mean, ko_stdev, ko_p50, ko_p75, ko_p95, ko_p99:** Fields that countain the metrics for request in status "ko".

Here is a JSON example of three records. the first two are user statistic records, the next two are request records:

``` json
{"time":1733821445032,"type":"USR","simulation":"SimulationCheckDebug","scenario":"Website","metric":"users.total_started","code":"000","ok_count":1,"ok_min":2,"ok_max":2,"ok_mean":2,"ok_stdev":0,"ok_p50":2,"ok_p75":2,"ok_p95":2,"ok_p99":2,"ko_count":null,"ko_min":null,"ko_max":null,"ko_mean":null,"ko_stdev":null,"ko_p50":null,"ko_p75":null,"ko_p95":null,"ko_p99":null},
{"time":1733821445032,"type":"USR","simulation":"SimulationCheckDebug","scenario":"Website","metric":"users.total_stopped","code":"000","ok_count":1,"ok_min":0,"ok_max":0,"ok_mean":0,"ok_stdev":0,"ok_p50":0,"ok_p75":0,"ok_p95":0,"ok_p99":0,"ko_count":null,"ko_min":null,"ko_max":null,"ko_mean":null,"ko_stdev":null,"ko_p50":null,"ko_p75":null,"ko_p95":null,"ko_p99":null},
{"time":1733821450400,"type":"REQ","simulation":"SimulationCheckDebug","scenario":"API.callInterface","metric":"fetchInfo Redirect 1","code":"302","ok_count":11,"ok_min":0,"ok_max":1,"ok_mean":1,"ok_stdev":0,"ok_p50":1,"ok_p75":1,"ok_p95":1,"ok_p99":1,"ko_count":null,"ko_min":null,"ko_max":null,"ko_mean":null,"ko_stdev":null,"ko_p50":null,"ko_p75":null,"ko_p95":null,"ko_p99":null},
{"time":1733821450400,"type":"REQ","simulation":"SimulationCheckDebug","scenario":"API.callInterface","metric":"fetchInfo Redirect 2","code":"200","ok_count":11,"ok_min":12,"ok_max":16,"ok_mean":14,"ok_stdev":1.41,"ok_p50":13,"ok_p75":13,"ok_p95":16,"ok_p99":16,"ko_count":null,"ko_min":null,"ko_max":null,"ko_mean":null,"ko_stdev":null,"ko_p50":null,"ko_p75":null,"ko_p95":null,"ko_p99":null},

```

## Reporting Configuration

For the reporting to be activated, you need to call `Gatlytron.start(REPORT_INTERVAL):`.
This call is already included in the example in the Method `TestGlobals.commonInitialization();`

There are various settings you can access through the static methods in the class `Gatlytron`. You can find examples in the class:   `com.performetriks.gatlytron.test.settings.TestGlobals.java` 

To add reporters, use  `Gatlytron.addReporter(new GatlytronReporter*());` to your simulation. 
It is recommended to only add the reporters you really need to not cause unnecessary performance overhead.
Here some examples:

```java
	//------------------------------
	// Gatlytron Configuration
	Gatlytron.addReporter(new GatlytronReporterJson("./target/gatlytron.json", true));
   Gatlytron.addReporter(new GatlytronReporterCSV("./target/gatlytron.csv", ";"));
   Gatlytron.addReporter(new GatlytronReporterSysoutJson());
```

## Reporting Interval
The reporting interval is defined in seconds by the method `Gatlytron.start(intervalSeconds);`.
If you like, you can use the the following to set the reporting interval to the same as the console writePeriod:
```
		public static final int REPORT_INTERVAL = Gatlytron.getConsoleWritePeriodSeconds();
    	Gatlytron.start(REPORT_INTERVAL);
```

If above is used, the following property from the `gatling.conf` is used.
```
gatling { 
  data { 
    console {                     
      writePeriod = 15                       # Write interval, in seconds
    }
```

## Reporting to Databases

There are Gatlytron reporters which allow you to report metrics to a database.
For this to work you will need to include the respective driver dependency. 

### Tables
There will be two tables created in the database:
* **{tableName}:** The base table containing all the metrics. (fields are explained above under section "Reporting")
* **{tableName}_testsettings:** This table will contain the settings for the scenarios that have been executed:
	* **time:** Time in epoch seconds.
	* **execID:** A unique id for the test execution.
	* **simulation:** The name of the simulation.
	* **scenario:** The name of the scenario.
	* **users:** The target number of users for the scenario.
	* **execsHour:** The target number of executions per hour for the scenario.
	* **startOffset:** The start offset in seconds for the scenario.
	* **rampUp:**  The number of users to ramp up per interval.
	* **rampUpInterval:** The ramp up interval in seconds.
	* **pacingSeconds:** The pacing of the use case in seconds. 
	

### Reporting to Postgres
Following an example for PostGres SQL:

```xml
<!-- https://mvnrepository.com/artifact/org.postgresql/postgresql -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <version>42.7.4</version>
    <scope>provided</scope>
</dependency>
```

Now you can add the Reporter for the database. The reporter will create the table with the given name if not exists:

```java
{ 	
	Gatlytron.addReporter(
    			new GatlytronReporterDatabasePostGres(
	    			"localhost"
	    			, 5432
	    			, "postgres"  // database name
	    			, "gatlytron" // table name
	    			, "dbuser"
	    			, "dbpassowrd"  
    			)
    		);
    	
	setUp(
			new SampleScenario().buildStandardLoad(10, 600, 0, 2)
	   ).protocols(TestSettings.getProtocol())
		.maxDuration(TEST_DURATION)
	   ;
    	
}

```

### Reporting to any JDBC Database
You can use the class `GatlytronReporterDatabaseJDBC` to connect to any SQL database that is available through JDBC.
You need to include the dependency which provides the driver for the database you want to connect to.
Following an example for an H2 database, which is also delivered with EMP:

``` xml
<!-- https://mvnrepository.com/artifact/com.h2database/h2 -->
<dependency>
	<groupId>com.h2database</groupId>
	<artifactId>h2</artifactId>
	<version>2.2.224</version> 
</dependency>
```

Now you can add the Reporter for the database.
You will need to implement the method `getCreateTableSQL()` and may or may not adjust the SQL to create the table:

```java
//------------------------------
// JDBC DB Reporter
Gatlytron.addReporter(
		new GatlytronReporterDatabaseJDBC("org.h2.Driver"
				, "jdbc:h2:tcp://localhost:8889/./datastore/h2database;MODE=MYSQL;IGNORECASE=TRUE"
				, REPORTING_TABLE_NAME
				, "sa"
				, "sa") {
			
			@Override
			public String getCreateTableSQL() {
				return GatlytronCarbonRecord.getSQLCreateTableTemplate(REPORTING_TABLE_NAME);
			}
		}
	);
```

### Reporting to EMP
Two things you might wanna know about reporting metrics to EMP:
* The max granularity of data you will have in EMP is 1 minute.
* EMP will do an age out of the data or housekeeping. EMP will be a good solution if diskspace usage needs to be considered.

To report data directly to EMP, you would need to do the following:
1. Sign into EMP with an admin account.
2. Go to "Admin >> Configuration >> EAV: Entity Attribute Value" and:
	* Set the setting "Statistic Max Granularity" to 1.
	* Set the age out settings as it fits your needs, typical values for performance testing are:
		* 1st Age Out: < 15 Minutes : 2160  (this is 90 days in hours)
		* 2nd Age Out: < 1 Hour: 150
		* 3rd Age Out: < 6 Hours: 210
		* 4th Age Out: < 24 Hours: 270
		* 5th Age Out: < 1 Week: 365
		* Age Out Interval: 1440
3. Go to "Tools >> API >> Manage Tokens" and add a new token.
4. Edit your new token and add the following permissions:
	* EAVStats.pushStats
	* EAVStats.pushStatsCSV

Now you should be able to connect to EMP and report metrics with Gatlytron by using your token:

```java
{ 	
	//------------------------------
	// EMP Reporter
	Gatlytron.addReporter(
			new GatlytronReporterEMP(
					"http://localhost:8888"
					,"{your-EMP-API-token}"
				)
			);
    	
}

```

### Reporting to OpenTelemetry (OTel)

You can send your data to an Open Telemetry endpoint using the reporter GatlytronReporterOTel.
Please consider the following points:

* **Prefix 'gtron_':** All the metrics generated by Gatlytron will be prefixed with 'gtron_'.
	
* **OTel Naming Conventions:** OTel only allow metric names with alphanumeric, ".", "_", "-", "/" 
	and the name must start with a alphanumeric. Any other characters will be replaced with "_" by 
	Gatlytron to avoid errors.
	
* **More Naming Conventions:** The software you are sending the data to might also enforce further 
	naming conventions. For Example Prometheus will remove dots.
	
* **Attributes:** Because the metrics names can be messed up, various attributes have been added, 
	including the metric name, to make filtering for you more convenient. 

Following is a simple example how to add an OTel reporter in your test configuration.
The url given below send the data to a locally running prometheus instance. If you want to try this too,
you will need to start prometheus with OTLP enabled like `prometheus.exe --web.enable-otlp-receiver`.


```java
{ 	
    	//------------------------------
    	// OTel Reporter
    	Gatlytron.addReporter(
    			new GatlytronReporterOTel("http://localhost:9090/api/v1/otlp/v1/metrics", REPORT_INTERVAL)
    		);
    	
}

```

After running a test with above reporter, you should be able to filter the metrics, here a Prometheus example query using various attributes:

```
{ simulation="SimulationCheckDebug", scenario="Website", type="REQ", status="ok"} 
```

## Setup EMP Dashboards
Gatlytron provides templates for EMP dashboards. 
If you want to use EMP to show your Gatling simulation data, here is how:

1. Download the latest release of EMP: https://github.com/xresch/EngineeredMonitoringPlatform/releases
2. Setup Tutorial for EMP: https://www.youtube.com/watch?v=0Ug1daCedfs

**EMP:** 
For showing data sent to EMP, import the template dashboards from the folder `./docs/templates`.
Open and edit the the parameter widget that lets you select the compare time. In the setting "Affected on Update", you have to remove and reselect the "Compare Statistics" widget.

**Postgres:**
1. In EMP, go to "Admin >> Context Settings >> Add >> Postgres Environment" and fill in the connection details.
2. Import Gatlytron Dashboard template for Postgres from the folder `./docs/templates`.
3. Open the Dashboard
4. Click the "Edit" button in the top left
5. Click on the button "Params"
  	- Change "ID" of 3 parameters:
  		- **database_id:** Select the context setting from the dropdown.
  		- **'simulation' and 'request':** In the queries remove existing "environment={...}" and use Ctrl+Space for autocomplete and inserting the new one which you have created.
6. On the dashboard, edit the the parameter widget that lets you select the compare time. In the setting "Affected on Update", you have to remove and reselect the "Compare Statistics" widget. 
  		
# Running with Gatling versions lower than v3.12.0
There where some major changes in gatling 3.12 which had to be addressed within Gatlytron. 
If you need to run test with older Gatling version, use Gatlytron v1.2.0 and look at the documentation here:
[Gatlytron v1.2.0](https://github.com/Performetriks/Gatlytron/tree/v1.2.0)


