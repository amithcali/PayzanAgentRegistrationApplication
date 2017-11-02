package calibrage.payzanagent.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.interfaces.RequestClickListiner;
import calibrage.payzanagent.model.AgentRequestModel;

/**
 * Created by Admin on 10/26/2017.
 */

public class AgentRequetAdapter extends RecyclerView.Adapter<AgentRequetAdapter.MyHolder>{

    private Context context;
    private ArrayList<AgentRequestModel.ListResult> data;
    private RequestClickListiner requestClickListiner;


    public AgentRequetAdapter(Context context,ArrayList<AgentRequestModel.ListResult> data ){
        this.context = context;
        this.data = data;

    }



    @Override
    public AgentRequetAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_agent_request, null);
        AgentRequetAdapter.MyHolder mh = new AgentRequetAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final AgentRequetAdapter.MyHolder holder, final int position) {
    String name = data.get(holder.getAdapterPosition()).getTitleType()+" "+data.get(holder.getAdapterPosition()).getFirstName()+" "+data.get(holder.getAdapterPosition()).getMiddleName()+" "+data.get(holder.getAdapterPosition()).getLastName();
    String address = data.get(holder.getAdapterPosition()).getAddressLine1()+","+data.get(holder.getAdapterPosition()).getAddressLine2()+","+data.get(holder.getAdapterPosition()).getVillageName()+","+data.get(holder.getAdapterPosition()).getMandalName()+","+data.get(holder.getAdapterPosition()).getDistrictName()+","+data.get(holder.getAdapterPosition()).getProvinceName()+","+data.get(holder.getAdapterPosition()).getCountryName();
    String mobileNumber = data.get(holder.getAdapterPosition()).getMobileNumber();
    String email =   data.get(holder.getAdapterPosition()).getEmail();

       if (name == null)
            holder.tvAgentName.setVisibility(View.GONE);
        else {
           holder.tvAgentName.setText(name);
        }if (mobileNumber== null){
            holder.tvMobileNumber.setVisibility(View.GONE);
        }else {
            holder.tvMobileNumber.setText(data.get(holder.getAdapterPosition()).getMobileNumber());
        }if (address == null){
            holder.tvAddress.setVisibility(View.GONE);
        }else {
            holder.tvAddress.setText(address);
        }
        if (email==null){
            holder.tvEmail.setVisibility(View.GONE);
        }else {
            holder.tvEmail.setText(email);
        }


      //  holder.tvBusinessCategory.setText(data.get(holder.getAdapterPosition()).getAddressLine1());
         holder.itemView.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               requestClickListiner.onAdapterClickListiner(holder.getAdapterPosition());
           }
       }); holder.btnPick.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               requestClickListiner.onAdapterClickListiner(holder.getAdapterPosition());
           }
       }); holder.btnHold.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               requestClickListiner.onAdapterClickListiner(holder.getAdapterPosition());
           }
       });

    }

    @Override
    public int getItemCount() {
        return data.size();
    }



    public class MyHolder extends RecyclerView.ViewHolder  {
        TextView tvAgentName,tvBusinessCategory,tvEmail,tvMobileNumber,tvAddress;
        Button btnPick,btnHold;
        public MyHolder(View itemView) {
            super(itemView);
            tvAgentName=(TextView) itemView.findViewById(R.id.tracking_list_agentname);
        //    tvBusinessCategory=(TextView)itemView.findViewById(R.id.tracking_list_businesscategory);
            tvMobileNumber = (TextView)itemView.findViewById(R.id.tracking_list_mobilenumber);
            tvEmail = (TextView)itemView.findViewById(R.id.tracking_list_email);
            tvAddress=(TextView)itemView.findViewById(R.id.tracking_list_address);
            btnPick = (Button)itemView.findViewById(R.id.btn_pick);
            btnHold = (Button)itemView.findViewById(R.id.btn_hold);
           /* btnPick.setOnClickListener(this);
            btnHold.setOnClickListener(this);*/
        }

       /* @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_pick:
                    comment();
                    break;
                case R.id.btn_hold:

                    break;

            }

        }*/
    }

    private void comment() {
    }

    public void setOnAdapterListener(RequestClickListiner onAdapterListener) {
        this.requestClickListiner = onAdapterListener;
    }
}
