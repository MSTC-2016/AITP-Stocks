package com.mstc.student.aitpstockideas;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ben on 3/14/2016.
 */
public class StockIdea {


    private String _dateTime;
    private double _GPS_Lat;
    private double _GPS_Lon;
    private double _startingPrice;
    private String _symbol;
    private String _imagePath;

    public StockIdea(String dateTime, double GPS_Lat, double GPS_Lon, double startingPrice, String symbol, String imagePath) {
        this._dateTime = dateTime;
        this._GPS_Lat = GPS_Lat;
        this._GPS_Lon = GPS_Lon;
        this._startingPrice = startingPrice;
        this._symbol = symbol;
        this._imagePath = imagePath;
    }

    public String getDateTime() {
        return _dateTime;
    }

    public void setDateTime(String dateTime) {
        this._dateTime = dateTime;
    }

    public double getGPS_Lat() {
        return _GPS_Lat;
    }

    public void setGPS_Lat(double GPS_Lat) {
        this._GPS_Lat = GPS_Lat;
    }

    public double getGPS_Lon() {
        return _GPS_Lon;
    }

    public void setGPS_Lon(double GPS_Lon) {
        this._GPS_Lon = GPS_Lon;
    }

    public double get_startingPrice() {
        return _startingPrice;
    }

    public void setStartingPrice(double startingPrice) {
        this._startingPrice = startingPrice;
    }

    public String getSymbol() {
        return _symbol;
    }

    public void setSymbol(String symbol) {
        this._symbol = symbol;
    }

    public void setImagePath(String imagePath) {
        this._imagePath = imagePath;
    }

    public String getImagePath() {
        return this._imagePath;
    }
}
