package com.example.email.fragments;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.email.R;
import com.example.email.activities.CreateRulesActivity;
import com.example.email.model.Rule;
import com.example.email.model.enums.ECondition;
import com.example.email.model.enums.EOperation;
import com.example.email.repository.Repository;
import com.example.email.retrofit.RetrofitClient;
import com.example.email.retrofit.rules.RuleService;
import com.example.email.utility.Helper;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.Set;


import okhttp3.ResponseBody;
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

    private int acc_id = (Helper.getActiveAccountId() != 0) ? Helper.getActiveAccountId() : 0;
    private int user_id = Repository.loggedUser.getId();
    private int folder_id;

    public MoveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_move, container, false);

        folder_id = getArguments().getInt("folderId");

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

        toText.setOnKeyListener(new RuleOnKeyListener(toText, toChipGroup));
        ccText.setOnKeyListener(new RuleOnKeyListener(ccText, ccChipGroup));
        fromText.setOnKeyListener(new RuleOnKeyListener(fromText, fromChipGroup));
        subjectText.setOnKeyListener(new RuleOnKeyListener(subjectText, subjectChipGroup));

        toText.setOnFocusChangeListener(new RuleOnFocusChangeListener(toText));
        ccText.setOnFocusChangeListener(new RuleOnFocusChangeListener(ccText));
        fromText.setOnFocusChangeListener(new RuleOnFocusChangeListener(fromText));
        subjectText.setOnFocusChangeListener(new RuleOnFocusChangeListener(subjectText));

        return rootView;
    }

    private void getRulesForFolder(int folder_id){

        Call<Set<Rule>> call = ruleService.getRulesByFolder(user_id, folder_id, acc_id, Repository.jwt);

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

        toChipGroup.removeAllViews();
        ccChipGroup.removeAllViews();
        fromChipGroup.removeAllViews();
        subjectChipGroup.removeAllViews();

        for(Rule rule : rules){

            if(rule.getOperation() == EOperation.MOVE) {

                if (rule.getCondition() == ECondition.TO) {
                    addChip(rule.getValue(), rule.getId(), toChipGroup);

                } else if (rule.getCondition() == ECondition.CC) {
                    addChip(rule.getValue(), rule.getId(), ccChipGroup);

                } else if (rule.getCondition() == ECondition.FROM) {
                    addChip(rule.getValue(), rule.getId(), fromChipGroup);

                } else if (rule.getCondition() == ECondition.SUBJECT) {
                    addChip(rule.getValue(), rule.getId(), subjectChipGroup);
                }
            }
        }
    }

    private void addChip(String rule_value, int rule_id, ChipGroup chipGroup){

        Chip chip = new Chip(chipGroup.getContext());
        chip.setText(rule_value);
        chip.setCloseIconResource(R.drawable.ic_close);
        chip.setCloseIconVisible(true);
        chip.setId(rule_id);
        chip.setOnCloseIconClickListener(new RuleOnRemoveClickListener(chip, chipGroup));
        chipGroup.addView(chip);
    }

    @Override
    public void onResume() {
        super.onResume();
        getRulesForFolder(folder_id);
    }

    class MyAsyncTask extends AsyncTask<Void, Integer, Boolean>{

        private int id;

        public MyAsyncTask(int folder_id){ this.id = folder_id;}


        @Override
        protected Boolean doInBackground(Void... voids) {
            getRulesForFolder(id);
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }
    }

    class RuleOnKeyListener implements View.OnKeyListener{

        EditText mRuleValue;
        Chip mChip;
        ChipGroup mChipGroup;
        String keyword;
        private Rule rule = new Rule();

        public RuleOnKeyListener(EditText mValue, ChipGroup mChipGroup){

            this.mChipGroup = mChipGroup;
            this.mRuleValue = mValue;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {

            keyword = mRuleValue.getText().toString();
            int edit_text_id = mRuleValue.getId();

            //Check does entered keyword value already exist in db for this rule, if exist -> make a toast message

            if(keyword != null && !keyword.isEmpty()){

                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_UP) {

                    if(isRuleValid(keyword, edit_text_id)) {

                        rule.setValue(keyword);
                        rule.setOperation(EOperation.MOVE);

                        if (edit_text_id == R.id.to_edit_txt) {
                            rule.setCondition(ECondition.TO);
                        } else if (edit_text_id == R.id.cc_edit_txt) {
                            rule.setCondition(ECondition.CC);
                        } else if (edit_text_id == R.id.from_edit_txt) {
                            rule.setCondition(ECondition.FROM);
                        } else if (edit_text_id == R.id.subject_edit_txt) {
                            rule.setCondition(ECondition.SUBJECT);
                        }

                        Call<Rule> call = ruleService.createRule(user_id, rule, folder_id, acc_id, Repository.jwt);

                        call.enqueue(new Callback<Rule>() {
                            @Override
                            public void onResponse(Call<Rule> call, Response<Rule> response) {
                                if (!response.isSuccessful()) {
                                    Log.i("ERROR: ", String.valueOf(response.code()));
                                    return;
                                }
                                rule = response.body();
                                folderRules.add(rule);
                                mChip = new Chip(mChipGroup.getContext());
                                mChip.setText(rule.getValue());
                                mChip.setCloseIconResource(R.drawable.ic_close);
                                mChip.setCloseIconVisible(true);
                                mChip.setId(rule.getId());

                                mChip.setOnCloseIconClickListener(new RuleOnRemoveClickListener(mChip, mChipGroup));

                                mChipGroup.addView(mChip);
                                mRuleValue.setText("");
                                Toast.makeText(getActivity(), "You successfully add a new rule!", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onFailure(Call<Rule> call, Throwable t) {
                                Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
                            }
                        });
                        return true;
                    }
                }
            }
            return false;
        }
    }

    class RuleOnFocusChangeListener implements View.OnFocusChangeListener{

        EditText mRuleValue;

        public RuleOnFocusChangeListener(EditText value){
            this.mRuleValue = value;
        }

        @Override
        public void onFocusChange(View v, boolean hasFocus) {

            if (!hasFocus) {
                mRuleValue.setText("");
            }
        }
    }

    class RuleOnRemoveClickListener implements View.OnClickListener{

        Chip mChip;
        ChipGroup mChipGroup;

        public RuleOnRemoveClickListener(Chip chip, ChipGroup chipGroup){
            this.mChip = chip;
            this.mChipGroup = chipGroup;
        }

        @Override
        public void onClick(View v) {

            AlertDialog alertDialog = new AlertDialog.Builder(getActivity()).create();

            alertDialog.setTitle("Confirm");
            alertDialog.setMessage("Are you sure you want to delete this rule?");
            alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alertDialog.dismiss();
                }
            });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Call<ResponseBody> call = ruleService.deleteRule(user_id, mChip.getId(), folder_id, acc_id, Repository.jwt);

                    call.enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.isSuccessful()) {
                                Log.i("ERROR: ", String.valueOf(response.code()));
                                return;
                            }
                            if(response.code() == 200){
                                mChipGroup.removeView(v);
                                Toast.makeText(getActivity(), "You successfully delete rule!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {
                            Log.i("ERROR: ", t.getMessage(), t.fillInStackTrace());
                        }
                    });
                    alertDialog.dismiss();
                }
            });
            alertDialog.show();
        }
    }

    private boolean isRuleValid(String rule_value, int edit_txt_id) {

        Toast toast = Toast.makeText(getActivity(), "This rule value is contradictory with existing one!", Toast.LENGTH_SHORT);
        Toast toast2 = Toast.makeText(getActivity(), "This rule value already exist!", Toast.LENGTH_SHORT);

        if (folderRules != null) {

            for (Rule r : folderRules) {

                String existing_value = r.getValue();
                EOperation operation = r.getOperation();
                ECondition condition = r.getCondition();

                if (edit_txt_id == R.id.to_edit_txt) {
                    if (existing_value.contains(rule_value) && operation == EOperation.DELETE && condition == ECondition.TO) {
                        toast.show();
                        return false;
                    }
                    if (existing_value.contains(rule_value) && operation == EOperation.MOVE && condition == ECondition.TO) {
                        toast2.show();
                        return false;
                    }
                } else if (edit_txt_id == R.id.cc_edit_txt) {
                    if (existing_value.contains(rule_value) && operation == EOperation.DELETE &&  condition == ECondition.CC) {
                        toast.show();
                        return false;
                    }
                    if (existing_value.contains(rule_value) && operation == EOperation.MOVE &&  condition == ECondition.CC) {
                        toast2.show();
                        return false;
                    }
                } else if (edit_txt_id == R.id.from_edit_txt) {
                    if (existing_value.contains(rule_value) && operation == EOperation.DELETE && condition == ECondition.FROM) {
                        toast.show();
                        return false;
                    }
                    if (existing_value.contains(rule_value) && operation == EOperation.MOVE && condition == ECondition.FROM) {
                        toast2.show();
                        return false;
                    }
                } else if (edit_txt_id == R.id.subject_edit_txt) {
                    if (existing_value.contains(rule_value) && operation == EOperation.DELETE && condition == ECondition.SUBJECT) {
                        toast.show();
                        return false;
                    }
                    if (existing_value.contains(rule_value) && operation == EOperation.MOVE && condition == ECondition.SUBJECT) {
                        toast2.show();
                        return false;
                    }
                }

            }
            return true;

        }else{

            return true;
        }
    }
}
