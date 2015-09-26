package com.olxtech.common;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListView;
import android.widget.TextView;

import com.olxtech.R;
import com.olxtech.adapters.CustomSpinnnerDialogAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deepak on 26/9/15.
 */
public class OlxCommon {

    private static int currentIndex;
    public static Dialog dialog;
    public static String[] CITY_LIST;
    public static String[] CITY_LIST_EDIT;
    public static HashMap<String, String> CITY_REVERSE_MAP;
    public static HashMap<String, String> CITY_MAP;

    private static boolean[] itemsChecked;


    public static void showSpinnerDialogCustom(String title,
                                               final TextView tView, final String[] list, Activity macActivity) {

        try {
            if ((list != null) && (list.length > 0)) {


                currentIndex = (Integer) tView.getTag();
                dialog = null;
                dialog = new Dialog(macActivity);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialog) {
                    }
                });
                dialog.setContentView(R.layout.custom_dialog_selecting_items);
                dialog.setTitle(title);
                CustomSpinnnerDialogAdapter mAdapter = new CustomSpinnnerDialogAdapter(macActivity, currentIndex, tView, dialog);

                ListView listView = (ListView) dialog.findViewById(R.id.my_list);
                listView.setSmoothScrollbarEnabled(true);
                listView.setFastScrollEnabled(true);

                for (int i = 0; i < list.length; i++) {
                    if (list[i].startsWith("#")) {
                        mAdapter.addSectionHeaderItem(list[i].substring(1));

                    } else {
                        mAdapter.addItem(list[i]);
                    }
                }

                listView.setAdapter(mAdapter);

                listView.setSelection(currentIndex);


                if (!macActivity.isFinishing())
                    dialog.show();

            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }


    public static void initializeCityList(
            ArrayList<HashMap<String, String>> arrayList) {
        try {

            ArrayList<String> list = new ArrayList<String>();
            ArrayList<String> list2 = new ArrayList<String>();

            for (HashMap<String, String> map : arrayList) {
                String pid = map.get("pid");
                String cid = map.get("cid");
                if (!list2.contains(pid))
                    list2.add(pid.trim());
                list2.add(cid);
                String pdesc = map.get("pdesc");
                String cdesc = map.get("cdesc");
                if (!list.contains("#" + pdesc.trim()))
                    list.add("#" + pdesc.trim());
                list.add(cdesc);
            }

            CITY_MAP = new HashMap<String, String>();
            CITY_REVERSE_MAP = new HashMap<String, String>();

            int size = list.size();
            CITY_LIST = new String[list.size()];
            for (int i = 0; i < size; i++) {
                CITY_REVERSE_MAP.put(list2.get(i), list.get(i));
                CITY_MAP.put(list.get(i), list2.get(i));
                CITY_LIST[i] = list.get(i);

                // //system.out.println("Key::::" + list2.get(i) + " Value:::" +
                // list.get(i));
            }
            if (CITY_LIST != null && CITY_LIST.length > 5) {
                CITY_LIST_EDIT = new String[CITY_LIST.length - 4];
                for (int i = 0; i < CITY_LIST.length - 4; i++) {
                    CITY_LIST_EDIT[i] = CITY_LIST[i + 4];

                }
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    public static void hideKeyboard(Activity mActivity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) mActivity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);

            inputManager.hideSoftInputFromWindow(mActivity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFile(Context context, int id) {
        String text = "";
        try {
            Resources res = context.getResources();
            InputStream is = res.openRawResource(id);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            text = new String(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text;
    }


    public static void parseCityFromFile(Context context, int txt) {


        String lookup = getFile(context, txt);

        try {
            JSONObject object = new JSONObject(lookup);
            lookup = object.getString("results");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        parseJson(lookup);


    }




    private static void parseJson(final String result1) {

        HashMap<String, String> map;
        final ArrayList<HashMap<String, String>> arrayList = new ArrayList<>();
        try {
            JSONArray result = new JSONArray(result1);

            for (int i = 0; i < result.length(); i++) {
                map = new HashMap<>();
                JSONObject jobj = result.getJSONObject(i);
                String pid = jobj.getString("pid");
                String pdesc = jobj.getString("pdesc");
                JSONArray childarray = jobj.getJSONArray("child");
                if (childarray.length() > 0) {

                    for (int j = 0; j < childarray.length(); j++) {
                        HashMap<String, String> childmap = new HashMap<>();
                        JSONObject childobj = childarray.getJSONObject(j);
                        String cid = childobj.getString("cid");
                        String cdesc = childobj.getString("cdesc");

                        childmap.put("pid", pid);
                        childmap.put("pdesc", pdesc);
                        childmap.put("cid", cid);
                        childmap.put("cdesc", cdesc);

                        arrayList.add(childmap);

                    }
                } else {

                    map.put("pid", pid);
                    map.put("pdesc", pdesc);
                    arrayList.add(map);

                }

            }


            try {
                initializeCityList(arrayList);

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
