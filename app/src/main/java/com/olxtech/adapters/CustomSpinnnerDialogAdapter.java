package com.olxtech.adapters;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;


import com.olxtech.R;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 * Created by F2853 on 26-06-2015.
 */
public class CustomSpinnnerDialogAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;

    private ArrayList<String> mData = new ArrayList<String>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();
    private Context mContext;

    private LayoutInflater mInflater;
    private static int currentIndex;
    private TextView tView;
    private Dialog dialog;

    public CustomSpinnnerDialogAdapter(Context context, int currentIndex, TextView tView, Dialog dialog) {
        mContext=context;
        CustomSpinnnerDialogAdapter.currentIndex =currentIndex;
        this.dialog=dialog;
        this.tView=tView;
    }

    public void addItem(final String item) {
        mData.add(item);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        mData.add(item);
        sectionHeader.add(mData.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public String getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        int rowType = getItemViewType(position);

        LayoutInflater li = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = li.inflate(R.layout.sub_layout_custom_selection_items, null);

         RadioButton btn = (RadioButton) convertView.findViewById(R.id.selected);

         TextView text = (TextView) convertView.findViewById(R.id.label_text);



        switch (rowType) {
            case TYPE_ITEM:
                final int j = position;
                btn.setText(mData.get(position).toString());
                convertView.findViewById(R.id.line).setVisibility(
                        View.GONE);
                if (position == currentIndex) {
                    btn.setChecked(true);
                }

                btn.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        try {

                            currentIndex = j;


                                tView.setText(mData.get(currentIndex));
                            tView.setTag(currentIndex);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                       if(dialog!=null&&dialog.isShowing()) {
                           dialog.dismiss();
                       }

                    }
                });




                break;
            case TYPE_SEPARATOR:


                convertView.findViewById(R.id.radioGroup_selection)
                        .setVisibility(View.GONE);
                convertView.findViewById(R.id.label).setVisibility(
                        View.VISIBLE);
                //if (position == 0) {
                    convertView.findViewById(R.id.line).setVisibility(
                            View.GONE);

                //}
                text.setText(mData.get(position).replace("#", ""));

                break;




        }
        return convertView;
    }

}