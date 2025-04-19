package com.yigit.toyexchanger.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.yigit.toyexchanger.Geofence.MapsActivity;
import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.adapters.RecyclerTouchListener;
import com.yigit.toyexchanger.adapters.myadapter;
import com.yigit.toyexchanger.callbacks.OnItemLongClicked;
import com.yigit.toyexchanger.models.dataholder;

public class ActivityTeslimatAdresi extends AppCompatActivity {
    FirebaseRecyclerOptions<dataholder> options;
    SharedPreferences sharedPreferences= null;
    SharedPreferences.Editor editor;
    TextView RVemptyText1;

    ImageView image_back_teslimat ;


    String lastuser="";

    RecyclerView recview1;

    myadapter adapter;

    Button btn_adres_ekle,btn_konum;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teslimat_adresi);

        recview1= findViewById(R.id.recyclerarkadasliste1);
        RVemptyText1= findViewById(R.id.RVemptyText1);
        image_back_teslimat= findViewById(R.id.image_back_teslimat);
        btn_adres_ekle= findViewById(R.id.btn_adres_ekle);
        btn_konum= findViewById(R.id.btn_konum);

        sharedPreferences = getSharedPreferences("MofidxMuhendis",0);
        lastuser= sharedPreferences.getString("username","");



        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, RecyclerView.VERTICAL,false);

        recview1.setLayoutManager(linearLayoutManager);



        options =
                new FirebaseRecyclerOptions.Builder<dataholder>()
                        .setQuery(FirebaseDatabase.getInstance().getReference("users").child(lastuser).child("Adreslerim"), dataholder.class)
                        .build();

        adapter=new myadapter(options, this, new OnItemLongClicked() {
            @Override
            public void OnItemLongClicked(dataholder dataholder) {
                showAlertDialog(R.layout.dialog_postive_layout, dataholder);
            }
        });
        recview1.setAdapter(adapter);

        recview1.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recview1, new RecyclerTouchListener.ClickListener() {
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
                RVemptyText1.setText("");

                // You can perform the desired actions here
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Firebase is not working
                RVemptyText1.setText("Adreslerinizi ekleyin veya internetle bağlanın");
                // Handle the error or display a message
            }
        });




        image_back_teslimat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityTeslimatAdresi.this,MainActivity.class);
                startActivity(i);
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });








        btn_adres_ekle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ActivityTeslimatAdresi.this,ActivityAdressDetail.class);
                startActivity(i);
//                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
            }
        });




btn_konum.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent i = new Intent(ActivityTeslimatAdresi.this, MapsActivity.class);
        startActivity(i);
    }
});





    }  // OnCreate










    private void showAlertDialog(int layout,dataholder secilendataholder) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(ActivityTeslimatAdresi.this);
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



        // Adapter'i bildirerek güncellemeleri uygula
//        adapter.notifyDataSetChanged();
        adapter.startListening();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ActivityTeslimatAdresi.this,MainActivity.class);
        startActivity(i);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


    }
}