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
import com.example.email.model.Folder;
import com.example.email.model.Message;
import com.example.email.model.interfaces.RecyclerClickListener;
import com.example.email.repository.Repository;

import java.util.ArrayList;
import java.util.Set;

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FoldersViewAdapter> {

    private Context context;
    private ArrayList<Folder> folders;
    private RecyclerClickListener recyclerClickListener;


    public FoldersAdapter(Context ctx, RecyclerClickListener listener){

        context = ctx;
        recyclerClickListener = listener;

    }

    public void setData(ArrayList<Folder> loadFolders){

        folders = loadFolders;

        notifyDataSetChanged();

    }

    @NonNull
    @Override
    public FoldersViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.folder_row, parent, false);

        return new FoldersViewAdapter(view, recyclerClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoldersViewAdapter holder, int position) {

        Set<Message> folderMessages = folders.get(position).getMessages();
        Set<Folder> childFolders = folders.get(position).getChildFolders();

        int messageCount = (folderMessages == null) ? 0 : folderMessages.size();
        int foldersCount = (childFolders == null) ? 0 : childFolders.size();

        holder.fNameView.setText(folders.get(position).getName());
        holder.mCountView.setText(messageCount + foldersCount  + "");
        holder.folderImage.setImageResource(R.drawable.ic_folder_purple);

        String folderName = folders.get(position).getName();
        if(folderName.equals("Drafts") || folderName.equals("Sent") ||
                folderName.equals("Trash") || folderName.equals("Favorites")){
            holder.deleteImage.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {

        int folders_number = (folders == null) ? 0 : folders.size();

        return folders_number;
    }

    public class FoldersViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView fNameView, mCountView;

        ImageView folderImage, deleteImage;

        RecyclerClickListener recyclerClickListener;

        public FoldersViewAdapter(@NonNull View itemview, RecyclerClickListener listener) {

            super(itemview);

            fNameView = itemview.findViewById(R.id.tv_folder_name);
            mCountView = itemview.findViewById(R.id.tv_mCount);
            folderImage = itemview.findViewById(R.id.imageFolder);
            deleteImage = itemview.findViewById(R.id.imageDeleteFolder);
            recyclerClickListener = listener;

            itemview.setOnClickListener(this);
            itemview.setOnLongClickListener(this);

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(recyclerClickListener != null){
                        int position = getLayoutPosition();
                        if(position != RecyclerView.NO_POSITION){
                            recyclerClickListener.onDeleteClick(itemview, position);
                        }
                    }
                }
            });
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

}
