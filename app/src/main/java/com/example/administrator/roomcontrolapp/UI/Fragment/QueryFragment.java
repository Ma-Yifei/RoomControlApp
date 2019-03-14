package com.example.administrator.roomcontrolapp.UI.Fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.roomcontrolapp.Adapter.QueryFragmentPagerAdapter;
import com.example.administrator.roomcontrolapp.R;
import com.example.administrator.roomcontrolapp.UI.QueryUI.ViewHalfYearFragment;
import com.example.administrator.roomcontrolapp.UI.QueryUI.ViewMonthFragment;
import com.example.administrator.roomcontrolapp.UI.QueryUI.ViewWeekFragment;

import java.util.ArrayList;
import java.util.List;

public class QueryFragment extends Fragment {
    private List<Fragment> queryFragments = new ArrayList<>();
    private QueryFragmentPagerAdapter adapter;
    private View view;
    ViewPager viewPager;
    private TabLayout.Tab one,two,three;
    private TabLayout tabLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.query_fragment,container,false);
        return view;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentPager();
        tabLayout();
    }
    /*
       用TabLayout来实现查询界面的顶部Tab导航
     */
    private void tabLayout(){
        tabLayout= (TabLayout) view.findViewById(R.id.query_top);
        tabLayout.setupWithViewPager(viewPager);
        one = tabLayout.getTabAt(0);
        two = tabLayout.getTabAt(1);
        three = tabLayout.getTabAt(2);

        one.setText("最近一周");
        two.setText("最近一月");
        three.setText("最近半年");

    }
    //在查询界面实现滑屏
    public void fragmentPager(){
        queryFragments.clear();
        queryFragments.add(new ViewWeekFragment());
        queryFragments.add(new ViewMonthFragment());
        queryFragments.add(new ViewHalfYearFragment());
        adapter = new QueryFragmentPagerAdapter(getChildFragmentManager(),queryFragments);
        viewPager = view.findViewById(R.id.query_vp);
        viewPager.setAdapter(adapter);
    }
}
