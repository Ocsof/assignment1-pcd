package ass01.concurrent.control;

import ass01.concurrent.model.Flag;
import ass01.concurrent.view.SimulationView;

public class MasterAgentWithGui extends AbstractMasterAgent{

    private final Flag stopFlag;
    private double vt; /* virtual time */
    private final SimulationView viewer;

    public MasterAgentWithGui(final int nSteps, final int numBodies, final int nWorkers, final SimulationView viewer) {
        super(numBodies, nSteps, nWorkers);
        this.viewer = viewer;
        this.stopFlag = this.viewer.getStopFlag();
    }

    @Override
    public void run() {
        this.vt = 0; /* init virtual time */
        long iter = 0;
        /* simulation loop */
        while (iter < this.getnSteps()) {
            this.createLatch();
            this.createBarrier();
            this.createAndStartWorkers();
            try {
                this.getLatch().waitCompletion();
                this.vt = this.vt + this.getDT(); /* update virtual time */
                iter++;
                this.stopFlag.waitWhile(true);
                this.viewer.display(this.getBodies(), this.vt, iter, this.getBounds()); /* display current stage */
            } catch (InterruptedException e) {
                log("interrupted");
                //viewer.changeState("Interrupted"); //capire come gestire sta cosa a livello view
            }
        }
    }
}
