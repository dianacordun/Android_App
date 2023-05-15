package com.example.android_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.Query;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton addSoppingListBtn;
    RecyclerView recyclerView;
    ImageButton menuBtn;
    ShoppingListAdapter shoppingListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addSoppingListBtn = findViewById(R.id.add_list_btn);
        recyclerView = findViewById(R.id.recyler_view);
        menuBtn = findViewById(R.id.menu_btn);

        addSoppingListBtn.setOnClickListener((v) -> startActivity(new Intent(MainActivity.this, ShoppingListDetailsActivity.class)));
        menuBtn.setOnClickListener((v)->showMenu() );
        setupRecyclerView();
    }

    void showMenu(){
        PopupMenu popupMenu  = new PopupMenu(MainActivity.this, menuBtn);
        popupMenu.getMenu().add("Logout");
        popupMenu.show();
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                if(menuItem.getTitle()=="Logout"){
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    void setupRecyclerView(){
        Query query  = Utility.getCollectionReferenceForLists().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<ShoppingList> options = new FirestoreRecyclerOptions.Builder<ShoppingList>()
                .setQuery(query,ShoppingList.class).build();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        shoppingListAdapter = new ShoppingListAdapter(options,this);
        recyclerView.setAdapter(shoppingListAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        shoppingListAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        shoppingListAdapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        shoppingListAdapter.notifyDataSetChanged();
    }
}