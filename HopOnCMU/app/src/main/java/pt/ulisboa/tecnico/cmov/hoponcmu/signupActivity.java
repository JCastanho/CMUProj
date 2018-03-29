package pt.ulisboa.tecnico.cmov.hoponcmu;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class signupActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
    }

    public void onSignup(View view){
        //TODO verify if unique username

        //TODO verify if used code

        //TODO add user to existing users

        //Return to sign in page
        signupActivity.this.finish();

        Toast.makeText(this, "Sucessfully registered!", Toast.LENGTH_SHORT).show();
    }
}
