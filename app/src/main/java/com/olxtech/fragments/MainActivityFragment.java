package com.olxtech.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.olxtech.R;
import com.olxtech.common.OlxCommon;

import net.yazeed44.imagepicker.model.ImageEntry;
import net.yazeed44.imagepicker.util.Picker;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements View.OnClickListener,View.OnFocusChangeListener {
    private ArrayList<ImageEntry> mSelectedImages;
    AutoCompleteTextView adtitle;
    EditText category, locationText,description,name,phone,price,email;
    ImageView camera;
    LinearLayout submit;
    LayoutInflater inflator;
    ScrollView menuView;
    private View simplesubmitBtn;
    private String location="";
    private String keyword="";
    private JSONObject newJson;
    ArrayList<String> arrayList = new ArrayList<>();

    private Dialog alertDialog;
    private boolean modify = false;
    private boolean fromMore=false;

  //  public  HorizontalScrollView scrollView;
    private View view;
    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_main,null);

        init();
        return view;

    }

    public void init()
    {
        adtitle=(AutoCompleteTextView)view.findViewById(R.id.keyword);
        locationText = (EditText) view.findViewById(R.id.location);
        locationText.setText(location);
        locationText.setOnFocusChangeListener(this);
        locationText.setTag(0);
        category=(EditText)view.findViewById(R.id.category);
        category.setOnClickListener(this);
        category.setOnFocusChangeListener(this);
        category.setTag(0);
        description=(EditText)view.findViewById(R.id.description);
        description.setOnClickListener(this);

        name=(EditText)view.findViewById(R.id.name);
        name.setOnClickListener(this);

        price=(EditText)view.findViewById(R.id.price);
        price.setOnClickListener(this);
        phone=(EditText)view.findViewById(R.id.phone);
        phone.setOnClickListener(this);
        email=(EditText)view.findViewById(R.id.email);
        email.setOnClickListener(this);
        camera=(ImageView)view.findViewById(R.id.camera);
        camera.setOnClickListener(this);
        submit=(LinearLayout)view.findViewById(R.id.submit_layout);
        submit.setOnClickListener(this);
      // scrollView=(HorizontalScrollView)view.findViewById(R.id.scrollView);


    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            showSpinner(v.getId());
            OlxCommon.hideKeyboard(getActivity());
        }
    }
    @Override
    public void onClick(View v) {
switch (v.getId())
{

    case R.id.camera:

        pickImages();

        break;


    case R.id.submit_layout:



        Toast.makeText(getActivity(),"Thanks for submitting",Toast.LENGTH_SHORT).show();
        break;
}
    }

    private void pickImages(){

        //You can change many settings in builder like limit , Pick mode and colors
        new Picker.Builder(getActivity(),new MyPickListener(),R.style.MIP_theme)
                .setLimit(8)
                .build()
                .startActivity();

    }




    private void showSpinner(int id) {
        try {
            switch (id) {
                case R.id.location:

                    OlxCommon.showSpinnerDialogCustom(
                            "Choose Location", locationText,
                            OlxCommon.CITY_LIST_EDIT, getActivity());

                    break;
                case R.id.category:

                    OlxCommon.showSpinnerDialogCustom(
                            "Choose Category", category,
                            OlxCommon.CAT_LIST, getActivity());
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private class MyPickListener implements Picker.PickListener
    {
        @Override
        public void onPickedSuccessfully(final ArrayList<ImageEntry> images)
        {

            //doSomethingWithImages(images);
            Log.e("Debug ","images "+images.size());


            mSelectedImages=images;
//            scrollView.setVisibility(View.VISIBLE);
            setProductImageInView();

        }

        @Override
        public void onCancel(){
           //User cancled the pick activity
        }
    }

    private void setProductImageInView() {

        Gallery gallery = (Gallery)view. findViewById(R.id.gallery);
        gallery.setAdapter(new ImageAdapter(getActivity()));
    }

    public class ImageAdapter extends BaseAdapter {
        private Context context;
        private int itemBackground;
        public ImageAdapter(Context c)
        {
            context = c;
            // sets a grey background; wraps around the images
           // TypedArray a =obtainStyledAttributes(R.styleable.MyGallery);
            //itemBackground = a.getResourceId(R.styleable.MyGallery_android_galleryItemBackground, 0);
            //a.recycle();
        }
        // returns the number of images
        public int getCount() {
            return mSelectedImages.size();
        }
        // returns the ID of an item
        public Object getItem(int position) {
            return position;
        }
        // returns the ID of an item
        public long getItemId(int position) {
            return position;
        }
        // returns an ImageView view
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView = null;
            try {


                imageView = new ImageView(context);
                imageView.setImageURI(Uri.parse(mSelectedImages.get(position).path));
                imageView.setLayoutParams(new Gallery.LayoutParams(200, 200));
                imageView.setBackgroundResource(itemBackground);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return imageView;
        }
    }



}
