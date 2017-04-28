package ua.cook.cooklist.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ua.cook.cooklist.R;
import ua.cook.cooklist.data.Receipt;

public class ReceiptDeteilsActivity extends AppCompatActivity {

    private ImageView mainImage;
    private TextView  nameTextView;
    private TextView descTextView;
    private TextView authorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt_deteils);

        mainImage = (ImageView) findViewById(R.id.details_image);
        nameTextView = (TextView) findViewById(R.id.detais_name);
        descTextView = (TextView) findViewById(R.id.details_desc);
        authorTextView = (TextView) findViewById(R.id.details_author);

        setData();
    }
    private void setData(){
        Receipt rec = getIntent().getParcelableExtra("receipt");
        Glide.with(this).load(rec.getImageUrl()).into(mainImage);
        nameTextView.setText(rec.getName());
        descTextView.setText(rec.getDescription());
        authorTextView.setText("Рецепт додав: " + rec.getAuthor());
    }
}
