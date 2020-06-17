package com.example.email.fragments;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.example.email.R;
import com.example.email.model.Rule;
import com.example.email.model.enums.ECondition;
import com.example.email.model.enums.EOperation;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.rules.RuleService;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.Executor;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 */
public class MoveFragment extends Fragment {

    ChipGroup toChipGroup, ccChipGroup, fromChipGroup, subjectChipGroup;
    EditText toText, ccText, fromText, subjectText;

    private ArrayList<Rule> folderRules;

    private final Retrofit mRetrofit = RetrofitClient.getRetrofitInstance();
    private final RuleService ruleService = mRetrofit.create(RuleService.class);

    private final int MOVE_OPERATION = EOperation.MOVE.ordinal();

    public MoveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_move, container, false);

        int folder_id = getArguments().getInt("folderId");

        MyAsyncTask m = new MyAsyncTask(folder_id);
        m.execute();

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

    private void getRulesForMoveOperation(int folder_id){

        Call<Set<Rule>> call = ruleService.getRulesByOperation(folder_id, MOVE_OPERATION, Repository.jwt);

        call.enqueue(new Callback<Set<Rule>>() {
            @Override
            public void onResponse(Call<Set<Rule>> call, Response<Set<Rule>> response) {

                if (!response.isSuccessful()) {
                    Log.i("ERROR: ", String.valueOf(response.code()));
                    return;
                }
                folderRules = new ArrayList<>(response.body());
                setRulesAsChips(folderRules);
            }
            @Override
            public void onFailure(Call<Set<Rule>> call, Throwable t) {
                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
            }
        });
    }

    private void setRulesAsChips(ArrayList<Rule> rules){

        for(Rule rule : rules){

            if(rule.getCondition() == ECondition.TO){
                addChip(rule.getValue(), toChipGroup);

            }else if(rule.getCondition() == ECondition.CC){
                addChip(rule.getValue(), ccChipGroup);

            }else if(rule.getCondition() == ECondition.FROM){
                addChip(rule.getValue(), fromChipGroup);

            }else if(rule.getCondition() == ECondition.SUBJECT){
                addChip(rule.getValue(), subjectChipGroup);
            }
        }
    }

    private void addChip(String rule_value, ChipGroup chipGroup){

        Chip chip = new Chip(chipGroup.getContext());
        chip.setText(rule_value);
        chip.setCloseIconResource(R.drawable.ic_close);
        chip.setCloseIconVisible(true);
        chipGroup.addView(chip);
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Boolean>{

        private int id;

        public MyAsyncTask(int folder_id){ this.id = folder_id;}


        @Override
        protected Boolean doInBackground(Void... voids) {
            getRulesForMoveOperation(id);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }
}
