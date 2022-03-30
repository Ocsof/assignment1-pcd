package ass01.concurrent.view;

import ass01.concurrent.control.Flag;
import ass01.concurrent.model.Body;
import ass01.concurrent.model.Boundary;
import ass01.concurrent.model.Point2D;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

/**
 * Simulation view
 *
 * @author Francesco Foschini & Davide Alpi
 *
 */
public class SimulationView {
        
	private final SimulationFrame frame;
    private final Flag stopFlag;
	
    /**
     * Creates a view of the specified size (in pixels)
     * 
     * @param width
     * @param height
     */
    public SimulationView(final int width, final int height){
        this.stopFlag = new Flag();
        this.frame = new SimulationFrame(width,height,this.stopFlag);
    }
        
    public void display(final ArrayList<Body> bodies, final double vt, final long iter, final Boundary bounds){
 	   this.frame.display(bodies, vt, iter, bounds);
    }

    public Flag getStopFlag(){
        return this.stopFlag;
    }
    
    
}
