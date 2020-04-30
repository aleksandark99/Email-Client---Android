package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.Message;

public class EmailActivity extends AppCompatActivity {

    TextView subjectText,fromText,toText,dateText,contentText,toLongText,fromLongText,ccLongText;

    String subjectString,fromString,toString,dateString,contentString,toLongString,fromLongString,ccLongString;

    LinearLayout detailToTexts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        subjectText = findViewById(R.id.emailSubjectText);
        fromText = findViewById(R.id.emailFromText);
        toText = findViewById(R.id.emailToText);
        dateText = findViewById(R.id.emailDateText);
        contentText = findViewById(R.id.emailContentText);
        toLongText = findViewById(R.id.EmailToLong);
        fromLongText = findViewById(R.id.EmailFromLong);
        ccLongText = findViewById(R.id.EmailCcLong);
        detailToTexts = findViewById(R.id.detailToTexts);
        getData();
        setData();


        toText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(detailToTexts.getVisibility()==View.VISIBLE){
                    detailToTexts.setVisibility(View.GONE);
                }else{
                    detailToTexts.setVisibility(View.VISIBLE);

                }

            }
        });

    }
    private void getData(){
        if(getIntent().hasExtra("message")){
            Message m = (Message)getIntent().getSerializableExtra("message");
            subjectString=m.getSubject();

        }else{
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
        }
    }
    private void setData(){
        subjectText.setText(subjectString);
    }
}
