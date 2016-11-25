package com.ghosts.android.nerdlauncher;



import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Allam on 08/03/2016.
 */
public abstract class SingleFragmentActivity extends AppCompatActivity {

    public abstract Fragment createfragment();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if(fragment == null){
            fragment = createfragment();

            fm.beginTransaction().add(R.id.fragment_container , fragment).commit();
        }

    }
}
