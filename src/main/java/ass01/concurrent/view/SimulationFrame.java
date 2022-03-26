package ass01.concurrent.view;

import ass01.concurrent.model.Body;
import ass01.concurrent.model.Boundary;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

public class SimulationFrame extends JFrame {
    private SimulationPanel panel;

    public SimulationFrame(final int width, final int height){
        setTitle("Bodies Simulation");
        setSize(width,height);
        setResizable(false);
        panel = new SimulationPanel(width,height);
        getContentPane().add(panel);
        addWindowListener(new WindowAdapter(){
            public void windowClosing(WindowEvent ev){
                System.exit(-1);
            }
            public void windowClosed(WindowEvent ev){
                System.exit(-1);
            }
        });
        this.setVisible(true);
    }

    public void display(ArrayList<Body> bodies, double vt, long iter, Boundary bounds){
        try {
            SwingUtilities.invokeLater(() -> {  //todo è ok oppure andava lasciato invokeAndWait??
                panel.display(bodies, vt, iter, bounds);
                repaint();
            });
        } catch (Exception ex) {}
    };

    public void updateScale(double k) {
        panel.updateScale(k);
    }
}

