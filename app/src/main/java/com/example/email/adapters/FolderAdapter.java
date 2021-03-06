package com.example.email.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.model.Contact;
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.example.email.model.interfaces.RecyclerClickListener;
import com.example.email.repository.Repository;
import com.example.email.utility.Helper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int TYPE_FOLDER = 0;
    public static int TYPE_EMAIL = 1;

    Context context;
    ArrayList<Folder> folders;
    ArrayList<Message> messages;

    private RecyclerClickListener recyclerClickListener;


    public FolderAdapter(Context ctx, RecyclerClickListener listener) {

        context = ctx;
        recyclerClickListener = listener;

    }

    public void setData(ArrayList<Folder> subFolders){
        folders = subFolders;
    }

    public void setMessages(ArrayList<Message> folderMessages) {messages = folderMessages;}


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(viewType == TYPE_FOLDER){

            view = LayoutInflater.from(context).inflate(R.layout.folder_row, parent, false);

            return new FolderViewHolder(view, recyclerClickListener);

        }else{

            view = LayoutInflater.from(context).inflate(R.layout.new_emails_row, parent, false);

            return new EmailViewHolder(view, recyclerClickListener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        switch(holder.getItemViewType()){

            case 0:

                if(folders != null) {

                    if(position < folders.size()) {

                        ((FolderViewHolder) holder).setFolderDetails(folders.get(position));
                    }
                }
                break;

            case 1:

                if(messages != null) {

                    if(position <= messages.size() || position > messages.size()) {

                        int foldersSize = (folders == null) ? 0 : folders.size();

                        ((EmailViewHolder) holder).setEmailDetails(messages.get(position - foldersSize));

                    }
                }
                break;
        }
    }

    @Override
    public int getItemViewType(int position) {

        if(folders == null){

            return TYPE_EMAIL;

        }if(messages == null){

            return TYPE_FOLDER;

        }if(position < folders.size()){

            return TYPE_FOLDER;

        }else{

            return TYPE_EMAIL;

        }

    }

    @Override
    public int getItemCount() {

        int foldersSize = (folders == null) ? 0 : folders.size();

        int messagesSize = (messages == null) ? 0 : messages.size();

        return foldersSize + messagesSize;
    }


    class FolderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView fNameView, mCountView;

        ImageView folderImage, deleteImage;

        RecyclerClickListener recyclerClickListener;

        public FolderViewHolder(@NonNull View itemView, RecyclerClickListener listener) {

            super(itemView);
            fNameView = itemView.findViewById(R.id.tv_folder_name);
            mCountView = itemView.findViewById(R.id.tv_mCount);
            folderImage = itemView.findViewById(R.id.imageFolder);
            deleteImage = itemView.findViewById(R.id.imageDeleteFolder);

            recyclerClickListener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerClickListener != null){
                        int position = getLayoutPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerClickListener.onDeleteClick(itemView, position);
                        }

                    }
                }
            });
        }

        private void setFolderDetails(Folder folder){

            fNameView.setText(folder.getName());
            int childFoldersSize = (folder.getChildFolders() == null) ? 0 : folder.getChildFolders().size();
            int childFolderMessages = (folder.getMessages() == null) ? 0 : folder.getMessages().size();
            //mCountView.setText(childFoldersSize + childFolderMessages  + "");
            folderImage.setImageResource(R.drawable.ic_folder_purple);
        }

        @Override
        public void onClick(View v) {

            this.recyclerClickListener.OnItemClick(v, getLayoutPosition());

        }

        @Override
        public boolean onLongClick(View v) {

            this.recyclerClickListener.OnLongItemClick(v, getLayoutPosition());

            return false;
        }
    }


    public class EmailViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView from, subject, shortContent, date;
        CardView cardView;
        ChipGroup chipGroup;
        ConstraintLayout layoutRow;
        ImageView attachment, profilePicture;

        RecyclerClickListener recyclerClickListener;

        public EmailViewHolder(@NonNull View itemView, RecyclerClickListener listener) {
            super(itemView);
            from = itemView.findViewById(R.id.fromText1);
            subject = itemView.findViewById(R.id.subjectText1);
            date = itemView.findViewById(R.id.dateText1);
            shortContent = itemView.findViewById(R.id.shortContentText1);
            cardView = itemView.findViewById(R.id.cardViewEmailRow1);//test za unread
            chipGroup = itemView.findViewById(R.id.ChipGroupRow);

            layoutRow = itemView.findViewById(R.id.new_email_row_id);

            attachment = itemView.findViewById(R.id.hasAttachmentIcon);

            profilePicture = itemView.findViewById(R.id.imageView5);

            recyclerClickListener = listener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        private void setEmailDetails(Message message){

            date.setText(customdateformater(message.getDate_time()));
            from.setText(message.getFrom());
            subject.setText(message.getSubject());
            shortContent.setText(message.getContent());
            if (message.isUnread()) {
                cardView.setBackgroundColor(0XFFC5D1D8);
            } else {
                cardView.setBackgroundColor(0xFFFFFFF);
            }
            chipGroup.removeAllViews();

            if( message.getTags()!=null){

                for (int i = 0; i < message.getTags().size(); i++) {
                    Chip chip = new Chip(chipGroup.getContext());
                    chip.setText(message.getTags().get(i).getTagName());
                    int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(color));
                    chipGroup.addView(chip);
                }
            }

            attachment.setVisibility(View.INVISIBLE);
            if (message.getAttachments().size() > 0) {
                attachment.setVisibility(View.VISIBLE);
            }

            Contact c;
            try {
                c = Repository.get(context).findContactByEmail(message.getFrom().toLowerCase());

            } catch (Exception e) {
                c = null;

            }
            if (c != null) {
                Helper.displayImageIntoImageView(c.getPhotoPath(), profilePicture, context);

            } else {
                profilePicture.setImageResource(R.drawable.ic_person_black_24dp);
            }

        }

        @Override
        public void onClick(View v) {

            this.recyclerClickListener.OnItemClick(v, getLayoutPosition());
        }

        @Override
        public boolean onLongClick(View v) {

            this.recyclerClickListener.OnLongItemClick(v, getLayoutPosition());

            return true;
        }
    }

    private String customdateformater(String datetoformat){
        return datetoformat.substring(0,10)+" "+datetoformat.substring(11,16);
    }
}
