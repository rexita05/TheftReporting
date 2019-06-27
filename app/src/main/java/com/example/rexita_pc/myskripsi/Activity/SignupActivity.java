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

import com.example.rexita_pc.myskripsi.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;

public class SignupActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword    ";
    private FirebaseAuth mAuth;

    @BindView(R.id.input_name) EditText _nameText;
//    @BindView(R.id.input_reEnterPassword) EditText _reEnterPasswordText;
    @BindView(R.id.input_email) EditText _emailText;
    @BindView(R.id.input_password) EditText _passwordText;
    @BindView(R.id.btn_signup) Button _signupButton;
    @BindView(R.id.link_login) TextView _loginLink;

    android.app.AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.bind(this);

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
