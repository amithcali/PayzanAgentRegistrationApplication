package calibrage.payzanagent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import calibrage.payzanagent.R;
import calibrage.payzanagent.interfaces.DeleteIdproofListiner;
import calibrage.payzanagent.interfaces.DeleteImageListiner;
import calibrage.payzanagent.model.GetIdproofModel;

/**
 * Created by Calibrage11 on 11/22/2017.
 */

public class IdproofAdapter extends RecyclerView.Adapter<IdproofAdapter.Myholder> {
    private Context context;
    private DeleteIdproofListiner deleteIdproofListiner;
    private List<GetIdproofModel.ListResult> listResultList;

    public IdproofAdapter(Context  context,List<GetIdproofModel.ListResult> listResultList){
        this.context = context;
        this.listResultList = listResultList;

    }
    @Override
    public IdproofAdapter.Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_idproof, null);
        IdproofAdapter.Myholder mh = new IdproofAdapter.Myholder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(IdproofAdapter.Myholder holder, int position) {

        holder.name.setText(listResultList.get(holder.getAdapterPosition()).getIdProofType());
        holder.value.setText(listResultList.get(holder.getAdapterPosition()).getIdProofNumber());

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listResultList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder{
        TextView name,value;
        ImageView deleteIcon;
        public Myholder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            value = (TextView) itemView.findViewById(R.id.value);
            deleteIcon = (ImageView) itemView.findViewById(R.id.deleteIcon);
        }


    }

    public void setOnAdapterListener(DeleteIdproofListiner onAdapterListener) {
        this.deleteIdproofListiner = onAdapterListener;
    }
}
