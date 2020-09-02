package com.faucet.faucetexample;

import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import android.Manifest;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.faucet.quickutils.views.groupheadview.CombineBitmap;
import com.faucet.quickutils.views.groupheadview.layout.DingLayoutManager;
import com.faucet.quickutils.views.groupheadview.layout.WechatLayoutManager;
import com.faucet.quickutils.views.groupheadview.listener.OnProgressListener;
import com.faucet.quickutils.views.groupheadview.listener.OnSubItemClickListener;

import java.util.List;

public class GroupHeadViewActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private String[] IMG_URL_ARR = {
            "https://dss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2417586904,2120716092&fm=26&gp=0.jpg",
            "测试",
            "https://ss0.bdstatic.com/70cFuHSh_Q1YnxGkpoWK1HF6hhy/it/u=1433226119,1535949713&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2076212417,3005667226&fm=26&gp=0.jpg",
            "https://ss1.bdstatic.com/70cFuXSh_Q1YnxGkpoWK1HF6hhy/it/u=2531897344,718638373&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=343856836,728466908&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2339488622,2744002712&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2119216127,753105151&fm=26&gp=0.jpg",
            "https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=2223756706,3188697330&fm=26&gp=0.jpg",
    };

    ImageView imageView1;
    ImageView imageView2;
    ImageView imageView3;
    ImageView imageView4;
    ImageView imageView5;
    ImageView imageView6;
    ImageView imageView7;
    ImageView imageView8;
    ImageView imageView9;
    ImageView imageView10;
    ImageView imageView11;


    private int[] getResourceIds(int count) {
        int[] res = new int[count];
        for (int i = 0; i < count; i++) {
            res[i] = R.mipmap.cat;
        }
        return res;
    }

    private String[] getUrls(int count) {
        String[] urls = new String[count];
        System.arraycopy(IMG_URL_ARR, 0, urls, 0, count);
        return urls;
    }

    private Bitmap[] getBitmaps(int count) {
        Bitmap[] bitmaps = new Bitmap[count];
        for (int i = 0; i < count; i++) {
            bitmaps[i] = BitmapFactory.decodeResource(getResources(), R.mipmap.cat);
        }
        return bitmaps;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_head_view);

        imageView1 = findViewById(R.id.nice_iv1);
        imageView2 = findViewById(R.id.nice_iv2);
        imageView3 = findViewById(R.id.nice_iv3);
        imageView4 = findViewById(R.id.iv4);
        imageView5 = findViewById(R.id.iv5);
        imageView6 = findViewById(R.id.iv6);
        imageView7 = findViewById(R.id.iv7);
        imageView8 = findViewById(R.id.iv8);
        imageView9 = findViewById(R.id.iv9);
        imageView10 = findViewById(R.id.iv10);
        imageView11 = findViewById(R.id.iv11);
        requestStoragePermission();
    }

    @AfterPermissionGranted(1000)
    private void requestStoragePermission() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            loadDingBitmap(imageView1, 2);

            loadDingBitmap(imageView2, 3);

            loadDingBitmap(imageView3, 4);

            loadWechatBitmap(imageView4, 2);

            loadWechatBitmap(imageView5, 3);

            loadWechatBitmap(imageView6, 4);

            loadWechatBitmap(imageView7, 5);

            loadWechatBitmap(imageView8, 6);

            loadWechatBitmap(imageView9, 7);

            loadWechatBitmap(imageView10, 8);

            loadWechatBitmap(imageView11, 9);

        } else {
            EasyPermissions.requestPermissions(this, "need storage permission", 1000, perms);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (1000 == requestCode) {
            if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
                new AppSettingsDialog.Builder(this)
                        .setTitle("tip")
                        .setRationale("need storage permission")
                        .build()
                        .show();
            }
        }
    }

    private void loadWechatBitmap(ImageView imageView, int count) {
        CombineBitmap.init(GroupHeadViewActivity.this)
                .setLayoutManager(new WechatLayoutManager())
                .setSize(180)
                .setGap(3)
                .setGapColor(Color.parseColor("#E8E8E8"))
                .setUrlsOrTexts(false, getUrls(count))
                .setImageView(imageView)
                .setOnSubItemClickListener(new OnSubItemClickListener() {
                    @Override
                    public void onSubItemClick(int index) {
                        Log.e("SubItemIndex", "--->" + index);
                    }
                })
                .build();
    }

    private void loadDingBitmap(final ImageView imageView, int count) {
        CombineBitmap.init(GroupHeadViewActivity.this)
                .setLayoutManager(new DingLayoutManager())
                .setSize(180)
                .setGap(2)
                .setUrlsOrTexts(false, getUrls(count))
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onComplete(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                })
                .build();
    }
}