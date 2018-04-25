package hr.fer.andriod.hw0036492049;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Main activity that serves as a router<br/>
 * User can choose to open calculus or form action
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    /**
     * Method to be called when calculus button is pressed<br/>
     * opens Calculus activity
     *
     * @param view
     */
    @OnClick(R.id.calculusButton)
    void startCalculus(View view) {
        Intent i = new Intent(MainActivity.this, CalculusActivity.class);
        startActivity(i);
    }


    /**
     * Method to be called when form button is pressed<br/>
     * Opens Form activity
     *
     * @param view
     */
    @OnClick(R.id.formButton)
    void startForm(View view) {
        Intent i = new Intent(MainActivity.this, FormActivity.class);
        startActivity(i);
    }
}
