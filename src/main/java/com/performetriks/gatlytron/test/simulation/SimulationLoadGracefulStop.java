package com.performetriks.gatlytron.test.simulation;
 
import java.time.Duration;

import com.performetriks.gatlytron.base.Gatlytron;
import com.performetriks.gatlytron.test.scenario.SampleScenario1;
import com.performetriks.gatlytron.test.scenario.SampleScenario2;
import com.performetriks.gatlytron.test.settings.TestGlobals;

import io.gatling.javaapi.core.Simulation;
 
 
public class SimulationLoadGracefulStop extends Simulation {
 
	private static final Duration SCENARIO_DURATION = Duration.ofMinutes(1);
    private static final Duration GRACEFUL_DURATION = SCENARIO_DURATION.plusMinutes(1); // add one minute to let user execution finish before stopping execution
 
    {
    	Gatlytron.setSimulationName(this.getClass().getSimpleName());
    	TestGlobals.commonInitialization();
    	
        //======================================================================
        // Example Scenario on how to make a Test Stop Gracefully
        //======================================================================
        setUp(
               new SampleScenario1().buildStandardLoad(1, 1000, 0, 2, SCENARIO_DURATION)
             , new SampleScenario2().buildStandardLoad(10, 3000, 0, 2, SCENARIO_DURATION)
        ).protocols( TestGlobals.getProtocol() )
         .maxDuration( GRACEFUL_DURATION )
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