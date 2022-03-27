package ass01.concurrent;


import ass01.concurrent.control.Controller;
import ass01.concurrent.view.SimulationView;

/**
 * Bodies simulation - legacy code: sequential, unstructured
 * 
 * @author aricci
 */
public class ConcurrentBodySimulationMain {

    public static void main(String[] args) {
                
    	SimulationView viewer = new SimulationView(620,620);

    	Controller sim = new Controller(viewer);
        sim.execute(5000);
    }
}
