package com.yigit.toyexchanger.YeniRekalm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.yigit.toyexchanger.R;

import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;


public class YeniReklamActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor;
    private String lastuser;
    EditText et_ilan_basligi, et_detay, et_telefon, et_konum;

    int fileCount =0;
    // Define the request code
    private static final int PICK_IMAGE_REQUEST = 1;
    TextView text_resim0_10;
    ImageView img_yukle1,img_yukle2,img_yukle3,img_yukle4,img_yukle5,img_yukle6,img_yukle7,img_yukle8,img_yukle9,img_yukle10,img_yukle11;
    Button btn_shareToy;
    String[] items;
    ArrayList<String> imgs_links;
    Spinner spinneryenireklam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yeni_reklam);

        et_ilan_basligi = findViewById(R.id.et_ilan_basligi);
        et_detay = findViewById(R.id.et_detay);
        et_telefon = findViewById(R.id.et_telefon);
        et_konum = findViewById(R.id.et_konum);

        btn_shareToy = findViewById(R.id.btn_share_toy);
        spinneryenireklam = findViewById(R.id.spinneryenireklam);
        img_yukle1 = findViewById(R.id.img_yukle1);
        img_yukle2 = findViewById(R.id.img_yukle2);
        img_yukle3 = findViewById(R.id.img_yukle3);
        img_yukle4 = findViewById(R.id.img_yukle4);
        img_yukle5 = findViewById(R.id.img_yukle5);
        img_yukle6 = findViewById(R.id.img_yukle6);
        img_yukle7 = findViewById(R.id.img_yukle7);
        img_yukle8 = findViewById(R.id.img_yukle8);
        img_yukle9 = findViewById(R.id.img_yukle9);
        img_yukle10 = findViewById(R.id.img_yukle10);
        img_yukle11 = findViewById(R.id.img_yukle11);

        sharedPreferences = getSharedPreferences("MofidxMuhendis",0);
        lastuser= sharedPreferences.getString("username","");


        text_resim0_10 = findViewById(R.id.text_resim0_10);

        items = new String[]{"Çocuk Oyuncakları", "Yetişkin Oyunları", "Zeka Oyunları", "Spor Oyunları", "ikinci El Oyunları", "Yeni Ürünler", "Video Oyunları", "Masa Oyunları","Sınıflandırılmamış"};
        imgs_links= new ArrayList<>();

        // ImageView'ları diziye ekleyin



        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinneryenireklam.setAdapter(adapter);






        img_yukle1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resmiyukle();

            }
        });







        spinneryenireklam.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selected = parent.getItemAtPosition(position).toString();
//                Toast.makeText(YeniReklamActivity.this, "Seçilen: " + selected, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Sometimes you need nothing here
            }
        });


        btn_shareToy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!validateilanbasligi() | ! validateSpinner()) {

                }else {

                    pushMyPostTofirebase();

                }


//                Toast.makeText(YeniReklamActivity.this, "tamamdır", Toast.LENGTH_SHORT).show();
//

            }
        });


    }//OnCreate


    private void pushMyPostTofirebase() {
//        int usernameVar = getCurrentUsername();
        if (!Objects.equals(lastuser, "")) {
            Toast.makeText(this, ""+lastuser, Toast.LENGTH_SHORT).show();



//            getNextPostNumber(lastuser, postNumber -> {
//                for (int i = 1; i <= 3; i++) {
//                    Uri imageUri = selectedImages[i-1]; // Kullanıcının seçtiği resimler
//                    uploadImageToFirebase(imageUri, postNumber, i);
//                }
//            });


        }else {
            Toast.makeText(this, "Misafir kullanıcı ile paylaşım yapılamamaktadır", Toast.LENGTH_SHORT).show();

        }


    //TODO

    }


//    private void getNextPostNumber(String username, OnSuccessListener<Integer> listener) {
//        StorageReference userRef = FirebaseStorage.getInstance().getReference()
//                .child("users/" + username);
//
//        userRef.listAll().addOnSuccessListener(listResult -> {
//            int nextPostNumber = listResult.getPrefixes().size() + 1; // Mevcut post klasörlerini say
//            listener.onSuccess(nextPostNumber);
//        }).addOnFailureListener(e -> {
//            listener.onSuccess(1); // Eğer hata varsa ilk post olarak başlat
//        });
//    }

    private void uploadImageToFirebase(Uri imageUri, int postNumber, int imageNumber) {
        String username = lastuser; // Kullanıcı adını al
        if (username.equals("unknown_user")) {
            Toast.makeText(this, "Kullanıcı adı bulunamadı!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Dosya yolu: users/{username}/post_{postNumber}/image_{imageNumber}.jpg
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("users/" + username + "/post_" + postNumber + "/image_" + imageNumber + ".jpg");

        fileRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                        String imageUrl = uri.toString();
                        Log.d("Firebase", "Yüklenen resim URL: " + imageUrl);
                        Toast.makeText(this, "Resim başarıyla yüklendi!", Toast.LENGTH_SHORT).show();
                    });
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Yükleme başarısız: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }




    private boolean validateSpinner() {
        int selectedPosition = spinneryenireklam.getSelectedItemPosition(); // Seçili pozisyonu al
        if (selectedPosition == 0) { // İlk öğe genellikle placeholder olur
            Toast.makeText(this, "Lütfen Katagoriden bir seçenek seçin!", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    public Boolean validateilanbasligi() {
        String val = et_ilan_basligi.getText().toString();
        if (val.isEmpty()) {
            et_ilan_basligi.setError("Bu Alan boş olamaz!");
            return false;
        } else {
            et_ilan_basligi.setError(null);
            return true;
        }
    }

    private void resmiyukle(){

    Intent intent = new Intent();
    intent.setType("image/*");
    intent.setAction(Intent.ACTION_GET_CONTENT);

    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

}


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            uploadImageToFirebase(imageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference fileRef = storageRef.child("images/" + UUID.randomUUID().toString());
        fileRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                // Display this imageUrl on your ImageView or store it in your database
                                Toast.makeText(YeniReklamActivity.this, ""+imageUrl, Toast.LENGTH_SHORT).show();
                                imgs_links.add(imageUrl);

//                                getUploadedImagesCount();
                                displayImageInImageView(imageUrl);
                            }
                        });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Upload Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void displayImageInImageView(String imageUrl) {
            int UploadedImages= imgs_links.size();

        if(UploadedImages==1){
            // XML'de tanımlanan ImageView'in ID'si
            Glide.with(this)
                    .load(imgs_links.get(0))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .into(img_yukle2);

                    text_resim0_10.setText("Resim 1/10 yüklendi");
                    text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        } else if (UploadedImages==2) {
            Glide.with(this)
                    .load(imgs_links.get(1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle3);

            text_resim0_10.setText("Resim 2/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==3) {
            Glide.with(this)
                    .load(imgs_links.get(2))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle4);
            text_resim0_10.setText("Resim 3/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==4) {
            Glide.with(this)
                    .load(imgs_links.get(UploadedImages-1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle5);
            text_resim0_10.setText("Resim 4/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==5) {
            Glide.with(this)
                    .load(imgs_links.get(UploadedImages-1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle6);
            text_resim0_10.setText("Resim 5/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==6) {
            Glide.with(this)
                    .load(imgs_links.get(UploadedImages-1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle7);
            text_resim0_10.setText("Resim 6/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==7) {
            Glide.with(this)
                    .load(imgs_links.get(UploadedImages-1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle8);
            text_resim0_10.setText("Resim 7/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==8) {
            Glide.with(this)
                    .load(imgs_links.get(UploadedImages-1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle9);
            text_resim0_10.setText("Resim 8/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==9) {
            Glide.with(this)
                    .load(imgs_links.get(UploadedImages-1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle10);
            text_resim0_10.setText("Resim 9/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }else if (UploadedImages==10) {
            Glide.with(this)
                    .load(imgs_links.get(UploadedImages-1))
                    .placeholder(R.drawable.baseline_cloud_upload_24) // Yüklenirken gösterilecek görsel
                    .error(R.drawable.ic_close)           // Hata durumunda gösterilecek görsel
                    .into(img_yukle11);
            text_resim0_10.setText("Resim 10/10 yüklendi");
            text_resim0_10.setTextColor(getColor(R.color.color_green));
            text_resim0_10.setCompoundDrawablesWithIntrinsicBounds(R.drawable.baseline_check_circle_outline_24, 0, 0, 0); // أيقونة على اليسار

        }


    }


    private void getUploadedImagesCount() {
        // Firebase Storage referansını alın

        StorageReference storageRef = FirebaseStorage.getInstance().getReference().child("images/");

        // Dizindeki tüm dosyaları listele
        storageRef.listAll()
                .addOnSuccessListener(listResult -> {
                    // Dosya sayısını al
                     fileCount = listResult.getItems().size();
                    Toast.makeText(this, "Yüklenen resim sayısı: " + fileCount, Toast.LENGTH_SHORT).show();

                })
                .addOnFailureListener(e -> {
                    // Hata durumunu ele al

                    Toast.makeText(this, "Resim sayısı alınamadı: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });



    }



}