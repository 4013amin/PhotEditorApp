package com.samansepahvand.photeditorapp.utility;

import android.os.Build;
import android.os.Environment;

import java.io.File;

public class FileUtility {


    public static final String AppAuthorize="com.samansepahvand.photeditorapp.fileprovider";
    public static final String FOLDER_NAME="PhotoEdit";


    public static File genEditFile(){
      return FileUtility.getEmptyFile(System.currentTimeMillis()+".jpg");
    }

    public static File getEmptyFile(String name){

        File folder=FileUtility.createFolder();
        if (folder!=null){
            if (folder.exists()){
                File file=new File(folder,name);
            return file;
            }
        }
        return null;

    }



    public static File createFolder(){

        File baseDir;

        //scCard/Pictures
        if (Build.VERSION.SDK_INT<8){
            baseDir= Environment.getExternalStorageDirectory();

        }else{
            baseDir=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        }
        if (baseDir==null){

            return Environment.getExternalStorageDirectory();
        }
        File arFoler=new File(baseDir,FOLDER_NAME);
        if (arFoler.exists()){
            return arFoler;
        }
        if (arFoler.isFile()){
            arFoler.delete();
        }
        if (arFoler.mkdirs()){
            return arFoler;
        }
        return Environment.getExternalStorageDirectory();


    }
}
