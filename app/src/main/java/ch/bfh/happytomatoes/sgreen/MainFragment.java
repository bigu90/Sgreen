package ch.bfh.happytomatoes.sgreen;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


/**
 * Created by Matthias on 29.04.2016.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private DBtoMySQLconnection dbUpdate;
    //private static String ORDER_BY = "time" + " DESC";
    private DBHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ListView listView;

        public MainFragment() {
            // Required empty public constructor
        }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        listView = (ListView) getView().findViewById(R.id.list_sensors);


        dbHelper = new DBHelper(getActivity());
        dbUpdate = new DBtoMySQLconnection(getActivity(), dbHelper);

        swipeRefreshLayout = (SwipeRefreshLayout) getView().findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);
        dbUpdate.getDataFromServer();
    }

    @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_main, container, false);



        }


    protected void onStart(Bundle savedInstanceState) {

    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();

    }


    /**
     * Shows a List with every available sensor and the Data
     */
    private void showEvents() {
        final Cursor cursor = dbHelper.getTemperature();

        String[] fromFields = new String[] {"name", "value", "time"};
        int[] to = new int[] {R.id.sensor_name, R.id.temp_text, R.id.sensor_informations};
        adapter = new SimpleCursorAdapter(getActivity(), R.layout.list_field, cursor, fromFields, to, 0);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3)
            {
                //int id = (int)((SimpleCursorAdapter) adapter.getAdapter()).getCursor().getLong(0);
                Intent intent = new Intent(getActivity().getApplicationContext(), ChartActivity.class);

                intent.putExtra("sensorID", adapter.getItemIdAtPosition(position));
                startActivity(intent);
            }
        });

    }



    public void refresh(View view){
        onResume();
    }


    public void fetchData(){
        new Runnable() {
            @Override
            public void run() {
                dbUpdate.getDataFromServer();
                if(adapter != null){
                    adapter.notifyDataSetChanged();
                }
                swipeRefreshLayout.setRefreshing(false);


            }
        }.run();
        showEvents();
    }

    /**
     * This method is called when swipe refresh is pulled down
     */
    @Override
    public void onRefresh() {
        fetchData();

    }
    }
