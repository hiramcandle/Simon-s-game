package cs349.uwaterloo.ca.a4;

import android.util.Log;

import java.util.Observable;
import java.util.Observer;
import java.util.Random;
import java.lang.String;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MVC2 Model
 * <p>
 * Created by J. J. Hartmann on 11/19/2017.
 * Email: j3hartma@uwaterloo.ca
 * Copyright 2017
 */

class Model extends Observable
{
    // Create static instance of this mModel
    private static final Model ourInstance = new Model();
    static Model getInstance()
    {
        return ourInstance;
    }

    // Private Variables
    enum State { START, COMPUTER, HUMAN, LOSE, WIN };
    enum Level {EASY,NORMAL,HARD};
    State state;
    int score;
    int length;
    int [] sequence;
    int index;
    // number of possible buttons
    int buttons;
    int level;
    Timer timer;

    /**
     * Model Constructor:
     * - Init member variables
     */
    Model() {
        init();
    }

    void init() {
        this.length = 1;
        this.score = 0;
        this.state = State.START;
        this.buttons = 4;
        this.level = 1000;
    }

    void init2(){
        this.length = 1;
        this.score = 0;
        this.state = State.START;

    }

    public String getMessage() {
        String s = "";
        switch (state) {
            // will only be in this state right after Simon object is contructed
            case START:
                s= "Press RED BLOCK to play";
                break;
            // they won last round
            // score is increased by 1, sequence length is increased by 1
            case WIN:
                s = "You won! Press RED BLOCK to continue.";
                break;
            // they lost last round
            // score is reset to 0, sequence length is reset to 1
            case LOSE:
                s = "You lose. Press RED BLOCK to play again.";
                break;
            case COMPUTER:
                s = "Watch what I do ...";
                break;
            case HUMAN:
                s = "Your turn ...";
                break;
            default:
                // should never be any other state at this point ...
                break;
        }
        return s;
    }

    public int getScore()
    {
        return score;
    }

    public void newRound() {

        Random random = new Random();
        // reset if they lost last time
        if (state == State.LOSE) {
            length = 1;
            score = 0;
        }

        sequence = new int[length];

        for (int i = 0; i < length; i++) {
            int b = random.nextInt(buttons);
            sequence[i]=b;
        }

        index = 0;
        state = State.COMPUTER;

        setChanged();
        notifyObservers();
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                nextButton();
                if(state != State.COMPUTER) timer.cancel();
            }
        },level, level);
    }

    void nextButton() {

        if (state != State.COMPUTER) {
            return;
        }
        index++;
        if (index >= length) {
            index = 0;
            state = State.HUMAN;
        }

        setChanged();
        notifyObservers();

        // advance to next displayable




    }




    public void verifyButton(int button) {

        if (state != State.HUMAN) {
            return;
        }

        boolean correct = (button-1 == sequence[index]);

        index++;

        if (!correct) {
            state = State.LOSE;
        } else {
            if (index == length) {
                state = State.WIN;
                score++;
                length++;
            }
        }
        setChanged();
        notifyObservers();
    }

    public void setButtons(int i){ buttons = i + 1;}

    public void setLevel(int i)
    {
        switch(i) {
            case 1 : level = 2000; break;
            case 2: level =1000; break;
            case 3: level = 500; break;
            default: level = 1000; break;
        }
        setChanged();
        notifyObservers();
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Observable Methods
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Helper method to make it easier to initialize all observers
     */
    public void initObservers()
    {
        setChanged();
        notifyObservers();
    }

    /**
     * Deletes an observer from the set of observers of this object.
     * Passing <CODE>null</CODE> to this method will have no effect.
     *
     * @param o the observer to be deleted.
     */
    @Override
    public synchronized void deleteObserver(Observer o)
    {
        super.deleteObserver(o);
    }

    /**
     * Adds an observer to the set of observers for this object, provided
     * that it is not the same as some observer already in the set.
     * The order in which notifications will be delivered to multiple
     * observers is not specified. See the class comment.
     *
     * @param o an observer to be added.
     * @throws NullPointerException if the parameter o is null.
     */
    @Override
    public synchronized void addObserver(Observer o)
    {
        super.addObserver(o);
    }

    /**
     * Clears the observer list so that this object no longer has any observers.
     */
    @Override
    public synchronized void deleteObservers()
    {
        super.deleteObservers();
    }

    /**
     * If this object has changed, as indicated by the
     * <code>hasChanged</code> method, then notify all of its observers
     * and then call the <code>clearChanged</code> method to
     * indicate that this object has no longer changed.
     * <p>
     * Each observer has its <code>update</code> method called with two
     * arguments: this observable object and <code>null</code>. In other
     * words, this method is equivalent to:
     * <blockquote><tt>
     * notifyObservers(null)</tt></blockquote>
     *
     * @see Observable#clearChanged()
     * @see Observable#hasChanged()
     * @see Observer#update(Observable, Object)
     */
    @Override
    public void notifyObservers()
    {
        super.notifyObservers();
    }
}
