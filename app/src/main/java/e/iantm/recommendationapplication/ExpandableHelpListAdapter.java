package e.iantm.recommendationapplication;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class ExpandableHelpListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private Map<String, List<String>> expandableListDetail;

    public ExpandableHelpListAdapter(Context context, List<String> expandableListTitle,
                                     Map<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }

    @Override
    public Object getChild(int listPosition, int expandedListPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .get(expandedListPosition);
    }

    @Override
    public long getChildId(int listPosition, int expandedListPosition) {
        return expandedListPosition;
    }

    @Override
    public View getChildView(int listPosition, final int expandedListPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String expandedListText = (String) getChild(listPosition, expandedListPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.expandedhelplistitem, null);
        }
        TextView expandedListTextView = (TextView) convertView
                .findViewById(R.id.expandedListItem);
        expandedListTextView.setText(expandedListText);
        return convertView;
    }

    @Override
    public int getChildrenCount(int listPosition) {
        return this.expandableListDetail.get(this.expandableListTitle.get(listPosition))
                .size();
    }

    @Override
    public Object getGroup(int listPosition) {
        return this.expandableListTitle.get(listPosition);
    }

    @Override
    public int getGroupCount() {
        return this.expandableListTitle.size();
    }

    @Override
    public long getGroupId(int listPosition) {
        return listPosition;
    }

    @Override
    public View getGroupView(int listPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(listPosition);
        if (convertView == null) {
            LayoutInflater layoutInflater = (LayoutInflater) this.context.
                    getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.helplistview, null);
        }
        TextView listTitleTextView = (TextView) convertView
                .findViewById(R.id.listTitle);
        listTitleTextView.setTypeface(null, Typeface.BOLD);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int listPosition, int expandedListPosition) {
        return true;
    }

    public static Map<String, List<String>> getData() {
        Map<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List question1 = new ArrayList();
        question1.add("Answer");
        List question2 = new ArrayList();
        question2.add("Answer");
        List question3 = new ArrayList();
        question3.add("Answer");
        List question4 = new ArrayList();
        question4.add("Answer");
        List question5 = new ArrayList();
        question5.add("Answer");
        List question6 = new ArrayList();
        question6.add("Answer");
        List question7 = new ArrayList();
        question7.add("Answer");
        List question8 = new ArrayList();
        question8.add("Answer");
        List question9 = new ArrayList();
        question9.add("Answer");
        List question10 = new ArrayList();
        question10.add("Answer");

        expandableListDetail.put("Question 1", question1);
        expandableListDetail.put("Question 2", question2);
        expandableListDetail.put("Question 3", question3);
        expandableListDetail.put("Question 4", question4);
        expandableListDetail.put("Question 5", question5);
        expandableListDetail.put("Question 6", question6);
        expandableListDetail.put("Question 7", question7);
        expandableListDetail.put("Question 8", question8);
        expandableListDetail.put("Question 9", question9);
        expandableListDetail.put("Question 10", question10);

        return expandableListDetail;
    }
}
