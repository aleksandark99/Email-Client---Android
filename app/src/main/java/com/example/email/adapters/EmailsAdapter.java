package com.example.email.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.email.R;
import com.example.email.model.Message;

import java.util.ArrayList;
import java.util.zip.Inflater;

public class EmailsAdapter extends RecyclerView.Adapter<EmailsAdapter.EmailsViewHolder> {

    ArrayList<Message> messages;
    Context ctx;
    public EmailsAdapter(Context ctx, ArrayList<Message> messages){
    this.messages=messages;
    this.ctx=ctx;
    }


    @NonNull
    @Override
    public EmailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(ctx);
        View view = inflater.inflate(R.layout.emails_row,parent,false);
        return new EmailsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmailsViewHolder holder, int position) {
        //Zanemraiti ovo glupost teska nastala usled zaboravljene linije u xml i ocajnickog pokusaja da se bug nastalom istom linijom resi ...
//        for(int i=0;i<messages.size();i++){
//            holder.date.setText("Resiti api 24/26");
//            holder.from.setText(messages.get(position).getFrom());
//            holder.subject.setText(messages.get(position).getSubject());
//            holder.shortContent.setText(messages.get(position).getContent());
//        }
        holder.date.setText("Resiti api 24/26");
        holder.from.setText(messages.get(position).getFrom());
        holder.subject.setText(messages.get(position).getSubject());
        holder.shortContent.setText(messages.get(position).getContent());
        if(messages.get(position).isUnread()==false){
            holder.cardView.setBackgroundColor(0xFFFFFFFF);
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public class EmailsViewHolder extends RecyclerView.ViewHolder {

        TextView from,subject,shortContent,date;
        CardView cardView;
        public EmailsViewHolder(@NonNull View itemView){
            super(itemView);
            from=itemView.findViewById(R.id.fromText);
            subject=itemView.findViewById(R.id.subjectText);
            date=itemView.findViewById(R.id.dateText);
            shortContent=itemView.findViewById(R.id.shortContentText);
            cardView=itemView.findViewById(R.id.cardViewEmailRow);//test za unread

        }



    }
}
