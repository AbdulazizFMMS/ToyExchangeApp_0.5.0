package com.yigit.toyexchanger.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.yigit.toyexchanger.R;

public class LoginActivity extends AppCompatActivity {
    private CheckBox checkboxRememberMe;
    AlertDialog alertDialog;
    AlertDialog.Builder dialogBuilder;
    EditText loginUsername, loginPassword;

    Button loginButton,girisiatla;
    TextView signupRedirectText,sifre_unuttum;
    TextView googleilegir, facebookilegir;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor;
    String lastuser,lastpass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sharedPreferences = getSharedPreferences("MofidxMuhendis",0);
        // كود يحفظ البيانات لاستعادتها من صفحة البداية
        lastuser= sharedPreferences.getString("username","");
        lastpass= sharedPreferences.getString("pass","");
        loginUsername = findViewById(R.id.login_username);
        loginPassword = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.login_button);
        girisiatla = findViewById(R.id.girisiatla);
        signupRedirectText = findViewById(R.id.signupRedirectText);
        sifre_unuttum = findViewById(R.id.sifre_unuttum);
        checkboxRememberMe = findViewById(R.id.checkboxRememberMe);
        googleilegir = findViewById(R.id.googleilegir);
        facebookilegir = findViewById(R.id.faceilegir);

        loginUsername.setText(lastuser);
        loginPassword.setText(lastpass);

//        if (validateUsername() && validatePassword()) {
//            checkUser();
//        }

        // Kaydedilmiş Bilgileri Yükle
        loadSavedCredentials();

//        if ( validateUsername() && validatePassword() ) {
//            // Beni Hatırla İşaretliyse Kaydet
//            if (checkboxRememberMe.isChecked()) {
//                editor = sharedPreferences.edit();
//                editor.putString("username", username);
//                editor.putString("password", password);
//                editor.putBoolean("remember", true);
//                editor.apply();
//                Toast.makeText(LoginActivity.this, "Bilgiler kaydedildi!", Toast.LENGTH_SHORT).show();
//            } else {
//                // İşaretlenmediyse Kaydedilenleri Temizle
//                editor.clear();
//                editor.apply();
//            }
//        }

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateUsername() | !validatePassword()) {
                } else {
                    checkUser();
                }



            }
        });

        girisiatla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
            }
        });

        signupRedirectText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(intent);
            }
        });

        sifre_unuttum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showAlertDialog(R.layout.dialog_forgot);
            }
        });


        facebookilegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Şu anda bu özellik aktif değildir!", Toast.LENGTH_SHORT).show();
            }
        });

        googleilegir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Şu anda bu özellik aktif değildir!", Toast.LENGTH_SHORT).show();
            }
        });



    } // on Create

    private void showAlertDialog(int layout) {
        dialogBuilder = new AlertDialog.Builder(LoginActivity.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnReset);
        Button dialogButton1 = layoutView.findViewById(R.id.btnCancel);
        EditText et_dialogacil = layoutView.findViewById(R.id.emailBox);

        dialogBuilder.setView(layoutView);
        alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.show();

//        yollanacak112ye = "Acil Durumdayım Acil Yardıma Ihtiyacım Var\n\nAdress:\n" + maplink;
//        et_dialogacil.setText(yollanacak112ye);
//        Text_diaogacil.setText(""+stringhastabilgileri);


        dialogButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                alertDialog.dismiss();
            }
        });
        //Positive
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str =  et_dialogacil.getText().toString();
                if (str.equals("")){
                    alertDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "geçerli Mail adresi giriniz!", Toast.LENGTH_SHORT).show();
                }else {
                    alertDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Kayıtlı olan < "+str+" > mailinize gelen mesajlara bakabilirsiniz!", Toast.LENGTH_SHORT).show();
                }

            }
        });



    }


    public Boolean validateUsername() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username hanesi boş olamaz!");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }
    public Boolean validatePassword(){
        String val = loginPassword.getText().toString();
        if (val.isEmpty()) {
            loginPassword.setError("Password hanesi boş olamaz!");
            return false;
        } else {
            loginPassword.setError(null);
            return true;
        }
    }
    public void checkUser(){
        String userUsername = loginUsername.getText().toString().trim();
        String userPassword = loginPassword.getText().toString().trim();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");
        Query checkUserDatabase = reference.orderByChild("username").equalTo(userUsername);
        checkUserDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    loginUsername.setError(null);
                    String passwordFromDB = snapshot.child(userUsername).child("password").getValue(String.class);
                    if (passwordFromDB.equals(userPassword)) {
                        loginUsername.setError(null);
                        String nameFromDB = snapshot.child(userUsername).child("name").getValue(String.class);
                        String emailFromDB = snapshot.child(userUsername).child("email").getValue(String.class);
                        String usernameFromDB = snapshot.child(userUsername).child("username").getValue(String.class);
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("name", nameFromDB);
                        intent.putExtra("email", emailFromDB);
                        intent.putExtra("username", usernameFromDB);
                        intent.putExtra("password", passwordFromDB);

                        editor = sharedPreferences.edit();
                        editor.putString("username",usernameFromDB);
                        editor.putString("pass",passwordFromDB);
                        if (checkboxRememberMe.isChecked()) {
                            editor.putBoolean("remember", true);
//                            editor.apply();
                            Toast.makeText(LoginActivity.this, "Bilgiler kaydedildi!", Toast.LENGTH_SHORT).show();
                        } else {
                            editor.putBoolean("remember", false);
                            // İşaretlenmediyse Kaydedilenleri Temizle
//                            editor.apply();
                        }
                        editor.commit();

                        startActivity(intent);
                        finish();
                    } else {
                        loginPassword.setError("şifre yanlıştır!");
                        loginPassword.requestFocus();
                    }
                } else {
                    loginUsername.setError("Bu User mecüt değil!");
                    loginUsername.requestFocus();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }

        });
    }



    private void loadSavedCredentials() {
        // Kaydedilen bilgileri kontrol et ve yükle
        if (sharedPreferences.getBoolean("remember", false)) {
            String savedUsername = sharedPreferences.getString("username", "");
            String savedPassword = sharedPreferences.getString("pass", "");

            loginUsername.setText(savedUsername);
            loginPassword.setText(savedPassword);


            checkboxRememberMe.setChecked(true);

            if (validateUsername() && validatePassword()) {
                checkUser();
            }

        }
    }


}