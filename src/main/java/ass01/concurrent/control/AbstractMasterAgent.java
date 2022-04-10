package ass01.concurrent.control;

import ass01.concurrent.model.*;

public abstract class AbstractMasterAgent extends AbstractSimulator{

    private final int nWorkers;
    private Latch synchLatch; //latch utilizzato per sincronizzare Master e Worker per l'aggiornamento della view (Master alla fine del ciclo)
    private Barrier barrier; //barrier utilizzato per sincronizzare i worker tra di loro
    private final double DT = 0.001; /* virtual time step */

    public AbstractMasterAgent(final int numBodies, final int nSteps, final int nWorkers) {
        super(numBodies, nSteps);
        this.nWorkers = nWorkers;
    }

    @Override
    public void run() {
        int iter = 0;
        /* simulation loop */
        while (iter < this.getnSteps()) {
            this.createLatch();
            this.createBarrier();
            this.createAndStartWorkers();
            try {
                this.getLatch().waitCompletion();
                iter++;
                this.manageGUI(iter);
            } catch (InterruptedException e) {
                log("interrupted");
                //viewer.changeState("Interrupted"); //capire come gestire sta cosa a livello view
            }
        }
    }

    protected void createAndStartWorkers(){
        int indexFrom = 0;
        int indexTo;
        int numberOfBodies = this.getBodies().size();
        int indexIncrement = numberOfBodies / this.nWorkers;

        for (int i = 0; i < this.nWorkers-1; i++){
            indexTo = (indexFrom + indexIncrement - 1);
            WorkerAgent worker = new WorkerAgent(this.getBodies(), indexFrom, indexTo,
                    this.getBounds(), this.DT, this.synchLatch, this.barrier);
            worker.start();
            indexFrom = indexFrom + indexIncrement;
        }
        //TODO: da togliere e mettere dentro il for
        WorkerAgent worker = new WorkerAgent(this.getBodies(), indexFrom, (numberOfBodies - 1),
                this.getBounds(), this.DT, this.synchLatch, this.barrier);
        worker.start();
    }

    protected abstract void manageGUI(final int iter);  //templateMethod

    protected void createLatch(){
        this.synchLatch = new LatchImpl(this.nWorkers);
    }

    protected void createBarrier(){
        this.barrier = new BarrierImpl(this.nWorkers);
    }

    protected Latch getLatch(){
        return this.synchLatch;
    }

    protected double getDT(){
        return this.DT;
    }

}
