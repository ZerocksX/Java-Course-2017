package hr.fer.andriod.hw0036492049;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.fer.andriod.hw0036492049.model.OperationResult;

/**
 * Activity that displays a message, and has 2 buttons, first closes this activity, second send a report email to "ana@baotic.org"
 */
public class DisplayActivity extends AppCompatActivity {

    /**
     * Request code for this activity
     */
    public static final int REQUEST_CODE = 101;
    /**
     * Displayed message
     */
    @BindView(R.id.displayMessage)
    TextView displayMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey(CalculusActivity.OPERATION_RESULT_KEY)) {
                OperationResult result = (OperationResult) extras.getSerializable(CalculusActivity.OPERATION_RESULT_KEY);
                displayMessage.setText(result.toString());

            } else if (extras.containsKey(CalculusActivity.ERROR_MESSAGE_KEY)) {
                displayMessage.setText(extras.getString(CalculusActivity.ERROR_MESSAGE_KEY));
            }
        }

    }

    /**
     * Method to be called when return button is pressed<br/>
     * Sets result to OK and finishes activity
     *
     * @param view view
     */
    @OnClick(R.id.returnButton)
    void returnToCalculusFromError(View view) {
        setResult(RESULT_OK);
        finish();
    }

    /**
     * Method to be called when send report button is pressed<br/>
     * Creates an E-Mail and starts email send action
     *
     * @param view view
     */
    @OnClick(R.id.sendReport)
    void sendReport(View view) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"ana@baotic.org"});
        i.putExtra(Intent.EXTRA_SUBJECT, "0036492049: dz report");
        i.putExtra(Intent.EXTRA_TEXT, displayMessage.getText().toString());

        startActivity(Intent.createChooser(i, "Send mail..."));

    }
}
