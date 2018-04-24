package dmitribak.taxi.taxidemo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.slots.PredefinedSlots;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

public class Registration extends AppCompatActivity {

    private TextView errorsText;
    private EditText phone_in_text;
    private Button but_Forgot_get_pass;
    public final String TAG = "performPostCall";

    public final String LOG_TAG = "onPostExecute";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(R.string.Forgot_get_pass);
        setContentView(R.layout.activity_get_password);
        errorsText = (TextView) findViewById(R.id.registratin_textView);
        phone_in_text = (EditText) findViewById(R.id.forgot_editText_phone);
        but_Forgot_get_pass = (Button) findViewById(R.id.Forgot_get_pass);

///////////////////////////////////////////////////////////////
//Ввод номера телефона
        init(R.id.forgot_editText_phone, new ExampleBehaviour() {
            @Override
            void changeMask(MaskImpl mask) {
                mask.setHideHardcodedHead(true);
            }
        });
///////////////////////////////////////////////////////////////

        but_Forgot_get_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer k = phone_in_text.length();
                if (k!=18) {
                    errorsText.setText(R.string.Registration_errorsText);
                } else {
                    new MyAsyncTask().execute();
                }
            }
        });
    }


    ///////////////////////////////////////////////////////////////
//Ввод номера телефона
    public void init(int editTextId, ExampleBehaviour behaviour) {
        EditText editText = (EditText) findViewById(editTextId);
        Slot[] slots = Slot.copySlotArray(PredefinedSlots.RUS_PHONE_NUMBER);
        behaviour.changeSlots(slots);
        MaskImpl mask = new MaskImpl(slots, behaviour.isTerminated());
        behaviour.changeMask(mask);
        MaskFormatWatcher watcher = new MaskFormatWatcher(mask);
        if (behaviour.fillWhenInstall()) {
            watcher.installOnAndFill(editText);
        } else {
            watcher.installOn(editText);
        }
    }
    class ExampleBehaviour {
        void changeMask(MaskImpl mask) {
        }

        void changeSlots(Slot[] slots) {
        }

        boolean isTerminated() {
            return true;
        }

        boolean fillWhenInstall() {
            return false;
        }
    }
//Ввод номера телефона
///////////////////////////////////////////////////////////////

    private class MyAsyncTask extends AsyncTask<String, Void, String> {

        String phone_text, answerHTTP, answerJSON;
        String method = "POST";
        String message;

        String server = "http://192.168.1.189/api/users/registrationPhone/";
        @Override
        protected void onPreExecute() {
            phone_text = phone_in_text.getText().toString();
            Log.v(TAG, phone_text);
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> postDataParams = new HashMap<String, String>();
            postDataParams.put("phone_users", phone_text);
            answerHTTP = performPostCall(server, method,postDataParams);

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            JSONObject dataJsonObj = null;
            String error_num = "";
            String error_text = "";
            String sms_otvet = "";
            try {
                dataJsonObj = new JSONObject(answerHTTP);
                error_num = dataJsonObj.getString("error_num");
                Log.v(LOG_TAG, "error_num: " + error_num);
                if (error_num.equals("0")){
                    Intent intent = new Intent(Registration.this, Input_pasword.class);
                    startActivity(intent);
                } else {
                    error_text = dataJsonObj.getString("error_text");
                    Log.v(LOG_TAG, "error_text: " + error_text);
                    errorsText.setText(error_text);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public String performPostCall(String requestURL, String requestMethod,
                                  HashMap<String, String> postDataParams) {
        URL url;
        String response = "";
        try {
            url = new URL(requestURL);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(15000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestMethod);
            conn.setDoInput(true);
            conn.setDoOutput(true);


            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(getPostDataString(postDataParams));

            writer.flush();
            writer.close();
            os.close();
            int responseCode = conn.getResponseCode();


            if (responseCode == HttpsURLConnection.HTTP_OK) {
                String line;
                BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = br.readLine()) != null) {
                    response += line;
                }
            } else {
                response = "";

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.v(TAG, response);
        return response;
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }
}
