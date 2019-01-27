package com.example.nwhacks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final CameraActions cameraActions = new CameraActions(this);
    static final int REQUEST_TAKE_PHOTO = 1;
    private String currentPhotoPath;
    private static String inputString = null;
    private static Drawable drawable = null;

    private static RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private static List<DataModel> dataModelList = new ArrayList<>();
    private static RecyclerView.Adapter adapter = new MyAdapter(dataModelList, getAppContext());

    public static Context getAppContext() {
        return MyApplication.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Floating Action Button
        final FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraActions.dispatchTakePictureIntent();
            }
        });

        // Recycler View
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0)
                    fab.hide();
                else if (dy < 0)
                    fab.show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                break;
            case R.id.action_sort:
                Collections.sort(dataModelList);
                recyclerView.setAdapter(adapter);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                File imageFile = new File(currentPhotoPath);
                String encodedFile = cameraActions.encodeFileToBase64Binary(currentPhotoPath);
                if (imageFile.exists()) {
                    drawable = Drawable.createFromPath(currentPhotoPath);
                    createNewItem(drawable, encodedFile);
                }
            }
        }
    }

    private void createNewItem(Drawable drawable, String encodedFile) {
        String url = "https://words-229901.appspot.com/words/recog";

        Post post = new Post();

        post.execute(url, encodedFile);
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    // JSON
    private String sendJSON() throws IOException {
        String url = "https://words-229901.appspot.com/words/recog";
//        String data = "data=" + encodedFile;

        return exampleJSON();
    }

    private String exampleJSON() {
        String json;
        try {
            InputStream is = getAssets().open("json-test.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return json;
    }

    private void getJSON() {
        String url = "https://words-229901.appspot.com/words/recog";
    }

    public static void parseJSON(String input, List<String> choices) {
        try {
            input = input.replaceAll("\\\\\"", "");
            Log.e("INPUT", input);
            JSONObject jsonObject = new JSONObject(input);
            JSONArray data = jsonObject.getJSONArray("data");
            Integer integer = data.length();
            Log.e("length", integer.toString());

            for (int i = 0; i < data.length(); i++) {
                String word = data.getString(i).toLowerCase();
//                JSONObject j = data.getJSONObject(i);
//                String word = j.toString();
                choices.add(word);
            }
        } catch (
                JSONException e) {
            // Do nothing
        }
    }

    protected static class Post extends AsyncTask<String, Void, String> {
//        ProgressDialog progDailog;
//
//        @Override
//        protected void onPreExecute() {
//            progDailog = new ProgressDialog(MainActivity.this);
//            super.onPreExecute();
//            progDailog.setMessage("Loading...");
//            progDailog.setIndeterminate(false);
//            progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
//            progDailog.setCancelable(true);
//            progDailog.show();
//        }

        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("data", params[1]);

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setRequestProperty("Accept", "application/json");
//                connection.setDoInput(true);
//                connection.setDoOutput(true);

                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(connection.getOutputStream()));
                String json = getPostDataString(jsonObject);
                writer.write(json);
                Log.e("string", json);
                writer.close();

                connection.connect();

                // Response: 400
                Log.e("Response", connection.getResponseMessage() + "");
                Log.e("Code", connection.getResponseCode() + "");

                if (connection.getResponseCode() == HttpsURLConnection.HTTP_OK) {

                    BufferedReader in = new BufferedReader(
                            new InputStreamReader(
                                    connection.getInputStream()));
                    StringBuffer sb = new StringBuffer("");
                    String line = "";

                    while ((line = in.readLine()) != null) {
                        sb.append(line);
                        break;
                    }

                    in.close();
                    inputString = sb.toString();
                    Log.e("Input", sb.toString());
//                    return sb.toString();
                }

            } catch (Exception e) {
                Log.e(e.toString(), "Something with request");
            }

            return null;
        }

        @Override
        protected void onPostExecute(String unused) {
            super.onPostExecute(unused);
            List<String> choices = new ArrayList<>();
            parseJSON(inputString, choices);
            dataModelList.add(0, new DataModel(drawable, choices));
            recyclerView.setAdapter(adapter);
        }
    }

    public static String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("\":\"");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        String str = "{\"" + result.toString() + "\"}";
        return str;
    }

    // Buttons
    @Override
    public void onClick(View v) {
        Snackbar snackbar;
        DataModel dataModel;
        String correct = "Incorrect :(";
        int tag;
        Button b = (Button) v;

        switch (b.getId()) {
            case R.id.action_button_1:
                tag = (int) b.getTag();
                dataModel = dataModelList.get(dataModelList.size() - 1 - tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord())) {
                    correct = "Correct :)";
                    dataModel.setTitle(dataModel.getCorrectWord());
                    recyclerView.setAdapter(adapter);
                }
                break;
            case R.id.action_button_2:
                tag = (int) b.getTag();
                dataModel = dataModelList.get(dataModelList.size() - 1 - tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord())) {
                    correct = "Correct :)";
                    dataModel.setTitle(dataModel.getCorrectWord());
                    recyclerView.setAdapter(adapter);
                }
                break;
            case R.id.action_button_3:
                tag = (int) b.getTag();
                dataModel = dataModelList.get(dataModelList.size() - 1 - tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord())) {
                    correct = "Correct :)";
                    dataModel.setTitle(dataModel.getCorrectWord());
                    recyclerView.setAdapter(adapter);
                }
                break;
            case R.id.action_button_4:
                tag = (int) b.getTag();
                dataModel = dataModelList.get(dataModelList.size() - 1 - tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord())) {
                    correct = "Correct :)";
                    dataModel.setTitle(dataModel.getCorrectWord());
                    recyclerView.setAdapter(adapter);
                }
                break;
        }

        snackbar = Snackbar.make(findViewById(R.id.container),
                correct, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}