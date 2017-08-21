package com.inception.isot;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Gajanan on 06-08-2017.
 */

public class Tab1MyThings extends Fragment {

    private static final String TAG = "Tab1MyThings";

    private ArrayList<String> listItems = new ArrayList<>();
    private MyThingsAdapter listAdapter;
    private ListView listView;
    View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstance)
    {
        // Array adapter will take data from a source and
        // use it to populate the ListView
        listAdapter = new MyThingsAdapter(getParentFragment(),
                getContext(),
                R.layout.tab_mythings,
                R.id.myItemsList,
                listItems);

        rootView = inflater.inflate(R.layout.tab_mythings,container,false);
        listView = (ListView) rootView.findViewById(R.id.myItemsList);
        listView.setAdapter(listAdapter);

        FloatingActionButton fabAddItem = (FloatingActionButton) rootView.findViewById(R.id.floatingActionButton);
        fabAddItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fragment_add_item addNewItem = new fragment_add_item();
/*
                android.support.v4.app.FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                transaction.replace(R.id.container, new fragment_add_item());

                transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);

                transaction.addToBackStack(null);
                transaction.commit();
*/

                ViewPager vp = (ViewPager) getActivity().findViewById(R.id.container);
                ((MainActivity)getActivity()).addViewPage(0,vp, addNewItem,"add Item");


            }
        });

        getMyThings();
        return rootView;
    }

    public void onCreate(Bundle saveInstanceState)
    {
        super.onCreate(saveInstanceState);

    }

    private void getMyThings()
    {
        TextView textView = (TextView) rootView.findViewById(R.id.section_label);
        textView.setText("");
        listAdapter.clear();
        listAdapter.add("Book1");
        listAdapter.add("Book2");
        listAdapter.add("Book3");
        listAdapter.add("Book4");
        listAdapter.notifyDataSetChanged();
    }

    private class MyThingsAdapter extends ArrayAdapter<String> {

        private ArrayList<String> items;
        Context context;

       /* MyThingsAdapter( getActivity(),
        R.layout.tab_mythings,
        R.id.myItemsList,
        listItems);
        */
        public MyThingsAdapter(android.support.v4.app.Fragment activity,
                               Context context,
                               int layout,
                               int textViewResourceId,
                               ArrayList<String> items) {
            super(context, textViewResourceId, items);
            this.context = context;
            this.items = items;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.row, null);
            }
            String request = items.get(position);
            if (request != null) {
                TextView title = (TextView) v.findViewById(R.id.topText);
                TextView category = (TextView) v.findViewById(R.id.textLine2);
                TextView description = (TextView) v.findViewById(R.id.textLine3);

                if (title != null) {
                    title.setText("Item Name: "+ request);
                }

                if(category != null){
                    category.setText("Category: "+ request);
                }

                if(description != null){
                    description.setText("Description: "+ request);
                }
            }
            return v;
        }
    }
}
