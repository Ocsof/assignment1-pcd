package ass01.concurrent.control;

import ass01.concurrent.model.*;
import ass01.concurrent.view.SimulationView;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

	private final SimulationView viewer;
	private MasterAgent masterAgent;

	public Controller(SimulationView viewer) {
		this.viewer = viewer;
	}
	
	public void execute(long nSteps) {
		this.masterAgent = new MasterAgent(this.viewer, nSteps, this.viewer.getStopFlag());
		this.masterAgent.start();
	}

}
