package ch.bfh.happytomatoes.sgreen;

/**
 * Created by Matthias on 31.05.2016.
 */

    import android.content.Intent;
    import android.database.Cursor;
    import android.os.Bundle;
    import android.support.annotation.Nullable;
    import android.support.v4.app.Fragment;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.AdapterView;
    import android.widget.ListView;
    import android.widget.SimpleCursorAdapter;


public class Sensor_Fragment extends Fragment {
    private DBHelper dbHelper;
    private SimpleCursorAdapter adapter;
    private ListView listView;

    public Sensor_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        dbHelper = new DBHelper(getActivity());
        listView = (ListView) getView().findViewById(R.id.sensorList);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sensor, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        showSensors();

    }

    private void showSensors() {
        Cursor cursor = dbHelper.getSensors();
        String[] fromFields = new String[]{"name"};
        int[] to = new int[]{android.R.id.text1};
        adapter = new SimpleCursorAdapter(getActivity(), android.R.layout.simple_list_item_1, cursor, fromFields, to, 0);


        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                //String value = (String)adapter.getItemAtPosition(position);
                int id = (int) adapter.getItemIdAtPosition(position);

                Intent intent = new Intent(getActivity().getApplicationContext(), SensorMenu.class);

                intent.putExtra("sensorID", id);
                startActivity(intent);
            }
        });
    }
}