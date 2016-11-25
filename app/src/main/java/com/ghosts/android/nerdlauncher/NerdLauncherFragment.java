package com.ghosts.android.nerdlauncher;

import android.app.Notification;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Allam on 08/03/2016.
 */
public class NerdLauncherFragment extends Fragment {
    private RecyclerView mRecyclerView;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.nerd_launcher_fragment, container, false);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.fragment_nerd_launcher_recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity() , 4));
        setUpAdapter();
        return v;
    }

    private void setUpAdapter() {

        Intent startUpIntent = new Intent(Intent.ACTION_MAIN);
        startUpIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        final PackageManager packageManager = getActivity().getPackageManager();

        List<ResolveInfo> activities = packageManager.queryIntentActivities(startUpIntent, 0);
        Collections.sort(activities, new Comparator<ResolveInfo>() {
            PackageManager pm = getActivity().getPackageManager();

            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                return String.CASE_INSENSITIVE_ORDER.compare(
                        lhs.loadLabel(pm).toString(),
                        rhs.loadLabel(pm).toString());
            }
        });
        Log.i("Allam Tag", "Found Activiteies #" + activities.size());
        mRecyclerView.setAdapter(new ActivityAdapter(activities));
    }

    private class ActivityHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ResolveInfo mResolveInfos;
        private ImageView mImageView;
        private TextView mTextView;

        public ActivityHolder(LayoutInflater inflater , ViewGroup container) {
            super(inflater.inflate(R.layout.list_item_nerd , container , false));

            mTextView = (TextView) itemView.findViewById(R.id.text_view);
            mImageView = (ImageView) itemView.findViewById(R.id.image_icon);
            mTextView.setOnClickListener(this);
            mImageView.setOnClickListener(this);
        }

        public void BindActivitiesIcon(ResolveInfo resolveInfos) {
            mResolveInfos = resolveInfos;
            PackageManager pm = getActivity().getPackageManager();
            Drawable icon = mResolveInfos.loadIcon(pm);
            String label = mResolveInfos.loadLabel(pm).toString();
            mImageView.setImageDrawable(icon);
            mTextView.setText(label);

        }



        @Override
        public void onClick(View v) {
            ActivityInfo activityInfo = mResolveInfos.activityInfo;

            Intent intent = new Intent(Intent.ACTION_MAIN)
                    .setClassName(activityInfo.applicationInfo.packageName , activityInfo.name)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }

    private class ActivityAdapter extends RecyclerView.Adapter<ActivityHolder>{
        private List<ResolveInfo> mResolveInfos;

        public ActivityAdapter(List<ResolveInfo> resolveInfos){
            mResolveInfos = resolveInfos;
        }
        @Override
        public ActivityHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            return new ActivityHolder(inflater , parent);
        }

        @Override
        public void onBindViewHolder(ActivityHolder holder, int position) {
        ResolveInfo resolveInfo = mResolveInfos.get(position);
            holder.BindActivitiesIcon(resolveInfo);
        }

        @Override
        public int getItemCount() {
            return mResolveInfos.size();
        }
    }

}


