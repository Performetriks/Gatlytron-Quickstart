package com.performetriks.gatlytron.test.simulation;
 
import java.time.Duration;

import com.performetriks.gatlytron.base.Gatlytron;
import com.performetriks.gatlytron.test.scenario.SampleScenario1;
import com.performetriks.gatlytron.test.scenario.SampleScenario2;
import com.performetriks.gatlytron.test.settings.TestGlobals;

import io.gatling.javaapi.core.Simulation;
 
 
public class SimulationLoadAverage extends Simulation {
 
    private static final Duration TEST_DURATION = Duration.ofMinutes(15);
 
    {
    	Gatlytron.setSimulationName(this.getClass().getSimpleName());
    	TestGlobals.commonInitialization();
    	
        //======================================================================
        // Average Load Example Scenario
        //======================================================================
        setUp(
               new SampleScenario1().buildStandardLoad(20, 6000, 0, 2)
             , new SampleScenario2().buildStandardLoad(10, 3000, 0, 2)
             //, new SampleScenario2().buildScenario(0).injectOpen(...) // Example of a Custom Scenario with 0 pacing
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