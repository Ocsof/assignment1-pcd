package ass01.concurrent;


import ass01.concurrent.control.MasterAgent;
import ass01.concurrent.view.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class ConcurrentBodySimulationMain {

    public static void main(String[] args) {

    	//SimulationView viewer = new SimulationView(620,620);

        int nSteps = 50000;
        int nBody = 100;
        //new MasterAgent(nSteps, nBody, viewer).start();
        new MasterAgent(nSteps, nBody).start();
    }
}
