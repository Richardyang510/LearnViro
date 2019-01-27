package com.example.nwhacks;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private static List<DataModel> dataModelList;
    private Context mContext;

    // View holder class whose objects represent each list item
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView cardImageView;
        public TextView titleTextView;
        public Button wordOneButton, wordTwoButton, wordThreeButton, wordFourButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardImageView = itemView.findViewById(R.id.imageView);
            titleTextView = itemView.findViewById(R.id.card_title);
            wordOneButton = itemView.findViewById(R.id.action_button_1);
            wordOneButton.setTag(dataModelList.size() - 1);
            wordTwoButton = itemView.findViewById(R.id.action_button_2);
            wordTwoButton.setTag(dataModelList.size() - 1);
            wordThreeButton = itemView.findViewById(R.id.action_button_3);
            wordThreeButton.setTag(dataModelList.size() - 1);
            wordFourButton = itemView.findViewById(R.id.action_button_4);
            wordFourButton.setTag(dataModelList.size() - 1);
        }

        public void bindData(DataModel dataModel, Context context) {
            cardImageView.setImageDrawable(dataModel.getDrawable());
            titleTextView.setText(dataModel.getTitle());
            wordOneButton.setText(dataModel.getChoices().get(0));
            wordTwoButton.setText(dataModel.getChoices().get(1));
            wordThreeButton.setText(dataModel.getChoices().get(2));
            wordFourButton.setText(dataModel.getChoices().get(3));
        }
    }

    public MyAdapter(List<DataModel> modelList, Context context) {
        dataModelList = modelList;
        mContext = context;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate out card list item
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // Return a new view holder
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        // Bind data for the item at position
        holder.bindData(dataModelList.get(position), mContext);
    }

    @Override
    public int getItemCount() {
        // Return the total number of items
        return dataModelList.size();
    }
}
