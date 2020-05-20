package com.example.email.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.email.R;
import com.example.email.fragments.AccountFragment;
import com.example.email.model.Account;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class AccountActivity extends AppCompatActivity {

    private static final String ACCOUNT_KEY = "ftn.sit.email.activities.account";
    private static final String ACCOUNTS_LIST_KEY = "ftn.sit.email.activities.accounts_list";

    private ViewPager mViewPager;
    private ArrayList<Account> mAccounts;
    private Account mAccount;
    private DotsIndicator mDotsIndicator;

    public static Intent newIntent(Context packageContext, int position, ArrayList<Account> accounts){
        Intent intent = new Intent(packageContext, AccountActivity.class);
        intent.putExtra(ACCOUNT_KEY, position);
        intent.putParcelableArrayListExtra(ACCOUNTS_LIST_KEY, accounts);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        int pos =  getIntent().getIntExtra(ACCOUNT_KEY, 0);

        mAccounts = getIntent().getParcelableArrayListExtra(ACCOUNTS_LIST_KEY);
        mAccount = mAccounts.get(pos);

        mDotsIndicator = findViewById(R.id.dot1);
        mViewPager = (ViewPager) findViewById(R.id.activity_account_pager_view_pager);


        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {



            @Override
            public Fragment getItem(int position) {
                Account account = mAccounts.get(position);
                return AccountFragment.newInstance(account);
            }

            @Override
            public int getCount() {
                return mAccounts.size();
            }
        });


        mAccounts.stream()
                .filter(account -> account.getId() == mAccount.getId())
                .forEach(account -> mViewPager.setCurrentItem(mAccounts.indexOf(account)));
    }



    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
