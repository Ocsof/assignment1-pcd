package ass01.concurrent;


import ass01.concurrent.control.MasterAgent;
import ass01.concurrent.control.MasterAgentWithGui;
import ass01.concurrent.model.Chronometer;
import ass01.concurrent.model.ChronometerImpl;
import ass01.concurrent.view.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class ConcurrentBodySimulationMain {

    public static void main(String[] args){

    	SimulationView viewer = new SimulationView(620,620);

        int nSteps = 10000;
        int nBody = 2000;
        int nWorkers = Runtime.getRuntime().availableProcessors() + 1;
        //int nWorkers = 9;
        Chronometer chrono = new ChronometerImpl();
        chrono.start();
        MasterAgentWithGui master = new MasterAgentWithGui(nSteps, nBody, nWorkers, viewer); //versione con view
        //MasterAgent master = new MasterAgent(nSteps, nBody, nWorkers);  //versione senza view
        master.start();
        try {
            master.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        chrono.stop();
        System.out.println("Time elapsed: " + chrono.getTime() + "ms");
        //new MasterAgent(nSteps, nBody, nWorkers).start();  //versione senza view
    }
}
