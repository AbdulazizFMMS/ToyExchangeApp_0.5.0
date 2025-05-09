package com.yigit.toyexchanger.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.yigit.toyexchanger.R;
import com.yigit.toyexchanger.models.Category;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;
import java.util.Random;

public class AdapterCategory extends RecyclerView.Adapter<AdapterCategory.ViewHolder> {

    private List<Category> items;

    Context context;
    private OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, Category obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public AdapterCategory(Context context, List<Category> items) {
        this.items = items;
        this.context = context;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView categoryName;
        public ImageView imgAlphabet;
        public ImageView imgCategory;
        public CardView cardView;
        public LinearLayout lytParent;

        public ViewHolder(View v) {
            super(v);
            categoryName = v.findViewById(R.id.txt_label_name);
            imgAlphabet = v.findViewById(R.id.img_alphabet);
            imgCategory = v.findViewById(R.id.img_category);
            cardView = v.findViewById(R.id.card_view);
            lytParent = v.findViewById(R.id.lyt_parent);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View menuItemView;
        menuItemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(menuItemView);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final Category c = items.get(position);

        holder.categoryName.setText(c.term);

        int[] colorArr = {R.color.color_red, R.color.color_pink, R.color.color_purple, R.color.color_deep_purple, R.color.color_indigo, R.color.color_blue, R.color.color_cyan, R.color.color_teal, R.color.color_green, R.color.color_lime, R.color.color_orange, R.color.color_brown, R.color.color_gray, R.color.color_blue_gray, R.color.color_black};
        int rnd = new Random().nextInt(colorArr.length);
        holder.imgAlphabet.setImageResource(colorArr[rnd]);

        if (c.image != null && !c.image.trim().isEmpty()) {
            Glide.with(context)
                    .load(c.image.replace(" ", "%20"))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(R.drawable.bg_button_transparent)
                    .centerCrop()
                    .into(holder.imgCategory);
            holder.imgCategory.setVisibility(View.VISIBLE);
        } else {
            Log.d("DEBUG", "Image URL: " + c.image);

            holder.imgCategory.setVisibility(View.GONE);
        }

        holder.lytParent.setOnClickListener(view -> {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(view, c, position);
            }
        });
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setListData(List<Category> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void resetListData() {
        this.items.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}