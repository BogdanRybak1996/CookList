package ua.cook.cooklist.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.util.ArrayList;
import java.util.List;

import ua.cook.cooklist.data.Receipt;
import ua.cook.cooklist.R;

public class ReceiptAdapter extends RecyclerView.Adapter<ReceiptAdapter.ReceiptViewHolder> {

    private List<Receipt> receipts = new ArrayList<>();
    private ItemClickListener clickListener;
    private Context context;

    public ReceiptAdapter(Context context, List<Receipt> receipts, ItemClickListener clickListener) {
        this.context = context;
        this.receipts = receipts;
        this.clickListener = clickListener;
    }

    @Override
    public ReceiptViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ReceiptViewHolder holder;
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_item, parent, false);
        holder = new ReceiptViewHolder(layoutView, context, clickListener);
        return holder;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public void onBindViewHolder(ReceiptViewHolder holder, int position) {
        holder.bind(receipts.get(position));
    }

    @Override
    public int getItemCount() {
        if(receipts != null){
            return receipts.size();
        }
        return 0;
    }

    public interface ItemClickListener {
        void onItemClick(Receipt receipt, int position);
    }

    class ReceiptViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final ItemClickListener clickListener;
        private ImageView image;
        private TextView name;
        private TextView desc;
        private Receipt receipt;
        private Context context;

        public ReceiptViewHolder(View itemView, Context context, ItemClickListener clickListener) {
            super(itemView);
            this.clickListener = clickListener;
            this.context = context;
            image = (ImageView) itemView.findViewById(R.id.item_picture);
            name = (TextView) itemView.findViewById(R.id.item_name);
            desc = (TextView) itemView.findViewById(R.id.item_desc);
            itemView.setOnClickListener(this);
        }
        void bind(Receipt receipt){
            this.receipt = receipt;
            name.setText(receipt.getName());
            desc.setText(receipt.getDescription());
            Glide.with(context).load(receipt.getImageUrl()).into(image);
        }

        @Override
        public void onClick(View v) {
            if(clickListener != null){
                clickListener.onItemClick(receipt,getAdapterPosition());
            }
        }
    }
}
