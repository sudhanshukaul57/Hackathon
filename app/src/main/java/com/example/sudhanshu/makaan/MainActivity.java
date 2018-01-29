package com.example.sudhanshu.makaan;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.google.gson.Gson;

public class MainActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private CustomNetworkRequest customNetworkRequest;
    private ImageLoader imageLoader;
    private MakaanListing makaanListing;
    private MakaanDataAdapter makaanDataAdapter=null;
    private RecyclerViewScrollListener recyclerViewScrollListener;
    private long start;
    private TextView button;
    private Switch aSwitch;
    private StringBuffer url;
    private StringBuffer listingType=new StringBuffer("[%22"+"Primary%22,%22Resale%22"+"]}}");
    private StringBuffer equalUrl;
    private String startUrl="https://www.makaan.com/petra/app/v4/listing?selector={%22fields%22:[%22localityId%22,%22displayDate%22,%22listing%22,%22property%22,%22project%22,%22builder%22,%22displayName%22,%22locality%22,%22suburb%22,%22city%22,%22state%22,%22currentListingPrice%22,%22companySeller%22,%22company%22,%22user%22,%22id%22,%22name%22,%22label%22,%22listingId%22,%22propertyId%22,%22projectId%22,%22propertyTitle%22,%22unitTypeId%22,%22resaleURL%22,%22description%22,%22postedDate%22,%22verificationDate%22,%22size%22,%22measure%22,%22bedrooms%22,%22bathrooms%22,%22listingLatitude%22,%22listingLongitude%22,%22studyRoom%22,%22servantRoom%22,%22pricePerUnitArea%22,%22price%22,%22localityAvgPrice%22,%22negotiable%22,%22rk%22,%22buyUrl%22,%22rentUrl%22,%22overviewUrl%22,%22minConstructionCompletionDate%22,%22maxConstructionCompletionDate%22,%22halls%22,%22facingId%22,%22noOfOpenSides%22,%22bookingAmount%22,%22securityDeposit%22,%22ownershipTypeId%22,%22furnished%22,%22constructionStatusId%22,%22tenantTypes%22,%22bedrooms%22,%22balcony%22,%22floor%22,%22totalFloors%22,%22listingCategory%22,%22possessionDate%22,%22activeStatus%22,%22type%22,%22logo%22,%22profilePictureURL%22,%22score%22,%22assist%22,%22contactNumbers%22,%22contactNumber%22,%22isOffered%22,%22mainImageURL%22,%22mainImage%22,%22absolutePath%22,%22altText%22,%22title%22,%22imageCount%22,%22geoDistance%22,%22defaultImageId%22,%22updatedAt%22,%22qualityScore%22,%22projectStatus%22,%22throughCampaign%22,%22addedByPromoter%22,%22listingDebugInfo%22,%22videos%22,%22imageUrl%22,%22rk%22,%22penthouse%22,%22studio%22,%22paidLeadCount%22,%22listingSellerTransactionStatuses%22,%22allocation%22,%22allocationHistory%22,%22masterAllocationStatus%22,%22status%22,%22sellerCompanyFeedbackCount%22,%22companyStateReraRegistrationId%22],%22filters%22:{%22and%22:[{%22equal%22:{%22cityId%22:11}},{%22equal%22:{%22listingCategory%22:";
    private String endUrl=",%22rows%22:20}}&includeNearbyResults=false&includeSponsoredResults=false&sourceDomain=Makaan";
    private String midUrl="]},%22paging%22:{%22start%22:";
    private boolean filter;
    private long count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start=0;
        equalUrl=new StringBuffer();
         url=new StringBuffer(startUrl +
                 listingType +
                 equalUrl+
                 midUrl +
                 start +endUrl
                 );
        button=findViewById(R.id.button);
        aSwitch=findViewById(R.id.aswitch);
        recyclerView=findViewById(R.id.recycler);
        layoutManager=new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        customNetworkRequest = CustomNetworkRequest.getInstance(getApplicationContext());
        imageLoader=customNetworkRequest.getImageLoader();

        /**
         * Button is used for applying filters. This will start new activity and after applying filters
         * intent will be passed to the current activity
         */

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(MainActivity.this,FilterRequestActivity.class);
                startActivityForResult(i,1);
            }
        });

        /**
         * RecyclerViewScrollListener class is used here for supporting pagination
         * Main class implements message method which will display message if no data is available
         *
         * onLoadMore will be called when last view position + threshold value exceeds total items ,so new data will
         * be fetched
         */

        recyclerViewScrollListener= new RecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                loadNextDataFromApi(page);
            }

            @Override
            public void message(int lastVisibleItemPosition) {
                if(lastVisibleItemPosition==-1) Toast.makeText(MainActivity.this,"No results found",Toast.LENGTH_LONG).show();
            }
        };
        recyclerView.addOnScrollListener(recyclerViewScrollListener);

        /**
         * Here network request takes place for the first time after that onResponse() method will be called
         * which is defined in this class
         */
        customNetworkRequest.callGetApi(this, this, ""+url);

    }

    private void loadNextDataFromApi(int page) {

        start=(page-1)*20;
        url.replace(0,url.length(),startUrl + listingType + equalUrl+ midUrl + start +endUrl);
        customNetworkRequest.callGetApi(this, this, ""+url);

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        Log.e("error",error.toString());

    }

    @Override
    public void onResponse(String response) {

        Log.e("NetworkRequest","responseListener is called");

        if(TextUtils.isEmpty(response))
            return;
        Gson gson=customNetworkRequest.getGson();
        makaanListing = gson.fromJson(response, MakaanListing.class);

        if(makaanListing!=null) {
            receiveData(filter);
        }
        else {
            Log.e("NetworkRequest","No Posts data available!");
        }

    }


    void receiveData(boolean filter){
        if(makaanDataAdapter==null){
            makaanDataAdapter=new MakaanDataAdapter(this,makaanListing,imageLoader);
            recyclerView.setAdapter(makaanDataAdapter);
        }

        /**
         * filter is a boolean which is set to true if filters are applied
         * if filters are not applied then update method will be called where new data will concatenate to the previous one
         * else array list will be cleared and new data will be assigned.
         */
        else{
            if(!filter){
                makaanDataAdapter.updateData(makaanListing);
                makaanDataAdapter.notifyDataSetChanged();
            }
            else{
                makaanDataAdapter.setNewData(makaanListing);
                makaanDataAdapter.notifyDataSetChanged();
                this.filter=false;
            }
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                filter=true;
                count=data.getLongExtra("count",0);

                setFilter();

                String result=data.getStringExtra("result");
                equalUrl=new StringBuffer(result);
                if(equalUrl.length()>0)
                    filter=true;
                url.replace(0,url.length(),startUrl + listingType + equalUrl+ midUrl + start +endUrl);

                /**
                 * here again network request takes place after applying filters
                 */
                customNetworkRequest.callGetApi(this, this, ""+url);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                filter=false;
            }
        }
    }

    /**
     * this method is used for displaying the count of filters
     */
    private void setFilter() {
        TextView textView=findViewById(R.id.filter_number);

        Log.e("setFilter ","value of count is "+count);
        if(count!=0)
            textView.setText(""+count);
        else
            textView.setText("Not Applied");

    }



    /**
     * onDestroy will clear filter data
     */

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences=getSharedPreferences("preferences",MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear().commit();

    }

    /**
     * this method is used selecting listing category
     */

    public void onCheckBoxClicked(View view) {
        filter=true;
        if(aSwitch.isChecked()){
            listingType.replace(0,listingType.length(),"%22Rental%22}}");
        }
        else{
            listingType.replace(0,listingType.length(),"[%22"+"Primary%22,%22Resale%22"+"]}}");
        }
        url.replace(0,url.length(),startUrl +
                listingType +
                equalUrl+
                midUrl +
                start +endUrl
        );
        Log.e("MainActivityCheckBox ",""+url);
        customNetworkRequest.callGetApi(this, this, ""+url);
    }
}

