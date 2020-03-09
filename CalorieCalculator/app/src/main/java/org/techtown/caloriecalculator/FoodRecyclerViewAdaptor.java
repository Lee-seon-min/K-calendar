package org.techtown.caloriecalculator;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FoodRecyclerViewAdaptor extends RecyclerView.Adapter<FoodRecyclerViewAdaptor.ItemHolder>{
    private List<FoodItem> list;
    private ISetOnUpdateCheckBoxListener listener;
    public interface ISetOnUpdateCheckBoxListener{
        void TouchListener(double kcal);
    }
    public void setList(List<FoodItem> list){
        this.list=list;
    }
    public void setListener(ISetOnUpdateCheckBoxListener listener){
        this.listener=listener;
    }
    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.food_items,parent,false);

        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        final FoodItem item=list.get(position);
        final ItemHolder itemHolder=holder;
        holder.foodName.setText(item.getName());
        holder.gramText.setText("("+item.getGram()+"g)");
        holder.sumkcal.setText(item.getKcal()+"kcal");
        holder.carboText.setText("탄 : "+item.getCarbor()+"g");
        holder.proteinText.setText("단 : "+item.getProtein()+"g");
        holder.fatText.setText("지 : "+item.getFat()+"g");
        holder.countBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                itemHolder.countText.setText(String.valueOf(((float)progress/10)));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
        holder.foodHouse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double count=Double.parseDouble(itemHolder.countText.getText().toString());
                listener.TouchListener(item.getKcal()*count);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder{
        private LinearLayout foodHouse;
        private TextView foodName;
        private TextView gramText;
        private TextView carboText;
        private TextView proteinText;
        private TextView fatText;
        private TextView sumkcal;
        private TextView countText;
        private SeekBar countBar;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            foodHouse=itemView.findViewById(R.id.foodhouse);
            foodName=itemView.findViewById(R.id.foodName);
            gramText=itemView.findViewById(R.id.gramText);
            carboText=itemView.findViewById(R.id.carboText);
            proteinText=itemView.findViewById(R.id.proteinText);
            fatText=itemView.findViewById(R.id.fatText);
            sumkcal=itemView.findViewById(R.id.sumkcal);
            countBar=itemView.findViewById(R.id.countBar);
            countText=itemView.findViewById(R.id.countText);
        }
    }
}
