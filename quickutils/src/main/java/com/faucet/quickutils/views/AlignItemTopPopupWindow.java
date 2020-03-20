package com.faucet.quickutils.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.faucet.quickutils.R;
import com.faucet.quickutils.utils.SizeUtils;

import java.util.List;

/**
 * 悬浮popwindow，如内容选择复制后弹出的复制框
 */

public class AlignItemTopPopupWindow {
    private LinearLayout popupView;

    private PopupWindow popupWindow;
    private Context context;

    public static AlignItemTopPopupWindow show(Context context, String strings, View view, PopupItemClickListener clickListener) {
        AlignItemTopPopupWindow alignItemTopPopupWindow = new AlignItemTopPopupWindow(context, new String[]{strings}, clickListener);
        alignItemTopPopupWindow.show(view);
        return alignItemTopPopupWindow;
    }
    public static AlignItemTopPopupWindow showAlignTop(Context context, String strings, View view, PopupItemClickListener clickListener) {
        AlignItemTopPopupWindow alignItemTopPopupWindow = new AlignItemTopPopupWindow(context, new String[]{strings}, clickListener);
        alignItemTopPopupWindow.showAlignTop(view);
        return alignItemTopPopupWindow;
    }

    public static AlignItemTopPopupWindow show(Context context, String[] strings, View view, PopupItemClickListener clickListener) {
        AlignItemTopPopupWindow alignItemTopPopupWindow = new AlignItemTopPopupWindow(context, strings, clickListener);
        alignItemTopPopupWindow.show(view);
        return alignItemTopPopupWindow;
    }

    public static AlignItemTopPopupWindow show(Context context, List<String> strings, View view, PopupItemClickListener clickListener) {
        AlignItemTopPopupWindow alignItemTopPopupWindow = new AlignItemTopPopupWindow(context, strings.toArray(new String[strings.size()]), clickListener);
        alignItemTopPopupWindow.show(view);
        return alignItemTopPopupWindow;
    }

    private AlignItemTopPopupWindow(Context context, String[] strings, PopupItemClickListener clickListener) {
        this.context = context;
        this.popupItemClickListener = clickListener;
        popupView = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.item_collect_popup, null);
        initPopupView(strings);
        popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable(context.getResources(), (Bitmap) null));
    }

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    private void initPopupView(String[] strings) {
        LinearLayout menu_layout = getView(R.id.popup_menu_layout);
        for (int i = 0; i < strings.length; i++) {
            if (i == 0) {
                menu_layout.addView(createMenuView(i, strings[i]));
            } else {
                menu_layout.addView(createDevide());
                menu_layout.addView(createMenuView(i, strings[i]));
            }
        }
    }


    public interface PopupItemClickListener {
        void onItemClick(int position, String key);
    }

    private PopupItemClickListener popupItemClickListener;


    private View createDevide() {
        View view = new View(popupView.getContext());
        view.setBackgroundColor(Color.parseColor("#515151"));
        ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(SizeUtils.dp2px(context, 0.5f), ViewGroup.LayoutParams.MATCH_PARENT);
        marginLayoutParams.topMargin = SizeUtils.dp2px(context, 8);
        marginLayoutParams.bottomMargin = SizeUtils.dp2px(context, 8);
        view.setLayoutParams(marginLayoutParams);
        return view;
    }

    private View createMenuView(int position, String string) {
        TextView textView = new TextView(popupView.getContext());
        textView.setText(string);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(14);
        int i = SizeUtils.dp2px(popupView.getContext(), 8);
        textView.setPadding(i, i, i, i);
        if (popupItemClickListener != null) {
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    popupItemClickListener.onItemClick(position, string);
                }
            });
        }
        return textView;
    }


    private void show(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();
        int popupWidth = popupView.getMeasuredWidth();
        int y = location[1] - popupHeight + SizeUtils.dp2px(popupView.getContext(), 13);
        View parent = (View) view.getParent();
        int[] location1 = new int[2];
        parent.getLocationInWindow(location1);
        // to avoid location of show out of parent
        if (y < location1[1]) {
            showAlignBottom(view); // show this popupWindow align item's bottom
        } else {
            popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, y);
        }
    }

    public void showAlignTop(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();
        int popupWidth = popupView.getMeasuredWidth();
        int y = location[1] - popupHeight + SizeUtils.dp2px(popupView.getContext(), 13);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, y);
    }

    public void showAlignBottom(View view) {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        View arror = getView(R.id.popup_menu_arror);
        View menu_layout = getView(R.id.popup_menu_layout);
        //change the triangle'position and direction in popupWindow
        arror.setRotation(180);
        popupView.removeView(menu_layout);
        popupView.addView(menu_layout);
        popupView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        int popupHeight = popupView.getMeasuredHeight();
        int popupWidth = popupView.getMeasuredWidth();
        int y = location[1] + view.getHeight() - SizeUtils.dp2px(popupView.getContext(), 13);
        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, (location[0] + view.getWidth() / 2) - popupWidth / 2, y);
    }


    private <T extends View> T getView(int id) {
        View view = popupView.findViewById(id);
        return (T) view;
    }

    private void dismiss() {
        popupWindow.dismiss();
    }

    public void setOnDismissListener(PopupWindow.OnDismissListener dismissListener) {
        popupWindow.setOnDismissListener(dismissListener);
    }
}
