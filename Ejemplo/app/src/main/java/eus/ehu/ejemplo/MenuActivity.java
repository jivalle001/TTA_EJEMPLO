package eus.ehu.ejemplo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        setTitle("Evaluación de TTA");

        Intent intent = getIntent();
        TextView textLogin = (TextView)findViewById(R.id.menu_login);
        textLogin.setText(("Bienvenido "+intent.getStringExtra(MainActivity.EXTRA_LOGIN)));
    }

    public void test(View view) {
        Intent intent = new Intent(this,TestActivity.class);
        startActivity(intent);
    }

    public void ejercicio(View view) {
        Intent intent = new Intent(this,ExerciseActivity.class);
        startActivity(intent);
    }
}
