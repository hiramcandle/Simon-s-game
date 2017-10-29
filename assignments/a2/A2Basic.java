import javax.swing.*;
import java.awt.*;

//I kindly changed the background of two bars into GREY, this looks better :)
//You don't need vecmath.jar to compile it.

//IMPORTANT
//I think it's not easy to select a stroke as required. So I make it a bit easier : )

public class A2Basic {

    public static void main(String[] args) {
        JFrame frame = new JFrame("A2Basic");
        DrawingModel model = new DrawingModel();

        Toolbar toolbar = new Toolbar(model);
        Canvas canvas = new Canvas(model);
        JScrollPane canvas_pane = new JScrollPane(canvas);
        Statusbar statusbar = new Statusbar(model);


        model.addObserver(toolbar);
        model.addObserver(canvas);
        model.addObserver(statusbar);

        model.notifyObservers();

        JPanel p = new JPanel(new BorderLayout(5, 0)); //which contains all views
        p.setBackground(Color.GRAY);
        p.add(toolbar, BorderLayout.NORTH);
        p.add(canvas_pane, BorderLayout.CENTER);
        p.add(statusbar, BorderLayout.SOUTH);
        frame.getContentPane().add(p);

        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

    }
}






