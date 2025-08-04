package com.performetriks.gatlytron.test.scenario;
 
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.group;
import static io.gatling.javaapi.http.HttpDsl.http;

import java.util.HashMap;
import java.util.Map;

import com.performetriks.gatlytron.base.GatlytronScenario;
import com.performetriks.gatlytron.test.settings.TestGlobals;
 
public class SampleScenario1 extends GatlytronScenario {
  
    public static final String SCENARIO_NAME = "API.callInterface";
    public static final String URL_API = TestGlobals.ENV.url + "";
  
    /***************************************************************************
     *
     ***************************************************************************/
    public SampleScenario1() {
        super(SCENARIO_NAME);
      
        this
        	//.debug(TestGlobals.DEBUG) // default is obtained from Gatlytron.isDebug()
            .feederBuilder(TestGlobals.getDataFeeder())
            .scenarioSteps(
            	group("My Test").on(
            		group("Sub.group").on(
		                exec( 
			                http("fetchInfo")
			                        .get(URL_API)
			                        //.body(ElFileBody("postbody.json")) //.asJson()
			                        //.headers(getHeader())
			                        //.check(bodyString().saveAs("responseBody"))
			                )
		                , http("testError").get("https://does.not.exist/fails?search=#{searchInput}")
	            	)
            	)
        );
 
    }
 
    public Map<CharSequence, String> getHeader(){
        Map<CharSequence, String> headers = new HashMap<>();
        headers.put("Accept", "application/json");
        headers.put("Accept-Encoding", "gzip, deflate, br, zstd");
        headers.put("Cache-Control", "no-cache");
        headers.put("Content-Type", "application/json");
        headers.put("Origin", TestGlobals.ENV.url );
        headers.put("x-dynatrace", TestGlobals.ENV.getXDynatraceHeader());
        
        //headers_space.put("authorization", "Bearer "+ TestGlobals.token);
 
        return headers;
    }
 
}