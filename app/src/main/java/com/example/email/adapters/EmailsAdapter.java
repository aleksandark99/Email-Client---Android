package com.example.email.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.activities.EmailActivity;
import com.example.email.activities.EmailsActivity;
import com.example.email.model.Contact;
import com.example.email.model.Message;
import com.example.email.model.items.Tag;
import com.example.email.repository.Repository;
import com.example.email.utility.Helper;
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
          //  holder.cardView.setBackgroundColor(0XFFD8E0E5);
            holder.cardView.setBackgroundColor(0XFFC5D1D8);
        }else{
            holder.cardView.setBackgroundColor(0xFFFFFFF);
        }
//        Chip chip = new Chip(holder.chipGroup.getContext());
//       chip.setText("test bravo 1");
       Integer img = R.drawable.ic_lens_black_24dp;

//        chip.setChipIconResource(img);
//        holder.chipGroup.addView(chip);
        holder.chipGroup.removeAllViews();
        for (int i =0;i<messages.get(position).getTags().size();i++) {

            Chip chip = new Chip(holder.chipGroup.getContext());
            chip.setText(messages.get(position).getTags().get(i).getTagName());
           // chip.setChipIconResource(img);
            int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
            chip.setChipBackgroundColor(ColorStateList.valueOf(color));
            holder.chipGroup.addView(chip);
        }
        holder.attachment.setVisibility(View.INVISIBLE);
        if(messages.get(position).getAttachments().size()>0){
            holder.attachment.setVisibility(View.VISIBLE);
        }
        Contact c= Repository.get(ctx).findContactByEmail(messages.get(position).getFrom().toString().toLowerCase());
        if(c!=null){
            Log.d("DA LI JE NASOA",c.getDisplayName());

            //   int draw=2;
           // holder.profilePicture.setImageResource(draw);

            Helper.displayImageIntoImageView(c.getPhotoPath(), holder.profilePicture, ctx);
            Log.d("myTag", "This is my message");

        }else {
            Log.d("My tag 2","nije nasao nista");
        }



        holder.layoutRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ctx, EmailActivity.class);
                intent.putExtra("message",messages.get(position));//Message.class je seriazable
                ctx.startActivity(intent);

            }
        });



        Log.i("ODODOSDOOSDDSOOSDOSD",messages.get(position).getTags().get(0).getTagName());


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

                    if (message.getContent().toLowerCase().contains(constraint.toString().toLowerCase())||
                            message.getSubject().toLowerCase().contains(constraint.toString().toLowerCase())||
                            message.getFrom().toLowerCase().contains(constraint.toString().toLowerCase())||
                            Helper.DoesItContainString(message.getTo(),constraint.toString().toLowerCase())||
                            Helper.DoesItContainTag(message.getTags(),constraint.toString().toLowerCase())
                        //    message.getTo().toLowerCase().contains(constraint.toString().toLowerCase())

                    ) {
                        filteredMessages.add(message);
                    }
                    for (Tag tag:message.getTags()) {
                        if(tag.getTagName().toLowerCase().contains(constraint.toString().toLowerCase())){
                            if(!filteredMessages.contains(message)){
                                filteredMessages.add(message);
                            }
                        }

                    }// URADITI ISTO I KAD SE CC TO promene na array list ....

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
        ConstraintLayout layoutRow;
        ImageView attachment,profilePicture;
        public EmailsViewHolder(@NonNull View itemView) {
            super(itemView);
            from = itemView.findViewById(R.id.fromText1);
            subject = itemView.findViewById(R.id.subjectText1);
            date = itemView.findViewById(R.id.dateText1);
            shortContent = itemView.findViewById(R.id.shortContentText1);
            cardView = itemView.findViewById(R.id.cardViewEmailRow1);//test za unread
            chipGroup = itemView.findViewById(R.id.ChipGroupRow);

            layoutRow = itemView.findViewById(R.id.new_email_row_id);

            attachment = itemView.findViewById(R.id.hasAttachmentIcon);

            profilePicture=itemView.findViewById(R.id.emailFromImage);
        }


    }
}
