package ass01.concurrent.control;

public class MasterAgent extends AbstractMasterAgent{


    public MasterAgent(int nSteps, int numBodies, int nWorkers) {
        super(numBodies, nSteps, nWorkers);
    }

    @Override
    public void run() {
        long iter = 0;
        /* simulation loop */
        while (iter < this.getnSteps()) {
            this.createLatch();
            this.createBarrier();
            this.createAndStartWorkers();
            try {
                this.getLatch().waitCompletion();
                iter++;
            } catch (InterruptedException e) {
                log("interrupted");
                //viewer.changeState("Interrupted"); //capire come gestire sta cosa a livello view
            }
        }
    }
}
