package ass01.concurrent.control;

import ass01.concurrent.model.Body;
import ass01.concurrent.model.Boundary;
import ass01.concurrent.model.Point2D;
import ass01.concurrent.model.Vector2D;
import ass01.concurrent.view.SimulationView;

import java.util.ArrayList;
import java.util.Random;

public class MasterAgent extends Thread{

    private final SimulationView viewer;
    private ArrayList<Body> bodies; /* bodies in the field */
    private Boundary bounds; /* boundary of the field */

    private final int nWorkers;
    private final TaskCompletionLatch synchTasks;

    private long nSteps;
    private double vt; /* virtual time */
    private static final double DT = 0.001; /* virtual time step */

    public MasterAgent(SimulationView viewer, long nSteps){
        this.viewer = viewer;
        this.nWorkers = Runtime.getRuntime().availableProcessors() + 1;
        this.synchTasks = new TaskCompletionLatch(nWorkers);
        this.nSteps = nSteps;
        /* initializing boundary and bodies */
        this.generateBodies(1000); //100, 5000
    }

    public void run(){
        this.vt = 0; /* init virtual time */
        long iter = 0;

        /* simulation loop */
        while (iter < this.nSteps) {
            for (int i = 0; i < this.bodies.size(); i++) { /* update bodies velocity */
                Body body = this.bodies.get(i);
                Vector2D totalForce = computeTotalForceOnBody(body);
                /* compute instant acceleration */
                Vector2D acc = new Vector2D(totalForce).scalarMul(1.0 / body.getMass());
                body.updateVelocity(acc, this.DT); /* update velocity */
            }
            for (Body b : this.bodies) { /* compute bodies new pos */
                b.updatePos(this.DT);
            }
            for (Body b : this.bodies) { /* check collisions with boundaries */
                b.checkAndSolveBoundaryCollision(this.bounds);
            }
            this.vt = this.vt + this.DT; /* update virtual time */
            iter++;
            this.viewer.display(this.bodies, this.vt, iter, this.bounds); /* display current stage */
        }
    }

    private Vector2D computeTotalForceOnBody(Body b) {
        Vector2D totalForce = new Vector2D(0, 0);
        /* compute total repulsive force */
        for (int j = 0; j < this.bodies.size(); j++) {
            Body otherBody = this.bodies.get(j);
            if (!b.equals(otherBody)) {
                try {
                    Vector2D forceByOtherBody = b.computeRepulsiveForceBy(otherBody);
                    totalForce.sum(forceByOtherBody);
                } catch (Exception ex) {
                }
            }
        }
        /* add friction force */
        totalForce.sum(b.getCurrentFrictionForce());
        return totalForce;
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
}
