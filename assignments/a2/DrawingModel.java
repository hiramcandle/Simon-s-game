import java.util.Observable;

import java.awt.*;
import java.awt.geom.*;
import java.util.ArrayList;

//drawingmodel is an observable


class DrawingModel extends Observable {
//use Path2d to complete my own shape
    class Shape extends Path2D.Double {
        //necessary info to update bars
        int nPoints = 0;
        int scale = 100;
        int rotate = 0;
        double x=0, y=0;

        AffineTransform at = null;
        java.awt.Shape draw = null;

        public void draw(Graphics2D g) {
            operation();
            g.draw(draw);
        }
//to calculate # of points in this stroke
        void addnpoints() {
            nPoints += 1;
        }

//do operation like rotate, translate or scale
        void operation() {
            if(at == null) {
                at = new AffineTransform();
                at.translate(x,y);
                at.scale(((double)scale) / 100, ((double)scale) / 100);
                at.rotate(Math.toRadians(rotate));
                draw = null;
            }
            if(draw == null) {
                draw = this.createTransformedShape(at);
            }
        }
//check if you are selecting this stroke
        public boolean hittest(double x, double y)
        {
            operation();
            double coor[] = new double[6];
            double last[] = new double[2];
            Point2D.Double m = new Point2D.Double(x,y);

            for(PathIterator it = draw.getPathIterator(null); !it.isDone(); it.next()) {
                if(it.currentSegment(coor) == PathIterator.SEG_LINETO) {
                    double proj[] = closestPoint(last, coor, m);
                    if(m.distance(proj[0], proj[1]) <= 10 ) return true;
                }

                last[0] = coor[0];
                last[1] = coor[1];
            }
            return false;
        }

        //move this stroke
        void move(double dx, double dy) {
            this.x += dx; this.y += dy;
        }

    }
//all strokes store here
    ArrayList<Shape> shapes = new ArrayList<>();
    private boolean ispainting;
    int s;

    DrawingModel() {
        shapes = new ArrayList<Shape>();
        s = -1;
        ispainting = false;
        setChanged();
    }
//this is from demo code, i only do a little change from it
    static double[] closestPoint(double P0[], double P1[], Point2D.Double M) {
        double v[] = new double[2];
        v[0] = P1[0] - P0[0];
        v[1] = P1[1] - P0[1];

        // early out if line is less than 1 pixel long
        if (v[0] * v[0] + v[1] * v[1] < 1)
            return P0;

        double u[] = new double[2];
        u[0] = P1[0] -P0[0];
        u[1] = P1[1] - P0[1];
        // scalar of vector projection ...
        double s = (v[0] * u[0] + v[1] * u[1]) / (v[0] * v[0] + v[1] * v[1]);

        // find point for constrained line segment
        if (s < 0)
            return P0;
        else if (s > 1)
            return P1;
        else {
            double proj[] = new double[2];
            proj[0] = P0[0] + s * v[0];
            proj[1] = P0[1] + s * v[1];
            return proj;
        }
    }

    //this func will call move() in selected stroke
    void moveshape(double dx, double dy) {
        shapes.get(s).move(dx, dy);
        shapes.get(s).at = null;
        setChanged();
        notifyObservers();
    }

//this func is called when delete button is pressed
    void delete() {
        if(s != -1) shapes.remove(s);
        s = -1;
        setChanged();
        notifyObservers();
    }

    //check which stroke is selected
    void select(double x, double y) {
            for (int i = shapes.size() - 1; i >= 0; --i) {
                if (shapes.get(i).hittest(x, y)) {
                    s = i;
                    setChanged();
                    notifyObservers();
                    return;
                }
                s = -1;
            }
            setChanged();
            notifyObservers();
    }
//tell the model you are stop drawing or moving selected stroke
     void enddrag(double x, double y) {
        if(ispainting) {
            ispainting = false;
            AffineTransform at = new AffineTransform();
            at.translate(-shapes.get(shapes.size()-1).getBounds().getCenterX(), -shapes.get(shapes.size()-1).getBounds().getCenterY());
            shapes.get(shapes.size() - 1).x = shapes.get(shapes.size()-1).getBounds().getCenterX();
            shapes.get(shapes.size() - 1).y = shapes.get(shapes.size()-1).getBounds().getCenterY();
            shapes.get(shapes.size() - 1).transform(at);
        }

        setChanged();
        notifyObservers();
    }

    //add points to the stroke you are drawing
    public void drawing(int x, int y) {

        if(!ispainting) {
            Shape shape = new Shape();
            shapes.add(shape);
            ispainting = true;
            shapes.get(shapes.size() - 1).moveTo(x, y);
        }else {
            shapes.get(shapes.size() - 1).lineTo(x, y);
        }

        shapes.get(shapes.size() - 1).addnpoints();
        shapes.get(shapes.size()-1).at = null;
        setChanged();
        notifyObservers();
    }

    void scale(int x) {
        shapes.get(s).scale = x;
        shapes.get(s).at = null;
        setChanged();
        notifyObservers();
    }

    void rotate(int x) {
        shapes.get(s).rotate = x;
        shapes.get(s).at = null;
        setChanged();
        notifyObservers();
    }




}
