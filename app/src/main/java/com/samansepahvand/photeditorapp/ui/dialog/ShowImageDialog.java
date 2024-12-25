package com.samansepahvand.photeditorapp.ui.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.Tag;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.widget.NestedScrollView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.github.chrisbanes.photoview.PhotoView;
import com.samansepahvand.photeditorapp.R;
import com.samansepahvand.photeditorapp.metamodel.PhotoViewModelMeta;

public class ShowImageDialog extends Dialog {
    private static final String TAG = "ShowImageDialog";

    private Activity mContext;
    private ConstraintLayout root,rootTolbar,rootDialog;
    private TextView txtDate,txtTitle,txtFullDetails;
    private ImageView imgBack;
    private NestedScrollView nestedScrollView;
    private PhotoView photoView;
    private int count=2;



    public ShowImageDialog(@NonNull Activity context, PhotoViewModelMeta photoViewModelMeta) {
        super(context);
        this.mContext=context;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_show_image_chat);
        this.getWindow().getAttributes().windowAnimations=R.style.AlertDialogAnimation;
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Window window=getWindow();

        WindowManager.LayoutParams wlp=window.getAttributes();
        wlp.gravity= Gravity.CENTER;
        wlp.flags&=WindowManager.LayoutParams.FLAG_BLUR_BEHIND;
        window.setAttributes(wlp);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);



        initView();
        initDataDialog(photoViewModelMeta);



    }

    private void initView() {

        txtDate=findViewById(R.id.txt_date);
        txtFullDetails=findViewById(R.id.txt_full_details);
        txtTitle=findViewById(R.id.txt_title);

        imgBack=findViewById(R.id.img_back);
        photoView=findViewById(R.id.main_image);


        rootDialog=findViewById(R.id.root);
        rootTolbar=findViewById(R.id.constraintLayout);

        nestedScrollView=findViewById(R.id.nestedscrollview);

        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });



        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (count%2==0){
                    rootTolbar.setVisibility(View.GONE);
                    txtFullDetails.setVisibility(View.GONE);
                    nestedScrollView.setVisibility(View.GONE);


                }else{
                    rootTolbar.setVisibility(View.VISIBLE);
                    txtFullDetails.setVisibility(View.VISIBLE);
                    nestedScrollView.setVisibility(View.VISIBLE);

                }
                count ++;

            }
        });

    }
    private void initDataDialog(PhotoViewModelMeta photoViewModelMeta) {

txtTitle.setText(photoViewModelMeta.getTitle());


        txtDate.setText(photoViewModelMeta.getDate());
        txtTitle.setText(getTitle(photoViewModelMeta.getTitle()));


        String str=ShowDisc(photoViewModelMeta);

        Spanned spanned= Html.fromHtml(str);

        txtFullDetails.setText(spanned);

        Glide.with(mContext)
                .load(photoViewModelMeta.getImageUrl())
                .error(R.drawable.no_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(photoView);



    }
    
    
    private String getTitle(String FullImageUrl){

        return FullImageUrl.split("/")[FullImageUrl.split("/").length-1];

    }

    private String  ShowDisc(PhotoViewModelMeta photoViewMetaModel) {

        String data = "<b>Details</b> " +
                "<br/>" +
                "<br/>" +
                "<br/>" +
                "Name: " +
                "<br/>" +
                " " +getTitle(photoViewMetaModel.getTitle())+
                "<br/>" +
                "Path:" +
                "<br/>" +
                " " +photoViewMetaModel.getDescription()+
                "<br/>" +
                "Date : " +
                "<br/>" +
                " " +photoViewMetaModel.getDate();

        return data;


    }




    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }


    public static Dialog Dismiss(){
        return Dismiss();
    }
}
