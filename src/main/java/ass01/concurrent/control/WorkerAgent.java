package ass01.concurrent.control;

import ass01.concurrent.model.Body;
import ass01.concurrent.model.Boundary;
import ass01.concurrent.model.Vector2D;

import java.util.ArrayList;

public class WorkerAgent extends Thread{
    private final ArrayList<Body> bodies; /* bodies in the field */
    private final Boundary bounds; /* boundary of the field */
    private final Latch synchLatch;
    private final Barrier barrier;
    private final int indexFrom;
    private final int indexTo;
    private final double DT; /* virtual time step */

    public WorkerAgent(final ArrayList<Body> bodies, final int indexFrom, final int indexTo,
                       final Boundary bounds, final double DT,
                       final Latch synchLatch, final Barrier barrier) {
        this.bodies = bodies;
        this.bounds = bounds;
        this.synchLatch = synchLatch;
        this.barrier = barrier;
        this.indexFrom = indexFrom;
        this.indexTo = indexTo;
        this.DT = DT;
    }

    public void run() {
        //occhio al <= deve prendere anche l'ultimo
        for (int i = this.indexFrom; i <= this.indexTo; i++) { /* update bodies velocity */
            Body body = this.bodies.get(i);
            Vector2D totalForce = computeTotalForceOnBody(body);
            /* compute instant acceleration */
            Vector2D acc = new Vector2D(totalForce).scalarMul(1.0 / body.getMass());
            body.updateVelocity(acc, this.DT); /* update velocity */
        }
        try {
            this.barrier.hitAndWaitAll();
            for (int i = this.indexFrom; i <= this.indexTo; i++) {
                this.bodies.get(i).updatePos(this.DT);
                this.bodies.get(i).checkAndSolveBoundaryCollision(this.bounds);
            }
            this.synchLatch.notifyCompletion(); //notifica il master agent che ha finito
        } catch (InterruptedException e) {
            log("Interrupted!");
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

    private void log(String msg) { //logger: si può fare meglio
        synchronized(System.out) {
            System.out.println("[ "+getName()+" ] "+msg);
        }
    }
}
