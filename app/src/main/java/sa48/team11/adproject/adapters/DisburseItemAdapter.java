package sa48.team11.adproject.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import sa48.team11.adproject.R;
import sa48.team11.adproject.models.ItemDisburse;

/**
 * Created by hninnwe on 2019-07-31
 */
public class DisburseItemAdapter extends RecyclerView.Adapter<DisburseItemAdapter.MyViewHolder> {

    private Context context;
    private List<ItemDisburse> list = new ArrayList<>();
    private LayoutInflater inflater;
    public DisburseItemAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void updateList(List<ItemDisburse> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v =inflater.inflate(R.layout.adapter_update_disburse, viewGroup, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int position) {
        myViewHolder.bind(list.get(position));
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvName, tvNeeded,tvActual;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName =itemView.findViewById(R.id.tv_name);
            tvNeeded =itemView.findViewById(R.id.tv_needed);
            tvActual =itemView.findViewById(R.id.tv_actual);
        }
        public void bind(ItemDisburse item) {
            tvName.setText(item.getDescription());
            tvNeeded.setText(String.format("%d",item.getNeededQty()));
            tvActual.setText(String.format("%d",item.getActualQty()));
        }
    }
}