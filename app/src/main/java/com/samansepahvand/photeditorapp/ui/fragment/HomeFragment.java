package com.samansepahvand.photeditorapp.ui.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.samansepahvand.photeditorapp.R;
import com.samansepahvand.photeditorapp.ui.activity.MainActivity;
import com.samansepahvand.photeditorapp.ui.adapter.ImageLoaderAdapter;
import com.samansepahvand.photeditorapp.utility.FileUtility;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment implements MainActivity.OnMainDataRefresh {

    private ImageView imgLeft1, imgLeft2, imgStory;


    private RecyclerView recyclerView;
   private ImageLoaderAdapter adapter;


   private MainActivity mainActivity=new MainActivity();


    public HomeFragment() {
        // Required empty public constructor
    }


    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity=(MainActivity) getActivity();
        mainActivity.setOnMainDataRefresh(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_home, container, false);
        initView(view);
        return view;
        //  return inflater.inflate(R.layout.fragment_home, container, false);
    }


    private void initView(View view) {

        imgLeft1 = view.findViewById(R.id.img_left_1);
        imgLeft2 = view.findViewById(R.id.img_left_2);
        imgStory = view.findViewById(R.id.img_story);


        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        defaultImage();
        getAllImagePath();





    }

    private void defaultImage() {
        imgLeft1.setImageResource(R.drawable.no_image);
        imgLeft2.setImageResource(R.drawable.no_image);
        imgStory.setImageResource(R.drawable.no_image);
    }

    private void getAllImagePath() {

        File baseDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File folder = new File(baseDir, FileUtility.FOLDER_NAME);

        if (folder.exists()) {

            List<String> arrayListPath = new ArrayList<>();
            List<Bitmap> bitmaps = new ArrayList<>();

            String[] filePathImageUrl = folder.list();

            for (String path : filePathImageUrl) {
                arrayListPath.add(folder.getAbsolutePath() + "/" + path);
                bitmaps.add(BitmapFactory.decodeFile(folder.getAbsolutePath() + "/" + path));
            }
            adapter=new ImageLoaderAdapter(bitmaps,arrayListPath,getActivity());
             recyclerView.setAdapter(adapter);

            if (arrayListPath.size() != 0) {
                imgStory.setImageBitmap(bitmaps.get(bitmaps.size() - 1));

                for (int i = 0; i <= arrayListPath.size() - 1; i++) {

                    switch (i) {

                        case 0:
                            imgLeft1.setImageBitmap(bitmaps.get(bitmaps.size() - 1));
                            break;

                        case 1:
                            imgLeft2.setImageBitmap(bitmaps.get(bitmaps.size() -2));
                            break;
                    }
                }
            }

        }


    }

    @Override
    public void onDataRefresh(boolean status) {

        if (status){
            getAllImagePath();
        }
    }
}