package pt.ulisboa.tecnico.cmu.dummyclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import pt.ulisboa.tecnico.cmu.dummyclient.asynctask.DummyTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText et = (EditText) findViewById(R.id.editText);
                new DummyTask(MainActivity.this).execute(et.getText().toString());
            }
        });
    }

    public void updateInterface(String reply) {
        TextView tv = (TextView) findViewById(R.id.textView);
        tv.setText(reply);
    }
}
