package com.example.email.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.email.R;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class DeleteFragment extends Fragment {

    ChipGroup toChipGroup, ccChipGroup, fromChipGroup, subjectChipGroup;

    EditText toText, ccText, fromText, subjectText;

    public DeleteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_delete, container, false);

        toChipGroup = rootView.findViewById(R.id.toChipGroup);
        ccChipGroup = rootView.findViewById(R.id.ccChipGroup);
        fromChipGroup = rootView.findViewById(R.id.fromChipGroup);
        subjectChipGroup = rootView.findViewById(R.id.subjectChipGroup);

        toText = rootView.findViewById(R.id.toEditText);
        ccText = rootView.findViewById(R.id.ccEditText);
        fromText = rootView.findViewById(R.id.fromEditText);
        subjectText = rootView.findViewById(R.id.subjectEditText);


        toText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                String keyword = toText.getText().toString();

                if(keyword != null || !keyword.isEmpty()) {

                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

                        Chip chip = new Chip(toChipGroup.getContext());

                        chip.setText(keyword);

                        chip.setCloseIconResource(R.drawable.ic_close);

                        chip.setCloseIconVisible(true);

                        chip.setOnCloseIconClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                toChipGroup.removeView(v);
                            }
                        });

                        toChipGroup.addView(chip);

                        toText.setText("");

                        return true;
                    }

                }

                return false;
            }

        });

        toText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                String keyword = toText.getText().toString();

                if(!hasFocus && !keyword.isEmpty()){

                    Chip chip = new Chip(toChipGroup.getContext());

                    chip.setText(keyword);

                    chip.setCloseIconResource(R.drawable.ic_close);

                    chip.setCloseIconVisible(true);

                    chip.setOnCloseIconClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                            toChipGroup.removeView(v);
                        }
                    });

                    toChipGroup.addView(chip);

                    chip.setText("");
                }
            }
        });


        return rootView;
    }
}
