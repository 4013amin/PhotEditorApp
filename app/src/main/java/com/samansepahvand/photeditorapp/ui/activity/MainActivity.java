package com.samansepahvand.photeditorapp.ui.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;

import com.samansepahvand.photeditorapp.R;
import com.samansepahvand.photeditorapp.ui.adapter.MyPagerAdapter;
import com.samansepahvand.photeditorapp.utility.FileUtility;

import java.io.File;
import java.io.FileOutputStream;


import io.ak1.BubbleTabBar;
import io.ak1.OnBubbleClickListener;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {


    public static final int CAMERA_REQUEST = 50;


    private static final int GALLERY_REQUEST = 60;
    private static final int TAKE_PHOTO_REQUEST = 70;
    public static final int REQUEST_CODE_EDIT_IMAGE = 80;
    private static final String TAG = "MainActivity";

    private ViewPager viewPager;
    private MyPagerAdapter myPagerAdapter;
    private BubbleTabBar bubbleTabBar;

    private String mCurrentPhotoPath = null;

    private OnAboutDataReceivedListener onAboutDataReceivedListener;
    private OnMainDataRefresh onMainDataRefresh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {

        bubbleTabBar = findViewById(R.id.bubbleTabBar);
        viewPager = findViewById(R.id.vpPager);
        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(myPagerAdapter);
        //viewPager.setCurrentItem(1);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //
            }

            @Override
            public void onPageSelected(int position) {

                switch (position) {
                    case 0:
                        //bubble tab bar 1
                        bubbleTabBar.setSelectedWithId(R.id.item_home, false);

                        onMainDataRefresh.onDataRefresh(true);
                        break;
                    case 1:

                        //bubble tab bar 2
                        bubbleTabBar.setSelectedWithId(R.id.item_result, false);

                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                //
            }
        });


        //bubble tab bar action

        bubbleTabBar.addBubbleListener(new OnBubbleClickListener() {
            @Override
            public void onBubbleClick(int position) {

                String strId = new Integer(position).toString();

                Log.e(TAG, " status Id onBubbleClick: " + strId);

                switch (position) {

                    case R.id.item_gallery:
                        openGallery();
                        break;

                    case R.id.item_camera:

                        openCamera();
                        break;
                    case R.id.item_home:

                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.item_result:
                        viewPager.setCurrentItem(1);
                        break;


                }

            }
        });


        if (!checkPermission()) {
            requestPermission();
        }


    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.CAMERA);
        int resultRead = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultWrite = ContextCompat.checkSelfPermission(getApplication(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (result == PackageManager.PERMISSION_GRANTED) {

            return resultRead == PackageManager.PERMISSION_GRANTED &&
                    resultWrite == PackageManager.PERMISSION_GRANTED;
        }
        return false;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE


                }, CAMERA_REQUEST
        );

    }


    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions
            , @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {

            case CAMERA_REQUEST:

                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean readExAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExAccepted = grantResults[2] == PackageManager.PERMISSION_GRANTED;


                    if ((!cameraAccepted || !readExAccepted) || !writeExAccepted) {


                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

                            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                                Toast.makeText(getBaseContext(), "دسترسی شما تایید نشده است. برای استفاده از سایر قابلیت های برنامه به این دسترسی نیازمندیم.", Toast.LENGTH_SHORT).show();
                                // finish()
                                startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
                                finish();
                            }

                            if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                                Toast.makeText(getBaseContext(), "دسترسی شما تایید نشده است. برای استفاده از سایر قابلیت های برنامه به این دسترسی نیازمندیم.", Toast.LENGTH_SHORT).show();
                                // finish()
                                startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
                                finish();
                            }

                            if (!shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                                Toast.makeText(getBaseContext(), "دسترسی شما تایید نشده است. برای استفاده از سایر قابلیت های برنامه به این دسترسی نیازمندیم.", Toast.LENGTH_SHORT).show();
                                // finish()
                                startActivity(new Intent(MainActivity.this, SplashScreenActivity.class));
                                finish();
                            }

                        }
                    }
                }
        }

    }

    private void openCamera() {

        try {
            String fileName = "photo";
            File storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            File imgFile = File.createTempFile(fileName, ".jpg", storageDirectory);
            mCurrentPhotoPath = imgFile.getAbsolutePath();
            Uri imgUri = FileProvider.getUriForFile(MainActivity.this, "com.samansepahvand.photeditorapp.fileprovider", imgFile);
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
            startActivityForResult(intent, TAKE_PHOTO_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getBaseContext(), "Failed TO open Camera !", Toast.LENGTH_SHORT).show();
        }

    }


    private void openGallery() {

        Intent intentGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(intentGallery, GALLERY_REQUEST);

    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // with camera
        if (resultCode == RESULT_OK && requestCode == TAKE_PHOTO_REQUEST && mCurrentPhotoPath != null) {

            Log.e(TAG, "onActivityResult TAKE_PHOTO_REQUEST: " + mCurrentPhotoPath);
            editImage(mCurrentPhotoPath);

        }


        // with gallery
        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST) {

            Uri imgUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imgUri, filePathColumn, null, null, null);

            cursor.moveToNext();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String imgFinalPath = cursor.getString(columnIndex);

            Log.e(TAG, "onActivityResult: " + imgFinalPath);

            editImage(imgFinalPath);
        }

        // photo edit

        if (resultCode == RESULT_OK && requestCode == GALLERY_REQUEST && data != null) {
            Uri imgUri = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(imgUri, filePathColumn, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgFinalPath = cursor.getString(columnIndex);

                Log.e(TAG, "onActivityResult: " + imgFinalPath);
                editImage(imgFinalPath);
                cursor.close();
            }
        }


    }


    private void editImage(String imagePath) {
        try {
            // Rotate image (for example, rotating 90 degrees)
            Bitmap originalBitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap rotatedBitmap = rotateImage(originalBitmap, 90); // Rotate 90 degrees

            // Adjust brightness (basic example, adjust as needed)
            Bitmap brightBitmap = adjustBrightness(rotatedBitmap, 1.2f); // Increase brightness by 20%

            // Save the edited image to a new file
            File outFile = FileUtility.genEditFile();
            FileOutputStream outStream = new FileOutputStream(outFile);
            brightBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outStream); // Save with quality 90

            outStream.flush();
            outStream.close();

//            // Now, you can pass the edited image path to your activity
//            Intent intentEditImage = new Intent(MainActivity.this, EditImageActivity.class);
//            intentEditImage.putExtra("image_path", outFile.getAbsolutePath());
//            startActivityForResult(intentEditImage, REQUEST_CODE_EDIT_IMAGE);

        } catch (Exception e) {
            Log.e(TAG, "Error while editing image", e);
            Toast.makeText(MainActivity.this, "An error occurred while editing the image.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    private Bitmap adjustBrightness(Bitmap source, float factor) {
        Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), source.getConfig());

        for (int x = 0; x < source.getWidth(); x++) {
            for (int y = 0; y < source.getHeight(); y++) {
                int pixel = source.getPixel(x, y);
                int red = (int) ((Color.red(pixel)) * factor);
                int green = (int) ((Color.green(pixel)) * factor);
                int blue = (int) ((Color.blue(pixel)) * factor);

                // Ensure the values stay within the valid color range
                red = Math.min(255, Math.max(0, red));
                green = Math.min(255, Math.max(0, green));
                blue = Math.min(255, Math.max(0, blue));

                result.setPixel(x, y, Color.rgb(red, green, blue));
            }
        }
        return result;
    }


    public interface OnAboutDataReceivedListener {
        void OnDataReceived(String photoUrl);
    }

    public void setOnAboutDataReceivedListener(OnAboutDataReceivedListener onAboutDataReceivedListener) {
        this.onAboutDataReceivedListener = onAboutDataReceivedListener;
    }


    public interface OnMainDataRefresh {

        void onDataRefresh(boolean status);
    }


    public void setOnMainDataRefresh(OnMainDataRefresh onMainDataRefresh) {
        this.onMainDataRefresh = onMainDataRefresh;
    }
}