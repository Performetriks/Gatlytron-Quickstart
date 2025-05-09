package com.performetriks.gatlytron.test.settings;

import static io.gatling.javaapi.core.CoreDsl.AllowList;
import static io.gatling.javaapi.core.CoreDsl.DenyList;
import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.http.HttpDsl.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.performetriks.gatlytron.base.Gatlytron;
import com.performetriks.gatlytron.reporting.GatlytronReporterCSV;
import com.performetriks.gatlytron.reporting.GatlytronReporterDatabasePostGres;
import com.performetriks.gatlytron.reporting.GatlytronReporterJson;
import com.performetriks.gatlytron.reporting.GatlytronReporterOTel;
import com.performetriks.gatlytron.reporting.GatlytronReporterSysoutCSV;

import ch.qos.logback.classic.Level;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class TestGlobals {

	private static final Logger logger = LoggerFactory.getLogger(TestGlobals.class);
	
	//================================================================
	// Define your Environments here
	//================================================================
	public enum Environment {
		
		  DEV ("http://localhost:8888/")
		, TEST("http://localhost:7777/")
		;

		public final String url;
		
		Environment(String url) {
			this.url = url;
		}
				
		// example on how to keep things in one place
		public String getAPIURL() { return url + "/rest/api"; }
		public String getXDynatraceHeader() { return "GatlytronPerfTest"; }
		
	}
	
	//================================================================
	// Set the Environment to run your Test against
	//================================================================
	public static Environment ENV = Environment.DEV;
	
	
	//================================================================
	// GLOBALS
	//================================================================
	public static final String DIR_RESULTS = "./target/";

	public static final String DB_TABLE_PREFIX = "gatlytron";
	// You can set the report interval to the same as the Console writePeriod of gatling.conf like this:
	//public static final int REPORT_INTERVAL = Gatlytron.getConsoleWritePeriodSeconds();
	public static final int REPORT_INTERVAL = 15;
	
	//================================================================
	// DATA FEEDER
	//================================================================
	public static FeederBuilder.Batchable<String> dataFeeder = csv("testdata_"+ENV.name()+".csv").circular();
	public static FeederBuilder.Batchable<String> getDataFeeder() { return dataFeeder; }


	/****************************************************************************
	 * 
	 ****************************************************************************/
	public static void commonInitialization() {
		
		//------------------------------
    	// Gatlytron Configuration
		Gatlytron.setDebug(false);
		Gatlytron.setLogLevelRoot(Level.INFO);
		Gatlytron.setLogLevel(Level.DEBUG, "com.performetriks.gatlytron");
		
		Gatlytron.setRawDataToSysout(false);
		Gatlytron.setRawDataLogPath( DIR_RESULTS + "/gatlytron-raw-"+ENV.name()+".log" );		

    	//------------------------------
    	// File Reporter
    	Gatlytron.addReporter(new GatlytronReporterJson( DIR_RESULTS + "/gatlytron-"+ENV.name()+".json", false) );
    	Gatlytron.addReporter(new GatlytronReporterCSV( DIR_RESULTS + "/gatlytron-"+ENV.name()+".csv", ";") );
    	
    	//------------------------------
    	// Sysout Reporter
    	//Gatlytron.addReporter(new GatlytronReporterSysoutJson());
    	Gatlytron.addReporter(new GatlytronReporterSysoutCSV(";"));
    	
    	//------------------------------
    	// PostGres DB Reporter
    	Gatlytron.addReporter(
    			new GatlytronReporterDatabasePostGres(
	    			 "localhost"
	    			, 5432
	    			, "postgres"
	    			, DB_TABLE_PREFIX
	    			, "postgres"
	    			, "postgres"
    			)
    		);
    	
    	//------------------------------
    	// EMP Reporter
//    	Gatlytron.addReporter(
//    			new GatlytronReporterEMP(
//    					"http://localhost:8888"
//    					,"gatlytron-test-token-MSGIUzrLyUsOypYOkekVgmlfjMpLbRCA"
//    				)
//    			);
    	
    	//------------------------------
    	// JDBC DB Reporter
//    	Gatlytron.addReporter(
//    			new GatlytronReporterDatabaseJDBC("org.h2.Driver"
//    					, "jdbc:h2:tcp://localhost:8889/./datastore/h2database;MODE=MYSQL;IGNORECASE=TRUE"
//    					, DB_TABLE_PREFIX
//    					, "sa"
//    					, "sa") {
//					
//					@Override
//					public GatlytronDBInterface getGatlytronDB(DBInterface dbInterface, String tableNamePrefix) {
//						return new GatlytronDBInterface(dbInterface, tableNamePrefix);
//					}
//				}
//    		);
    	
    	//------------------------------
    	// OTel Reporter
    	Gatlytron.addReporter(
    			new GatlytronReporterOTel("http://localhost:9090/api/v1/otlp/v1/metrics", REPORT_INTERVAL)
    		);
    	
    	
    	//------------------------------
    	// Start Gatlytron
    	Gatlytron.start(REPORT_INTERVAL);

	}
	
	/****************************************************************************
	 * Set the environment, this should only be called during the setup of a 
	 * simulation and not after it started.
	 ****************************************************************************/
	public static void setEnvironment(Environment env) {
		ENV = env;
	}
	
	/****************************************************************************
	 * 
	 ****************************************************************************/
	public static void commonTermination() {
		try {
		Gatlytron.terminate();
		}catch(Throwable e){
			logger.warn("Exception occured during termination: "+e.getMessage(), e);
		}
	}
	
	/****************************************************************************
	 * 
	 ****************************************************************************/
	public static HttpProtocolBuilder getProtocol() { 
		HttpProtocolBuilder httpProtocol= http
				.baseUrl(ENV.url)
				.disableUrlEncoding()
				.inferHtmlResources(AllowList(), DenyList(
						  ".*\\.js"
						, ".*\\.css"
						, ".*\\.gif"
						, ".*\\.jpeg"
						, ".*\\.jpg"
						, ".*\\.png"
						, ".*\\.ico"
						, ".*\\.woff"
						, ".*\\.woff2"
						, ".*\\.(t|o)tf"
						, ".*\\.svg"
						, ".*detectportal\\.firefox\\.com.*"))
				.userAgentHeader("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/128.0.0.0 Safari/537.36"); 
		
		return httpProtocol;
	}
}