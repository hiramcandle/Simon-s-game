package cs349.uwaterloo.ca.a4;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by tommy on 2017/12/3.
 */

public class Welcome extends AppCompatActivity implements Observer {
    Model mModel;
    Button Button0;
    TextView T0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set Content View
        setContentView(R.layout.welcome);

        // Get Model instance
        mModel = Model.getInstance();
        mModel.addObserver(this);

        // Get button reference.
        Button0 = (Button) findViewById(R.id.B0);
        T0 = (TextView) findViewById(R.id.T0);

        Button0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Welcome.this, MainActivity.class);
                mModel.init();
                // Start activity
                startActivity(intent);
                finish();

            }
        });
        mModel.initObservers();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Remove observer when activity is destroyed.
        mModel.deleteObserver(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.view1, menu);

        return super.onCreateOptionsMenu(menu);
    }

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

    @Override
    public void update(Observable o, Object arg)
    {

    }

}
