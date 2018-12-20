package app.example.app.postjsondata;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    postJSONData postTask;
    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = (TextView) findViewById(R.id.textView);

        postTask = new postJSONData();
        postTask.execute(new String[]{""});
    }


    private class postJSONData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection connection = null;
            BufferedReader reader = null;
            OutputStreamWriter writer = null;
            StringBuffer buffer =  null;
            String results = null;


            try{
                URL url = new URL(params[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.connect();


                JSONObject inputs = new JSONObject();
                inputs.put("email", "muthuraja@gmail.com");
                inputs.put("password", "1234");


                writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(inputs.toString());
                writer.close();


                int HTTPResult = connection.getResponseCode();
                if(HTTPResult == HttpURLConnection.HTTP_OK){
                    buffer =  new StringBuffer();
                    String line;
                    reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    if(reader != null) {
                        while((line = reader.readLine()) != null){
                            buffer.append(line+"\n");
                        }
                        results = buffer.toString();
                        reader.close();
                    }
                }else{
                    Log.e("Error",connection.getResponseMessage());
                }
            }catch (Exception e){
                Log.e("Error", e.toString());
            }
            return results;
        }


        @Override
        protected void onPostExecute(String values){

            try {
                JSONObject obj = new JSONObject(values);
                text.setText(obj.getJSONObject("data").getString("token"));

            }catch (Exception e){
                Log.e("Error", e.toString());
            }
        }
    }
}
