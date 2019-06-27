package com.example.rexita_pc.myskripsi.Fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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

public class LogoutFragment extends Fragment {

        Button btnLogout;
        ImageView mImage;
        private FirebaseAuth mAuth;
        private GoogleSignInClient mGoogleSignInClient;

        @Nullable
        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            // Inflate the layout for this fragment
            View rootView = inflater.inflate(R.layout.fragment_logout, container, false);

            mImage = (ImageView)rootView.findViewById(R.id.user);
            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestIdToken(getString(R.string.default_web_client_id))
                    .requestEmail()
                    .build();

            mGoogleSignInClient = GoogleSignIn.getClient(getActivity(), gso);

            mAuth = FirebaseAuth.getInstance();
            Uri url = mAuth.getCurrentUser().getPhotoUrl();
            Glide.with(getActivity()).load(url).thumbnail(0.5f).into(mImage);
            btnLogout = (Button)rootView.findViewById(R.id.btnLogout);

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
        }

        private void signOut() {
            // Firebase sign out
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
}
