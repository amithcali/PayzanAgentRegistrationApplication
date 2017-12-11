package calibrage.payzanagent.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

import calibrage.payzanagent.R;
import calibrage.payzanagent.interfaces.DeleteImageListiner;
import calibrage.payzanagent.interfaces.RequestClickListiner;

/**
 * Created by Calibrage11 on 11/11/2017.
 */

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyHolder> {
    private Context context;
    private ArrayList<Bitmap> bitmapArrayList;
    private DeleteImageListiner deleteImageListiner;

    public ImageAdapter(Context context, ArrayList<Bitmap> bitmaipArrayList) {
        this.context = context;
        this.bitmapArrayList = bitmaipArrayList;
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_images, null);
        ImageAdapter.MyHolder mh = new ImageAdapter.MyHolder(v);
        return mh;
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        if(bitmapArrayList.get(holder.getAdapterPosition())!=null){
            holder.imageView.setImageBitmap(bitmapArrayList.get(holder.getAdapterPosition()));
        }else {
            holder.imageView.setImageResource(R.drawable.file_exe);
        }

        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteImageListiner.onAdapterClickListiner(holder.getAdapterPosition(),false);
            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(bitmapArrayList.get(holder.getAdapterPosition())!=null){
                    deleteImageListiner.onAdapterClickListiner(holder.getAdapterPosition(),true);
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return bitmapArrayList.size();
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        private ImageView imageView, deleteIcon;

        public MyHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image);
            deleteIcon = (ImageView) itemView.findViewById(R.id.deleteIcon);
        }
    }

    public void setOnAdapterListener(DeleteImageListiner onAdapterListener) {
        this.deleteImageListiner = onAdapterListener;
    }
}
