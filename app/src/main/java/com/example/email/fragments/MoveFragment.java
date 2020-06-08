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
public class MoveFragment extends Fragment {

    ChipGroup toChipGroup, ccChipGroup, fromChipGroup, subjectChipGroup;

    EditText toText, ccText, fromText, subjectText;

    public MoveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_move, container, false);

        toChipGroup = rootView.findViewById(R.id.chipGroupTo);
        ccChipGroup = rootView.findViewById(R.id.chipGroupCc);
        fromChipGroup = rootView.findViewById(R.id.chipGroupFrom);
        subjectChipGroup = rootView.findViewById(R.id.chipGroupSubject);

        toText = rootView.findViewById(R.id.to_edit_txt);
        ccText = rootView.findViewById(R.id.cc_edit_txt);
        fromText = rootView.findViewById(R.id.from_edit_txt);
        subjectText = rootView.findViewById(R.id.subject_edit_txt);


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

//                    chip.setText("");
                    toText.setText("");
                }
            }
        });


        return rootView;
    }
}
