package org.techtown.caloriecalculator;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdaptor extends RecyclerView.Adapter<RecyclerViewAdaptor.ItemHolder>{
    private List<MealItem> list;
    private ArrayList<Drawable> imageList;
    interface ISetOnUpdateButtonListener{
        void updateListener(int position);
    }
    ISetOnUpdateButtonListener listener;
    public RecyclerViewAdaptor(ArrayList<Drawable> imgList){
        this.imageList=imgList;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.meal_items,parent,false);

        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        MealItem item=list.get(position);
        holder.kindOfMeal.setText(item.getKind());
        holder.thiskcal.setText(item.getKcal()+"kcal");
        holder.imageBox.setImageDrawable(imageList.get(position));
        holder.updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.updateListener(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public void setList(List<MealItem> list){
        this.list=list;
    }
    public void setListener(ISetOnUpdateButtonListener listener){
        this.listener=listener;
    }

    public class ItemHolder extends RecyclerView.ViewHolder{
        protected TextView kindOfMeal;
        protected TextView thiskcal;
        protected Button updateButton;
        protected ImageView imageBox;
        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            kindOfMeal=itemView.findViewById(R.id.kindOfMeal);
            thiskcal=itemView.findViewById(R.id.thisKcal);
            updateButton=itemView.findViewById(R.id.updateButton);
            imageBox=itemView.findViewById(R.id.imageBox);
        }
    }
}
