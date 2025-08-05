package com.performetriks.gatlytron.test.scenario;
 
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.performetriks.gatlytron.base.GatlytronScenario;
import com.performetriks.gatlytron.test.settings.TestGlobals;
 
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
        	.feederBuilder(TestGlobals.getCustomFeeder())
            .scenarioSteps(
            	// feed(TestGlobals.getDataFeeder())
                //.feed(TestGlobals.getCustomFeeder())
        		exec(
                  	  http("Report").get(URL)
                  )
                  .exec(session -> {

                	String username = session.getString("username");
                	String password = session.getString("password");
                	String searchInput = session.getString("searchInput");
                	
                	String firstname = session.getString("firstname");
                	String lastname = session.getString("lastname");
                	String address = session.getString("address");
                	String city = session.getString("city");
                	String country = session.getString("country");
                	String phone = session.getString("phone");
                	String email = session.getString("email");
                	String birthday = session.getString("birthday");
                	String description = session.getString("description");
                	String status = session.getString("status");
                                	
                	System.out.println("========= Feeder data ==========");
                	System.out.println("username = '"+username+"'");
                	System.out.println("password = '"+password+"'");
                	System.out.println("searchInput = '"+searchInput+"'");
                	
                	System.out.println("firstname = '"+firstname+"'");
                	System.out.println("lastname = '"+lastname+"'");
                	System.out.println("address = '"+address+"'");
                	System.out.println("city = '"+city+"'");
                	System.out.println("country = '"+country+"'");
                	System.out.println("phone = '"+phone+"'");
                	System.out.println("email = '"+email+"'");
                	System.out.println("birthday = '"+birthday+"'");
                	System.out.println("description = '"+description+"'");
                	System.out.println("status = '"+status+"'");
                	
                	return session;

                })
        );
 
    }

}