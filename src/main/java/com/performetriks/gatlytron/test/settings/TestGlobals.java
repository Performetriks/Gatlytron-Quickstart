package com.performetriks.gatlytron.test.settings;

import static io.gatling.javaapi.core.CoreDsl.AllowList;
import static io.gatling.javaapi.core.CoreDsl.DenyList;
import static io.gatling.javaapi.core.CoreDsl.csv;
import static io.gatling.javaapi.http.HttpDsl.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.performetriks.gatlytron.base.Gatlytron;
import com.performetriks.gatlytron.database.DBInterface;
import com.performetriks.gatlytron.database.GatlytronDBInterface;
import com.performetriks.gatlytron.reporting.GatlytronReporterCSV;
import com.performetriks.gatlytron.reporting.GatlytronReporterDatabaseJDBC;
import com.performetriks.gatlytron.reporting.GatlytronReporterDatabasePostGres;
import com.performetriks.gatlytron.reporting.GatlytronReporterJson;

import ch.qos.logback.classic.Level;
import io.gatling.javaapi.core.FeederBuilder;
import io.gatling.javaapi.http.HttpProtocolBuilder;

public class TestGlobals {

	private static final Logger logger = LoggerFactory.getLogger(TestGlobals.class);
	
	public static final String URL_BASE = "http://localhost:8888/";
	
	public static final String REPORTING_TABLE_NAME = "gatlytron";
	
	public static final String DIR_RESULTS = "./target";
	
	
	public static FeederBuilder.Batchable<String> dataFeeder = csv("testdata.csv").circular();
	public static FeederBuilder.Batchable<String> getDataFeeder() { return dataFeeder; }

	/****************************************************************************
	 * 
	 ****************************************************************************/
	public static void commonInitialization() {
		
		// You can add system properties if you don't want to to use gatling.conf
    	// System.setProperty("gatling.graphite.host", "localhost");
		
		//------------------------------
    	// Gatlytron Configuration
		Gatlytron.setDebug(false);
		Gatlytron.setLogLevelRoot(Level.INFO);
		Gatlytron.setLogLevel(Level.DEBUG, "com.performetriks.gatlytron");
		
		Gatlytron.setRawDataToSysout(true);
		Gatlytron.setRawDataLogPath( DIR_RESULTS + "/gatlytron-raw.log" );
		//Gatlytron.setKeepEmptyRecords(false);
		
    	
    	//------------------------------
    	// File Reporter
    	Gatlytron.addReporter(new GatlytronReporterJson( DIR_RESULTS + "/gatlytron.json", true) );
    	Gatlytron.addReporter(new GatlytronReporterCSV( DIR_RESULTS + "/gatlytron.csv", ";") );
    	
    	//------------------------------
    	// Sysout Reporter
    	//Gatlytron.addReporter(new GatlytronReporterSysoutJson());
    	//Gatlytron.addReporter(new GatlytronReporterSysoutCSV(";"));
    	
    	//------------------------------
    	// EMP Reporter
    	/*Gatlytron.addReporter(
    			new GatlytronReporterEMP(
    					"http://localhost:8888"
    					,"gatlytron-test-token-MSGIUzrLyUsOypYOkekVgmlfjMpLbRCA"
    				)
    			);*/
    	
    	//------------------------------
    	// PostGres DB Reporter
    	Gatlytron.addReporter(
    			new GatlytronReporterDatabasePostGres(
	    			 "localhost"
	    			, 5432
	    			, "postgres"
	    			, REPORTING_TABLE_NAME
	    			, "postgres"
	    			, "postgres"
    			)
    		);
    	
    	//------------------------------
    	// JDBC DB Reporter
    	Gatlytron.addReporter(
    			new GatlytronReporterDatabaseJDBC("org.h2.Driver"
    					, "jdbc:h2:tcp://localhost:8889/./datastore/h2database;MODE=MYSQL;IGNORECASE=TRUE"
    					, REPORTING_TABLE_NAME
    					, "sa"
    					, "sa") {
					
					@Override
					public GatlytronDBInterface getGatlytronDB(DBInterface dbInterface, String tableNamePrefix) {
						return new GatlytronDBInterface(dbInterface, tableNamePrefix);
					}
				}
    		);
    	
    	

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
				.baseUrl(URL_BASE)
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