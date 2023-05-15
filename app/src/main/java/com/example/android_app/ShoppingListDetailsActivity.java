package com.example.android_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;

public class ShoppingListDetailsActivity extends AppCompatActivity {

    EditText titleEditText,contentEditText;
    ImageButton saveListBtn;
    TextView pageTitleTextView;
    String title,content,docId;
    boolean isEditMode = false;
    TextView deleteListTextViewBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list_details);

        titleEditText = findViewById(R.id.lists_title_text);
        contentEditText = findViewById(R.id.lists_content_text);
        saveListBtn = findViewById(R.id.save_list_btn);
        pageTitleTextView = findViewById(R.id.page_title);
        deleteListTextViewBtn  = findViewById(R.id.delete_list_text_view_btn);

        // Receive data
        title = getIntent().getStringExtra("title");
        content= getIntent().getStringExtra("content");
        docId = getIntent().getStringExtra("docId");

        if(docId!=null && !docId.isEmpty()){
            isEditMode = true;
        }

        titleEditText.setText(title);
        contentEditText.setText(content);

        // User clicked on existing shopping list
        if(isEditMode){
            pageTitleTextView.setText("Edit your list");
            deleteListTextViewBtn.setVisibility(View.VISIBLE);
        }

        saveListBtn.setOnClickListener( (v)-> saveList());

        deleteListTextViewBtn.setOnClickListener((v)-> deleteListFromFirebase() );

    }

    void saveList(){
        String listTitle = titleEditText.getText().toString();
        String listContent = contentEditText.getText().toString();
        if(listTitle==null || listTitle.isEmpty() ){
            titleEditText.setError("Title is required");
            return;
        }
        ShoppingList list = new ShoppingList();
        list.setTitle(listTitle);
        list.setContent(listContent);
        list.setTimestamp(Timestamp.now());

        saveListToFirebase(list);

    }

    void saveListToFirebase(ShoppingList list){
        DocumentReference documentReference;
        if(isEditMode){
            // Update the shopping list
            documentReference = Utility.getCollectionReferenceForLists().document(docId);
        }else{
            // Create new shopping list
            documentReference = Utility.getCollectionReferenceForLists().document();
        }

        documentReference.set(list).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //List is added
                    Utility.showToast(ShoppingListDetailsActivity.this,"Shopping list added successfully");
                    finish();
                }else{
                    Utility.showToast(ShoppingListDetailsActivity.this,"Failed while adding shopping list");
                }
            }
        });
    }

    void deleteListFromFirebase(){
        DocumentReference documentReference;
        documentReference = Utility.getCollectionReferenceForLists().document(docId);
        documentReference.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    // Shoppin list is deleted
                    Utility.showToast(ShoppingListDetailsActivity.this," Shopping list deleted successfully");
                    finish();
                }else{
                    Utility.showToast(ShoppingListDetailsActivity.this,"Failed while deleting  shopping List");
                }
            }
        });
    }


}