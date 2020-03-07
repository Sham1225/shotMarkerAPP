package com.example.shotmarker.ui.dashboard;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.shotmarker.MainActivity;
import com.example.shotmarker.R;
import com.google.android.material.navigation.NavigationView;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import pub.devrel.easypermissions.EasyPermissions;

import static android.app.Activity.RESULT_OK;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;
    private String[] permissions = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
        //final TextView textView = root.findViewById(R.id.text_dashboard);
        dashboardViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                //textView.setText(s);
            }
        });
        return root;
    }

    //获取权限
    private void getPermission() {
        if (EasyPermissions.hasPermissions(getActivity(), permissions)) {
            //已经打开权限
            Toast.makeText(getActivity(),"已申请",Toast.LENGTH_SHORT);
        } else {
            //没有打开相关权限、申请权限
            EasyPermissions.requestPermissions(this, "需要获取您的相册、照相使用权限", 1, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //框架要求必须这么写
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
    //成功打开权限
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

        Toast.makeText(getActivity(), "相关权限获取成功", Toast.LENGTH_SHORT).show();
    }
    //用户未同意权限
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Toast.makeText(getActivity(), "请同意相关权限，否则功能无法使用", Toast.LENGTH_SHORT).show();
    }

    private File cameraSavePath;//拍照照片路径
    private Uri uri;//照片uri

    //激活相机操作
    private void goCamera() {
        cameraSavePath = new File(Environment.getExternalStorageDirectory().getPath() + "/" + System.currentTimeMillis() + ".jpg");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //第二个参数为 包名.fileprovider
            uri = FileProvider.getUriForFile(getContext(), "com.example.hxd.pictest.fileprovider", cameraSavePath);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            uri = Uri.fromFile(cameraSavePath);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        getActivity().startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //点击拍照按钮的时候干的事儿
        final ImageButton shot = getView().findViewById(R.id.shotButton);
        shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
                goCamera();
            }
        });

        //拍照按钮的缩放动画
        final ScaleAnimation animation = new ScaleAnimation((float)0.5, 1, (float)0.4, 1, Animation.RELATIVE_TO_SELF, 0.5f,1, 0.5f);
        /**
         *
         * @param fromX 起始x轴位置，0为最小，1为原始，float形
         * @param toX 同上
         * @param fromY 同上T
         * @param toY 同上
         * @param pivotXType 用来约束pivotXValue的取值。取值有三种：Animation.ABSOLUTE，Animation.RELATIVE_TO_SELF，Animation.RELATIVE_TO_PARENT
         * Type：Animation.ABSOLUTE：绝对，如果设置这种类型，后面pivotXValue取值就必须是像素点；比如：控件X方向上的中心点，pivotXValue的取值mIvScale.getWidth() / 2f
         *      Animation.RELATIVE_TO_SELF：相对于控件自己，设置这种类型，后面pivotXValue取值就会去拿这个取值是乘上控件本身的宽度；比如：控件X方向上的中心点，pivotXValue的取值0.5f
         *      Animation.RELATIVE_TO_PARENT：相对于它父容器（这个父容器是指包括这个这个做动画控件的外一层控件）， 原理同上，
         * @param pivotXValue  配合pivotXType使用，原理在上面
         * @param pivotYType 同from/to
         * @param pivotYValue 原理同上
         */
        animation.setDuration(500);
        //设置持续时间
        animation.setFillAfter(true);
        //设置动画结束之后的状态是否是动画的最终状态，true，表示是保持动画结束时的最终状态
        animation.setRepeatCount(0);
        //设置循环次数
        shot.startAnimation(animation);
        //开始动画

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run() {
                ScaleAnimation animation2 = new ScaleAnimation(1, (float)0.93, 1, (float)0.93, Animation.RELATIVE_TO_SELF, 0.5f,1, 0.5f);
                animation2.setDuration(700);
                animation2.setFillAfter(false);
                animation2.setRepeatCount(99999);
                animation2.setRepeatMode(Animation.REVERSE);
                shot.startAnimation(animation2);
            } }, 500);

    }
}