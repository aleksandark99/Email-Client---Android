package com.example.email.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.model.Attachment;

import java.util.ArrayList;

public class AttachmentAdapter extends RecyclerView.Adapter<AttachmentAdapter.AtachmentViewHolder> {

    private ArrayList<Attachment> attachments;
    private Context ctx;
    public AttachmentAdapter(ArrayList<Attachment> att, Context context){
        attachments=att;
        ctx=context;
    }

    public void setData(ArrayList<Attachment> at){
        this.attachments=at;
        notifyDataSetChanged();
    }
    public ArrayList<Attachment> getAttachments(){
        return this.attachments;
    }


    @NonNull
    @Override
    public AtachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(ctx);
        View view=layoutInflater.inflate(R.layout.attachment_row,parent,false);


        return new AtachmentViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull AtachmentViewHolder holder, int position) {
            holder.name.setText(attachments.get(position).getName());
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    attachments.remove(attachments.get(position));
                    notifyDataSetChanged();
                }
            });
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    public class AtachmentViewHolder  extends  RecyclerView.ViewHolder{

        TextView name;
        ImageView imageView;


        public AtachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.attachmentNameId);
            imageView=itemView.findViewById(R.id.x_image_view);
        }
    }


}
