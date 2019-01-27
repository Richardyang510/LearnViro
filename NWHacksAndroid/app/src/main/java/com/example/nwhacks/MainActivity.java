package com.example.nwhacks;

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
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
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
    private static String inputString;
    String encodedFile;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<DataModel> dataModelList = new ArrayList<>();
    private RecyclerView.Adapter adapter = new MyAdapter(dataModelList, this);

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
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                File imageFile = new File(currentPhotoPath);
                encodedFile = cameraActions.encodeFileToBase64Binary(currentPhotoPath);
                if (imageFile.exists()) {
                    Drawable drawable = Drawable.createFromPath(currentPhotoPath);
                    createNewItem(drawable);
                }
            }
        }
    }

    private void createNewItem(Drawable drawable) {
        String input = exampleJSON();
        List<String> choices = new ArrayList<>();
        parseJSON(input, choices);
        dataModelList.add(new DataModel(drawable, choices));
        recyclerView.setAdapter(adapter);
    }

    public void setCurrentPhotoPath(String currentPhotoPath) {
        this.currentPhotoPath = currentPhotoPath;
    }

    // JSON
     private String sendJSON() throws IOException {
        String url = "https://words-229901.appspot.com/words/recog";
        String data = "data=" + encodedFile;

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

    public void parseJSON(String input, List<String> choices) {
        try {
            JSONObject jsonObject = new JSONObject(input);
            JSONArray data = jsonObject.getJSONArray("data");

            for (int i = 0; i < data.length(); i++) {
                JSONObject j = data.getJSONObject(i);
                String word = j.getString("word");
                choices.add(word);
            }
        } catch (
                JSONException e) {
            // Do nothing
        }
    }

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
                dataModel = dataModelList.get(tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord())) {
                    correct = "Correct :)";
                }
                break;
            case R.id.action_button_2:
                tag = (int) b.getTag();
                dataModel = dataModelList.get(tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord()))
                    correct = "Correct :)";
                break;
            case R.id.action_button_3:
                tag = (int) b.getTag();
                dataModel = dataModelList.get(tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord()))
                    correct = "Correct :)";
                break;
            case R.id.action_button_4:
                tag = (int) b.getTag();
                dataModel = dataModelList.get(tag);
                if (b.getText().toString().equals(dataModel.getCorrectWord()))
                    correct = "Correct :)";
                break;
        }

        snackbar = Snackbar.make(findViewById(R.id.container),
                correct, Snackbar.LENGTH_SHORT);
        snackbar.show();
    }
}