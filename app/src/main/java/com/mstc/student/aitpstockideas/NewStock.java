package com.mstc.student.aitpstockideas;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;
import android.view.View.OnClickListener;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Ben on 3/14/2016.
 */
public class NewStock extends Activity {
    private LocationManager locationManager;
    private String provider;
    String stockSymbol, message;
    Calendar currentDateTime;
    double startingPrice;
    TextView longitudeTV, latitudeTV, currentDateTV, startingPriceTV, currentTimeTV;
    EditText stockSymbolET;
    Button backButton, addStockButton, lookupButton,cameraButton;
    double lat=0.0, lng=0.0;
    String currentDateTimeFormated, latString, lngString;

    private static final int REQUEST_TAKE_PHOTO = 0;
    private static final int SELECT_FILE = 1;
    private Uri mUri;

    Intent takePicture = new Intent();
    String path = "";
    private ImageView ideaImage;
    String mCurrentPhotoPath;
    private Bitmap mPhoto;

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
        cameraButton = (Button) findViewById(R.id.photoButton);
        ideaImage = (ImageView) findViewById(R.id.addStockIV);

        //clear the textviews
        latitudeTV.setText("");
        longitudeTV.setText("");
        startingPriceTV.setText("");
        currentDateTV.setText("");
        cameraButton.setOnClickListener(cameraListener);



        //call the methods that set the location and the data and time
        getLocation();
        getDateTime();

    }

    private View.OnClickListener cameraListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            takePhoto(v);
        }
    };

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";

        File storageDir = this.getDir("productImages", Context.MODE_PRIVATE);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = image.toString();
        path = mCurrentPhotoPath;
        return image;

    }
    //****************************************************************************************************
    //This method is used to get data back from the activity stock info that is called
    //the current price is returned and the text view is set
    //************************************************************************************************
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    mPhoto = (Bitmap) data.getExtras().get("data");
                    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                    mPhoto.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                    try {
                        File photoFile = createImageFile();
                        FileOutputStream fo;
                        fo = new FileOutputStream(photoFile);
                        fo.write(bytes.toByteArray());
                        mUri = Uri.fromFile(photoFile);
                        takePicture.putExtra(MediaStore.EXTRA_OUTPUT,
                                Uri.fromFile(photoFile));

                        fo.close();
                    }
                    catch (IOException e){
                        e.printStackTrace();
                    }
                    ideaImage.setImageBitmap(mPhoto);
                    break;
                }

            case SELECT_FILE:
                if (resultCode == RESULT_OK) {
                    Uri selectedImageUri = data.getData();
                    String[] projection = {MediaStore.MediaColumns.DATA};
                    CursorLoader cursorLoader = new CursorLoader(this, selectedImageUri, projection, null, null,
                            null);
                    Cursor cursor = cursorLoader.loadInBackground();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                    cursor.moveToFirst();
                    String selectedImagePath = cursor.getString(column_index);
                    Bitmap bm;
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inJustDecodeBounds = true;
                    BitmapFactory.decodeFile(selectedImagePath, options);
                    int targetW = ideaImage.getWidth();
                    int targetH = ideaImage.getHeight();
                    int scale = 1;
                    while (options.outWidth / scale / 2 >= targetW
                            && options.outHeight / scale / 2 >= targetH)
                        scale *= 2;
                    options.inSampleSize = scale;
                    options.inJustDecodeBounds = false;
                    bm = BitmapFactory.decodeFile(selectedImagePath, options);
                    ideaImage.setImageBitmap(bm);
                    break;
                }
            case 2:
                message = data.getStringExtra("MESSAGE");
                startingPrice = Double.parseDouble(message);
                startingPriceTV.setText(message);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("mURI", mUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        mUri = savedInstanceState.getParcelable("mURI");
    }

    private void takePhoto(View v){
        final CharSequence[] items = { "Take Photo", "Choose from Library", "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(takePicture, REQUEST_TAKE_PHOTO);
                } else if (items[item].equals("Choose from Library")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();;
    }




    //************************************************************************************************************
    //This method gets the gps coordinates and sets the longitude and latitude textviews
    //************************************************************************************************************
    public void getLocation() {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // Define the criteria how to select the location provider -> use

        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null || provider != null) {
            lat =  (location.getLatitude());
            lng = (location.getLongitude());
            latString = Double.toString(lat);
            lngString = Double.toString(lng);
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
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        currentDateTime = Calendar.getInstance();
        String currentDateFormated = sdfDate.format(currentDateTime.getTime());
        String currentTimeFormated = sdfTime.format(currentDateTime.getTime());
        currentDateTimeFormated = sdfDateTime.format(currentDateTime.getTime());
        currentTimeTV.setText(currentTimeFormated);
        currentDateTV.setText(currentDateFormated);

    }
    //*********************************************************************************
    //this method is called when the lookup stock button is clicked
    //the onclicklistener is set in the xml
    //**********************************************************************************
    public void lookupStockInfo(View view) {

        stockSymbol = stockSymbolET.getText().toString();

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

    //********************************************************************************
    //this is the onclick method for the add stock button it is implemented via xml
    //****************************************************************************
    public void addStockButtonClicked(View v) {
        MyDBHandler dbHandler = new MyDBHandler(v.getContext(), null, null, 1);

        if(message == null || message.equals("")) {
            Toast.makeText(getApplicationContext(), "Insert All The Fields message " + message + " latString " + latString +"lngString " + lngString + "StartingPrice " + startingPrice, Toast.LENGTH_LONG).show();
            return;
        } else {
            startingPrice = Float.parseFloat(message);
        }
        /*if(latString == null || latString.equals("")) {
            Toast.makeText(getApplicationContext(), "Insert All The Fields message " + message + " latString " + latString +"lngString " + lngString + "StartingPrice " + startingPrice, Toast.LENGTH_LONG).show();
            //Toast.makeText(getApplicationContext(), "Insert All The Fields", Toast.LENGTH_LONG).show();
            return;
        } else {
            lat = Float.parseFloat(latString);
        }
        if (lngString == null || lngString.equals("")){
            Toast.makeText(getApplicationContext(), "Insert All The Fields message " + message + " latString " + latString +"lngString " + lngString + "StartingPrice " + startingPrice, Toast.LENGTH_LONG).show();
            return;
        } else {
            lng = Float.parseFloat(lngString);
        }
        */
        lat = 0.0;
        lng = 0.0;
        StockIdea idea = new StockIdea(currentDateTimeFormated ,lat, lng, startingPrice, stockSymbol, path);
        dbHandler.addStockIdea(idea);


        stockSymbolET.setText("");
        startingPriceTV.setText("");
    }
}
