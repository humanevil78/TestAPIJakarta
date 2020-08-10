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
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .authenticator(new Authenticator() {
//                    @Override
//                    public Request authenticate(Route route, Response response) throws IOException {
//                        return response.request().newBuilder()
//                                .header("Authorization", "v3pP0k7wzdvXvNYUrFBJ/h74wiqRe2je+/i9Y5Zl0AQpeR3nzokuRbCdLt6JXams")
//                                .build();
//                    }
//                })
//                .build();

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
                            DataSekolahAdapter dsa = new DataSekolahAdapter(listData);
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
        DataSekolahAdapter dsa = new DataSekolahAdapter(listFind);
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

    public class DataSekolah {
        private String id;
        private String namaSekolah;
        private String alamat;
        private String kecamatan;
        private String kota;
        private String jenjang;
        private String telepon;
        private String lat;
        private String lng;

        public String getTelepon() {
            return telepon;
        }

        public void setTelepon(String telepon) {
            this.telepon = telepon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNamaSekolah() {
            return namaSekolah;
        }

        public void setNamaSekolah(String namaSekolah) {
            this.namaSekolah = namaSekolah;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public String getKecamatan() {
            return kecamatan;
        }

        public void setKecamatan(String kecamatan) {
            this.kecamatan = kecamatan;
        }

        public String getKota() {
            return kota;
        }

        public void setKota(String kota) {
            this.kota = kota;
        }

        public String getJenjang() {
            return jenjang;
        }

        public void setJenjang(String jenjang) {
            this.jenjang = jenjang;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLng() {
            return lng;
        }

        public void setLng(String lng) {
            this.lng = lng;
        }
    }

    public class DataSekolahAdapter extends RecyclerView.Adapter<DataSekolahAdapter.ViewHolder> {

        List<DataSekolah> listData;

        public DataSekolahAdapter(List<DataSekolah> listData) {
            this.listData = listData;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_data_sekolah, viewGroup, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

            final DataSekolah data = listData.get(i);
            viewHolder.tvNamaSekolah.setText(data.getNamaSekolah());
            viewHolder.tvKecamatan.setText(data.getKecamatan());
            viewHolder.tvKota.setText(data.getKota());

            viewHolder.llCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    // Get the layout inflater
                    LayoutInflater inflater = MainActivity.this.getLayoutInflater();

                    View dialogView = inflater.inflate(R.layout.alert_detail_sekolah, null);

                    builder.setView(dialogView);
                    builder.setCancelable(true);

                    TextView tvAlertNamaSekolah = (TextView) dialogView.findViewById(R.id.tv_alert_nama_sekolah);
                    TextView tvAlertAlamat = (TextView) dialogView.findViewById(R.id.tv_alert_alamat);
                    TextView tvAlertKecamatan = (TextView) dialogView.findViewById(R.id.tv_alert_kecamatan);
                    TextView tvAlertKota = (TextView) dialogView.findViewById(R.id.tv_alert_kota);
                    TextView tvAlertTelepon = (TextView) dialogView.findViewById(R.id.tv_alert_telepon);
                    Button btnAlertLetak = (Button) dialogView.findViewById(R.id.btn_alert_mark);
                    Button btnAlertArah = (Button) dialogView.findViewById(R.id.btn_alert_nav);

                    tvAlertNamaSekolah.setText(data.getNamaSekolah());
                    tvAlertAlamat.setText(data.getAlamat());
                    tvAlertKecamatan.setText(data.getKecamatan());
                    tvAlertKota.setText(data.getKota());
                    tvAlertTelepon.setText(data.getTelepon());

                    final String lat = data.getLat();
                    final String lng = data.getLng();

                    btnAlertArah.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create a Uri from an intent string. Use the result to create an Intent.
                            Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lng+"");

                            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            // Make the Intent explicit by setting the Google Maps package
                            mapIntent.setPackage("com.google.android.apps.maps");

                            // Attempt to start an activity that can handle the Intent
                            startActivity(mapIntent);
                        }
                    });

                    btnAlertLetak.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Create a Uri from an intent string. Use the result to create an Intent.
                            Uri gmmIntentUri = Uri.parse("google.streetview:cbll="+lat+","+lng+"");

                            // Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
                            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                            // Make the Intent explicit by setting the Google Maps package
                            mapIntent.setPackage("com.google.android.apps.maps");

                            // Attempt to start an activity that can handle the Intent
                            startActivity(mapIntent);
                        }
                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

        }

        @Override
        public int getItemCount() {
            return listData.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {

            TextView tvNamaSekolah, tvKecamatan, tvKota;
            LinearLayout llCard;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvNamaSekolah = itemView.findViewById(R.id.tv_nama_sekolah);
                tvKecamatan = itemView.findViewById(R.id.tv_kecamatan);
                tvKota = itemView.findViewById(R.id.tv_kota);
                llCard = itemView.findViewById(R.id.ll_card);
            }
        }
    }
}
