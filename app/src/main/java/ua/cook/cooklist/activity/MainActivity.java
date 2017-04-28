package ua.cook.cooklist.activity;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ua.cook.cooklist.adapter.ReceiptAdapter;
import ua.cook.cooklist.data.Receipt;
import ua.cook.cooklist.R;

public class MainActivity extends AppCompatActivity implements ReceiptAdapter.ItemClickListener {
    private FirebaseAuth auth;
    private RecyclerView receiptRecyclerView;
    private ReceiptAdapter receiptAdapter;
    private List<Receipt> currReceipts;
    private RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase database;
    private DatabaseReference ref;
    private FloatingActionButton fab;
    private Context ctx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();

        receiptRecyclerView = (RecyclerView) findViewById(R.id.main_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        receiptRecyclerView.setLayoutManager(layoutManager);

        fab = (FloatingActionButton) findViewById(R.id.main_activity_fab);


        currReceipts = new ArrayList<>();
        receiptAdapter = new ReceiptAdapter(this,currReceipts,this);
        receiptAdapter.setHasStableIds(true);
        receiptRecyclerView.setAdapter(receiptAdapter);

        database = FirebaseDatabase.getInstance();

        ref = database.getReference("receipts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                currReceipts.clear();
                for(DataSnapshot data : dataSnapshot.getChildren()){
                    currReceipts.add(data.getValue(Receipt.class));
                    receiptAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        receiptRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE){
                    fab.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 ||dy<0 && fab.isShown())
                    fab.hide();
            }
        });
        ctx = this;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx,AddReceiptActivity.class);
                ctx.startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout_menu_item:
                auth.signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    @Override
    public void onItemClick(Receipt receipt, int position) {
        Intent intent = new Intent(this, ReceiptDeteilsActivity.class);
        intent.putExtra("receipt",receipt);
        startActivity(intent);
    }
}
