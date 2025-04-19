package com.yigit.toyexchanger.fragments;

import static androidx.core.content.ContextCompat.getDrawable;
import static androidx.core.content.ContextCompat.startActivity;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

//import com.yigit.toyexchanger.fragments.placeholder.PlaceholderContent.PlaceholderItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.activities.ActivityAdressDetail;
import com.yigit.toyexchanger.activities.Kisisel_Bilgileri_Activity;
import com.yigit.toyexchanger.activities.LoginActivity;
import com.yigit.toyexchanger.databinding.FragmentProfileBinding;

import java.util.ArrayList;

/**
// * {@link RecyclerView.Adapter} that can display a {@link PlaceholderItem}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<String> mValues;
    private final Context mcontext;
    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor;
    String lastuser = "";

    public MyItemRecyclerViewAdapter(ArrayList<String> items, Context context) {
        mValues = items;
        mcontext =context ;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder( FragmentProfileBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        sharedPreferences = mcontext.getSharedPreferences("MofidxMuhendis",0);
        lastuser= sharedPreferences.getString("username","");

        holder.mItem = mValues;
//        holder.mIdView.setText(mValues.get(position));
        holder.mContentView.setText(mValues.get(position));

        if (position==4 && lastuser!="" ){
            holder.mContentView.setTextColor(Color.RED); // Yazı rengini kırmızı yap
        } else if (position==4 && lastuser=="") {
            holder.mContentView.setTextColor(Color.BLUE); // Yazı rengini kırmızı yap
            holder.mContentView.setText("Giriş yap");
        }
        if (position==5){
            holder.mContentView.setTextColor(Color.RED); // Yazı rengini kırmızı yap
            holder.mContentView.setCompoundDrawablesWithIntrinsicBounds(R.drawable.delete, 0, 0, 0); // أيقونة على اليسار

        }



        holder.mcardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position==4){
                    if (lastuser!=""){
                        editor = sharedPreferences.edit();
                        editor.putString("username","");
                        editor.putString("pass","");
                        editor.putBoolean("remember", false);
                        editor.commit();
                        Intent intent = new Intent(mcontext, LoginActivity.class);
                        mcontext.startActivity(intent);
                    }else {
                        Intent intent = new Intent(mcontext, LoginActivity.class);
                        mcontext.startActivity(intent);
                    }




                } else if (position==1) {
                    Intent intent = new Intent(mcontext, Kisisel_Bilgileri_Activity.class);
                    mcontext.startActivity(intent);
                } else if (position==5) {
                    showAcilAlertDialog(R.layout.dialog_hesab_silme);

                    editor.putBoolean("remember", false);
                }
                else if (position==2) {
                    Intent intent = new Intent(mcontext, ActivityAdressDetail.class);
                    mcontext.startActivity(intent);


                }


            }

            private void showAcilAlertDialog(int layout) {
                AlertDialog alertDialog;
                AlertDialog.Builder dialogBuilder;
                dialogBuilder = new AlertDialog.Builder(mcontext);
                LayoutInflater inflater = LayoutInflater.from(mcontext);
                View layoutView = inflater.inflate(layout, null);
                Button dialogButton = layoutView.findViewById(R.id.btnDialog);
                Button dialogButton1 = layoutView.findViewById(R.id.btnDialog1);
                TextView et_dialogacil = layoutView.findViewById(R.id.et_diaogacil);
                TextView Text_diaogacil = layoutView.findViewById(R.id.Text_diaogacil);
                dialogBuilder.setView(layoutView);
                alertDialog = dialogBuilder.create();
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
                alertDialog.show();

//                Text_diaogacil.setText(""+stringhastabilgileri);


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

                        alertDialog.dismiss();

                        SharedPreferences sharedPreferences = mcontext.getSharedPreferences("MofidxMuhendis", Context.MODE_PRIVATE);
                        String currentUsername = sharedPreferences.getString("username", null);

                        if (currentUsername != null) {
                            // إنشاء مرجع إلى قاعدة بيانات Firebase
                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users");

                            // حذف المستخدم من قاعدة البيانات
                            reference.child(currentUsername).removeValue().addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // إزالة اسم المستخدم من SharedPreferences
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    editor.remove("username");
                                    editor.remove("pass");
                                    editor.putBoolean("remember", false);
                                    editor.apply();

                                    // عرض رسالة نجاح
                                    Toast.makeText(mcontext, "Hesabınızı başarlı bir şekilde silindi.", Toast.LENGTH_SHORT).show();

                                    // إعادة توجيه المستخدم (اختياري)
                                    Intent intent = new Intent(mcontext, LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    mcontext.startActivity(intent);
                                } else {
                                    // عرض رسالة خطأ
                                    Toast.makeText(mcontext, "hesabı silerken bir problemle karşılaştık!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        } else {
                            Toast.makeText(mcontext, "kayıtlı bir hesabınız bulunmamaktadır.", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(mcontext, "Hesabınız silindi.... ", Toast.LENGTH_LONG).show();




                    }
                });



            }


        });




    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final CardView mcardview;
        public final TextView mIdView;
        public final TextView mContentView;
        public ArrayList<String> mItem;

        public ViewHolder( FragmentProfileBinding binding) {
            super(binding.getRoot());

            mcardview = binding.cardViewProfile;
            mIdView = binding.itemNumber;
            mContentView = binding.content;
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}

