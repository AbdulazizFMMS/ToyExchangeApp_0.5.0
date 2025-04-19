package com.yigit.toyexchanger.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.adapters.RecyclerTouchListener;
import com.yigit.toyexchanger.adapters.myadapter;
import com.yigit.toyexchanger.callbacks.OnItemLongClicked;
import com.yigit.toyexchanger.models.dataholder;

import org.checkerframework.checker.units.qual.A;

public class ActivityAdressDetail extends AppCompatActivity  {
    String lastuser="";
    RecyclerView recview;

    myadapter adapter;
    EditText et_name, et_tel, et_mesaj;
    Button btn_listeyeekle;
    SharedPreferences sharedPreferences= null;
    SharedPreferences.Editor editor;

    TextView RVemptyText;
    private static final int PICK_CONTACT_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adressdetail);




        sharedPreferences = getSharedPreferences("MofidxMuhendis",0);
        lastuser= sharedPreferences.getString("username","");

        RVemptyText= findViewById(R.id.RVemptyText);
        recview= findViewById(R.id.recyclerarkadasliste);
        et_name = findViewById(R.id.et_name);
        et_mesaj = findViewById(R.id.et_mesajyaz);
        et_tel = findViewById(R.id.et_telefon);
        btn_listeyeekle = findViewById(R.id.btn_listeyeekle);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        recview.setLayoutManager(linearLayoutManager);




        FirebaseRecyclerOptions<dataholder> options =
                new FirebaseRecyclerOptions.Builder<dataholder>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("users").child(lastuser).child("Adreslerim"), dataholder.class)
                        .build();

        adapter=new myadapter(options, this, new OnItemLongClicked() {
            @Override
            public void OnItemLongClicked(dataholder dataholder) {
                showAlertDialog(R.layout.dialog_postive_layout, dataholder);
            }
        });
        recview.setAdapter(adapter);


        recview.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recview, new RecyclerTouchListener.ClickListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onClick(View view, int position) {

                dataholder selectedData = options.getSnapshots().get(position);
                // Tüm öğeleri "unchecked" yap
                for (int i = 0; i < options.getSnapshots().size(); i++) {
                    options.getSnapshots().get(i).setSelected(false);
                }


                // Şu anki öğeyi seçili yap
                selectedData.setSelected(true);

                // RecyclerView'ı güncelle
                adapter.notifyDataSetChanged();



                editor = sharedPreferences.edit();
                editor.putInt("posi_cheked_Radio_btn",position);
                editor.commit();
//                adapter.notifyDataSetChanged();
//                Toast.makeText(ActivityTeslimatAdresi.this, ""+position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));







        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users").child(lastuser).child("Adreslerim");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Firebase is working
                RVemptyText.setText("");

                // You can perform the desired actions here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Firebase is not working
                RVemptyText.setText("Listeye Ekleyin");
                // Handle the error or display a message
            }
        });












        btn_listeyeekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!etname() | !ettel() | !etmesaj() ) {

                }else {
                    if (lastuser!=""){
                        listeyeeklensin();  
                    }else {
                        Toast.makeText(ActivityAdressDetail.this, "Önce giriş yapmanız gerekir", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });


    }

    private void pickContact() {

        Intent pickContactIntent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
        startActivityForResult(pickContactIntent, PICK_CONTACT_REQUEST);

    }

    private void listeyeeklensin() {
        FirebaseDatabase database  = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("users").child(lastuser).child("Adreslerim");
        String name = et_name.getText().toString();
        String tel = et_tel.getText().toString();
        String mesaj = et_mesaj.getText().toString();

        dataholder dataholder1 = new dataholder(name,tel,mesaj);
        reference.child(name).setValue(dataholder1);
        Toast.makeText(ActivityAdressDetail.this, "Yeni Adress başarılı şekilde eklendi!", Toast.LENGTH_SHORT).show();

        editor = sharedPreferences.edit();
        editor.putString(name,name);
        editor.putString(tel,tel);
        editor.putString(name+tel,mesaj);
        editor.commit();


    }

    private boolean etmesaj() {
        String value = et_mesaj.getText().toString();
        if (value.isEmpty()) {
            et_mesaj.setError("Tam adress hanesini doldurun!");
            return false;
        } else {
            et_mesaj.setError(null);
            return true;
        }
    }

    private boolean ettel() {
        String value = et_tel.getText().toString();
        if (value.isEmpty()) {
            et_tel.setError("Şehir hanesini doldurun!");
            return false;
        } else {
            et_tel.setError(null);
            return true;
        }
    }

    private boolean etname() {
        String value = et_name.getText().toString();
        if (value.isEmpty()) {
            et_name.setError("Adresin Adı hanesini doldurun!");
            return false;
        } else {
            et_name.setError(null);
            return true;
        }


    }


    private void showAlertDialog(int layout,dataholder secilendataholder) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityAdressDetail.this);
        View layoutView = getLayoutInflater().inflate(layout, null);
        Button dialogButton = layoutView.findViewById(R.id.btnDialog);
        Button dialogButton1 = layoutView.findViewById(R.id.btnDialog1);
        TextView tv_diaogarkadas = layoutView.findViewById(R.id.tv_diaogarkadas);
        dialogBuilder.setView(layoutView);
        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        tv_diaogarkadas.setText("Bunu Silmek Istiyor Musunuz ?");
        dialogButton.setText("Silin");
        alertDialog.show();




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



                FirebaseDatabase database  = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users").child(lastuser).child("Adreslerim");
                DatabaseReference objectReference = reference.child(secilendataholder.getName());
                // Delete the object
                objectReference.removeValue();
                Toast.makeText(getApplicationContext(), ""+secilendataholder.getName()+" Silindi", Toast.LENGTH_SHORT).show();

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

//                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                            dataholder dataModel = snapshot.getValue(dataholder.class);
//                            if (dataModel != null) {
//
//                            }
//                        }

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error
                    }
                });






                alertDialog.dismiss();




            }
        });



    }






    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_CONTACT_REQUEST) {
            if (resultCode == RESULT_OK) {
                // Kişi seçildiğinde
                Uri contactUri = data.getData();
                String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER};
                assert contactUri != null;
                Cursor cursor = getContentResolver().query(contactUri, projection, null, null, null);

                if (cursor != null && cursor.moveToFirst()) {
                    int numberColumnIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
                    String phoneNumber = cursor.getString(numberColumnIndex);
                    et_tel.setText(phoneNumber);
                    cursor.close();
                }
            }
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent i = new Intent(ActivityAdressDetail.this,MainActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}