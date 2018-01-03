package com.multibitmap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {


    public static final String[] urls = new String[]{
            "http://pic.qiantucdn.com/58pic/22/06/55/57b2d98e109c6_1024.jpg"
            , "http://www.zhlzw.com/UploadFiles/Article_UploadFiles/201204/20120412123914329.jpg"
            , "http://pic.58pic.com/58pic/15/14/14/18e58PICMwt_1024.jpg"
            , "http://dynamic-image.yesky.com/740x-/uploadImages/2014/289/01/IGS09651F94M.jpg"
            , "http://pic.58pic.com/58pic/13/61/00/61a58PICtPr_1024.jpg"
            , "http://pic.58pic.com/58pic/13/61/00/61a58PICtPr_1024.jpg"
            , "http://pic.58pic.com/58pic/15/36/00/73b58PICgvY_1024.jpg"
            , "http://pic.58pic.com/58pic/15/35/96/97j58PICUhD_1024.jpg"
            , "http://pic.yesky.com/uploadImages/2015/131/58/62KPG7ZYL453.jpg"
    };
    private ImageView[] views;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();

    }

    private void initView() {
        views = new ImageView[]{
                findViewById(R.id.image1)
                , findViewById(R.id.image2)
                , findViewById(R.id.image3)
                , findViewById(R.id.image4)
                , findViewById(R.id.image5)
                , findViewById(R.id.image6)
                , findViewById(R.id.image7)
                , findViewById(R.id.image8)
                , findViewById(R.id.image9)
        };

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            load();
//            handler.sendEmptyMessage(what);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void load() {
        int i = 1;
        RequestManager manager = Glide.with(this);
        for (ImageView imageView : views) {
            MultiBitmapUrl url = new MultiBitmapUrl(getResources().getDrawable(R.mipmap.ic_launcher), Arrays.copyOf(urls, i));
            try {
                manager.load(url).into(imageView);
            } catch (Exception e) {
                e.printStackTrace();
            }
            i++;
        }
    }
}
