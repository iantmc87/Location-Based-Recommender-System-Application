package e.iantm.recommendationapplication;

//imports packages
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

/************************************************************
 Author - Ian McManus
 Version - 1.0.0
 Date - 30/04/2019
 Description - List Adapter for help screen questions and answers

 ************************************************************/

public class ExpandableHelpListAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> expandableListTitle;
    private Map<String, List<String>> expandableListDetail;

    public ExpandableHelpListAdapter(Context context, List<String> expandableListTitle,
                                     Map<String, List<String>> expandableListDetail) {
        this.context = context;
        this.expandableListTitle = expandableListTitle;
        this.expandableListDetail = expandableListDetail;
    }//end adapter constructor

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
        Map<String, List<String>> expandableListDetail = new LinkedHashMap<String, List<String>>();

        List question1 = new ArrayList();
        question1.add("If current location is not showing on the map, you may have location services turned off or you are in a low signal area. Please check both of these and try again.");
        List question2 = new ArrayList();
        question2.add("No, as the username is taken directly from your device, upon installation all data held on our database will be automatically re-downloaded.");
        List question3 = new ArrayList();
        question3.add("Currently, accounts are unable to be deleted automatically from within the application. Please contact support at support@recommendations.co.uk and ask for your account to be deleted.");
        List question4 = new ArrayList();
        question4.add("Try adding more of your preferred features, to enable a wider range of restaurants to be recommended.");
        List question5 = new ArrayList();
        question5.add("Please contact support with your username and issue at support@recommendations.co.uk");
        List question6 = new ArrayList();
        question6.add("You may be experiencing a network connectivity issue, please check your internet connection and try again.");
        List question7 = new ArrayList();
        question7.add("Currently the recommendation system doesn't cover all locations in the UK, we are adding more places constantly, please check again at a later date.");
        List question8 = new ArrayList();
        question8.add("To enable these systems a review needs to be added first, please search for your favourite places visited and leave a review.");
        List question9 = new ArrayList();
        question9.add("Please check settings page to see if notifications are turned off, if still having issues check notification settings on your device, to see notifications are allowed for this application.");
        List question10 = new ArrayList();
        question10.add("Unfortunately at this moment in time there is no option to change your username, please check back for future updates.");

        expandableListDetail.put("Why is my location not showing on the map?", question1);
        expandableListDetail.put("Why am I not getting any recommendations?", question6);
        expandableListDetail.put("I need to reinstall the application will I lose all my data?", question2);
        expandableListDetail.put("I don't like any of the recommended places suggested!", question4);
        expandableListDetail.put("I want to change my username!", question10);
        expandableListDetail.put("Postcode not appearing in search bar autocomplete?", question7);
        expandableListDetail.put("Why can't I choose Collaborative/Hybrid systems?", question8);
        expandableListDetail.put("I want to delete my account!", question3);
        expandableListDetail.put("Why am I not receiving notifications?", question9);
        expandableListDetail.put("Still having problems?", question5);

        return expandableListDetail;
    }//end method for populating questions and answers
}//end class
