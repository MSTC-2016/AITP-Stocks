package com.mstc.student.aitpstockideas;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Ben on 3/12/2016.
 */
public class HomeScreen extends Fragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.home_screen, container, false);
        Button newStockButton, listStocksButton;

        newStockButton = (Button) v.findViewById(R.id.newStockButton);
        newStockButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newStockEntry = new Intent(v.getContext(), NewStock.class);
                startActivity(newStockEntry);
            }
        });
        return v;
    }
    public static HomeScreen newInstance(){return new HomeScreen();}
}
