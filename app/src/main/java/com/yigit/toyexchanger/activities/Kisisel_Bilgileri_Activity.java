package com.yigit.toyexchanger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yigit.toyexchanger.R;


public class Kisisel_Bilgileri_Activity extends AppCompatActivity {

    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor;
    String lastuser1,lastpass1;
    TextView profileName, profileEmail, profileUsername, profilePassword;
    TextView titleName, titleUsername;
    Button editProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        sharedPreferences = getSharedPreferences("MofidxMuhendis",0);
        // كود يحفظ البيانات لاستعادتها من صفحة البداية
        lastuser1= sharedPreferences.getString("username","");
        lastpass1= sharedPreferences.getString("pass","");


        profileName = findViewById(R.id.profileName);
        profileEmail = findViewById(R.id.profileEmail);
        profileUsername = findViewById(R.id.profileUsername);
        profilePassword = findViewById(R.id.profilePassword);
        titleName = findViewById(R.id.titleName);
        titleUsername = findViewById(R.id.titleUsername);
        editProfile = findViewById(R.id.editButton);
        showAllUserData();
        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passUserData();
            }
        });




    }


    public void showAllUserData(){

checkUser();
//        Intent intent = getIntent();
//        String nameUser = intent.getStringExtra("name");
//        String emailUser = intent.getStringExtra("email");
//        String usernameUser = intent.getStringExtra("username");
//        String passwordUser = intent.getStringExtra("password");


    }
    public void passUserData(){
        String userUsername = profileUsername.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(lastuser1);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String nameFromDB = snapshot.child(lastuser1).child("name").getValue(String.class);
                    String emailFromDB = snapshot.child(lastuser1).child("email").getValue(String.class);
                    String usernameFromDB = snapshot.child(lastuser1).child("username").getValue(String.class);
                    String passwordFromDB = snapshot.child(lastuser1).child("password").getValue(String.class);
                    Intent intent = new Intent(Kisisel_Bilgileri_Activity.this, EditProfileActivity.class);
                    intent.putExtra("name", nameFromDB);
                    intent.putExtra("email", emailFromDB);
                    intent.putExtra("username", usernameFromDB);
                    intent.putExtra("password", passwordFromDB);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }



    public void checkUser(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(lastuser1);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
//                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(lastuser1).child("password").getValue(String.class);
                    if (passwordFromDB.equals(lastpass1)) {
//                        loginUsername.setError(null);
                        String nameFromDB = snapshot.child(lastuser1).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(lastuser1).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(lastuser1).child("username").getValue(String.class);
//                        Intent intent = new Intent(LoginActivity.this, BaseMainActivity.class);
//                        intent.putExtra("name", nameFromDB);
//                        intent.putExtra("email", emailFromDB);
//                        intent.putExtra("username", usernameFromDB);
//                        intent.putExtra("password", passwordFromDB);

                        titleName.setText(nameFromDB);
                        titleUsername.setText(usernameFromDB);
                        profileName.setText(nameFromDB);
                        profileEmail.setText(emailFromDB);
                        profileUsername.setText(usernameFromDB);
                        profilePassword.setText(passwordFromDB);
//                        editor = sharedPreferences.edit();
//                        editor.putString("username",usernameFromDB);
//                        editor.putString("pass",passwordFromDB);
//                        editor.commit();

                    } else {
//                        loginPassword.setError("şifre yanlıştır!");
//                        loginPassword.requestFocus();
                    }
                } else {
//                    loginUsername.setError("Bu User mecüt değil!");
//                    loginUsername.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }



}