package ass01.concurrent.control;

import ass01.concurrent.model.Vector2D;
import ass01.concurrent.model.Body;
import ass01.concurrent.model.Boundary;
import ass01.concurrent.model.Point2D;
import ass01.concurrent.view.SimulationView;

import java.util.ArrayList;
import java.util.Random;

public class Simulator {

	private SimulationView viewer;

	/* bodies in the field */
	ArrayList<Body> bodies;

	/* boundary of the field */
	private Boundary bounds;

	/* virtual time */
	private double vt;

	/* virtual time step */
	double dt;

	public Simulator(SimulationView viewer) {
		this.viewer = viewer;

		/* initializing boundary and bodies */
		this.generateBodies(1000); //100, 5000

	}
	
	public void execute(long nSteps) {

		/* init virtual time */

		vt = 0;
		dt = 0.001;

		long iter = 0;

		/* simulation loop */

		while (iter < nSteps) {

			/* update bodies velocity */

			for (int i = 0; i < bodies.size(); i++) {
				Body b = bodies.get(i);

				/* compute total force on bodies */
				Vector2D totalForce = computeTotalForceOnBody(b);

				/* compute instant acceleration */
				Vector2D acc = new Vector2D(totalForce).scalarMul(1.0 / b.getMass());

				/* update velocity */
				b.updateVelocity(acc, dt);
			}

			/* compute bodies new pos */

			for (Body b : bodies) {
				b.updatePos(dt);
			}

			/* check collisions with boundaries */

			for (Body b : bodies) {
				b.checkAndSolveBoundaryCollision(bounds);
			}

			/* update virtual time */

			vt = vt + dt;
			iter++;

			/* display current stage */

			viewer.display(bodies, vt, iter, bounds);

		}
	}

	private Vector2D computeTotalForceOnBody(Body b) {

		Vector2D totalForce = new Vector2D(0, 0);

		/* compute total repulsive force */

		for (int j = 0; j < bodies.size(); j++) {
			Body otherBody = bodies.get(j);
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
		bounds = new Boundary(-6.0, -6.0, 6.0, 6.0);
		Random rand = new Random(System.currentTimeMillis());
		bodies = new ArrayList<>();
		for (int i = 0; i < nBodies; i++) {
			double x = bounds.getX0() * 0.25 + rand.nextDouble() * (bounds.getX1() - bounds.getX0()) * 0.25;
			double y = bounds.getY0() * 0.25 + rand.nextDouble() * (bounds.getY1() - bounds.getY0()) * 0.25;
			Body b = new Body(i, new Point2D(x, y), new Vector2D(0, 0), 10);
			bodies.add(b);
		}
	}
	
	

}
