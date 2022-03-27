package ass01.concurrent.control;

import ass01.concurrent.model.*;
import ass01.concurrent.view.SimulationView;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

	private final SimulationView viewer;
	private MasterAgent masterAgent;
	private final Chronometer chronometer;

	public Controller(SimulationView viewer) {
		this.viewer = viewer;
		this.chronometer = new ChronometerImpl();
	}
	
	public void execute(long nSteps) {
		this.masterAgent = new MasterAgent(this.viewer, nSteps);
		this.chronometer.start();
		this.masterAgent.start();
		this.chronometer.stop();
		System.out.println("Time elapsed: " + this.chronometer.getTime() + "ms");
	}

}
