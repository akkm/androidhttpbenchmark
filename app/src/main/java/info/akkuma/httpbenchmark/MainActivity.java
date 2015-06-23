package info.akkuma.httpbenchmark;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import info.akkuma.httpbenchmark.common.BenchmarkItems;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ArrayAdapter<BenchmarkItems> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<BenchmarkItems>(this, android.R.layout.simple_list_item_1, BenchmarkItems.values());
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, BenchmarkActivity.class);
        intent.putExtra(BenchmarkActivity.EXTRA_BENCHMARK_ID, mAdapter.getItem(position).getId());
        startActivity(intent);
    }
}
