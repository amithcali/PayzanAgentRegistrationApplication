package calibrage.payzanagent.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.interfaces.RequestClickListiner;
import calibrage.payzanagent.model.AgentRequestModel;
import calibrage.payzanagent.model.CommentsModel;
import calibrage.payzanagent.networkservice.ApiConstants;
import calibrage.payzanagent.networkservice.MyServices;
import calibrage.payzanagent.networkservice.ServiceFactory;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Admin on 11/2/2017.
 */

public class ApprovedAgentsAdapter extends RecyclerView.Adapter<ApprovedAgentsAdapter.MyHolder>{

    private Context context;
    private String middlename,name;
    private ArrayList<AgentRequestModel.ListResult> data;
    private RequestClickListiner requestClickListiner;
    private Subscription commentSubscription;

    public ApprovedAgentsAdapter(Context context,ArrayList<AgentRequestModel.ListResult> data ){
        this.context = context;
        this.data = data;

    }
    @Override
    public ApprovedAgentsAdapter.MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_approved_agents, null);
        ApprovedAgentsAdapter.MyHolder mh = new ApprovedAgentsAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final ApprovedAgentsAdapter.MyHolder holder, final  int position) {
        middlename = String.valueOf(data.get(holder.getAdapterPosition()).getMiddleName());
        if (middlename.equalsIgnoreCase("null"))
        {
            middlename = " ";
            name = data.get(holder.getAdapterPosition()).getTitleType()+" "+data.get(holder.getAdapterPosition()).getFirstName()+" "+data.get(holder.getAdapterPosition()).getLastName();

        }
        else {
            name = data.get(holder.getAdapterPosition()).getTitleType()+" "+data.get(holder.getAdapterPosition()).getFirstName()+" "+middlename+" "+data.get(holder.getAdapterPosition()).getLastName();
        }
       // String name = data.get(holder.getAdapterPosition()).getTitleType()+" "+data.get(holder.getAdapterPosition()).getFirstName()+" "+data.get(holder.getAdapterPosition()).getLastName();
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


      /*  //  holder.tvBusinessCategory.setText(data.get(holder.getAdapterPosition()).getAddressLine1());
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
        });*/
        final boolean[] isOpen = {true};
        holder.openComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // requestClickListiner.onAdapterClickListiner(holder.getAdapterPosition(),true,true);
                if(isOpen[0]){
                    holder.commentRecylerview.setVisibility(View.VISIBLE);
                    getRequestComments(data.get(holder.getAdapterPosition()).getId(), holder);
                    isOpen[0] = false;
                }else{
                    holder.commentRecylerview.setVisibility(View.GONE);
                    isOpen[0] = true;
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class MyHolder extends RecyclerView.ViewHolder  {
        TextView tvAgentName,tvBusinessCategory,tvEmail,tvMobileNumber,tvAddress,openComment;
        RecyclerView commentRecylerview;
       // Button btnPick,btnHold;
        public MyHolder(View itemView) {
            super(itemView);
            tvAgentName=(TextView) itemView.findViewById(R.id.tracking_list_agentname);
            commentRecylerview = (RecyclerView) itemView.findViewById(R.id.commentRecylerview);
            openComment = (TextView) itemView.findViewById(R.id.openComment);
            //    tvBusinessCategory=(TextView)itemView.findViewById(R.id.tracking_list_businesscategory);
            tvMobileNumber = (TextView)itemView.findViewById(R.id.tracking_list_mobilenumber);
            tvEmail = (TextView)itemView.findViewById(R.id.tracking_list_email);
            tvAddress=(TextView)itemView.findViewById(R.id.tracking_list_address);
          /*  btnPick = (Button)itemView.findViewById(R.id.btn_pick);
            btnHold = (Button)itemView.findViewById(R.id.btn_hold);*/
           /* btnPick.setOnClickListener(this);
            btnHold.setOnClickListener(this);*/
        }
    }
    public void getRequestComments(int requestId, final ApprovedAgentsAdapter.MyHolder holder) {
        MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
        commentSubscription = service.GetStatusHistory(ApiConstants.STATUS_HISTORY + requestId)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommentsModel>() {
                    @Override
                    public void onCompleted() {
                        // Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (e instanceof HttpException) {
                            ((HttpException) e).code();
                            ((HttpException) e).message();
                            ((HttpException) e).response().errorBody();
                            try {
                                ((HttpException) e).response().errorBody().string();
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                            e.printStackTrace();
                        }
                        Toast.makeText(context, "fail", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommentsModel commentsModel) {

                        if (!commentsModel.getListResult().isEmpty()) {
                            CommentAdapter commentAdapter = new CommentAdapter(context, commentsModel.getListResult());
                            holder.commentRecylerview.setLayoutManager(new LinearLayoutManager(context));
                            holder.commentRecylerview.setAdapter(commentAdapter);

                        }

                    }
                });

    }

}
