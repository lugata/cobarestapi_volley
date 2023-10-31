package com.lugata_ata.restapi_volley;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText etSearchDoa;
    private RecyclerView rvDoaList;
    private DoaAdapter doaAdapter;
    private RequestQueue requestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etSearchDoa = findViewById(R.id.etSearchDoa);
        Button btnSearch = findViewById(R.id.btnSearch);
        rvDoaList = findViewById(R.id.rvDoaList);
        rvDoaList.setLayoutManager(new LinearLayoutManager(this));

        requestQueue = Volley.newRequestQueue(this);

        btnSearch.setOnClickListener(view -> searchDoa());
    }

    private void searchDoa() {
        String doaName = etSearchDoa.getText().toString().trim();
        if (doaName.isEmpty()) {
            Toast.makeText(this, "Masukkan nama doa terlebih dahulu", Toast.LENGTH_SHORT).show();
            return;
        }
        // Show a progress dialog
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Fetching data...");
        progressDialog.show();
        String apiUrl = "https://doa-doa-api-ahmadramadhan.fly.dev/api/doa/" + doaName;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                apiUrl,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        progressDialog.dismiss();
                        if (response != null) {
                            String title = response.optString("doa", "Doa not found");
                            String ayat = response.optString("ayat", "Ayat not found");
                            String latin = response.optString("latin", "Latin not found");
                            String artinya = response.optString("artinya", "Artinya not found");

                            DoaModel doa = new DoaModel();
                            doa.setTitle(title);
                            doa.setAyat(ayat);
                            doa.setLatin(latin);
                            doa.setArtinya(artinya);

                            List<DoaModel> doaList = new ArrayList<>();
                            doaList.add(doa);
                            doaAdapter = new DoaAdapter(doaList);
                            rvDoaList.setAdapter(doaAdapter);
                        } else {
                            Toast.makeText(MainActivity.this, "Response is null", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Terjadi kesalahan: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );


        requestQueue.add(request);
    }
}
