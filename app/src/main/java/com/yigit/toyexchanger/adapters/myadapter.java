package com.yigit.toyexchanger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.callbacks.OnItemLongClicked;
import com.yigit.toyexchanger.models.dataholder;


public class myadapter extends FirebaseRecyclerAdapter<dataholder,myadapter.myviewholder>
{


    Context context;
    int theposition;
    OnItemLongClicked listener;

    SharedPreferences sharedPreferences=null;
    SharedPreferences.Editor editor;
    dataholder dataholderr;
    FirebaseRecyclerOptions<dataholder> options;

    public myadapter(@NonNull FirebaseRecyclerOptions<dataholder> options , Context c, OnItemLongClicked onItemLongClicked) {
        super(options);
        this.context = c;
        this.options = options;
        this.listener = onItemLongClicked;


    }


    @Override
    protected void onBindViewHolder(@NonNull final myviewholder holder,  int position, @NonNull final dataholder model) {
//        dataholder data_pos = options.getSnapshots().get(position);
            sharedPreferences = context.getSharedPreferences("MofidxMuhendis",0);
            int posi_cheked = sharedPreferences.getInt("posi_cheked_Radio_btn",-1);
//            Toast.makeText(context, "Seçilen pos: "+posi_cheked, Toast.LENGTH_SHORT).show();
        boolean bb = sharedPreferences.getBoolean("bool_posi",false);


        holder.radioButton.setChecked(model.isSelected());

        holder.name.setText(model.getName());
        holder.tel.setText(model.getTel());
        holder.mesaj.setText(model.getMesaj());
        theposition = holder.getLayoutPosition();
        dataholderr = options.getSnapshots().get(theposition);



        if ( posi_cheked == position ){

            holder.radioButton.setChecked(bb);

        }



        holder.bind(dataholderr);

        holder.radioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {


                if (b){

                    // Seçili öğeyi güncelle
                    for (int i = 0; i < getItemCount(); i++) {
                        options.getSnapshots().get(i).setSelected(false);
                    }
//                    notifyDataSetChanged();


                    int selectedPosition = holder.getAdapterPosition();
//                   dataholder data_pos = options.getSnapshots().get(selectedPosition);

                    model.setSelected(true);

                    String strmsj=options.getSnapshots().get(selectedPosition).getName()+"("+options.getSnapshots().get(selectedPosition).getMesaj()+")";
//                    Toast.makeText(context, "Seçilen pos: "+p, Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, "Seçilen Adres: "+ options.getSnapshots().size(), Toast.LENGTH_SHORT).show();
//                    Toast.makeText(context, "Seçilen Adres: "+options.getSnapshots().get(selectedPosition).getName(), Toast.LENGTH_SHORT).show();
                    editor = sharedPreferences.edit();
                    editor.putString("teslimatkey",strmsj);
                    editor.putInt("posi_cheked_Radio_btn",selectedPosition);
                    editor.putBoolean("bool_posi", true);
                    editor.commit();

//
//                    // RecyclerView'ı güncelle
//                    notifyDataSetChanged();

                }

            }
        });

//        holder.CardViewItem.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//
//                theposition = holder.getLayoutPosition();
//
//                dataholder = options.getSnapshots().get(theposition);
//
//
//
//                return true;
//            }
//        });





//        int readed1 = sharedPreferences.getInt(posi1_readed[position],-1);
//
//        if (readed1 == position){
//            holder.imgreaded.setVisibility(View.VISIBLE);
//        } else {
//            holder.imgreaded.setVisibility(View.INVISIBLE);
//        }






    }




    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)

    {


       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view,parent,false);
       return new myviewholder(view);




    }

    class myviewholder extends RecyclerView.ViewHolder {
        CardView CardViewItem;
        dataholder dataholder;
        TextView name,tel,mesaj;
        RadioButton radioButton;

        public myviewholder(@NonNull View itemView)
        {
            super(itemView);
            radioButton = itemView.findViewById(R.id.radio_btn);
            name= itemView.findViewById(R.id.txtitem1);
            tel= itemView.findViewById(R.id.txtitem2);
            mesaj= itemView.findViewById(R.id.txtitem3);
            CardViewItem= itemView.findViewById(R.id.CardViewItem);




            CardViewItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.OnItemLongClicked(dataholder);
                    return true;
                }
            });

        }

        void bind (dataholder dataname){
            this.dataholder = dataname;


        }


    }





}
