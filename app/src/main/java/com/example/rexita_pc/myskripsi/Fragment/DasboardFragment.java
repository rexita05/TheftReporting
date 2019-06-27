package com.example.rexita_pc.myskripsi.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rexita_pc.myskripsi.Adapter.DataPencurianAdapter;
import com.example.rexita_pc.myskripsi.Model.DataPencurian;
import com.example.rexita_pc.myskripsi.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.android.volley.VolleyLog.TAG;

public class DasboardFragment extends Fragment {
    @BindView(R.id.rvData)
    RecyclerView rvData;

    float bobotKerugian = 0;
    float bobotWaktu = 0;
    DatabaseReference databaseReference;
    RecyclerView.Adapter adapter;
    DataPencurian dataPencurian;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<DataPencurian> dataPencurianList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //finding listview
        ButterKnife.bind(this,rootView);
        layoutManager = new LinearLayoutManager(getActivity());
        rvData.setLayoutManager(layoutManager);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bobotKerugian();
    }

    private void bobotKerugian(){
        databaseReference.child("data_bobotkerugian").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float kerugian = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    kerugian = postSnapshot.child("kerugian").getValue(Float.class);
                }
                bobotKerugian  = kerugian;
                bobotWaktu();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getView(), "Data Kosong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void bobotWaktu(){
        databaseReference.child("data_bobotwaktu").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                float waktu = 0;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    waktu = postSnapshot.child("waktu").getValue(Float.class);
                }
                bobotWaktu  = waktu;
                view();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getView(), "Data Kosong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void view(){
        databaseReference.child("data_pencurian").orderByValue().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                dataPencurianList = new ArrayList<>();

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                    dataPencurian= postSnapshot.getValue(DataPencurian.class);
                    dataPencurian.setKey(postSnapshot.getKey());
                    if (dataPencurian.getStatus() == 1)

                        dataPencurianList.add(dataPencurian);
                        Collections.sort(dataPencurianList, new Comparator<DataPencurian>() {
                            @Override
                            public int compare(DataPencurian o1, DataPencurian o2) {
                                return Double.valueOf(o2.getRangking()).compareTo(Double.valueOf(o1.getRangking()));
                            }
                        });

                }
                //Start Metode
                long jml_kerugian = 0;
                long jml_waktu = 0;
                for (int i=0;i <dataPencurianList.size();i++){
                    jml_kerugian += (long) Math.pow(Integer.parseInt(dataPencurianList.get(i).getKerugian()),2);
                    jml_waktu += (long) Math.pow(Integer.parseInt(dataPencurianList.get(i).getInterval()),2);

                }

                ArrayList<DataPencurian> rank =new ArrayList<DataPencurian>();
                for (int j=0;j < dataPencurianList.size();j++){
                    double kerugian = Float.parseFloat(dataPencurianList.get(j).getKerugian()) /
                            (double) Math.sqrt(jml_kerugian)*bobotKerugian;
//                    Log.d(TAG, "Normalisasi Kerugian*Bobot: "+String.valueOf(kerugian));

                    double waktu = Float.parseFloat(dataPencurianList.get(j).getInterval()) /
                            (double) Math.sqrt(jml_waktu)*bobotWaktu*-1;
//                    Log.d(TAG, "Normalisasi Waktu*Bobot: "+String.valueOf(waktu));
                    double hasil = kerugian+waktu;
//                    Log.d(TAG, "Perangkingan: " + hasil);

                    dataPencurian = new DataPencurian(dataPencurianList.get(j).getWaktu(), dataPencurianList.get(j).getTanggal(),
                            dataPencurianList.get(j).getKejadian(), dataPencurianList.get(j).getLokasi(),
                            dataPencurianList.get(j).getKecamatan(),dataPencurianList.get(j).getStatus(),
                            dataPencurianList.get(j).getKerugian(),dataPencurianList.get(j).getInterval(),
                            hasil,dataPencurianList.get(j).getKey());
                    rank.add(dataPencurian);
                }

                Iterator itr=rank.iterator();
                while(itr.hasNext()){
                    DataPencurian st=(DataPencurian) itr.next();
                    Log.d(TAG, "onDataChange: "+st.getRangking());
                    databaseReference.child("data_pencurian").child(st.getKey()).
                            setValue(st).
                            addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    /**
                                     * Baris kode yang akan dipanggil apabila proses update barang sukses
                                     */
                                }
                            });
                }
                adapter = new DataPencurianAdapter(getActivity(), dataPencurianList);
                rvData.setAdapter(adapter);
            }
            //End Metode

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getView(), "Data Kosong", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

//    private void updatePerangkingan(DataPencurian dataPencurian) {
//        databaseReference.child("data_pencurian").child(dataPencurian.getKey()).setValue(dataPencurian).
//                addOnSuccessListener(getActivity(), new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        /**
//                         * Baris kode yang akan dipanggil apabila proses update barang sukses
//                         */
//                    }
//                });
//    }
}
