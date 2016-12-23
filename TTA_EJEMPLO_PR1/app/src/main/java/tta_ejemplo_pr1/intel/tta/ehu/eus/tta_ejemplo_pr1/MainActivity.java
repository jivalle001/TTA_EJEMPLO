package tta_ejemplo_pr1.intel.tta.ehu.eus.tta_ejemplo_pr1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_LOGIN = "tta_ejemplo_pr1.intel.tta.ehu.eus.tta_ejemplo_pr1.login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Alumno de TTA");
    }

    public void login(View view) {
        Intent intent = new Intent(this,MenuActivity.class);
        String login = ((EditText)findViewById(R.id.login)).getText().toString();
        String passwd = ((EditText)findViewById(R.id.passwd)).getText().toString();
        if(login.matches("") || passwd.matches("")) {
            Toast.makeText(getApplicationContext(),"¡Login Incorrecto!",Toast.LENGTH_LONG).show();
        }
        else {
            intent.putExtra(MainActivity.EXTRA_LOGIN,login);
            Toast.makeText(getApplicationContext(),"¡Login Correcto!",Toast.LENGTH_SHORT).show();
            startActivity(intent);
        }
   }
}
