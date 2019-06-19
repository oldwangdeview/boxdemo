package com.oldwang.boxdemo.adpater;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.oldwang.boxdemo.R;
import com.oldwang.boxdemo.bean.ImageBean;
import com.oldwang.boxdemo.interfice.ListOnclickLister;
import com.oldwang.boxdemo.util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by oldwang on 2019/1/16 0016.
 */

public class Addimageadpater extends BaseAdapter {
    private List<ImageBean> filepath = new ArrayList<>();
    private Context mContent ;
    LinearLayout addimage;
    ImageView image;
    ImageView detle_image;
    private ListOnclickLister mlister;

    private ImageBean imageBean;


    public Addimageadpater(Context mContent, List<ImageBean> filepath){
        this.mContent = mContent;
        this.filepath = filepath;

    }
    @Override
    public int getCount() {
        return filepath.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
          if(convertView==null){
              convertView =  LayoutInflater.from(mContent).inflate( R.layout.item_addimage, null);
          }


          addimage = convertView.findViewById(R.id.layout_addimage);
          image = convertView.findViewById(R.id.image);
          detle_image = convertView.findViewById(R.id.delete_image);
          if(filepath.get(position) != null){
              if(filepath.get(position).isDetele()){
                  addimage.setVisibility(View.VISIBLE);
                  image.setVisibility(View.GONE);
                  detle_image.setVisibility(View.GONE);
              }else{
                  addimage.setVisibility(View.GONE);
                  image.setVisibility(View.VISIBLE);
                  detle_image.setVisibility(View.VISIBLE);
                  Glide.with(mContent).load(filepath.get(position).getUri()).into(image);
              }

              detle_image.setOnClickListener(new View.OnClickListener() {
                  @Override
                  public void onClick(View v) {
                      filepath.remove(position);
                      if (!filepath.contains(imageBean)){
                          filepath.add(imageBean);
                      }
                      notifyDataSetChanged();
                  }
              });

          }
        addimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mlister!=null){
                    mlister.onclick(v,position);
                }
            }
        });

        return convertView;
    }

    public void setOnclickItemLister(ListOnclickLister mlister){
        this.mlister = mlister;
    }

    public void setDeletImageBean(ImageBean imageBean){
        this.imageBean = imageBean;
    }

    public List<File> getFiles(){
        List<File> fileList = new ArrayList<>();

        for(ImageBean image:filepath){
            if(!image.isDetele()){
                fileList.add(FileUtils.uriToFile(image.getUri(),mContent));
            }
        }
        return  fileList;
    }

}
