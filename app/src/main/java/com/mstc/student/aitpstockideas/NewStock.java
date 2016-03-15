package com.mstc.student.aitpstockideas;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
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
    Button backButton, addStockButton;

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

        latitudeTV.setText("");
        longitudeTV.setText("");
        startingPriceTV.setText("");
        currentDateTV.setText("");


        getLocation();
        getDateTime();


    }



public void getLocation(){
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Define the criteria how to select the location provider -> use
    // default
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

    //Get the Current Date and Time
    public void getDateTime(){
        SimpleDateFormat sdfDate= new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat sdfTime= new SimpleDateFormat("hh:mm:ss");
        currentDateTime = Calendar.getInstance();
        String currentDateFormated = sdfDate.format(currentDateTime.getTime());
        String currentTimeFormated = sdfTime.format(currentDateTime.getTime());

        currentTimeTV.setText(currentTimeFormated);
        currentDateTV.setText(currentDateFormated);

    }







    public void backButtonClicked(View view) {
        finish();
    }
}
