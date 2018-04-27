package com.example.facesample.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.example.facesample.R;
import com.example.facesample.adapters.SimilarAdapter;
import com.example.facesample.engine.imgscan.GallyPageTransformer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VerifyActivity extends AppCompatActivity {
    public final static int FILE = 1;
    public final static int PHOTO = 2;
    private ImageView mIv;
    private ViewPager mVp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_verify);

        findView();

        initData();
    }

    private void initData() {
        Intent intent = getIntent();

        int type = intent.getIntExtra("type", 0);
        switch (type){
            case FILE:
                String path = intent.getStringExtra("path");
                Bitmap bitmap = BitmapFactory.decodeFile(path);
                mIv.setImageBitmap(bitmap);
                break;
        }
    }

    private void setFullScreen() {
        /*set it to be no title*/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
       /*set it to be full screen*/
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    private List<File> mFiles = new ArrayList<>();
    // private List<Integer> mFiles = new ArrayList<>();

    void findView(){
        mIv = findViewById(R.id.verify_iv);
        mIv.setImageResource(R.mipmap.ai_2);
        mVp = findViewById(R.id.verify_vp);
        initFilesData();
        mVp.setOffscreenPageLimit(3);
        int pagerWidth = (int) (getResources().getDisplayMetrics().widthPixels * 3.0f / 5.0f);
        ViewGroup.LayoutParams lp = mVp.getLayoutParams();
        if (lp == null) {
            lp = new ViewGroup.LayoutParams(pagerWidth, ViewGroup.LayoutParams.MATCH_PARENT);
        } else {
            lp.width = pagerWidth;
        }
        mVp.setLayoutParams(lp);
        mVp.setPageMargin(-50);
        mVp.setPageTransformer(true, new GallyPageTransformer());
        mVp.setAdapter(new SimilarAdapter(mFiles));
    }
    void initFilesData(){

        File res = new File(Environment.getExternalStorageDirectory(), "res");
        File[] files = res.listFiles();
        for (File f : files) {
            boolean b = f.isFile() && f.getName().endsWith(".jpg");
            if(b){
                mFiles.add(f);
                Log.e(TAG, "initFilesData: "+f.getName());
            }
        }
       /* mFiles.add(R.mipmap.ai_1);
        mFiles.add(R.mipmap.ai_2);
        mFiles.add(R.mipmap.ai_3);
        mFiles.add(R.mipmap.ai_4);
        mFiles.add(R.mipmap.ai_5);
        mFiles.add(R.mipmap.ai_6);*/
    }

    private static final String TAG = "VerifyActivity";
}
