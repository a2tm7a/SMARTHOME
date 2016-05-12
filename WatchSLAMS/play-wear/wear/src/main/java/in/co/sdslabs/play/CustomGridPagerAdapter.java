package in.co.sdslabs.play;

/**
 * Created by Akshay on 12-03-2015.
 */
/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.wearable.view.CardFragment;
import android.support.wearable.view.FragmentGridPagerAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Constructs fragments as requested by the GridViewPager. For each row a different background is
 * provided.
 * <p/>
 * Always avoid loading resources from the main thread. In this sample, the background images are
 * loaded from an background task and then updated using {@link #notifyRowBackgroundChanged(int)}
 * and {@link #notifyPageBackgroundChanged(int, int)}.
 */
public class CustomGridPagerAdapter extends FragmentGridPagerAdapter {
    private static final int TRANSITION_DURATION_MILLIS = 100;

    private final Context mContext;
    private List<Row> mRows;
    private ColorDrawable mDefaultBg;

    private ColorDrawable mClearBg;
    private CardFragment customFragment;

    public MasterControlsFragment mcf;
    public NextTrackFragment ntf;

    public CustomGridPagerAdapter (Context ctx, FragmentManager fm) {
        super(fm);
        mContext = ctx;

        mRows = new ArrayList<CustomGridPagerAdapter.Row>();

       /* mcf = new MasterControlsFragment();
        mRows.add(new Row(
                mcf,
                cardFragment("Burn","Ellie Goulding")));
       */
        mDefaultBg = new ColorDrawable(R.color.dark_grey);
        mClearBg = new ColorDrawable(android.R.color.transparent);
    }


    LruCache<Integer, Drawable> mRowBackgrounds = new LruCache<Integer, Drawable>(3) {
        @Override
        protected Drawable create (final Integer row) {
            int resid = BG_IMAGES[row % BG_IMAGES.length];
            new DrawableLoadingTask(mContext) {
                @Override
                protected void onPostExecute (Drawable result) {
                    TransitionDrawable background = new TransitionDrawable(new Drawable[]{
                            mDefaultBg,
                            result
                    });
                    mRowBackgrounds.put(row, background);
                    notifyRowBackgroundChanged(row);
                    background.startTransition(TRANSITION_DURATION_MILLIS);
                }
            }.execute(resid);
            return mDefaultBg;
        }
    };

    LruCache<Point, Drawable> mPageBackgrounds = new LruCache<Point, Drawable>(3) {
        @Override
        protected Drawable create (final Point page) {
            // place bugdroid as the background at row 2, column 1
/*
            if (page.y == 2 && page.x == 1) {
                int resid = R.drawable.bugdroid_large;
                new DrawableLoadingTask(mContext) {
                    @Override
                    protected void onPostExecute (Drawable result) {
                        TransitionDrawable background = new TransitionDrawable(new Drawable[]{
                                mClearBg,
                                result
                        });
                        mPageBackgrounds.put(page, background);
                        notifyPageBackgroundChanged(page.y, page.x);
                        background.startTransition(TRANSITION_DURATION_MILLIS);
                    }
                }.execute(resid);
            }
*/
            return CustomGridPagerAdapter.BACKGROUND_NONE;
        }
    };

    private Fragment cardFragment (String title, String artist) {
        Resources res = mContext.getResources();
        customFragment =
                CardFragment.create(title, artist);
        // Add some extra bottom margin to leave room for the page indicator
        customFragment.setCardMarginBottom(
                res.getDimensionPixelSize(R.dimen.card_margin_bottom));
        return customFragment;
    }

    int[] BG_IMAGES = new int[]{
            R.drawable.debug_background_1,
            R.drawable.debug_background_2,
    };

    /**
     * A convenient container for a row of fragments.
     */
    private class Row {
        final List<Fragment> columns = new ArrayList<Fragment>();

        public Row (Fragment... fragments) {
            for (Fragment f : fragments) {
                add(f);
            }
        }

        public void add (Fragment f) {
            columns.add(f);
        }

        Fragment getColumn (int i) {
            return columns.get(i);
        }

        public int getColumnCount () {
            return columns.size();
        }
    }

    @Override
    public Fragment getFragment (int row, int col) {
        Row adapterRow = mRows.get(row);
        return adapterRow.getColumn(col);
    }

    @Override
    public Drawable getBackgroundForRow (final int row) {
        return mRowBackgrounds.get(row);
    }

    @Override
    public Drawable getBackgroundForPage (final int row, final int column) {
        return mPageBackgrounds.get(new Point(column, row));
    }

    @Override
    public int getRowCount () {
        return mRows.size();
    }

    @Override
    public int getColumnCount (int rowNum) {
        return mRows.get(rowNum).getColumnCount();
    }

    class DrawableLoadingTask extends AsyncTask<Integer, Void, Drawable> {
        private static final String TAG = "Loader";
        private Context context;

        DrawableLoadingTask (Context context) {
            this.context = context;
        }

        @Override
        protected Drawable doInBackground (Integer... params) {
            Log.d(TAG, "Loading asset 0x" + Integer.toHexString(params[0]));
            return context.getResources().getDrawable(params[0]);
        }
    }

/*
    public void updateMasterFragment(String title, String artist , Bitmap bitmap){
        mRows.clear();
        MasterControlsFragment mcf = new MasterControlsFragment(bitmap);
        mcf.bitmap = bitmap;
        mRows.add(new Row(mcf,customFragment));
    }
    public void updateAdapter(String song , String artist , Bitmap background){
        mRows.clear();
        mRows.add(new Row());
    }
*/

    public void updateAdapter(String lights, String security, String fan) {
        mRows.clear();
        mcf = new MasterControlsFragment(lights, security, fan);
        mRows.add(new Row(
                mcf));
        notifyDataSetChanged();
    }
}

