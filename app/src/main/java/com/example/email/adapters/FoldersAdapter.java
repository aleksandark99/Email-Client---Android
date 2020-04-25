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

public class FoldersAdapter extends RecyclerView.Adapter<FoldersAdapter.FoldersViewAdapter> {

    Context context;
    String[] folderNames;
    int[] messageCount;
    int[] images;

    private OnNoteListener mOnNoteListener;

    public FoldersAdapter(Context ctx, String[] names, int[] messages, int[] idImage, OnNoteListener mListener){

        context = ctx;
        folderNames = names;
        messageCount = messages;
        images = idImage;
        mOnNoteListener = mListener;

    }

    @NonNull
    @Override
    public FoldersViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(R.layout.folder_row, parent, false);

        return new FoldersViewAdapter(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull FoldersViewAdapter holder, int position) {

        holder.fNameView.setText(folderNames[position]);
        holder.mCountView.setText(messageCount[position] + "");
        holder.folderImage.setImageResource(images[0]);
    }

    @Override
    public int getItemCount() {

        return folderNames.length;
    }

    public class FoldersViewAdapter extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView fNameView, mCountView;

        ImageView folderImage;

        OnNoteListener onNoteListener;

        public FoldersViewAdapter(@NonNull View itemview, OnNoteListener listener) {

            super(itemview);

            fNameView = itemview.findViewById(R.id.tv_folder_name);
            mCountView = itemview.findViewById(R.id.tv_mCount);
            folderImage = itemview.findViewById(R.id.imageFolder);
            onNoteListener = listener;

            itemview.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onNoteListener.onNoteClick(getLayoutPosition());
        }
    }

    public interface OnNoteListener{

        void onNoteClick(int position);
    }

}
