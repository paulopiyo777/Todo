package com.example.todo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemHolder> {

    private List<String> itemList;
    private ItemInteraction itemInteraction;

    public ItemAdapter(ItemInteraction itemInteraction) {
        this.itemInteraction = itemInteraction;
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
        return new ItemHolder(view, itemInteraction);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, int position) {
        holder.bind(itemList.get(position));
    }

    public void setData(List<String> itemList) {
        this.itemList = itemList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ItemInteraction itemInteraction;
        TextView tvItem;

        public ItemHolder(@NonNull View itemView, final ItemInteraction itemInteraction) {
            super(itemView);
            this.itemInteraction = itemInteraction;

            tvItem = itemView.findViewById(android.R.id.text1);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    itemInteraction.todoLongClick(getAdapterPosition());
                    return true;
                }
            });
        }

        public void bind(String s) {
            tvItem.setText(s);
        }

        @Override
        public void onClick(View v) {
            itemInteraction.todoClicked(itemList.get(getAdapterPosition()));
        }
    }

    public interface ItemInteraction {

        void todoClicked(String s);

        void todoLongClick(int position);
    }
}
