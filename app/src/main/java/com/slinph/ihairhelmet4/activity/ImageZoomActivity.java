package com.slinph.ihairhelmet4.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.slinph.ihairhelmet4.R;
import com.veinhorn.scrollgalleryview.MediaInfo;
import com.veinhorn.scrollgalleryview.ScrollGalleryView;
import com.veinhorn.scrollgalleryview.loader.DefaultImageLoader;

public class ImageZoomActivity extends FragmentActivity {

    private ScrollGalleryView scrollGalleryView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_zoom);

        Intent intent = getIntent();
        String path1 = intent.getStringExtra("path1");
        String path2 = intent.getStringExtra("path2");

        scrollGalleryView = (ScrollGalleryView) findViewById(R.id.scroll_gallery_view);
        if (path1 != null&&path2 != null){
            scrollGalleryView
                    .setThumbnailSize(100)
                    .setZoom(true)
                    .setFragmentManager(getSupportFragmentManager())
                    .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(path2Bitmap(path1))))
                    .addMedia(MediaInfo.mediaLoader(new DefaultImageLoader(path2Bitmap(path2))));
        }
    }

    public Bitmap path2Bitmap(String Path){
        Bitmap bitmap = BitmapFactory.decodeFile(Path);
        return bitmap;
    }
}
