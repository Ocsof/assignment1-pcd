package ass01.concurrent.control;

import ass01.concurrent.model.*;
import ass01.concurrent.view.SimulationView;

import java.util.ArrayList;
import java.util.Random;

public class MasterAgent extends Thread{

    private final SimulationView viewer;
    private ArrayList<Body> bodies; /* bodies in the field */
    private Boundary bounds; /* boundary of the field */
    private final Chronometer chronometer;

    private final int nWorkers;
    private Latch synchLatch; //latch utilizzato per sincronizzare Master e Worker per l'aggiornamento della view (Master alla fine del ciclo)
    private Barrier barrier; //barrier utilizzato per sincronizzare i worker tra di loro
    private Flag stopFlag;

    private long nSteps;
    private double vt; /* virtual time */
    private static final double DT = 0.001; /* virtual time step */

    public MasterAgent(SimulationView viewer, long nSteps, Flag stopFlag){
        this.viewer = viewer;
        this.nWorkers = Runtime.getRuntime().availableProcessors() + 1;
        this.nSteps = nSteps;
        this.chronometer = new ChronometerImpl();
        this.stopFlag = stopFlag;
        /* initializing boundary and bodies */
        this.generateBodies(100); //100, 5000
    }

    public void run(){
        this.vt = 0; /* init virtual time */
        long iter = 0;

        this.chronometer.start();
        /* simulation loop */
        while (iter < this.nSteps) {
            this.synchLatch = new LatchImpl(this.nWorkers);
            this.barrier = new BarrierImpl(this.nWorkers);
            int indexFrom = 0;
            int indexTo;
            int numberOfBodies = this.bodies.size();
            int indexIncrement = numberOfBodies / this.nWorkers;

            for (int i = 0; i < this.nWorkers-1; i++){
                indexTo = (indexFrom + indexIncrement - 1);
                WorkerAgent worker = new WorkerAgent(this.bodies, indexFrom, indexTo,
                                                     this.bounds, this.DT, this.synchLatch, this.barrier);
                worker.start();
                indexFrom = indexFrom + indexIncrement;
            }
            //TODO: da togliere e mettere dentro il for
            WorkerAgent worker = new WorkerAgent(this.bodies, indexFrom, (numberOfBodies - 1),
                                                 this.bounds, this.DT, this.synchLatch, this.barrier);
            worker.start();


            try {
                this.synchLatch.waitCompletion();
                this.vt = this.vt + this.DT; /* update virtual time */
                iter++;
                this.stopFlag.waitWhile(true);
                this.viewer.display(this.bodies, this.vt, iter, this.bounds); /* display current stage */
            } catch (InterruptedException e) {
                log("interrupted");
                //viewer.changeState("Interrupted"); //capire come gestire sta cosa a livello view
            }

        }
        this.chronometer.stop();
        System.out.println("Time elapsed: " + this.chronometer.getTime() + "ms");
    }

    private void generateBodies(int nBodies) {
        this.bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
        Random rand = new Random(System.currentTimeMillis());
        this.bodies = new ArrayList<>();
        for (int i = 0; i < nBodies; i++) {
            double x = this.bounds.getX0() * 0.25 + rand.nextDouble() * (this.bounds.getX1() - this.bounds.getX0()) * 0.25;
            double y = this.bounds.getY0() * 0.25 + rand.nextDouble() * (this.bounds.getY1() - this.bounds.getY0()) * 0.25;
            Body b = new Body(i, new Point2D(x, y), new Vector2D(0, 0), 10);
            this.bodies.add(b);
        }
    }

    private void log(String msg){  //logger per eventuali stampe: da migliorare
        synchronized(System.out){
            System.out.println("[ master ] " + msg);
        }
    }
}
