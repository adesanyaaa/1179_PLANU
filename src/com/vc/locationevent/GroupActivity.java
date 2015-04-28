package com.vc.locationevent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

import com.vc.locationevent.PinnedSectionListView.PinnedSectionListAdapter;
import android.annotation.SuppressLint;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds;
import android.provider.ContactsContract.CommonDataKinds.GroupMembership;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Data;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;

public class GroupActivity extends ListActivity implements OnClickListener {

	private boolean hasHeaderAndFooter;
	private boolean isFastScroll;
	private boolean addPadding;
	private boolean isShadowVisible = true;
	private int mDatasetUpdateCount;
	static List<String> groupList;
	ArrayList<Item1> groups;
	ContentResolver cResolver;
	List<String> C_ID;
	static List<String> C_NAME;
	List<String> C_NUMBER;
	int no_of_groups=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_group);
		
		groupList=new ArrayList<String>();
		C_ID=new ArrayList<String>();
		C_NAME=new ArrayList<String>();
		C_NUMBER=new ArrayList<String>();
		
		if (savedInstanceState != null) {
		    isFastScroll = savedInstanceState.getBoolean("isFastScroll");
		    addPadding = savedInstanceState.getBoolean("addPadding");
		    isShadowVisible = savedInstanceState.getBoolean("isShadowVisible");
		    hasHeaderAndFooter = savedInstanceState.getBoolean("hasHeaderAndFooter");
		}
		
		
		
		cResolver=getContentResolver();
		Cursor cr=cResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
		
		while(cr.moveToNext())
		{
			C_ID.add(cr.getString(cr.getColumnIndex(ContactsContract.Contacts._ID)));
			C_NAME.add(cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
			C_NUMBER.add(cr.getString(cr.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
		}
		
		groups=fetchGroups();
		int i=0;
		while(i<groups.size())
		{
			groupList.add(groups.get(i).name);
			i++;
		}
		
		no_of_groups=groupList.size()+1;
		
		initializeHeaderAndFooter();
		initializeAdapter();
		initializePadding();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.group, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private ArrayList<Item1> fetchGroups(){
	    ArrayList<Item1> groupList = new ArrayList<Item1>();
	    String[] projection = new String[]{ContactsContract.Groups._ID,ContactsContract.Groups.TITLE};
	    Cursor cursor = getContentResolver().query(ContactsContract.Groups.CONTENT_URI, 
	            projection, null, null, null);
	    ArrayList<String> groupTitle = new ArrayList<String>();
	    while(cursor.moveToNext()){
	            Item1 Item1 = new Item1();
	            Item1.id = cursor.getString(cursor.getColumnIndex(ContactsContract.Groups._ID));
	            
	            String groupName =cursor.getString(cursor.getColumnIndex(ContactsContract.Groups.TITLE));
	             
	        if(groupName.contains("Group:"))
	        groupName = groupName.substring(groupName.indexOf("Group:")+"Group:".length()).trim();
	             
	        if(groupName.contains("Favorite_"))
	        groupName = "Favorite";
	              
	        if(groupName.contains("Starred in Android") || groupName.contains("My Contacts"))
	        continue;
	             
	        if(groupTitle.contains(groupName)){
	            for(Item1 group:groupList){
	             if(group.name.equals(groupName)){
	                
	            	 group.id += ","+Item1.id;
	                
	                break;
	             }
	           }
	        }else{
	          groupTitle.add(groupName);
	          Item1.name = groupName;
	         groupList.add(Item1);
	         }
	             
	    }
	     
	    cursor.close();
	    Collections.sort(groupList,new Comparator<Item1>() {
	        public int compare(Item1 Item11, Item1 Item12) {
	        return Item12.name.compareTo(Item11.name)<0
	                    ?0:-1;
	        }
	    });
	    return groupList;
	}
	
	
	private ArrayList<Item1> fetchGroupMembers(String groupId){
    	ArrayList<Item1> groupMembers = new ArrayList<Item1>();
    	String where =  CommonDataKinds.GroupMembership.GROUP_ROW_ID +"="+groupId
			       +" AND "
			       +CommonDataKinds.GroupMembership.MIMETYPE+"='"
			       +CommonDataKinds.GroupMembership.CONTENT_ITEM_TYPE+"'";
    	String[] projection = new String[]{GroupMembership.RAW_CONTACT_ID,Data.DISPLAY_NAME};
    	Cursor cursor = getContentResolver().query(Data.CONTENT_URI, projection, where,null,
    			Data.DISPLAY_NAME+" COLLATE LOCALIZED ASC");
    	while(cursor.moveToNext()){
    		Item1 item = new Item1();
    		item.name = cursor.getString(cursor.getColumnIndex(Data.DISPLAY_NAME));
    		item.id = cursor.getString(cursor.getColumnIndex(GroupMembership.RAW_CONTACT_ID));
    		Cursor phoneFetchCursor = getContentResolver().query(Phone.CONTENT_URI,
    				new String[]{Phone.NUMBER,Phone.DISPLAY_NAME,Phone.TYPE},
    				Phone.CONTACT_ID+"="+item.id,null,null);
    		while(phoneFetchCursor.moveToNext()){
    			item.phNo = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.NUMBER));
    			item.phDisplayName = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.DISPLAY_NAME));
    			item.phType = phoneFetchCursor.getString(phoneFetchCursor.getColumnIndex(Phone.TYPE));
    		}
    		phoneFetchCursor.close();
    		groupMembers.add(item);
    	}	
    	cursor.close();
    	return groupMembers;
    }
	
	
	public class Item1{
	    public String name,id,phNo,phDisplayName,phType;
	    public boolean isChecked =false;
	}
	
	static class SimpleAdapter extends ArrayAdapter<Item> implements PinnedSectionListAdapter {

        private static final int[] COLORS = new int[] {
            R.color.green_light, R.color.orange_light,
            R.color.blue_light, R.color.red_light };

        public SimpleAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
            generateDataset('A', 'Z', false);
        }

        public void generateDataset(char from, char to, boolean clear) {
        	
        	if (clear) clear();
        	
            final int sectionsNumber = to - from + 1;
            prepareSections(sectionsNumber);

            int sectionPosition = 0, listPosition = 0;
            String head="Contacts";
            for (char i=0; i<2; i++) {
            	if(i==0)
            	{
            		head="Groups";
            		Item section = new Item(Item.SECTION, head);
                    section.sectionPosition = sectionPosition;
                    section.listPosition = listPosition++;
                    onSectionAdded(section, sectionPosition);
                    add(section);

            		for (int j=0;j<groupList.size();j++) {
                    	
                    	
                        Item item = new Item(Item.ITEM,groupList.get(j) );
                        item.sectionPosition = sectionPosition;
                        item.listPosition = listPosition++;
                        add(item);
                    }
            	}
            	else
            	{
            		head="Contacts";
            		Item section = new Item(Item.SECTION, head);
                    section.sectionPosition = sectionPosition;
                    section.listPosition = listPosition++;
                    onSectionAdded(section, sectionPosition);
                    add(section);

            		for (int j=0;j<C_NAME.size();j++) {
                    	
                    	
                        Item item = new Item(Item.ITEM,C_NAME.get(j) );
                        item.sectionPosition = sectionPosition;
                        item.listPosition = listPosition++;
                        add(item);
                    }
            	}
            	
                
                
//                final int itemsNumber = (int) Math.abs((Math.cos(2f*Math.PI/3f * sectionsNumber / (i+1f)) * 25f));
                

                sectionPosition++;
        	
        	
            }
        	
        	
        }
        
        protected void prepareSections(int sectionsNumber) { }
        protected void onSectionAdded(Item section, int sectionPosition) { }

        @Override public View getView(int position, View convertView, ViewGroup parent) {
            TextView view = (TextView) super.getView(position, convertView, parent);
            view.setTextColor(Color.DKGRAY);
            view.setTag("" + position);
            Item item = getItem(position);
            if (item.type == Item.SECTION) {
                //view.setOnClickListener(PinnedSectionListActivity.this);
                view.setBackgroundColor(parent.getResources().getColor(COLORS[item.sectionPosition % COLORS.length]));
            }
            return view;
        }

        @Override public int getViewTypeCount() {
            return 2;
        }

        @Override public int getItemViewType(int position) {
            return getItem(position).type;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return viewType == Item.SECTION;
        }

    }

    static class FastScrollAdapter extends SimpleAdapter implements SectionIndexer {

        private Item[] sections;

        public FastScrollAdapter(Context context, int resource, int textViewResourceId) {
            super(context, resource, textViewResourceId);
        }

        @Override protected void prepareSections(int sectionsNumber) {
            sections = new Item[sectionsNumber];
        }

        @Override protected void onSectionAdded(Item section, int sectionPosition) {
            sections[sectionPosition] = section;
        }

        @Override public Item[] getSections() {
            return sections;
        }

        @Override public int getPositionForSection(int section) {
            if (section >= sections.length) {
                section = sections.length - 1;
            }
            return sections[section].listPosition;
        }

        @Override public int getSectionForPosition(int position) {
            if (position >= getCount()) {
                position = getCount() - 1;
            }
            return getItem(position).sectionPosition;
        }

    }

	static class Item {

		public static final int ITEM = 0;
		public static final int SECTION = 1;

		public final int type;
		public final String text;

		public int sectionPosition;
		public int listPosition;

		public Item(int type, String text) {
		    this.type = type;
		    this.text = text;
		}

		@Override public String toString() {
			return text;
		}

	}

	
	
	
	private void initializePadding() {
	    float density = getResources().getDisplayMetrics().density;
	    int padding = addPadding ? (int) (16 * density) : 0;
	    getListView().setPadding(padding, padding, padding, padding);
	}

    private void initializeHeaderAndFooter() {
        setListAdapter(null);
        if (hasHeaderAndFooter) {
            ListView list = getListView();

            LayoutInflater inflater = LayoutInflater.from(this);
            TextView header1 = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, list, false);
            header1.setText("First header");
            list.addHeaderView(header1);

            TextView header2 = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, list, false);
            header2.setText("Second header");
            list.addHeaderView(header2);

            TextView footer = (TextView) inflater.inflate(android.R.layout.simple_list_item_1, list, false);
            footer.setText("Single footer");
            list.addFooterView(footer);
        }
        initializeAdapter();
    }

    @SuppressLint("NewApi")
    private void initializeAdapter() {
        getListView().setFastScrollEnabled(isFastScroll);
        if (isFastScroll) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                getListView().setFastScrollAlwaysVisible(true);
            }
            setListAdapter(new FastScrollAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1));
        } else {
            setListAdapter(new SimpleAdapter(this, android.R.layout.simple_list_item_1, android.R.id.text1));
        }
    }

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		   Toast.makeText(this, "Item: " + v.getTag() , Toast.LENGTH_SHORT).show();
		
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
	    super.onSaveInstanceState(outState);
	    outState.putBoolean("isFastScroll", isFastScroll);
	    outState.putBoolean("addPadding", addPadding);
	    outState.putBoolean("isShadowVisible", isShadowVisible);
	    outState.putBoolean("hasHeaderAndFooter", hasHeaderAndFooter);
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
	    
		Item item = (Item) getListView().getAdapter().getItem(position);
	    
	    if (item != null) {
	    	if(position!=0)
	    	{
	    		if(position<no_of_groups)
	        	{	
	        		Intent intent=new Intent(GroupActivity.this,AddActivity.class);
	        			intent.putExtra("Group", item.text);
	        		startActivity(intent);
	        		finish();
	        	}
	    	}
	    }
	    else {
	        
	    	
	    }
	}

	

}
