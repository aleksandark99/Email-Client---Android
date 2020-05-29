package com.example.email.activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.model.items.Tag;
import com.example.email.repository.Repository;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Random;

public class TagsActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ChipGroup tagsChipGroup;
    private Button add,cancel;
    private EditText chipText;
    private ArrayList<Tag> tags;
    View forDelete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tags);

        tags=Repository.get(this).getMyTags();

        tagsChipGroup=findViewById(R.id.tagsChipGroup);
        add=findViewById(R.id.addTagButton);
        cancel=findViewById(R.id.btnCancelTagChange);
        chipText=findViewById(R.id.tagField);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chipText.setText("");
                add.setText("Add new tag");
                forDelete=null;
                cancel.setVisibility(View.GONE);
            }
        });
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        /// ovo uraditi tek ako prodje na bekendu
                        Chip c=(Chip)forDelete;
                        tagsChipGroup.removeView(forDelete);
                        // OVO RADITI AKO PRODJE NA BEKU i radit za repo ne samo ovde vizuelno
                        tags.removeIf(tag -> (tag.getTagName().equals(c.getText().toString())));
                        // takodje removati iz iz repository liste
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(TagsActivity.this, "Delete canceled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chipText.getText().toString().equals("")&&add.getText().toString().equals("Add new tag") ) {
                    Chip chip = new Chip(tagsChipGroup.getContext());
                    chip.setText(chipText.getText().toString());
                    chip.setCloseIconResource(R.drawable.exit_icon);
                    chip.setCloseIconVisible(true);
                    int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                    chip.setChipBackgroundColor(ColorStateList.valueOf(color));


//                    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            switch (which) {
//                                case DialogInterface.BUTTON_POSITIVE:
//                                    /// ovo uraditi tek ako prodje na bekendu
//                                    tagsChipGroup.removeView(forDelete);
//                                    // takodje removati iz iz repository liste
//                                    break;
//
//                                case DialogInterface.BUTTON_NEGATIVE:
//                                    Toast.makeText(TagsActivity.this, "Delete canceled", Toast.LENGTH_SHORT).show();
//                                    break;
//                            }
//                        }
//                    };
                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            forDelete = v;
                            AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                            builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                    .setNegativeButton("No", dialogClickListener).show();
                        }
                    });

                    chip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            cancel.setVisibility(View.VISIBLE);

                            chipText.setText(chip.getText().toString());
                            add.setText("CONFIRM CHANGE");
                            forDelete=v;

                        }
                    });
                    Tag t = new Tag();
                    t.setTagName(chip.getText().toString());
                    tags.add(t);
                    tagsChipGroup.addView(chip);
                    chipText.setText("");

                }
                else{
                    if(!chipText.getText().toString().equals("") && add.getText().toString().equals("CONFIRM CHANGE")){
                        Toast.makeText(TagsActivity.this, "Change", Toast.LENGTH_SHORT).show();
                        Chip c=(Chip)forDelete;
                        c.setText(chipText.getText().toString());
                        chipText.setText("");
                        add.setText("Add new tag");
                        cancel.setVisibility(View.GONE);

                    }else{
                        Toast.makeText(TagsActivity.this, Integer.toString(tags.size()), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
            // dodavanje tagova iz repoa
        if(tags.size()>0){
            for (Tag t:tags) {
                Chip chip = new Chip(tagsChipGroup.getContext());
                chip.setText(t.getTagName());
                chip.setCloseIconResource(R.drawable.exit_icon);
                chip.setCloseIconVisible(true);
                int color = ((int) (Math.random() * 16777215)) | (0xFF << 24);
                chip.setChipBackgroundColor(ColorStateList.valueOf(color));

                chip.setOnCloseIconClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        forDelete = v;
                        AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });

                chip.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cancel.setVisibility(View.VISIBLE);

                        chipText.setText(chip.getText().toString());
                        add.setText("CONFIRM CHANGE");
                        forDelete=v;

                    }
                });
                tagsChipGroup.addView(chip);
            }
        }
    }
}
