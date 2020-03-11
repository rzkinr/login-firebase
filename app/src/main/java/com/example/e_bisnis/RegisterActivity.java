package com.example.e_bisnis;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    private EditText namadaftar, emaildaftar, katasandidaftar;
    private Button Btn_daftar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        namadaftar = (EditText)findViewById(R.id.namadaftar);
        emaildaftar = (EditText)findViewById(R.id.emaildaftar);
        katasandidaftar = (EditText)findViewById(R.id.katasandidaftar);

        Btn_daftar = (Button)findViewById(R.id.Btn_daftar1);
        Btn_daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });


    }

    private void register(){
        final String nama = namadaftar.getText().toString();
        String email = emaildaftar.getText().toString();
        String katasandi = katasandidaftar.getText().toString();

        if (nama.isEmpty() && email.isEmpty() && katasandi.isEmpty()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Peringatan");
            builder.setMessage("Mohon isi e-mail dan password!");
            builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setCancelable(false);
            builder.show();
        }else {
            mAuth.createUserWithEmailAndPassword(email, katasandi)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = mAuth.getCurrentUser();
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(nama)
                                        .setPhotoUri(Uri.parse("https://randomuser.me/api/portraits/men/76.jpg"))
                                        .build();

                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(RegisterActivity.this, "Login Berhasil", Toast.LENGTH_SHORT).show();
                                                    Intent homepage = new Intent(RegisterActivity.this, HomeActivity.class);
                                                    startActivity(homepage);
                                                    finish();
                                                }
                                            }
                                        });
                            } else {
                                // If sign in fails, display a message to the user.
                                AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                                builder.setTitle("Peringatan");
                                builder.setMessage("Daftar gagal!");
                                builder.setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                                builder.setCancelable(false);
                                builder.show();
                            }

                            // ...
                        }
                    });
        }


    }
}
