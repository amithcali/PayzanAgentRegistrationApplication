package calibrage.payzanagent.adapter;

import android.content.Context;
import android.support.v4.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.interfaces.DeleteIdproofListiner;
import calibrage.payzanagent.interfaces.DeleteLocalIdproofListiner;

/**
 * Created by Calibrage11 on 11/23/2017.
 */

public class IdproofLocalAdapter extends RecyclerView.Adapter<IdproofLocalAdapter.MyHolder> {

    private ArrayList<Pair<String, String>> addIdproof;
    private Context context;
    private DeleteLocalIdproofListiner deleteIdproofListiner;

    public IdproofLocalAdapter(Context context, ArrayList<Pair<String, String>> addIdproof) {
        this.context = context;
        this.addIdproof = addIdproof;

    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_idproof, null);
        IdproofLocalAdapter.MyHolder mh = new IdproofLocalAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        holder.name.setText(addIdproof.get(holder.getAdapterPosition()).first);
        holder.value.setText(addIdproof.get(holder.getAdapterPosition()).second);
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteIdproofListiner.onAdapterDeleteClickListiner(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return addIdproof.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        TextView name, value;
        ImageView deleteIcon;

        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            value = (TextView) itemView.findViewById(R.id.value);
            deleteIcon = (ImageView) itemView.findViewById(R.id.deleteIcon);
        }
    }

    public void setOnAdapterListener(DeleteLocalIdproofListiner onAdapterListener) {
        this.deleteIdproofListiner = onAdapterListener;
    }
}
