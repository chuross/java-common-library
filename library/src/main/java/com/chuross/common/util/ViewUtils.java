package com.chuross.common.library.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public final class ViewUtils {

    public static View inflate(Context context, int resourceId, ViewGroup root, boolean attachToRoot) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resourceId, root, attachToRoot);
    }

    public static <T> T findViewById(View view, int id, Class<T> clazz) {
        return clazz.cast(view.findViewById(id));
    }

}
