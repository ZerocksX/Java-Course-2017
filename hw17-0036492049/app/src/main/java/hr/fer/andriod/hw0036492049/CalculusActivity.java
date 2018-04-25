package hr.fer.andriod.hw0036492049;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.fer.andriod.hw0036492049.model.OperationResult;

/**
 * A simple activity that gives user an option to chose an operation and its parameters<br/>
 * After calculating and wrapping the result into a {@link OperationResult} data is sent to {@link DisplayActivity}<br/>
 * If something fails corresponding message will be displayed on {@link DisplayActivity}
 */
public class CalculusActivity extends AppCompatActivity {

    /**
     * Spinner that contains strings representing one of four operations<br/>
     * Addition, subtraction, multiplication and division
     */
    @BindView(R.id.operationSpinner)
    Spinner operationSpinner;
    /**
     * First input
     */
    @BindView(R.id.firstInput)
    EditText firstInput;
    /**
     * Second input
     */
    @BindView(R.id.secondInput)
    EditText secondInput;

    /**
     * Key for operation result object in extras bundle
     */
    public static final String OPERATION_RESULT_KEY = "operationResultKey";
    /**
     * Key for error message string in extras bundle
     */
    public static final String ERROR_MESSAGE_KEY = "errorMessageKey";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculus);
        ButterKnife.bind(this);

        // spinner initialization block

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                this,
                R.array.operation_array,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        operationSpinner.setAdapter(adapter);

    }

    /**
     * Method to be called when start operation button has been pressed<br/>
     * If input is valid calculates a result and sends it to {@link DisplayActivity} and starts it, or sends a message
     *
     * @param view view
     */
    @OnClick(R.id.startOperation)
    void startOperation(View view) {
        Intent i = new Intent(CalculusActivity.this, DisplayActivity.class);
        Bundle extras = new Bundle();
        int first = 0, second = 0;

        Log.d("JAVA TEČAJ", "First: " + firstInput.getText().toString());
        Log.d("JAVA TEČAJ", "Second: " + secondInput.getText().toString());
        Log.d("JAVA TEČAJ", operationSpinner.getSelectedItem().toString());

        try {
            first = Integer.parseInt(firstInput.getText().toString());
        } catch (Exception e) {
            Toast.makeText(CalculusActivity.this, "Upišite prvi broj", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            second = Integer.parseInt(secondInput.getText().toString());
        } catch (Exception e) {
            Toast.makeText(CalculusActivity.this, "Upišite drugi broj", Toast.LENGTH_SHORT).show();
            return;
        }

        double result = 0;
        String selectedOperation = operationSpinner.getSelectedItem().toString();
        try {
            switch (selectedOperation) {
                case "Zbrajanje":
                    result = first + second;
                    break;
                case "Oduzimanje":
                    result = first - second;
                    break;
                case "Množenje":
                    result = 1.0 * first * second;
                    break;
                case "Dijeljenje":
                    result = 1.0 * first / second;
                    break;
                default:
                    throw new RuntimeException("Nepoznata operacija " + selectedOperation);
            }

            OperationResult operationResult = new OperationResult(first, second, result, selectedOperation);
            extras.putSerializable(OPERATION_RESULT_KEY, operationResult);

        } catch (Exception e) {
            extras.putString(ERROR_MESSAGE_KEY, String.format(
                    "Prilikom obavljanja operacije %s nad unosima %s i %s došlo je do sljedeće grešlke: %s.",
                    selectedOperation,
                    first,
                    second,
                    e.getMessage()
            ));
        }

        i.putExtras(extras);

        startActivityForResult(i, DisplayActivity.REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //if Display activity is closed with OK button, clear input
        if (requestCode == DisplayActivity.REQUEST_CODE && resultCode == RESULT_OK) {
            Log.d("JAVA TEČAJ", "Returned OK from Display activity");
            firstInput.setText("");
            secondInput.setText("");
            operationSpinner.setSelection(0);
        }
    }
}
