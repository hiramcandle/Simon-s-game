import javax.swing.*;
import java.awt.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.*;
import java.util.Observable;
import java.util.Observer;


class Toolbar extends JPanel implements Observer {
    private DrawingModel model;

    private JButton button;
    private JSlider scale;
    private JSlider rotate;
    private JLabel label1;
    private JLabel label2;
    private JLabel labelscale;
    private JLabel labelrotate;
// constructor
    Toolbar(DrawingModel model) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.setBackground(Color.GRAY);

        this.model =  model;


        button = new JButton("Delete");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.delete();
            }
        });


        label1 = new JLabel("Scale");


        label2 = new JLabel("Rotate");

        labelscale = new JLabel("1");


        labelrotate = new JLabel("0");

        scale = new JSlider(JSlider.HORIZONTAL, 50, 200, 100);
        scale.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider s = (JSlider) e.getSource();
                labelscale.setText(Double.toString((double) s.getValue() / 100));
                model.scale(s.getValue());
            }
        });



        rotate = new JSlider(JSlider.HORIZONTAL,-180, 180, 0);
        rotate.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider s = (JSlider) e.getSource();
                labelrotate.setText(Integer.toString(s.getValue()));
                model.rotate(s.getValue());
            }
        });

        this.add(button);
        this.add(Box.createRigidArea(new Dimension(8,0)));
        this.add(label1);
        this.add(scale);
        this.add(Box.createRigidArea(new Dimension(5,0)));
        this.add(labelscale);
        this.add(Box.createRigidArea(new Dimension(10,0)));
        this.add(label2);
        this.add(rotate);
        this.add(Box.createRigidArea(new Dimension(8,0)));
        this.add(labelrotate);

    }
//make sure two slides value show correctly
    void set(int sc, int ro) {
        scale.setValue(sc);
        labelscale.setText(Double.toString((double) sc / 100));
        rotate.setValue(ro);
        labelrotate.setText(Integer.toString(ro));
    }
//make sure you cannot use this bar when nothing is selected
    @Override
    public void setEnabled(boolean b) {
        for(Component j: getComponents())
            j.setEnabled(b);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (model.s == -1) {
            this.setEnabled(false);
        } else {
            set(model.shapes.get(model.s).scale,model.shapes.get(model.s).rotate);
            this.setEnabled(true);
        }
    }

}
