package com.example.testapijakarta;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class DataSekolahAdapter  extends RecyclerView.Adapter<DataSekolahAdapter.ViewHolder> {

    List<DataSekolah> listData;
    Activity activity;

    public DataSekolahAdapter(List<DataSekolah> listData, Activity activity) {
        this.listData = listData;
        this.activity = activity;
    }

    @NonNull
    @Override
    public DataSekolahAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
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
                final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                // Get the layout inflater
                LayoutInflater inflater = activity.getLayoutInflater();

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
                        activity.startActivity(mapIntent);
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
                        activity.startActivity(mapIntent);
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
