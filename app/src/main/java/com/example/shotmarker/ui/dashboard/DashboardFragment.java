package com.example.shotmarker.ui.dashboard;

import android.os.Bundle;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.example.shotmarker.R;
import com.google.android.material.navigation.NavigationView;

import java.util.Timer;
import java.util.TimerTask;

public class DashboardFragment extends Fragment {

    private DashboardViewModel dashboardViewModel;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        //点击拍照按钮的时候干的事儿
        final ImageButton shot = getView().findViewById(R.id.shotButton);
        shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"clicked",Toast.LENGTH_SHORT).show();
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