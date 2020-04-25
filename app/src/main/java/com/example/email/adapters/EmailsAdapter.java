package com.example.email.adapters;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.model.Message;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Collection;
import java.util.zip.Inflater;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailsViewHolder> implements Filterable {

    ArrayList<Message> messages;
    Context ctx;
    //za filter messages
    ArrayList<Message> messagesAll;


    public EmailsAdapter(Context ctx, ArrayList<Message> messages) {
        this.messages = messages;
        this.ctx = ctx;
        //za FilterProba
        this.messagesAll = new ArrayList<>(messages);

    }

    @NonNull
    @Override
    public EmailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
//        View view = inflater.inflate(R.layout.emails_row, parent, false);
        View view = inflater.inflate(R.layout.new_emails_row, parent, false);

        return new EmailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailsViewHolder holder, int position) {
        holder.date.setText("Resiti api 24/26");
        holder.from.setText(messages.get(position).getFrom());
        holder.subject.setText(messages.get(position).getSubject());
        holder.shortContent.setText(messages.get(position).getContent());
        // ovde make attachment icon visible ako ima attachmenta poruka
        if (messages.get(position).isUnread()) {
            holder.cardView.setBackgroundColor(0xFFF6A8A8);
        }
        Chip chip = new Chip(holder.chipGroup.getContext());
        chip.setText("test bravo 1");
        Integer img = R.drawable.ic_lens_black_24dp;

        chip.setChipIconResource(img);


        holder.chipGroup.addView(chip);


    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    Filter filter = new Filter() {
        //run on background thread
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            ArrayList<Message> filteredMessages = new ArrayList<Message>();
            if (constraint.toString().isEmpty()) {
                filteredMessages.addAll(messagesAll);
            } else {
                for (Message message : messagesAll) {
                    if (message.getContent().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        filteredMessages.add(message);
                    }
                }
            }
            FilterResults filterResults = new FilterResults();
            filterResults.values = filteredMessages;

            return filterResults;
        }

        //run on ui thread
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            messages.clear();
            messages.addAll((Collection<? extends Message>) results.values);


            notifyDataSetChanged();

        }
    };

    public class EmailsViewHolder extends RecyclerView.ViewHolder {

        TextView from, subject, shortContent, date;
        CardView cardView;
        ChipGroup chipGroup;
        public EmailsViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.fromText1);
            subject = itemView.findViewById(R.id.subjectText1);
            date = itemView.findViewById(R.id.dateText1);
            shortContent = itemView.findViewById(R.id.shortContentText1);
            cardView = itemView.findViewById(R.id.cardViewEmailRow1);//test za unread
            chipGroup = itemView.findViewById(R.id.ChipGroupRow);
        }


    }
}
