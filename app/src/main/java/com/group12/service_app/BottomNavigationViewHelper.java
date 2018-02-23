//package com.group12.service_app;
//
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.support.design.internal.BottomNavigationItemView;
//import android.support.design.internal.BottomNavigationMenuView;
//import android.support.design.widget.BottomNavigationView;
//import android.util.Log;
//import android.widget.TextView;
//import java.lang.reflect.Field;
//
//public class BottomNavigationViewHelper {
//    public static void disableShiftMode(Context context,BottomNavigationView view) {
//        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
//        try {
//            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
//            shiftingMode.setAccessible(true);
//            shiftingMode.setBoolean(menuView, false);
//            shiftingMode.setAccessible(false);
//            for (int i = 0; i < menuView.getChildCount(); i++) {
//                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
//
//                Field LargeText = item.getClass().getDeclaredField("mLargeLabel");
//                LargeText.setAccessible(true);
//                Field SmallText = item.getClass().getDeclaredField("mSmallLabel");
//                SmallText.setAccessible(true);
//
////                 TextView SmallTextView =(TextView) SmallText.get(item);
////                 TextView LargeTextView =(TextView) LargeText.get(item);
//
////                 Typeface tf = TypefaceHelper.getTypeface(context);
////                 SmallTextView.setTypeface(tf);
////                 LargeTextView.setTypeface(tf);
//
//                //noinspection RestrictedApi
//                item.setShiftingMode(false);
//                // set once again checked value, so view will be updated
//                //noinspection RestrictedApi
//                item.setChecked(item.getItemData().isChecked());
//            }
//        } catch (NoSuchFieldException e) {
//            Log.e("BNVHelper", "Unable to get shift mode field", e);
//        } catch (IllegalAccessException e) {
//            Log.e("BNVHelper", "Unable to change value of shift mode", e);
//        }
//    }
//}