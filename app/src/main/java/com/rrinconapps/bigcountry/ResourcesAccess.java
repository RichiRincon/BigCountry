package com.rrinconapps.bigcountry;

import android.content.Context;

/**
 * Created by Ricardo on 07/01/2016.
 */
public final class ResourcesAccess {

    /**
     *
     * @param context
     * @param name
     * @return
     */
    public static int getIconId(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
