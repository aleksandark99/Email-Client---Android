package com.example.email.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;

public class FolderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static int TYPE_FOLDER = 0;
    public static int TYPE_EMAIL = 1;

    Context context;
    int[] imageFolder;
    ArrayList<Folder> folders;
    ArrayList<Message> messages;


    public FolderAdapter(Context ctx, int[] idImage, ArrayList<Folder> childFolders, ArrayList<Message> folderMessages) {

        context = ctx;
        imageFolder = idImage;
        folders = childFolders;
        messages = folderMessages;

    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if(viewType == TYPE_FOLDER){

            view = LayoutInflater.from(context).inflate(R.layout.folder_row, parent, false);

            return new FolderViewHolder(view);

        }else{

            view = LayoutInflater.from(context).inflate(R.layout.new_emails_row, parent, false);

            return new EmailViewHolder(view);
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

                    if(position <= messages.size()) {

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


    class FolderViewHolder extends RecyclerView.ViewHolder{

        TextView fNameView, mCountView;

        ImageView folderImage;

        public FolderViewHolder(@NonNull View itemView) {

            super(itemView);
            fNameView = itemView.findViewById(R.id.tv_folder_name);
            mCountView = itemView.findViewById(R.id.tv_mCount);
            folderImage = itemView.findViewById(R.id.imageFolder);
        }

        private void setFolderDetails(Folder folder){

            fNameView.setText(folder.getName());
            int childFoldersSize = (folder.getChildFolders() == null) ? 0 : folder.getChildFolders().size();
            mCountView.setText(childFoldersSize + "");
            folderImage.setImageResource(imageFolder[0]);
        }
    }


    public class EmailViewHolder extends RecyclerView.ViewHolder{

        TextView from, subject, shortContent, date;
        CardView cardView;
        ChipGroup chipGroup;

        public EmailViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.fromText1);
            subject = itemView.findViewById(R.id.subjectText1);
            date = itemView.findViewById(R.id.dateText1);
            shortContent = itemView.findViewById(R.id.shortContentText1);
            cardView = itemView.findViewById(R.id.cardViewEmailRow1);//test za unread
            chipGroup = itemView.findViewById(R.id.ChipGroupRow);

        }

        private void setEmailDetails(Message message){

            from.setText(message.getFrom());
            subject.setText(message.getSubject());
            date.setText("Api problem");
            shortContent.setText(message.getContent());

        }
    }
}
