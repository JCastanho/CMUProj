package pt.ulisboa.tecnico.cmov.hoponcmu.client;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;
import pt.ulisboa.tecnico.cmov.hoponcmu.client.asynctask.SignupTask;

public class SignUpActivity extends AppCompatActivity {

    private SignupTask signupTask = null;
    private EditText usernameView;
    private EditText passwordView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        Button signInButton = (Button) findViewById(R.id.btn_signup);
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSignIn();
            }
        });

        usernameView = (EditText) findViewById(R.id.txt_username);
        passwordView = (EditText) findViewById(R.id.txt_code);
    }

    private void attemptSignIn() {
        String username = usernameView.getText().toString();
        String password = passwordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check if the user entered password
        if (TextUtils.isEmpty(password)) {
            passwordView.setError(getString(R.string.error_field_required));
            focusView = passwordView;
            cancel = true;
        }

        // Check if the user entered username.
        if (TextUtils.isEmpty(username)) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            signupTask = new SignupTask(SignUpActivity.this, (ApplicationContextProvider) getApplicationContext());
            signupTask.execute(username, password);
        }
    }

    public void updateInterface(Boolean success){
        if(success) {
            Toast.makeText(this, "Sucessfully registered!", Toast.LENGTH_SHORT).show();
            SignUpActivity.this.finish();
        } else {
            Toast.makeText(this, "Registration Failed", Toast.LENGTH_SHORT).show();
        }
    }
}