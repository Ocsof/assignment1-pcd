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

    	SimulationView viewer = new SimulationView(620,620);

        int nSteps = 1000;
        int nBody = 5000;
        int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
        //int nWorkers = 9;
        new MasterAgent(nSteps, nBody, nWorkers, viewer).start(); //versione con view
        //new MasterAgent(nSteps, nBody, nWorkers).start();  //versione senza view
    }
}
