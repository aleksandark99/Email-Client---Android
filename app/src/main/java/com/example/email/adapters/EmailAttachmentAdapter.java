package com.example.email.adapters;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.activities.EmailActivity;
import com.example.email.model.Attachment;
import com.example.email.utility.Writer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Base64;

public class EmailAttachmentAdapter  extends RecyclerView.Adapter<EmailAttachmentAdapter.EmailAttachmentViewHolder> {

    private ArrayList<Attachment> attachments;
    private Context ctx;
    public EmailAttachmentAdapter(ArrayList<Attachment> att, Context context){
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
    public EmailAttachmentAdapter.EmailAttachmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(ctx);
        View view=layoutInflater.inflate(R.layout.attachment_row,parent,false);


        return new EmailAttachmentAdapter.EmailAttachmentViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull EmailAttachmentAdapter.EmailAttachmentViewHolder holder, int position) {
        holder.name.setText(attachments.get(position).getName());
        holder.imageView.setVisibility(View.GONE);
        holder.layoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ctx, "download ttachment", Toast.LENGTH_SHORT).show();
                byte[] decodedBytes = Base64.getDecoder().decode(attachments.get(position).getData());
                String decodedString = new String(decodedBytes);

                String filename=attachments.get(position).getName()+"."+attachments.get(position).getMime_type();

                Writer.writeFileExternalStorage(ctx,filename,decodedBytes);

            }
        });
    }

    @Override
    public int getItemCount() {
        return attachments.size();
    }

    public class EmailAttachmentViewHolder  extends  RecyclerView.ViewHolder{

        TextView name;
        ImageView imageView;
        ConstraintLayout layoutRow;


        public EmailAttachmentViewHolder(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.attachmentNameId);
            imageView=itemView.findViewById(R.id.x_image_view);
            layoutRow=itemView.findViewById(R.id.attachment_row_id_s);
        }
    }
}
