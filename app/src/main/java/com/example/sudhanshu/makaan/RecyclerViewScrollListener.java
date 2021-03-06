package com.example.sudhanshu.makaan;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by sudhanshu on 1/25/18.
 */

abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener{

    /**
     * The minimum amount of items to have below your current scroll position before loading more.
     */

    private int visibleThreshold = 5;

    // The current offset index of data you have loaded

    private int currentPage = 1;

    // The total number of items in the dataset after the last load

    private int previousTotalItemCount = 0;

    // True if we are still waiting for the last set of data to load.
    private boolean loading = true;

    // Sets the starting page index
    private int startingPageIndex = 0;

    RecyclerView.LayoutManager mLayoutManager;

    public RecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
    }



    @Override
    public void onScrolled(RecyclerView view, int dx, int dy) {
        int lastVisibleItemPosition = 0;
        int totalItemCount = mLayoutManager.getItemCount();
        lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        /**
         * if no data is available then lastVisibleItemPosition will be -1 and hence a toast can be displayed for
         * results.
         * For that an abstract method message() is declared in this class which is defined in main class
         */
        if(lastVisibleItemPosition==-1)
            message(lastVisibleItemPosition);

        /**
         * if totalItemCount < previousTotalCount, it means new data has been fetched so current page should be set
         * to starting page index
         */
        if (totalItemCount < previousTotalItemCount) {
            currentPage = startingPageIndex;
            if (totalItemCount == 0) {
                loading = true;
                previousTotalItemCount = totalItemCount;
            }
            else
                previousTotalItemCount=totalItemCount-1;

        }
        /**
         *  If it’s still loading, we check to see if the dataset count has
          changed, if so we conclude it has finished loading and update the current page
          number and total item count.
         */

        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        /**
         * If it isn’t currently loading, we check to see if we have breached
          the visibleThreshold and need to reload more data.
          If we do need to reload some more data, we execute onLoadMore to fetch the data.
          threshold should reflect how many total columns there are too
         */
        if (!loading && (lastVisibleItemPosition + visibleThreshold) > totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, view);

            loading = true;
        }
    }


    // Defines the process for actually loading more data based on page
    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);
    public abstract void message(int lastVisibleItemPosition);

}
