import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;

class Canvas extends JPanel implements Observer{
    private DrawingModel model;
    private double x = 0;
    private double y = 0;


    Canvas(DrawingModel model) {
        this.model = model;
        this.setBackground(Color.WHITE);
        this.setPreferredSize(new Dimension(780, 520));

        this.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                model.enddrag(e.getX(), e.getY());
            }

            public void mousePressed(MouseEvent e) {
                x = e.getX();
                y = e.getY();
            }

            public void mouseClicked(MouseEvent e) {
                model.select(e.getX(), e.getY());
            }
        });


        this.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                if(model.s == -1) {
                    model.drawing(e.getX(), e.getY());
                } else {
                    x = e.getX() - x;
                    y = e.getY() - y;
                    model.moveshape(x, y);
                    x = e.getX();
                    y = e.getY();
                }
            }
        });

    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g; // cast to get 2D drawing methods
        if(model.s != -1) {
            g2.setColor(Color.YELLOW);
            g2.setStroke(new BasicStroke(6));
            model.shapes.get(model.s).draw(g2);
        }

        for (DrawingModel.Shape s: model.shapes) {
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            s.draw(g2);
        }

    }

    @Override
    public void update(Observable arg0, Object arg1) {
        repaint();
    }

}




