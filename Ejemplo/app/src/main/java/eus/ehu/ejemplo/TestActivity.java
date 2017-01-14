package eus.ehu.ejemplo;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TestActivity extends AppCompatActivity {

    /*String baseUrl = "http://u017633.ehu.eus:28080/ServidorTta/rest/tta";

    String question = "¿Cuál de las siguientes opciones NO se indica en el fichero de manifiesto de la app?";
    String[] answers = {"Versión de la aplicación","Listado de los componentes de la aplicación","Opciones del menú de ajustes","Nivel mínimo de la API Android requerida","Nombre del paquete java de la aplicación"};
    boolean[] corrects = {false,false,true,false,false};
    String[] advise = {"https://www.google.es/","<html><body>The manifest describes the <b>components of the application</b>: " +
            "the activities, services, broadcast receivers, and content providers that...",null,"http://u017633.ehu.eus:28080/static/ServidorTta/AndroidManifest.mp4","http://u017633.ehu.eus:28080/static/ServidorTta/AndroidManifest.mp4"};
    String[] adviseType = {"text/html","text/html",null,"video/mp4","audio/mp4"};
    int selected;*/

    RestClient restClient = new RestClient("http://u017633.ehu.eus:28080/ServidorTta/rest/tta");
    Test test;
    int selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        Intent intent = getIntent();
        setTitle("Nuevo Test");

        new AsyncTask<Void, Void, Void>(){
            @Override
            protected Void doInBackground(Void... voids) {
                test = getTest();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                RadioGroup group = (RadioGroup)findViewById(R.id.test_choices);
                TextView test_text = (TextView)findViewById(R.id.test_text);
                test_text.setText(test.getWording());
                for(int i=0;i<test.getChoices().size();i++) {
                    RadioButton rb = new RadioButton(getApplicationContext());
                    group.addView(rb);
                    rb.setText(test.getChoices().get(i).getAnswer());
                    rb.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            findViewById(R.id.button_send_test).setVisibility(View.VISIBLE);
                        }
                    });
                }

            }
        }.execute();

        /*RadioGroup group = (RadioGroup)findViewById(R.id.test_choices);
        TextView test_text = (TextView)findViewById(R.id.test_text);
        test_text.setText(question);
        for(int i=0;i<answers.length;i++) {
            RadioButton rb = new RadioButton(this);
            group.addView(rb);
            rb.setText(answers[i]);
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    findViewById(R.id.button_send_test).setVisibility(View.VISIBLE);
                }
            });
        }*/
    }

    public Test getTest(){
        final String dni = ((EditText)findViewById(R.id.login)).getText().toString();
        final String passwd = ((EditText)findViewById(R.id.passwd)).getText().toString();
        try {
            restClient.setHttpBasicAuth(dni,passwd);
            Test test = new Test();
            JSONObject json = restClient.getJson(String.format("getTest?id=1"));
            test.setWording(json.getString("wording"));
            JSONArray array = json.getJSONArray("choices");
            for (int i = 0; i < array.length(); i++){
                JSONObject item = array.getJSONObject(i);
                Test.Choice choice = new Test.Choice();
                choice.setId(item.getInt("id"));
                choice.setAnswer(item.getString("answer"));
                choice.setCorrect(item.getBoolean("correct"));
                choice.setAdvise(item.getString("advise"));
                choice.setMime(item.optString("mime",null));
                test.getChoices().add(choice);
            }
            return test;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /*public void putTest(Test tes) {
        try{
            JSONObject json = new JSONObject();
            json.put("wording",test.getWording());
            JSONArray array = new JSONArray();
            for (Test.Choice choice : test.getChoices()){

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }*/

    public void send (View view) {
        int correct=0;
        RadioGroup group = (RadioGroup)findViewById(R.id.test_choices);
        int choices = group.getChildCount();
        for(int i=0;i<choices;i++)
            group.getChildAt(i).setEnabled(false);

        for(int i=0;i<test.getChoices().size();i++) {
            if(test.getChoices().get(i).isCorrect()==true)
                correct=i;
        }

        ViewGroup layout = (ViewGroup)findViewById(R.id.test_layout);
        layout.removeView(findViewById(R.id.button_send_test));

        group.getChildAt(correct).setBackgroundColor(Color.GREEN);

        selected = group.indexOfChild(group.findViewById(group.getCheckedRadioButtonId()));
        if(selected!=correct) {
            group.getChildAt(selected).setBackgroundColor(Color.RED);
            Toast.makeText(getApplicationContext(),"¡Has fallado!",Toast.LENGTH_SHORT).show();
            findViewById(R.id.button_help_test).setVisibility(View.VISIBLE);
        }
        else
            Toast.makeText(getApplicationContext(),"¡Correcto!",Toast.LENGTH_SHORT).show();
    }

    public void help(View view) {
        switch (test.getChoices().get(selected).getMime()) {
            case "text/html":
                showHTML(test.getChoices().get(selected).getAdvise());
                break;
            case "video/mp4":
                showVideo(test.getChoices().get(selected).getAdvise());
                break;
            case "audio/mp4":
                try {
                    showAudio(test.getChoices().get(selected).getAdvise());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showHTML(String advise){
        if (advise.substring(0, 10).contains("://")) {
            Uri uri = Uri.parse(advise);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
        } else {
            WebView web = new WebView(this);
            web.loadData(advise, "text/html", null);
            web.setBackgroundColor(Color.TRANSPARENT);
            web.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
            ViewGroup layout = (ViewGroup)findViewById(R.id.test_layout);
            layout.addView(web);
        }
    }

    private void showVideo(String advise) {
        VideoView video = new VideoView(this);
        video.setVideoURI(Uri.parse(advise));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        video.setLayoutParams(params);

        MediaController controller = new MediaController(this){
            @Override
            public void hide(){}

            @Override
            public boolean dispatchKeyEvent(KeyEvent event){
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
                    finish();
                return super.dispatchKeyEvent(event);
            }
        };
        controller.setAnchorView(video);
        video.setMediaController(controller);

        ViewGroup layout = (ViewGroup)findViewById(R.id.test_layout);
        layout.addView(video);
        video.start();
    }

    private void showAudio(String advise) throws IOException {
        View view = new View(this);
        AudioPlayer audio = new AudioPlayer(view);
        audio.setAudioUri(Uri.parse(advise));
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(params);

        ViewGroup layout = (ViewGroup)findViewById(R.id.test_layout);
        layout.addView(view);
        audio.start();
    }
}
