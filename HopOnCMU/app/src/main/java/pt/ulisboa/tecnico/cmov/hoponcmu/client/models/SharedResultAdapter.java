package pt.ulisboa.tecnico.cmov.hoponcmu.client.models;

/**
 * Created by Daniela on 13/05/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

import pt.ulisboa.tecnico.cmov.hoponcmu.R;

public class SharedResultAdapter extends BaseExpandableListAdapter {
    private final List<String> array;
    private final Context context;
    private final HashMap<String,List<String>> expandableItems;

    public SharedResultAdapter(Context c_context, List<String> a_array, HashMap<String,List<String>> e_array) {
        //super(c_context, R.layout.content_sharedresult, a_array);
        context = c_context;
        array = a_array;
        expandableItems = e_array;
    }

    @Override
    public int getGroupCount() {
        return array.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandableItems.get(array.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return array.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandableItems.get(array.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String currentResult = (String) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.content_sharedresult, null);
        }

        TextView sharedResult = (TextView) convertView.findViewById(R.id.sharedresult);
        sharedResult.setText(currentResult);

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition,childPosition);

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.expandable_item, null);
        }

        TextView expandableItems = (TextView) convertView.findViewById(R.id.expandableItem);
        expandableItems.setText(childText);

        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}