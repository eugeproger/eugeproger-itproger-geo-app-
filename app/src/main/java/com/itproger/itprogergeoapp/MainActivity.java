package com.itproger.itprogergeoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itproger.itprogergeoapp.models.User;
import com.rengwuxian.materialedittext.MaterialEditText;

public class MainActivity extends AppCompatActivity {

    Button signIn, signUp;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference users;
    RelativeLayout mainLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signIn = findViewById(R.id.sing_in);
        signUp = findViewById(R.id.sing_up);
        mainLayout = findViewById(R.id.main_layout);

        firebaseAuth = firebaseAuth.getInstance();
        firebaseDatabase = firebaseDatabase.getInstance();
        users = firebaseDatabase.getReference("Users");

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSighUpWindow();
            }
        });
    }

    private void showSighUpWindow() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("Sign Up");
        alertDialog.setMessage("Fill all fields for registration");

        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View signUpWindow = layoutInflater.inflate(R.layout.sign_up, null);
        alertDialog.setView(signUpWindow);

        MaterialEditText email = signUpWindow.findViewById(R.id.email_field);
        MaterialEditText password = signUpWindow.findViewById(R.id.password_field);
        MaterialEditText name = signUpWindow.findViewById(R.id.name_field);
        MaterialEditText phone = signUpWindow.findViewById(R.id.phone_field);

        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (TextUtils.isEmpty(email.getText().toString())) {
                    Snackbar.make(mainLayout, "Enter your email", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(name.getText().toString())) {
                    Snackbar.make(mainLayout, "Enter your name", Snackbar.LENGTH_LONG).show();
                    return;
                }
                if (TextUtils.isEmpty(phone.getText().toString())) {
                    Snackbar.make(
                            mainLayout,
                            "Enter your phone number",
                            Snackbar.LENGTH_LONG
                    ).show();
                    return;
                }
                if (password.getText().toString().length() < 5) {
                    Snackbar.make(
                            mainLayout,
                            "Enter password that is longer than 5 symbols",
                            Snackbar.LENGTH_LONG
                    ).show();
                    return;
                }

                //User registration
                firebaseAuth.createUserWithEmailAndPassword(
                        email.getText().toString(),
                        password.getText().toString()
                ).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override
                    public void onSuccess(AuthResult authResult) {
                        User user = new User(
                                email.getText().toString(),
                                password.getText().toString(),
                                name.getText().toString(),
                                phone.getText().toString()
                        );

                        users.child(user.getEmail()).setValue(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Snackbar.make(
                                        mainLayout,
                                        "User is added",
                                        Snackbar.LENGTH_LONG
                                ).show();
                            }
                        });
                    }
                });
            }
        });
        alertDialog.show();
    }
}