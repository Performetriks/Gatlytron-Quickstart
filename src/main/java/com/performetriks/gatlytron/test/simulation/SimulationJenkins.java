package com.performetriks.gatlytron.test.simulation;

import java.time.Duration;

import com.performetriks.gatlytron.base.Gatlytron;
import com.performetriks.gatlytron.test.scenario.SampleScenario1;
import com.performetriks.gatlytron.test.scenario.SampleScenario2;
import com.performetriks.gatlytron.test.settings.TestGlobals;
import com.performetriks.gatlytron.test.settings.TestGlobals.Environment;

import ch.qos.logback.classic.Level;
import io.gatling.javaapi.core.Simulation;


public class SimulationJenkins extends Simulation {

    //======================================================================
    // This class can be used to start load test from Jenkins.
	// It is referred by the Jenkinsfile and uses parameters defined in
	// the pipeline.
    //======================================================================
    
    {

    	//--------------------------------------
    	// Get Jenkins Parameters
        int percent = Integer.getInteger("Workload", 10);
        
        String logLevelString = System.getProperty("loglevel", "INFO");
        Level logLevel = Level.toLevel(logLevelString);
        
        int durationMinutes = Integer.getInteger("testDurationInMinutes", 1);
        Duration TEST_DURATION = Duration.ofMinutes(durationMinutes);
       
        String environment = System.getProperty("Environment", "DEV");
        Environment enviroment = Environment.valueOf(environment);

    	//--------------------------------------
    	// Print Parameters
        System.out.println("============== Jenkins Parameters ==============");
        System.out.println("Environment:       " + environment);
        System.out.println("Workload Percent:  " + percent);
        System.out.println("Duration Minutes:  " + durationMinutes);
        System.out.println("Log Level:         " + logLevel);
        System.out.println("================================================");
        
    	//--------------------------------------
    	// Setup Test
    	Gatlytron.setSimulationName(this.getClass().getSimpleName());
        TestGlobals.commonInitialization(enviroment);

        Gatlytron.setLogLevelRoot(logLevel);
        Gatlytron.setLogLevel(logLevel, "io.gatling.http.engine.response"); // logging level for requests and response

    	//--------------------------------------
    	// Setup Simulation
        setUp(
        	  new SampleScenario1()   .buildStandardLoad(percent, 10, 6000, 0, 2)
            , new SampleScenario2().buildStandardLoad(percent, 10, 600 , 0, 2)
            
        ).protocols(TestGlobals.getProtocol())
         .maxDuration(TEST_DURATION)
        ;


    }
    
    /********************************************************************
     * 
     *********************************************************************/
    @Override
    public void after() {
      TestGlobals.commonTermination();
    }
}
