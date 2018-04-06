package com.example.prateek.testmp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class UserListActivity extends AppCompatActivity {

        private SectionsPageAdapter sectionsPageAdapter;
        private ViewPager viewPager;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_list);



            sectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());

            viewPager = (ViewPager) findViewById(R.id.container);

            sectionsPageAdapter.addFragment(new teacher_list_fragment(), "Teachers");
            sectionsPageAdapter.addFragment(new student_list_fragment(), "Students");


            viewPager.setAdapter(sectionsPageAdapter);

            TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
            tabLayout.setupWithViewPager(viewPager);
        }

        public class SectionsPageAdapter extends FragmentPagerAdapter {

            private final List<Fragment> mFragmentsList = new ArrayList<>();
            private final List<String> mFragmentTitleList = new ArrayList<>();

            public SectionsPageAdapter(FragmentManager fm) {
                super(fm);
            }

            public void addFragment(Fragment fragment, String title) {
                mFragmentsList.add(fragment);
                mFragmentTitleList.add(title);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return mFragmentTitleList.get(position);
            }

            @Override
            public android.support.v4.app.Fragment getItem(int position) {
                return mFragmentsList.get(position);
            }

            @Override
            public int getCount() {
                return mFragmentsList.size();
            }
        }
    }
