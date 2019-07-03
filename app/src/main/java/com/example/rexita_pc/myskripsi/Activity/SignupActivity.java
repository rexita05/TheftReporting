package com.example.rexita_pc.myskripsi.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.rexita_pc.myskripsi.Model.mUser;
import com.example.rexita_pc.myskripsi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword    ";
    private FirebaseAuth mAuth;
//    private DatabaseReference databaseReference;

    @BindView(R.id.input_name) EditText _nameText;
    @BindView(R.id.input_address) EditText _addressText;
    @BindView(R.id.input_number) EditText _numberText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
//    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    android.app.AlertDialog dialog;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        mAuth = FirebaseAuth.getInstance();
        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount(_emailText.getText().toString(),
                        _passwordText.getText().toString());
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.push_left_in, R.anim.push_left_out);
            }
        });
    }

    private void createAccount(String email, String password) {
        if (!validateForm()) {
            return;
        }
        dialog = new SpotsDialog(this);
        dialog.show();
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                            mUser userInfo = new mUser();
                            userInfo.setUid(user.getUid());
                            userInfo.setName(_nameText.getText().toString());
                            userInfo.setAddress(_addressText.getText().toString());
                            userInfo.setEmail(_emailText.getText().toString());
                            userInfo.setNumber(_numberText.getText().toString());
                            databaseReference.child(user.getUid()).setValue(userInfo);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Snackbar.make(getCurrentFocus(), "Authentication failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }
                        // [START_EXCLUDE]
                        dialog.dismiss();
                        // [END_EXCLUDE]
                    }
                });
        // [END create_user_with_email]
    }

    private boolean validateForm() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        if (TextUtils.isEmpty(name)|| name.length() < 3){
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

        String address = _addressText.getText().toString();
        if (TextUtils.isEmpty(address)|| address.length() <5){
            _addressText.setError("at least 5 characters");
            valid = false;
        } else {
            _addressText.setError(null);
        }

        String number = _numberText.getText().toString();
        if (TextUtils.isEmpty(number)|| number.length() <11){
            _numberText.setError("at least 11 characters");
            valid = false;
        } else {
            _numberText.setError(null);
        }

        String email = _emailText.getText().toString();
        if (TextUtils.isEmpty(email)|| !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        String password = _passwordText.getText().toString();
        if (TextUtils.isEmpty(password)) {
            _passwordText.setError("please input password");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

//        String reenterpassword = _reEnterPasswordText.getText().toString();
//        if (TextUtils.isEmpty(reenterpassword)|| !(_reEnterPasswordText.equals(password))){
//            _reEnterPasswordText.setError("password do not match");
//            valid = false;
//        } else {
//            _reEnterPasswordText.setError(null);
//        }
        return valid;
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        }
    }
}
