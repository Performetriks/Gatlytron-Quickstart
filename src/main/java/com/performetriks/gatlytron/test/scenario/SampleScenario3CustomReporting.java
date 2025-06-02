package com.performetriks.gatlytron.test.scenario;
 
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.performetriks.gatlytron.base.GatlytronScenario;
import com.performetriks.gatlytron.injection.InjectedDataReceiver;
import com.performetriks.gatlytron.stats.GatlytronRecordRaw.GatlytronRecordType;
import com.performetriks.gatlytron.test.settings.TestGlobals;

import io.gatling.javaapi.core.Session;
 
public class SampleScenario3CustomReporting extends GatlytronScenario {
  
    public static final String SCENARIO_NAME = "CustomReporting";
    public static final String URL = TestGlobals.ENV.url + "";
  
    /***************************************************************************
     *
     ***************************************************************************/
    public SampleScenario3CustomReporting() {
        super(SCENARIO_NAME);
      
        this
            .feederBuilder(TestGlobals.getDataFeeder())
            .scenarioSteps(
                exec(
                	  http("Report").get(URL)
                )
                .exec(session -> {
                	int customValue = (int)Math.floor(Math.random() * 120);
                	boolean isSuccess = (Math.random() > 0.2f) ? true : false;
                	reportCustomValue(session, "MyCustomValue", customValue , isSuccess);
                	return session;
                })
        );
 
    }
    
    /***************************************************************************
     * An example that shows how you can report your own values into the 
     * reporting engine.
     * The reported values will be aggregated and sent to all the reporters
     * you have registered.
     ***************************************************************************/
    private static void reportCustomValue(Session session, String metricName, int metricValue, boolean isSuccess) {

    	String status = (isSuccess) ? "OK" : "KO";
    	
    	long currentTime = System.currentTimeMillis();
    	InjectedDataReceiver.createRecord(
				  GatlytronRecordType.REQUEST
				, session.scenario()
				, session.groups()
				, metricName
				, currentTime
				, currentTime
				, status
				, "200"
				, "NoMessage"
				, metricValue
			);
    }

}