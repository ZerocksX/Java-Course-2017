package hr.fer.andriod.hw0036492049;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.fer.andriod.hw0036492049.model.User;
import hr.fer.andriod.hw0036492049.services.UserService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Activity that fetches user data and displays it
 */
public class FormActivity extends AppCompatActivity {

    /**
     * Avatar image
     */
    @BindView(R.id.avatarImage)
    ImageView avatarImage;

    /**
     * Path text
     */
    @BindView(R.id.pathText)
    TextView pathText;

    /**
     * First name
     */
    @BindView(R.id.firstName)
    TextView firstName;

    /**
     * Last name
     */
    @BindView(R.id.lastName)
    TextView lastName;

    /**
     * Phone number
     */
    @BindView(R.id.phoneNumber)
    TextView phoneNumber;

    /**
     * E mail
     */
    @BindView(R.id.eMail)
    TextView eMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        ButterKnife.bind(this);
    }

    /**
     * Method to be called when fetch button is pressed<br/>
     * Fetches data from path text relative to "http://m.uploadedit.com" and displays it
     *
     * @param view view
     */
    @OnClick(R.id.fetchButton)
    void fetch(View view) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://m.uploadedit.com")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        UserService service = retrofit.create(UserService.class);

        if (pathText.getText().toString().trim().isEmpty()) {
            return;
        }

        try {
            service.getUser(pathText.getText().toString()).enqueue(new Callback<User>() {
                @Override
                public void onResponse(Call<User> call, Response<User> response) {
                    try {
                        User user = response.body();
                        Log.d("JAVA TEČAJ", user.toString());
                        Glide.with(FormActivity.this)
                                .load(user.getAvatarLocation())
                                .into(avatarImage);
                        avatarImage.setVisibility(View.VISIBLE);
                        firstName.setText(user.getFirstName());
                        lastName.setText(user.getLastName());
                        phoneNumber.setText(user.getPhoneNumber());
                        eMail.setText(user.geteMail());
                    } catch (Exception e) {
                        Toast.makeText(FormActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<User> call, Throwable t) {
                    Toast.makeText(FormActivity.this, t.getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(FormActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Method to be called when phone number is pressed<br/>
     * If phone number exist call action will be started
     *
     * @param view view
     */
    @OnClick(R.id.phoneNumber)
    void callPhoneNumber(View view) {
        String phoneNum = phoneNumber.getText().toString();
        if (!phoneNum.trim().isEmpty()) {
            Uri uri = Uri.parse(phoneNum);
            Intent i = new Intent(Intent.ACTION_CALL, uri);
            startActivity(Intent.createChooser(i, "Phone call..."));

        }
    }

    /**
     * Method to be called when email is pressed<br/>
     * If email exists email send action will be started
     *
     * @param view
     */
    @OnClick(R.id.eMail)
    void sendEMail(View view) {
        String email = eMail.getText().toString();
        if (!email.trim().isEmpty()) {

            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("message/rfc822");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{email});

            startActivity(Intent.createChooser(i, "Send mail..."));
        }
    }
}
