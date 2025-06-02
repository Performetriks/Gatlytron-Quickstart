package com.performetriks.gatlytron.test.simulation;
 
import java.time.Duration;

import com.performetriks.gatlytron.base.Gatlytron;
import com.performetriks.gatlytron.test.scenario.SampleScenario1;
import com.performetriks.gatlytron.test.scenario.SampleScenario2;
import com.performetriks.gatlytron.test.scenario.SampleScenario3CustomReporting;
import com.performetriks.gatlytron.test.settings.TestGlobals;

import io.gatling.javaapi.core.Simulation;
 
 
public class SimulationCheckDebug extends Simulation {
 
    private static final Duration TEST_DURATION = Duration.ofSeconds(60);

    {
    	
    	Gatlytron.setSimulationName(this.getClass().getSimpleName());
    	TestGlobals.commonInitialization();
    	Gatlytron.setDebug(true);
    	
        //======================================================================
        // Use this simulation class for testing and debugging.
        // This is done to have an easy way to develop while not messing
        // up the real thing. Feel free to do your worst in this class.
        // (I hope I won't regret writting this)
        //======================================================================
       
    	setUp(
                new SampleScenario1().buildStandardLoad(10, 600, 0, 2)
                , new SampleScenario2().buildStandardLoad(10, 600, 0, 2)
                , new SampleScenario3CustomReporting().buildStandardLoad(10, 600, 0, 2)
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
 