package com.example.testapijakarta;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    List<DataSekolah> listData, listFind;
    RecyclerView rvDataSekolah;
    EditText edtFindName;
    RadioGroup rgJenjang;
    String jenjang = "sd";
    public String text = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvDataSekolah = findViewById(R.id.rv_data_sekolah);
        rgJenjang = findViewById(R.id.rg_jenjang);
        edtFindName = findViewById(R.id.edt_find_name);

        rvDataSekolah.setHasFixedSize(true); //agar recyclerView tergambar lebih cepat
        rvDataSekolah.setLayoutManager(new LinearLayoutManager(MainActivity.this)); //menset layout manager sebagai LinearLayout(scroll kebawah)
        listData = new ArrayList<>();
        listFind = new ArrayList<>();
        AndroidNetworking.initialize(MainActivity.this);
        getData();
        rgJenjang.setOnCheckedChangeListener(this);
        edtFindName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                text = editable.toString();
                getFind();
            }
        });
    }

    private void getData() {
        AndroidNetworking.get("https://api.jakarta.go.id/v1/sekolah")
                .addHeaders("Authorization", "v3pP0k7wzdvXvNYUrFBJ/h74wiqRe2je+/i9Y5Zl0AQpeR3nzokuRbCdLt6JXams")
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("WOW", String.valueOf(response));
                        try {
                            listData.clear();
                            JSONArray arrayData = response.getJSONArray("data");
                            Log.d("WOW2", String.valueOf(arrayData));
                            Log.d("WOW2", String.valueOf(arrayData.length()));
                            for (int i = 0; i < arrayData.length(); i++) {
                                DataSekolah data = new DataSekolah();
                                if (jenjang.equals(arrayData.getJSONObject(i).get("jenjang").toString().toLowerCase())) {
                                        data.setId(arrayData.getJSONObject(i).get("id").toString());
                                        data.setNamaSekolah(arrayData.getJSONObject(i).get("nama_sekolah").toString());
                                        data.setAlamat(arrayData.getJSONObject(i).get("alamat").toString());
                                        data.setKecamatan(arrayData.getJSONObject(i).get("kecamatan").toString());
                                        data.setKota(arrayData.getJSONObject(i).get("kota").toString());
                                        data.setTelepon(arrayData.getJSONObject(i).get("telp").toString());
                                        data.setJenjang(arrayData.getJSONObject(i).get("jenjang").toString());
                                        data.setLat(arrayData.getJSONObject(i).get("lat").toString());
                                        data.setLng(arrayData.getJSONObject(i).get("lng").toString());
                                        listData.add(data);
                                    }
//                                List<DataSekolah> listFind;
                                Log.d("WOW3", String.valueOf(listData.size()));
                            }
                            DataSekolahAdapter dsa = new DataSekolahAdapter(listData, MainActivity.this);
                            dsa.notifyDataSetChanged();
                            rvDataSekolah.setAdapter(dsa);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.d("WOWERROR", String.valueOf(anError));
                    }
                });
    }

    private void getFind(){
        listFind.clear();
        for(DataSekolah hello : listData){
            if(hello.getNamaSekolah().toLowerCase().matches(".*"+text.toLowerCase()+".*")){
                hello.setId(hello.getId());
                hello.setNamaSekolah(hello.getNamaSekolah());
                hello.setAlamat(hello.getAlamat());
                hello.setKecamatan(hello.getKecamatan());
                hello.setKota(hello.getKota());
                hello.setTelepon(hello.getTelepon());
                hello.setJenjang(hello.getJenjang());
                hello.setLat(hello.getLat());
                hello.setLng(hello.getLng());
                listFind.add(hello);
            }
        }
        Log.d("WOW4", String.valueOf(listFind.size()));
        DataSekolahAdapter dsa = new DataSekolahAdapter(listFind, MainActivity.this);
        dsa.notifyDataSetChanged();
        rvDataSekolah.setAdapter(dsa);
    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        switch(i){
            case R.id.rb_sd:
                this.jenjang = "sd";
                getData();
                break;
            case R.id.rb_smp:
                this.jenjang = "smp";
                getData();
                break;
            case R.id.rb_sma:
                this.jenjang = "sma";
                getData();
                break;
        }
    }
}
