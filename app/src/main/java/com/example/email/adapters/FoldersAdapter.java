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

        int messageCount = (folderMessages == null) ? 0 : folderMessages.size();

        holder.fNameView.setText(folders.get(position).getName());
        holder.mCountView.setText(messageCount + "");
        holder.folderImage.setImageResource(R.drawable.ic_folder_purple);
    }

    @Override
    public int getItemCount() {

        int folders_number = (folders == null) ? 0 : folders.size();

        return folders_number;
    }

    public class FoldersViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        TextView fNameView, mCountView;

        ImageView folderImage;

        RecyclerClickListener recyclerClickListener;

        public FoldersViewAdapter(@NonNull View itemview, RecyclerClickListener listener) {

            super(itemview);

            fNameView = itemview.findViewById(R.id.tv_folder_name);
            mCountView = itemview.findViewById(R.id.tv_mCount);
            folderImage = itemview.findViewById(R.id.imageFolder);
            recyclerClickListener = listener;

            itemview.setOnClickListener(this);
            itemview.setOnLongClickListener(this);
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
