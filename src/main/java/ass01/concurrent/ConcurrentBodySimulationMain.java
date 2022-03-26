package ass01.concurrent;


import ass01.concurrent.control.Simulator;
import ass01.concurrent.view.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class ConcurrentBodySimulationMain {

    public static void main(String[] args) {
                
    	SimulationView viewer = new SimulationView(620,620);

    	Simulator sim = new Simulator(viewer);
        sim.execute(50000);
    }
}
