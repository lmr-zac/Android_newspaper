package qz.rg.newspaper.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import qz.rg.newspaper.R;
import qz.rg.newspaper.fragment.HomeFragment;
import qz.rg.newspaper.fragment.MyFragment;
import qz.rg.newspaper.fragment.VideoFragment;
//管理底部导航栏和切换不同的 Fragment
public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment;
    private VideoFragment videoFragment;
    private MyFragment myFragment;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 初始化 Fragment
        homeFragment = new HomeFragment();
        videoFragment = new VideoFragment();
        myFragment = new MyFragment();



        // 添加并隐藏videoFragment和myFragment
        FragmentTransaction initialTransaction = getSupportFragmentManager().beginTransaction();
        initialTransaction.add(R.id.fragment_container, homeFragment);
        initialTransaction.add(R.id.fragment_container, videoFragment);
        initialTransaction.add(R.id.fragment_container, myFragment);
        initialTransaction.hide(videoFragment);
        initialTransaction.hide(myFragment);
        initialTransaction.commit();

        //导航栏点击事件
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            Fragment fragment = null;
            switch (item.getItemId()) {
                case R.id.nav_home:
                    showFragment(homeFragment);  // 直接显示已初始化的实例
                    break;
                case R.id.nav_video:
                    showFragment(videoFragment);
                    break;
                case R.id.nav_my:
                    showFragment(myFragment);
                    break;
            }
            return true;
        });
    }

    // 控制 Fragment 显示/隐藏
    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.hide(homeFragment);
        transaction.hide(videoFragment);
        transaction.hide(myFragment);
        transaction.show(fragment);
        transaction.commit();
    }
}