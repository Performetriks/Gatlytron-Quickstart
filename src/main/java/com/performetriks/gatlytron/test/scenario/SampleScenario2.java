package com.performetriks.gatlytron.test.scenario;
 
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.performetriks.gatlytron.base.GatlytronScenario;
import com.performetriks.gatlytron.test.settings.TestGlobals;
 
public class SampleScenario2 extends GatlytronScenario {
  
    public static final String SCENARIO_NAME = "Website";
    public static final String URL = TestGlobals.ENV.url + "";
  
    /***************************************************************************
     *
     ***************************************************************************/
    public SampleScenario2() {
        super(SCENARIO_NAME);
      
        this
            .feederBuilder(TestGlobals.getDataFeeder())
            .scenarioSteps(
                exec(
                	  http("Open Homepage").get(URL)
                	, http("Load Dashboard").get(URL)
                )
        );
 
    }

}