package com.example.rexita_pc.myskripsi.Fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.rexita_pc.myskripsi.Activity.LoginActivity;
import com.example.rexita_pc.myskripsi.Activity.MenuActivity;
import com.example.rexita_pc.myskripsi.Model.DataPencurian;
import com.example.rexita_pc.myskripsi.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.text.TextUtils.isEmpty;
import static com.android.volley.VolleyLog.TAG;

public class FragmentAddData extends Fragment {

    @BindView(R.id.txtWaktu)
    TextView txtWaktu;
    @BindView(R.id.txtTanggal)
    TextView txtTanggal;
    @BindView(R.id.txtKejadian)
    TextView txtKejadian;
    @BindView(R.id.txtLokasi)
    TextView txtLokasi;
    @BindView(R.id.txtKerugian)
    EditText txtKerugian;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.spinnerKecamatan)
    Spinner spinnerKecamatan;
    private FirebaseAuth mAuth;
    List<String> list = new ArrayList<>();
    Calendar c;
    ProgressDialog pd;
    private int mYear, mMonth, mDay, mHour, mMinute;
    long interval;

    DatabaseReference databaseReference;
    private String kecamatan = null;
    String kejadian = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_add_data, container, false);
        //finding listview
        ButterKnife.bind(this, rootView);
        pd = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        txtKerugian.addTextChangedListener(onTextChangedListener());
        c = Calendar.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //mengambil data pada tabel data_kecamatan
        databaseReference.child("data_kecamatan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    String areaName = postSnapshot.child("kecamatan").getValue(String.class);
                    list.add(areaName);
                }
                //perintah membuat spinner dropdown adapter
                final ArrayAdapter<String> areasAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, list);
                areasAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinnerKecamatan.setAdapter(areasAdapter);
                spinnerKecamatan.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        Toast.makeText(getActivity(), "Selected User: "+list.get(position) ,Toast.LENGTH_SHORT).show();
                        String selectedItemText = (String) parent.getItemAtPosition(position);
                        if (position > 0) {
                            Snackbar.make(getView(), "Applying : " + selectedItemText, Snackbar.LENGTH_SHORT).show();
                        }
                        kecamatan = list.get(position);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Snackbar.make(getView(), "Data Kosong", Snackbar.LENGTH_SHORT).show();
            }
        });

        txtWaktu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                txtWaktu.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                // Menampilkan Dialog Waktu
                timePickerDialog.show();
            }
        });

        txtTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);

                // Launch Date Picker Dialog
                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                c.set(Calendar.YEAR, year);
                                c.set(Calendar.MONTH, monthOfYear);
                                c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                Date date = new Date();
                                SimpleDateFormat myFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
                                String inputString1 = myFormat.format(date);
                                String inputString2 = myFormat.format(c.getTime());

                                try {
                                    Date date1 = myFormat.parse(inputString1);
                                    Date date2 = myFormat.parse(inputString2);
                                    long diff = date2.getTime() - date1.getTime();
                                    Log.d(TAG, "onClick: " + Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS)));
                                    interval = Math.abs(TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                txtTanggal.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                // Menampilkan Dialog Tanggal
                datePickerDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser currentUser = mAuth.getCurrentUser();
                pd.setMessage("Please Wait..");
                pd.show();
                if (currentUser == null) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage("Silahkan Login terlebih dahulu!")
                            .setCancelable(false)
//                            .setNegativeButton("Batal", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//
//                                }
//                            })
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    //do things
                                    startActivity(new Intent(getActivity(), LoginActivity.class));
                                    getActivity().finish();
                                    pd.dismiss();
                                }
                            });
                    AlertDialog alert = builder.create();
                    alert.show();


                } else if (!isEmpty(txtWaktu.getText().toString()) && !isEmpty(txtTanggal.getText().toString()) && !isEmpty(txtKejadian.getText().toString())
                        && !isEmpty(txtLokasi.getText().toString()) && !isEmpty(txtKerugian.getText().toString()) && kecamatan != null) {
                    kejadian = txtKejadian.getText().toString();
                    String kerugian = txtKerugian.getText().toString();
                    String names = kerugian.replaceAll(",", "");
                    String waktu = String.valueOf(interval);
                    submitDataPencurian(new DataPencurian(txtWaktu.getText().toString(), txtTanggal.getText().toString(), txtKejadian.getText().toString()
                            , txtLokasi.getText().toString(), kecamatan, 0, names, waktu, 0, null));
                    Toast.makeText(getActivity(), "Applying " + kecamatan, Toast.LENGTH_SHORT).show();
                } else {
                    pd.dismiss();
                    Snackbar.make(getView(), "Data tidak boleh kosong", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    //cek data sebelum di tambahkan dalam firebase database
    private void submitDataPencurian(DataPencurian dataPencurian) {

        databaseReference.child("data_pencurian").push().setValue(dataPencurian).addOnSuccessListener(getActivity(),
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Snackbar.make(getView(), "Data berhasil ditambahkan", Snackbar.LENGTH_SHORT).show();
                        txtWaktu.setText("");
                        txtTanggal.setText("");
                        txtKejadian.setText("");
                        txtLokasi.setText("");
                        txtKerugian.setText("");
                        sendFCMPush();
                        pd.dismiss();
//                        spinKecamatan.setText("");
                    }
                });

    }

    private void sendFCMPush() {

        final String Legacy_SERVER_KEY = "AAAAfB-91dA:APA91bGQcVaQKJL641OwljhtbCRweMUg4-CuvwA-CUUIPBCKOynDFUCDtJYxPdfUdqW0GciWWn0RdPc64dJlm_kw5XlRa9ITd1teOGnFyilYlGMh3QQBSDyrUCAnR2OXkgcVt_umbk4O";
        String msg = kejadian;
        String title = "Theft Reporting";
        String token = "cwj03uHf47k:APA91bH8uOqeIjO7m77qYHuncmDST0O4kknUqjQJC8y2buOYMtFiGVnxo4cJJLLa--5bY6Qaf8WlwMp-LaL4Mw6tFh97U7J1cz9D-IcdE8jJ8_8q7vw5McUlyyCPdAPrRHP2nBJ7RkHy";
// xiaomi iko        String token = "dZ4UGBy2Exs:APA91bFH-2zQ1Be66ttiIWH00vuWcdctNWt6sOmPF8D4R-mMVD-Ym0R9RkXYP5eGsLj2mtIDmDvRGz1PRGyRyGYHAawJTUHQmYWHcrd21NRIKRIC4xlR-jcHrDTIMLc2UREX_ZyhKj77";
        JSONObject obj = null;
        JSONObject objData = null;
        JSONObject dataobjData = null;

        try {
            obj = new JSONObject();
            objData = new JSONObject();

            objData.put("body", msg);
            objData.put("title", title);
            objData.put("sound", "default");
            objData.put("icon", "icon_name"); //   icon_name image must be there in drawable
            objData.put("tag", token);
            objData.put("priority", "high");

            dataobjData = new JSONObject();
            dataobjData.put("text", msg);
            dataobjData.put("title", title);

            obj.put("to", token);
            //obj.put("priority", "high");

            obj.put("notification", objData);
            obj.put("data", dataobjData);
            Log.e("!_@rj@_@@_PASS:>", obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Legacy_SERVER_KEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        int socketTimeout = 1000 * 60;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }

    private TextWatcher onTextChangedListener() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                txtKerugian.removeTextChangedListener(this);

                try {
                    String originalString = s.toString();

                    Long longval;
                    if (originalString.contains(",")) {
                        originalString = originalString.replaceAll(",", "");
                    }
                    longval = Long.parseLong(originalString);

                    DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);
                    formatter.applyPattern("#,###,###,###");
                    String formattedString = formatter.format(longval);

                    //setting text after format to EditText
                    txtKerugian.setText(formattedString);
                    txtKerugian.setSelection(txtKerugian.getText().length());
                } catch (NumberFormatException nfe) {
                    nfe.printStackTrace();
                }
                txtKerugian.addTextChangedListener(this);
            }
        };
    }

}
