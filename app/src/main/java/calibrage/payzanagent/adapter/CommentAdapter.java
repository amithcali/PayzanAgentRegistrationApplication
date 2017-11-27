package calibrage.payzanagent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import calibrage.payzanagent.R;
import calibrage.payzanagent.model.CommentsModel;

/**
 * Created by Calibrage11 on 11/27/2017.
 */

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.Myholder> {
    private Context context;
    private List<CommentsModel.ListResult> listResultList;

   public CommentAdapter(Context context,List<CommentsModel.ListResult> listResultList){
       this.context = context;
       this.listResultList = listResultList;

   }
    @Override
    public Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_comment, null);
        CommentAdapter.Myholder mh = new CommentAdapter.Myholder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(Myholder holder, int position) {
       if(listResultList.get(holder.getAdapterPosition()).getAssignToUser()!=null)
       holder.assignTo.setText("Assign To"+" "+":"+" "+listResultList.get(holder.getAdapterPosition()).getAssignToUser().toString());
        if(listResultList.get(holder.getAdapterPosition()).getStatusType()!=null)
       holder.statusType.setText("Status"+" "+":"+" "+listResultList.get(holder.getAdapterPosition()).getStatusType().toString());
        if(listResultList.get(holder.getAdapterPosition()).getComments()!=null)
       holder.commentsTxt.setText("Comments"+" "+":"+" "+listResultList.get(holder.getAdapterPosition()).getComments().toString());
        if(listResultList.get(holder.getAdapterPosition()).getCreated()!=null)
       holder.created.setText("Created Date"+" "+":"+" "+listResultList.get(holder.getAdapterPosition()).getCreated().toString());

    }

    @Override
    public int getItemCount() {
        return listResultList.size();
    }

    public class Myholder extends RecyclerView.ViewHolder {

       private TextView statusType,assignTo,commentsTxt,created;
        public Myholder(View itemView) {
            super(itemView);
            statusType=(TextView) itemView.findViewById(R.id.statusType);
            assignTo=(TextView) itemView.findViewById(R.id.assignTo);
            commentsTxt=(TextView) itemView.findViewById(R.id.commentsTxt);
            created=(TextView) itemView.findViewById(R.id.created);
        }
    }
}
