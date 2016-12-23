package tta_ejemplo_pr1.intel.tta.ehu.eus.tta_ejemplo_pr1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.view.View;
import android.view.ViewGroup;
import android.graphics.Color;
import android.widget.TextView;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        setTitle("Nuevo Test");

        RadioGroup group = (RadioGroup)findViewById(R.id.test_choices);
        for(int i=0;i<5;i++) {

            RadioButton rb = new RadioButton(this);
            group.addView(rb);
            rb.setText("Test");
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.button_send_test).setVisibility(View.VISIBLE);
                }
            });
        }
    }

    public void send (View view) {
        RadioGroup group = (RadioGroup)findViewById(R.id.test_choices);
        int choices = group.getChildCount();
        for(int i=0;i<choices;i++)
            group.getChildAt(i).setEnabled(false);

        ViewGroup layout = (ViewGroup)findViewById(R.id.test_layout);
        layout.removeView(findViewById(R.id.button_send_test));

        group.getChildAt(2).setBackgroundColor(Color.GREEN);

        int selected = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
        if(selected!=2) {
            group.getChildAt(selected).setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(),"¡Has fallado!",Toast.LENGTH_SHORT).show();
            findViewById(R.id.button_help_test).setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(getApplicationContext(),"¡Correcto!",Toast.LENGTH_SHORT).show();
    }

    public void help(View view) {
        TextView tv = (TextView)findViewById(R.id.help_text);
        tv.setText("The manifest describes the components os the aplication: the activities, " +
                "service, broadcast receivers, and content providers that the aplication is " +
                "composed of. It names the classes that implement each of the components and " +
                "publishes their capabilities (for example, which intent message they can handle). " +
                "These declarations let the Android system know what the components are and under " +
                "what conditions they can be launched.");
        findViewById(R.id.help_text).setVisibility(View.VISIBLE);
    }
}
