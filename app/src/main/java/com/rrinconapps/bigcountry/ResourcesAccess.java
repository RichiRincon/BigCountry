package com.rrinconapps.bigcountry;

import android.content.Context;

/**
 * Includes methods to improve resources treatment.
 *
 * @author Ricardo Rincon
 * @since 2016-01-07
 */
public final class ResourcesAccess {

    /**
     * Gets an icon id given its name and context.
     *
     * @param context Resources context
     * @param name Icon file name (file extension not needed)
     * @return Icon id
     */
    public static int getIconId(Context context, String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName());
    }
}
