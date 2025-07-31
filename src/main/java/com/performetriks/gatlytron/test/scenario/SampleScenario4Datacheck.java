package com.performetriks.gatlytron.test.scenario;
 
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.performetriks.gatlytron.base.GatlytronScenario;
import com.performetriks.gatlytron.injection.InjectedDataReceiver;
import com.performetriks.gatlytron.stats.GatlytronRecordRaw.GatlytronRecordType;
import com.performetriks.gatlytron.test.settings.TestGlobals;

import io.gatling.javaapi.core.Session;
 
public class SampleScenario4Datacheck extends GatlytronScenario {
  
    public static final String SCENARIO_NAME = "Datacheck";
    public static final String URL = TestGlobals.ENV.url + "";
  
    /***************************************************************************
     *
     ***************************************************************************/
    public SampleScenario4Datacheck() {
        super(SCENARIO_NAME);
      
        this
            .feederBuilder(TestGlobals.getDataFeeder())
            .scenarioSteps(
        		exec(
                  	  http("Report").get(URL)
                  )
                  .exec(session -> {

                	String username = session.getString("username");
                	String password = session.getString("password");
                	String searchInput = session.getString("searchInput");
                	
                	System.out.println("========= Feeder data ==========");
                	System.out.println("username = '"+username+"'");
                	System.out.println("password = '"+password+"'");
                	System.out.println("searchInput = '"+searchInput+"'");
                	
                	return session;

                })
        );
 
    }

}