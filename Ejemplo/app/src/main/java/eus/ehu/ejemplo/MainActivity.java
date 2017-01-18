package eus.ehu.ejemplo;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    public final static String EXTRA_LOGIN = "eus.ehu.ejemplo.authentication";
    public final static String EXTRA_DNI = "eus.ehu.ejemplo.authentication";
    public final static String EXTRA_PASSWD = "eus.ehu.ejemplo.authentication";
    RestClient restClient = new RestClient("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Alumno de TTA");
    }

    /*public void login(View view) {
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
   }*/

    public void authentication(View view) throws IOException {
        final String dni = ((EditText)findViewById(R.id.login)).getText().toString();
        final String passwd = ((EditText)findViewById(R.id.passwd)).getText().toString();
        new AsyncTask<Void,Void,Void>(){
            User user;
            @Override
            protected Void doInBackground(Void... voids) {
                try {
                    restClient.setHttpBasicAuth(dni,passwd);
                    JSONObject json = restClient.getJson(String.format("getStatus?id=%s",dni));
                    user = new User(json.getInt("id"),json.getString("user"),json.getInt("lessonNumber"),
                            json.getString("lessonTitle"),json.getInt("nextText"),json.getInt("nextExercise"));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Intent intent = new Intent(getApplicationContext(),MenuActivity.class);
                intent.putExtra(MainActivity.EXTRA_LOGIN,user.getUser());
                intent.putExtra(MainActivity.EXTRA_DNI,dni);
                intent.putExtra(MainActivity.EXTRA_PASSWD,passwd);
                startActivity(intent);
            }
        }.execute();
    }


}
