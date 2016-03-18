package com.mstc.student.aitpstockideas;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.LocationListener;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;
import android.view.View.OnClickListener;


import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ben on 3/14/2016.
 */
public class NewStock extends Activity {
    private LocationManager locationManager;
    private String provider;
    String gpsLocation;
    String stockSymbol;
    Calendar currentDateTime;
    double startingPrice;
    TextView longitudeTV, latitudeTV, currentDateTV, startingPriceTV, currentTimeTV;
    EditText stockSymbolET;
    Button backButton, addStockButton, lookupButton;

    public final static String STOCK_SYMBOL = "STOCK_SYMBOL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_stock_layout);

        //Initialize variables
        longitudeTV = (TextView) findViewById(R.id.longitudeTextView);
        latitudeTV = (TextView) findViewById(R.id.latitudeTextView);
        currentDateTV = (TextView) findViewById(R.id.currentDateTV);
        currentTimeTV = (TextView) findViewById(R.id.currentTimeTV);
        startingPriceTV = (TextView) findViewById(R.id.startingPriceTextView);
        stockSymbolET = (EditText) findViewById(R.id.stockSymbolEditText);
        backButton = (Button) findViewById(R.id.backButton);
        addStockButton = (Button) findViewById(R.id.addStockButton);
        lookupButton = (Button) findViewById(R.id.lookupButton);

        //clear the textviews
        latitudeTV.setText("");
        longitudeTV.setText("");
        startingPriceTV.setText("");
        currentDateTV.setText("");

        //call the methods that set the location and the data and time
        getLocation();
        getDateTime();

    }

    //****************************************************************************************************
    //This method is used to get data back from the activity stock info that is called
    //the current price is returned and the text view is set
    //************************************************************************************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2)
        {
            String message=data.getStringExtra("MESSAGE");
            startingPriceTV.setText(message);
        }
    }
    //************************************************************************************************************
    //This method gets the gps coordinates and sets the longitude and latitude textviews
    //************************************************************************************************************
    public void getLocation(){
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null || provider != null) {
             double lat = (double) (location.getLatitude());
            double lng = (double) (location.getLongitude());
            String latString = Double.toString(lat);
            String lngString = Double.toString(lng);
            latitudeTV.setText(latString);
            //String lngString = String.format("%4f",lng);
            longitudeTV.setText(lngString);
         } else {
            latitudeTV.setText("Provider not available");
            longitudeTV.setText("Provider not available");
         }

    }
    //*******************************************************************************************************
    //Get the Current Date and Time
    //********************************************************************************************************
    public void getDateTime(){
        SimpleDateFormat sdfDate= new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat sdfTime= new SimpleDateFormat("hh:mm:ss");
        currentDateTime = Calendar.getInstance();
        String currentDateFormated = sdfDate.format(currentDateTime.getTime());
        String currentTimeFormated = sdfTime.format(currentDateTime.getTime());

        currentTimeTV.setText(currentTimeFormated);
        currentDateTV.setText(currentDateFormated);

    }
    //*********************************************************************************
    //this method is called when the lookup stock button is clicked
    //the onclicklistener is set in the xml
    //**********************************************************************************
    public void lookupStockInfo(View view) {

        String stockSymbol = stockSymbolET.getText().toString();

        // An intent is an object that can be used to start another activity
        Intent intent = new Intent(NewStock.this, StockInfoActivity.class);

        // Add the stock symbol to the intent
        intent.putExtra(STOCK_SYMBOL, stockSymbol);

        startActivityForResult(intent, 2);

    }
    //*********************************************************************************
    //this method is called when the back button is clicked
    //the onclicklistener is set in the xml
    //**********************************************************************************
    public void backButtonClicked(View view) {
        finish();
    }
}
