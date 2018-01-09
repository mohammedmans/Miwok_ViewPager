package com.example.mohammed.miwok;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by Mohammed on 1/9/2018.
 */

public class CategoeryAdapter extends FragmentPagerAdapter {
    //  we need a Context object in order to turn the string resource ID into an actual String
    // because  we rely on string resources instead of hardcoded strings in the Java code.
    /** Context of the app */
    private Context mContext;

    //we modify the CategoryAdapter constructor to also require a Context input so that we can get the proper text string.
    // firstly constructor look like public CategoeryAdapter(FragmentManager fm)
    public CategoeryAdapter(Context context,FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0)
            return new NumberFragment();
        else if (position == 1)
            return new ColorsFragment();
        else if (position == 2)
            return new FamilyFragment();
        else
            return new PhrasesFragment();
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0)
            return mContext.getString(R.string.category_numbers);
        else if (position == 1)
            return mContext.getString(R.string.category_colors);
        else if (position == 2)
            return mContext.getString(R.string.category_family);
        else
            return mContext.getString(R.string.category_phrases);
    }

    /*
    Since we modified the CategoryAdapter constructor,
    we also need to update the MainActivity (which uses that constructor).
    When we create a CategoryAdapter,
    we pass in a Context (which is “this” or the activity) and the FragmentManager.
     */
}
