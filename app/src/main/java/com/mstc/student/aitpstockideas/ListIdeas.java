package com.mstc.student.aitpstockideas;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

/**
 * Created by Ben on 3/11/2016.
 */
public class ListIdeas extends Fragment {
    ListView tournamentListView;
    Context context;
    MyDBHandler listhandler;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.list_ideas, container, false);

        listhandler = new MyDBHandler(v.getContext(), null, null, 1);


        tournamentListView = (ListView)v.findViewById(R.id.ideaListView);

                String[] tournamentColumns = new  String [] {"ideasymbol", "ideastartingprice", "ideaimage"};

        int[]  arrayViewIDs = new int[]{R.id.ideaSymbolTextView, R.id.ideaStartingPriceTextView, R.id.listviewIV};



        Cursor cursor;
        cursor = listhandler.queueAllIdeas();
        getActivity().startManagingCursor(cursor);

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(getActivity(), R.layout.listview_layout, cursor,tournamentColumns,arrayViewIDs);
        tournamentListView.setAdapter(adapter);
        return v;
    }
    public static ListIdeas newInstance(){return new ListIdeas();}
}
