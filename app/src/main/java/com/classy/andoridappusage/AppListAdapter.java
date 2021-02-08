package com.classy.andoridappusage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class AppListAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<AppInfo> appInfoList;

    AppListAdapter(Context context, List<AppInfo> appInfoList) {
        layoutInflater =(android.view.LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        this.appInfoList = appInfoList;
    }

    @Override
    public int getCount() {
        return appInfoList.size();
    }

    @Override
    public AppInfo getItem(int position) {
        return appInfoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder listViewHolder;
        if(convertView == null) {
            listViewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.app_list, parent, false);

            listViewHolder.appName = convertView.findViewById(R.id.list_app_name);
            listViewHolder.appIcon = convertView.findViewById(R.id.list_app_icon);
            convertView.setTag(listViewHolder);
        }
        else {
            listViewHolder = (ViewHolder)convertView.getTag();
        }
        listViewHolder.appName.setText(appInfoList.get(position).getAppName());
        listViewHolder.appIcon.setImageDrawable(appInfoList.get(position).getIcon());


        return convertView;
    }

    class ViewHolder {
        TextView appName;
        ImageView appIcon;

    }
}
