package com.example.email.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;

import com.example.email.R;
import com.example.email.adapters.RulePagerAdapter;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class CreateRulesActivity extends AppCompatActivity {

    Toolbar toolbar;
    ViewPager pager;
    TabLayout mTabLayout;
    TabItem moveItem, deleteItem;
    RulePagerAdapter pagerAdapter;

    private int folder_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_rules);

        folder_id = getIntent().getIntExtra("folder_id", 0);

        toolbar = findViewById(R.id.add_rule_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("Add new rules");

        pager = findViewById(R.id.rule_view_pager);
        mTabLayout = findViewById(R.id.rule_tab_layout);
        moveItem = findViewById(R.id.tab_move);
        deleteItem = findViewById(R.id.tab_delete);

        pagerAdapter = new RulePagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, mTabLayout.getTabCount());
        pagerAdapter.setFolderId(folder_id);
        pager.setAdapter(pagerAdapter);

        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                pager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

    }
}
