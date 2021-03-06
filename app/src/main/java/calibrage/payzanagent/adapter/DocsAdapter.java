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
import calibrage.payzanagent.fragments.AggrementDocumentsFragment;
import calibrage.payzanagent.interfaces.DeleteIdproofListiner;
import calibrage.payzanagent.interfaces.DocListiner;
import calibrage.payzanagent.model.GetDocumentsResponseModel;

/**
 * Created by Admin on 11/23/2017.
 */

public class DocsAdapter extends RecyclerView.Adapter<DocsAdapter.Myholder>{
    private Context context;
    private DocListiner docListiner;
    private List<GetDocumentsResponseModel.ListResult> listResultList;


    public DocsAdapter(Context  context,List<GetDocumentsResponseModel.ListResult> listResultList){
        this.context = context;
        this.listResultList = listResultList;

    }

    @Override
    public DocsAdapter.Myholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_doc, null);
        DocsAdapter.Myholder mh = new DocsAdapter.Myholder(v);
        return mh;

    }

    @Override
    public void onBindViewHolder(final DocsAdapter.Myholder holder, int positio) {
        holder.name.setText(listResultList.get(holder.getAdapterPosition()).getFileName());

        if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".png")){
            holder.sourceImage.setImageResource(R.drawable.png_sbi);
        }else if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".jpg")){
            holder.sourceImage.setImageResource(R.drawable.jpg_sbi);
        }else if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".jpeg")){
            holder.sourceImage.setImageResource(R.drawable.jpeg_sbi);
        }else if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".pdf")){
            holder.sourceImage.setImageResource(R.drawable.pdf_sbi);
        }else if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".docx")){
            holder.sourceImage.setImageResource(R.drawable.docx_sbi);
        }else if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".doc")){
            holder.sourceImage.setImageResource(R.drawable.doc_sbi);
        }else if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase("txt")){
            holder.sourceImage.setImageResource(R.drawable.txt_sbi);
        }else if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".rtf")){
            holder.sourceImage.setImageResource(R.drawable.exe_sbi);
        }
      //  holder.value.setText(listResultList.get(holder.getAdapterPosition()).getFileLocation());
        holder.deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                docListiner.onDocAdapterClickListiner(holder.getAdapterPosition(),"delete");
            }
        });
        holder.dowmload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".pdf")||listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".docx")||listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".doc")||listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".txt")||listResultList.get(holder.getAdapterPosition()).getFileExtension().equalsIgnoreCase(".rtf")){
                    docListiner.onDocAdapterClickListiner(holder.getAdapterPosition(),"downloadPdf");
                }else{
                    docListiner.onDocAdapterClickListiner(holder.getAdapterPosition(),"downloadImage");
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return listResultList.size();
    }



    public class Myholder extends RecyclerView.ViewHolder{
        TextView name,value;
        ImageView deleteIcon,sourceImage,dowmload;
        public Myholder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            sourceImage = (ImageView)itemView.findViewById(R.id.attach_img);

           // value = (TextView) itemView.findViewById(R.id.value);
            deleteIcon = (ImageView) itemView.findViewById(R.id.deleteIcon);
            dowmload = (ImageView) itemView.findViewById(R.id.dowmload);
        }


    }

    public void setOnAdapterListener(DocListiner onAdapterListener) {
        this.docListiner = onAdapterListener;
    }


}
