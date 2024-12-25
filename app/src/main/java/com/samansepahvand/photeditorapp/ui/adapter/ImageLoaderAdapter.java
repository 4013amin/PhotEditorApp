package com.samansepahvand.photeditorapp.ui.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.samansepahvand.photeditorapp.R;
import com.samansepahvand.photeditorapp.metamodel.PhotoViewModelMeta;
import com.samansepahvand.photeditorapp.ui.dialog.ShowImageDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ImageLoaderAdapter  extends RecyclerView.Adapter<ImageLoaderAdapter.ViewHolder> {


    private List<Bitmap> datas=new ArrayList<>();
    private List<String> dataSts=new ArrayList<>();


    private Context context;


    public ImageLoaderAdapter(List<Bitmap> datas, List<String> dataSt, Context context) {
        this.datas = datas;
        this.context = context;
        this.dataSts=dataSt;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

      View view=null;
      if (view==null)view= LayoutInflater.from(context).inflate(R.layout.item_image_loader,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        if (datas!=null){
            holder.imgView.setImageBitmap(datas.get(position));
        }else{

            holder.imgView.setImageResource(R.drawable.no_image);
        }

    }

    @Override
    public int getItemCount() {
        if (datas!=null)
        return datas.size();
        else return 3;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {


private ImageView imgView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);


            imgView=itemView.findViewById(R.id.img_view);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                PhotoViewModelMeta meta=new PhotoViewModelMeta();
                   meta.setTitle(dataSts.get(getAdapterPosition())+"");
                   meta.setDescription(dataSts.get(getAdapterPosition()));
                   meta.setImageUrl(dataSts.get(getAdapterPosition()));
                   meta.setDate(Calendar.getInstance().getTime().toString());

                    ShowImageDialog dialog=new ShowImageDialog((Activity) context,meta);
                    dialog.show();


                }
            });

        }
    }
}
