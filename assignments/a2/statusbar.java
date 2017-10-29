import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;


class Statusbar extends JPanel implements Observer {
    private DrawingModel model;

    private JLabel label1;
    private JLabel label2;
//constructor
    Statusbar(DrawingModel model) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.GRAY);

        this.model =  model;

        label1 = new JLabel("0 Stroke");

        label2 = new JLabel("");

        this.add(label1);
        this.add(label2);
    }
//update value when something changed
    @Override
    public void update(Observable o, Object arg) {
        label1.setText(model.shapes.size() + " Stroke");
        if(model.s != -1) {
            String s = ", Selection(" + Integer.toString(model.shapes.get(model.s).nPoints) + " points, scale: " + Double.toString(((double) model.shapes.get(model.s).scale) / 100) + ", rotation " + Integer.toString(model.shapes.get(model.s).rotate) + ")";
            label2.setText(s);
        } else {
            label2.setText("");
        }
    }


}
