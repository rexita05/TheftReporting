package com.example.rexita_pc.myskripsi.Fragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.rexita_pc.myskripsi.Activity.LoginActivity;
import com.example.rexita_pc.myskripsi.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogoutFragment extends Fragment {

        Button btnLogout,btnLogin;
        ImageView mImage;
        private FirebaseAuth mAuth;
        private GoogleSignInClient mGoogleSignInClient;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_logout, container, false);

            mImage = (ImageView)rootView.findViewById(R.id.user);
            btnLogout = (Button)rootView.findViewById(R.id.btnLogout);
            btnLogin = (Button)rootView.findViewById(R.id.btnLogin);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

            mAuth = FirebaseAuth.getInstance();
            FirebaseUser currentUser = mAuth.getCurrentUser();
            btnLogout.setVisibility(View.INVISIBLE);
            if (currentUser != null){
                Uri url = mAuth.getCurrentUser().getPhotoUrl();
                Glide.with(getActivity()).load(url).thumbnail(0.5f).into(mImage);
                btnLogout.setVisibility(View.VISIBLE);
                btnLogin.setVisibility(View.INVISIBLE);
            }else {
                btnLogout.setVisibility(View.INVISIBLE);
                btnLogin.setVisibility(View.VISIBLE);
            }



            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            btnLogout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    signOut();
                }
            });
            btnLogin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().finish();
                }
            });
        }

        private void signOut() {
            // Firebase sign out

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Apa anda yakin ingin keluar ?")
                    .setCancelable(false)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //do things
                            mAuth.signOut();
                            // Google sign out
                            mGoogleSignInClient.signOut().addOnCompleteListener(getActivity(),
                                    new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            startActivity(new Intent(getActivity(), LoginActivity.class));
                                            getActivity().finish();
                                        }
                                    });
                        }
                    });
            AlertDialog alert = builder.create();
            alert.show();


        }
}
