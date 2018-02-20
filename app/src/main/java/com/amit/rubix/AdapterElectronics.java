package com.amit.rubix;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by amitchipkar on 20/02/18.
 */

public class AdapterElectronics extends BaseExpandableListAdapter {
    Context context = null;
    List<String> groupItem = null;
    Map<String, List<String>> childItem = null;
    int count = 0;

    public AdapterElectronics(Context context, List<String> groupItem, Map<String, List<String>> childItem)
    {
        this.context = context;
        this.groupItem = groupItem;
        this.childItem = childItem;
    }

    @Override
    public int getGroupCount() {
        return groupItem.size();
    }

    @Override
    public int getChildrenCount(int groupPosition)
    {
        return childItem.get(groupItem.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition)
    {
        return groupItem.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition)
    {
        return childItem.get(groupItem.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition)
    {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition)
    {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent)
    {
        /* THIS IS MY GROUP NAME EG. FEATURE */
        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.adapter_header, parent, false);
        }

        TextView tvHeader = (TextView)convertView.findViewById(R.id.tv_group_name);

        final String groupName = (String) getGroup(groupPosition);
        tvHeader.setText(groupName);

        return convertView;
    }

    /* SETTING CHILD VALUE. WHEN USER CLICK ON HEADER */
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent)
    {
        if (convertView == null)
        {
            LayoutInflater childInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = childInflater.inflate(R.layout.adapter_child, parent, false);
        }

        final String childGroupName = (String) getChild(groupPosition, childPosition);
        String strColumn = "", strValue = "";

        int index = childGroupName.indexOf(":");
        strColumn = childGroupName.substring(0, index);
        strValue = childGroupName.substring(index+1);

        TextView tvChildName = (TextView)convertView.findViewById(R.id.tv_child_name);
        tvChildName.setText(strColumn.trim());



        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
