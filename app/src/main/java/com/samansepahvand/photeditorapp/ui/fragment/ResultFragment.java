package com.samansepahvand.photeditorapp.ui.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.samansepahvand.photeditorapp.R;
import com.samansepahvand.photeditorapp.ui.activity.MainActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.BitSet;


public class ResultFragment extends Fragment implements MainActivity.OnAboutDataReceivedListener, View.OnClickListener {


    private ImageView imgSave, imgOutputImage;
    private String ImagePath;
    private MainActivity mainActivity = new MainActivity();


    public ResultFragment() {
        // Required empty public constructor
    }


    public static ResultFragment newInstance(String param1, String param2) {
        ResultFragment fragment = new ResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mainActivity.setOnAboutDataReceivedListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_result, container, false);


        initView(view);


        return view;


    }

    private  void initView(View view){

        imgOutputImage=view.findViewById(R.id.output_image);
        imgSave=view.findViewById(R.id.img_save);

        imgSave.setOnClickListener(this);

    }


    @Override
    public void OnDataReceived(String photoUrl) {

        if (photoUrl!=null){

            ImagePath=photoUrl;
            imgOutputImage.setImageURI(Uri.parse(ImagePath));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_save:

                //doing save

                saveFinalImage();

                break;
        }
    }


    private void saveFinalImage(){

        String dirPath= Environment.getExternalStorageDirectory().getAbsolutePath()
                +"/"+getString(R.string.app_name)+"/";
        String fileName=ImagePath.substring(ImagePath.indexOf('/')+1);
        File dir=new File(dirPath);
        imgOutputImage.buildDrawingCache();
        Bitmap bitmap=imgOutputImage.getDrawingCache();
        saveImage(bitmap,dir,fileName);
    }

    private String saveImage(Bitmap bitmap, File dir, String fileName) {
        String saveImagePath=null;
        boolean isSuccess=true;
        if (!dir.exists()){
            isSuccess=dir.mkdirs();
        }
        if (isSuccess){
            File imageFile=new File(dir,fileName);
            saveImagePath=imageFile.getAbsolutePath();
            try{

                OutputStream fOut=new FileOutputStream(imageFile);
                bitmap.compress(Bitmap.CompressFormat.JPEG,100,fOut);
                fOut.close();
            }catch (Exception e){
                e.printStackTrace();
            }
            galleryAddImage(saveImagePath);

        }
        Toast.makeText(getActivity(), "Image Save!", Toast.LENGTH_SHORT).show();
        return saveImagePath;

    }

    private void galleryAddImage(String saveImagePath) {
        Intent mediaScanIntent=new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file=new File(saveImagePath);
        Uri contentUri=Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);


    }
}