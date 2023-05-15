package com.example.android_app;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class ShoppingListAdapter extends FirestoreRecyclerAdapter<ShoppingList, ShoppingListAdapter.ShoppingListViewHolder> {
    Context context;

    public ShoppingListAdapter(@NonNull FirestoreRecyclerOptions<ShoppingList> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull ShoppingListViewHolder holder, int position, @NonNull ShoppingList list) {
        holder.titleTextView.setText(list.title);
        holder.contentTextView.setText(list.content);
        holder.timestampTextView.setText(Utility.timestampToString(list.timestamp));

        // On click, set Shopping List Details Page
        holder.itemView.setOnClickListener((v)->{
            Intent intent = new Intent(context,ShoppingListDetailsActivity.class);
            intent.putExtra("title",list.title);
            intent.putExtra("content",list.content);
            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId",docId);
            context.startActivity(intent);
        });

    }

    @NonNull
    @Override
    public ShoppingListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_item,parent,false);
        return new ShoppingListViewHolder(view);
    }

    class ShoppingListViewHolder extends RecyclerView.ViewHolder{
        TextView titleTextView,contentTextView,timestampTextView;

        public ShoppingListViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.list_title_text_view);
            contentTextView = itemView.findViewById(R.id.list_content_text_view);
            timestampTextView = itemView.findViewById(R.id.list_timestamp_text_view);
        }
    }
}