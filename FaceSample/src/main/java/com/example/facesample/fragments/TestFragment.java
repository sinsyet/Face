package com.example.facesample.fragments;

import android.app.Dialog;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.facesample.R;
import com.example.facesample.adapters.ImgAdapter;
import com.example.facesample.bean.ImgBean;
import com.example.facesample.engine.imgscan.Function;
import com.example.facesample.engine.imgscan.ImgScanner;
import com.example.facesample.engine.imgscan.ImgSubscriber;
import com.example.facesample.ui.dialogs.SelectFolderDialog;
import com.example.facesample.utils.ToastUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class TestFragment extends Fragment implements View.OnClickListener, SelectFolderDialog.Callback {

    private RecyclerView mRecyView;
    private ImgAdapter adapter;
    private List<ImgBean> imgBeans;
    private View mBtnUpload;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_test, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findView(view);

    }

    private void findView(View v) {
        mBtnUpload = v.findViewById(R.id.test_btn_upload);
        mBtnUpload.setOnClickListener(this);
        mRecyView = v.findViewById(R.id.test_recyV);
        initRecyViewAdapter();
    }

    private void initRecyViewAdapter() {
        //设置layoutManager
        mRecyView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        //设置adapter
        imgBeans = new ArrayList<>();
        adapter = new ImgAdapter(imgBeans);
        mRecyView.setAdapter(adapter);
        //设置item之间的间隔
        mRecyView.addItemDecoration(new RecyclerView.ItemDecoration() {

            private int space = 30;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                outRect.left = space;
                outRect.right = space;
                outRect.bottom = space;
                //第一个不设置间隔,否则顶部有空白
                if (parent.getChildPosition(view) == 0 || parent.getChildPosition(view) == 1) {
                    outRect.top = space;
                }
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.test_btn_upload:
                showSelectDialog();
                break;
        }
    }

    private void showSelectDialog() {
        SelectFolderDialog dialog = new SelectFolderDialog(getActivity(), this);
        dialog.show();
    }

    @Override
    public void onSelected(Dialog dialog, boolean selected, @Nullable String folderPath) {

        if (dialog.isShowing())
            dialog.dismiss();

        if (selected && !TextUtils.isEmpty(folderPath)) {
            ToastUtils.show(getContext(), folderPath);
            loadImgs(folderPath);
            mRecyView.setVisibility(View.VISIBLE);
            mBtnUpload.setVisibility(View.GONE);
        }
    }

    private void loadImgs(String path) {
        ImgScanner.scanSDCard(
                new ImgSubscriber<ImgBean>(new File(path),
                        new Function<File, ImgBean>() {
                            @Override
                            public ImgBean applyAs(File p) {
                                return new ImgBean(p, p.getName());
                            }
                        }) {
                    @Override
                    public void onScanStart() {
                        imgBeans.clear();
                    }

                    @Override
                    public void onScanCompleted(List<ImgBean> files) {
                        imgBeans.addAll(files);
                    }

                    @Override
                    public void onScanEnd() {
                        adapter.notifyDataSetChanged();
                    }
                });
    }
}
