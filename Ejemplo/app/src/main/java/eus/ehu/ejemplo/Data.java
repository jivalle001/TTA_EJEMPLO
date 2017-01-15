package eus.ehu.ejemplo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by jose on 15/01/17.
 */
public class Data {

    public Data() {
    }

    public Test getTest(String dni, String passwd, RestClient restClient){
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

    public void putTest(Test test) {
        try{
            JSONObject json = new JSONObject();
            json.put("wording",test.getWording());
            JSONArray array = new JSONArray();
            for (Test.Choice choice : test.getChoices()){
                JSONObject item = new JSONObject();
                item.put("id",choice.getId());
                item.put("wording",choice.getAnswer());
                item.put("correct",choice.isCorrect());
                item.put("advise",choice.getAdvise());
                item.put("mime",choice.getMime());
                array.put(item);
            }
            json.put("choices",array);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Exercise getExercise(int id, RestClient restClient) {
        try {
            JSONObject json = restClient.getJson(String.format("getExercise?id="+id));
            Exercise exercise = new Exercise();
            exercise.setWording(json.getString("wording"));
            JSONObject object = json.getJSONObject("lessonBean");
            exercise.getLessonBean().setNumber(object.getInt("number"));
            exercise.getLessonBean().setTitle(object.getString("title"));
            return exercise;
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
