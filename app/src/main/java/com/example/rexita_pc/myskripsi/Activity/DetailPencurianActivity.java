package com.example.rexita_pc.myskripsi.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.rexita_pc.myskripsi.Model.DataPencurian;
import com.example.rexita_pc.myskripsi.R;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailPencurianActivity extends AppCompatActivity {

    @BindView(R.id.txtWaktu) TextView txtWaktu;
    @BindView(R.id.txtKejadian) TextView txtKejadian;
    @BindView(R.id.txtLokasi) TextView txtLokasi;
    @BindView(R.id.txtKecamatan) TextView txtKecamatan;
    @BindView(R.id.txtKerugian) TextView txtKerugian;
    @BindView(R.id.txtTanggal) TextView txtTanggal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_pencurian);
        ButterKnife.bind(this);
        DataPencurian dataPencurian = (DataPencurian) getIntent().getSerializableExtra("data");
        if(dataPencurian!=null){
            txtWaktu.setText("Waktu                 : "+dataPencurian.getWaktu());
            txtTanggal.setText("Tanggal              : "+dataPencurian.getTanggal());
            txtKejadian.setText("Kejadian             : "+dataPencurian.getKejadian());
            txtLokasi.setText("Lokasi                 : "+dataPencurian.getLokasi());
            txtKecamatan.setText("Kecamatan       : "+dataPencurian.getKecamatan());
//            txtKerugian.setText("Kerugian            : "+dataPencurian.getKerugian());
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(',');
            DecimalFormat decimalFormat = new DecimalFormat("Rp ###,###,###,###", symbols);
            String prezzo = decimalFormat.format(Integer.parseInt(dataPencurian.getKerugian()));
            txtKerugian.setText("Kerugian            : "+prezzo);
//            if (dataPencurian.getKerugian() == null ){
//                txtKerugian.setText("Kerugian            : -");
//            }
//            else {
//                txtKerugian.setText("Kerugian            : Rp."+dataPencurian.getKerugian() );
//            }
        }
    }
    public static Intent getActIntent(Activity activity){
        return new Intent(activity, DetailPencurianActivity.class);
    }
}
