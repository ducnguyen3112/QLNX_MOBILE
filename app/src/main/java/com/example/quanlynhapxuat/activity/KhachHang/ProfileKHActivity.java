package com.example.quanlynhapxuat.activity.KhachHang;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.quanlynhapxuat.R;
import com.example.quanlynhapxuat.fragment.KhachHang.LichSuKHFragment;
import com.example.quanlynhapxuat.fragment.KhachHang.ThongTinKHFragment;
import com.example.quanlynhapxuat.model.KhachHang;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;


public class ProfileKHActivity extends AppCompatActivity {

    private TabLayout mTableLayout;
    private ViewPager2 mViewPager;
    private KHViewPagerAdapter khViewPagerAdapter;
    KhachHang dto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_khactivity);
        Intent intent = getIntent();
        dto = (KhachHang) intent.getSerializableExtra("KH");
        mTableLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        khViewPagerAdapter = new KHViewPagerAdapter(this);
        mViewPager.setAdapter(khViewPagerAdapter);

        new TabLayoutMediator(mTableLayout, mViewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText("Thông Tin");
                    break;
                case 1:
                    tab.setText("Lịch sử");
                    break;
            }

        }).attach();
    }

    public class KHViewPagerAdapter extends FragmentStateAdapter {
        public KHViewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            Intent intent = getIntent();
            KhachHang dto = (KhachHang) intent.getSerializableExtra("KH");
            Fragment fragment;
            Bundle args = new Bundle();
            args.putSerializable("KH", dto);
            switch (position) {
                case 0:
                    fragment = new ThongTinKHFragment();
                    fragment.setArguments(args);
                    return fragment;
                case 1:
                    fragment = new LichSuKHFragment();
                    fragment.setArguments(args);
                    return fragment;
                default:
                    fragment = new ThongTinKHFragment();
                    fragment.setArguments(args);
                    return fragment;
            }
        }

        @Override
        public int getItemCount() {
            return 2;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(ProfileKHActivity.this, ListKHActivity.class);
        startActivity(intent);
    }
}