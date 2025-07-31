package com.performetriks.gatlytron.test.simulation;
 
import com.performetriks.gatlytron.base.Gatlytron;
import com.performetriks.gatlytron.test.scenario.SampleScenario1;
import com.performetriks.gatlytron.test.scenario.SampleScenario4Datacheck;
import com.performetriks.gatlytron.test.settings.TestGlobals;

import io.gatling.javaapi.core.Simulation;
 
 
public class SimulationCheckData extends Simulation {
	
    {
    	Gatlytron.setSimulationName(this.getClass().getSimpleName());
    	TestGlobals.commonInitialization();
    	
        //======================================================================
        // Runs every script once, useful to check if all the scripts are working.
        //======================================================================
        setUp(
            new SampleScenario4Datacheck().buildDatacheck()
        ).protocols(TestGlobals.getProtocol())
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