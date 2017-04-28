package ua.cook.cooklist.activity;

import android.app.ProgressDialog;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.HashMap;
import java.util.Map;

import ua.cook.cooklist.R;
import ua.cook.cooklist.data.Receipt;

public class AddReceiptActivity extends AppCompatActivity implements View.OnClickListener, IPickResult {

    private ImageView mainImage;
    private EditText nameEditText;
    private EditText descEditText;
    private Button completeButton;
    private Receipt receipt;
    private Uri localImageUri;
    private FirebaseStorage storage;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_receipt);

        key = FirebaseDatabase.getInstance().getReference().child("receipts").push().getKey();

        receipt = new Receipt();
        receipt.setAuthor(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());

        mainImage = (ImageView) findViewById(R.id.add_receipt_image);
        nameEditText = (EditText) findViewById(R.id.add_edittext_name);
        descEditText = (EditText) findViewById(R.id.add_edittext_desc);
        completeButton = (Button) findViewById(R.id.add_button_complete);

        mainImage.setOnClickListener(this);
        completeButton.setOnClickListener(this);


        storage = FirebaseStorage.getInstance();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.add_receipt_image:
                PickImageDialog.build(new PickSetup()).show(this);
                break;
            case R.id.add_button_complete:
                if(localImageUri != null && !(nameEditText.getText().toString().equals("")) && !(descEditText.getText().toString().equals(""))){
                    receipt.setId(key);
                    receipt.setName(nameEditText.getText().toString());
                    receipt.setDescription(descEditText.getText().toString());
                    final ProgressDialog pd = new ProgressDialog(this);
                    pd.setTitle("Завантаження");
                    pd.setMessage("Зачекайте, Ваш рецепт завантажується на сервер");
                    pd.show();
                    storage.getReference().child(key).putFile(localImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storage.getReference().child(key).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    receipt.setImageUrl(uri.toString());
                                    Map<String, String> receiptValues = receipt.toMap();
                                    Map<String, Object> childUpdate = new HashMap<>();
                                    childUpdate.put("/receipts/" + key, receiptValues);
                                    FirebaseDatabase.getInstance().getReference().updateChildren(childUpdate);
                                    pd.hide();
                                    finish();
                                }
                            })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            pd.hide();
                                            Log.e("error",e.getMessage());
                                        }
                                    });
                        }
                    });
                }
                else{
                    Toast.makeText(this,"Не всі дані заповено",Toast.LENGTH_SHORT).show();
                }
        }
    }

    @Override
    public void onPickResult(PickResult pickResult) {
        if(pickResult.getError() == null){
            mainImage.setImageBitmap(pickResult.getBitmap());
            localImageUri = pickResult.getUri();
        }
    }
}
