package cs349.uwaterloo.ca.a4;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity implements Observer
{
    // Private Vars
    Model mModel;
    Button Button1;
    Button Button2;
    Button Button3;
    Button Button4;
    Button Button5;
    Button Button6;
    Button Button7;
    TextView T1;
    TextView T2;


    /**
     * OnCreate
     * -- Called when application is initially launched.
     *    @see <a href="https://developer.android.com/guide/components/activities/activity-lifecycle.html">Android LifeCycle</a>
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.activity_main);

        // Get Model instance
        mModel = Model.getInstance();
        mModel.addObserver(this);

        // Get button reference.
        Button1 = (Button) findViewById(R.id.B1);
        Button2 = (Button) findViewById(R.id.B2);
        Button3 = (Button) findViewById(R.id.B3);
        Button4 = (Button) findViewById(R.id.B4);
        Button5 = (Button) findViewById(R.id.B5);
        Button6 = (Button) findViewById(R.id.B6);
        Button7 = (Button) findViewById(R.id.B7);
        T1 = (TextView) findViewById(R.id.T1);
        T2 = (TextView) findViewById(R.id.T2);


        // Create controller to increment counter
        Button1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(mModel.state == Model.State.WIN || mModel.state == Model.State.LOSE || mModel.state == Model.State.START){
                    mModel.newRound();
                } else {
                    // Increment mModel counter
                    mModel.verifyButton(1);
                }
            }
        });
        Button2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment mModel counter
                mModel.verifyButton(2);
            }
        });
        Button3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment mModel counter
                mModel.verifyButton(3);
            }
        });
        Button4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment mModel counter
                mModel.verifyButton(4);
            }
        });
        Button5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment mModel counter
                mModel.verifyButton(5);
            }
        });
        Button6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment mModel counter
                mModel.verifyButton(6);
            }
        });
        Button7.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                // Increment mModel counter
                Intent intent = new Intent(MainActivity.this, Welcome.class);

                // Start activity
                startActivity(intent);
                finish();
            }
        });

        // Init observers
        mModel.initObservers();


    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //
    // Options Menu
    //
    ////////////////////////////////////////////////////////////////////////////////////////////////


    /**
     * Initialize the contents of the Activity's standard options menu.  You
     * should place your menu items in to <var>menu</var>.
     * <p>
     * <p>This is only called once, the first time the options menu is
     * displayed.  To update the menu every time it is displayed, see
     * {@link #onPrepareOptionsMenu}.
     * <p>
     * <p>The default implementation populates the menu with standard system
     * menu items.  These are placed in the {@link Menu#CATEGORY_SYSTEM} group so that
     * they will be correctly ordered with application-defined menu items.
     * Deriving classes should always call through to the base implementation.
     * <p>
     * <p>You can safely hold on to <var>menu</var> (and any items created
     * from it), making modifications to it as desired, until the next
     * time onCreateOptionsMenu() is called.
     * <p>
     * <p>When you add items to the menu, you can implement the Activity's
     * {@link #onOptionsItemSelected} method to handle them there.
     *
     * @param menu The options menu in which you place your items.
     * @return You must return true for the menu to be displayed;
     * if you return false it will not be shown.
     * @see #onPrepareOptionsMenu
     * @see #onOptionsItemSelected
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view1, menu);

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * This hook is called whenever an item in your options menu is selected.
     * The default implementation simply returns false to have the normal
     * processing happen (calling the item's Runnable or sending a message to
     * its Handler as appropriate).  You can use this method for any items
     * for which you would like to do processing without those other
     * facilities.
     * <p>
     * <p>Derived classes should call through to the base class for it to
     * perform the default menu handling.</p>
     *
     * @param item The menu item that was selected.
     * @return boolean Return false to allow normal menu processing to
     * proceed, true to consume it here.
     * @see #onCreateOptionsMenu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle interaction based on item selection
        switch (item.getItemId())
        {
            case R.id.menu1_goToView2:
                // Create Intent to launch second activity
                Intent intent = new Intent(this, SecondActivity.class);

                // Start activity
                startActivity(intent);
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void changeColor(int i){
        switch (i) {
            case 1: Button1.setBackgroundColor(Color.BLACK);break;
            case 2: Button2.setBackgroundColor(Color.BLACK);break;
            case 3: Button3.setBackgroundColor(Color.BLACK);break;
            case 4: Button4.setBackgroundColor(Color.BLACK);break;
            case 5: Button5.setBackgroundColor(Color.BLACK);break;
            case 6: Button6.setBackgroundColor(Color.BLACK);break;
        }
    }

    public void changeBack( int i){
        switch (i) {
            case 1: Button1.setBackgroundColor(Color.parseColor("#FFFF0000"));break;
            case 2: Button2.setBackgroundColor(Color.parseColor("#FFFF9800"));break;
            case 3: Button3.setBackgroundColor(Color.parseColor("#ff4285f4"));break;
            case 4: Button4.setBackgroundColor(Color.parseColor("#ff0f9d58"));break;
            case 5: Button5.setBackgroundColor(Color.parseColor("#ff7b1fa2"));break;
            case 6: Button6.setBackgroundColor(Color.parseColor("#ff9e9e9e"));break;
        }
    }



        /**
         * This method is called whenever the observed object is changed. An
         * application calls an <tt>Observable</tt> object's
         * <code>notifyObservers</code> method to have all the object's
         * observers notified of the change.
         *
         * @param o   the observable object.
         * @param arg an argument passed to the <code>notifyObservers</code>
         */
        @Override
        public void update (Observable o, Object arg) {
            int i = mModel.buttons;
            switch (i) {
                case 1:
                    Button2.setVisibility(View.GONE);
                case 2:
                    Button3.setVisibility(View.GONE);
                case 3:
                    Button4.setVisibility(View.GONE);
                case 4:
                    Button5.setVisibility(View.GONE);
                case 5:
                    Button6.setVisibility(View.GONE);
                case 6:
                    break;
                default:
                    break;
            }

            if(mModel.state == Model.State.COMPUTER || mModel.state == Model.State.HUMAN) {
                for(int k = 1;k <= mModel.buttons; ++k) changeBack(k);
            }

            if(mModel.state == Model.State.COMPUTER) {
               changeColor(mModel.sequence[mModel.index]+1);
            }

            // Update button with click count from model
            T1.setText(mModel.getMessage());
            T2.setText(String.valueOf(mModel.getScore()));

        }
}
