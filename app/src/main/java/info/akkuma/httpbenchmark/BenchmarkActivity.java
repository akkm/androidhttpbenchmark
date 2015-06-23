package info.akkuma.httpbenchmark;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import info.akkuma.httpbenchmark.common.BenchmarkItems;
import info.akkuma.httpbenchmark.common.BenchmarkWorker;


public class BenchmarkActivity extends AppCompatActivity implements BenchmarkAsyncTask.OnBenchmarkResultListener {

    public static final String EXTRA_BENCHMARK_ID = "info.akkuma.httpbenchmark.BenchmarkActivity.EXTRA_BENCHMARK_ID";

    private BenchmarkWorker mWorker;
    private int count = 0;
    private static final int PASS = 5;

    private ArrayAdapter<String> mAdapter;
    private List<Long> mResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_benchmark);

        int id = getIntent().getIntExtra(EXTRA_BENCHMARK_ID, -1);
        if (id == -1) {
            finish();
            return;
        }

        BenchmarkItems item = BenchmarkItems.findItemById(id);
        try {
            mWorker = item.getWorkerClass().newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
            finish();
            return;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            finish();
            return;
        }

        ListView listView = (ListView) findViewById(R.id.listView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mAdapter);

        Button button = (Button) findViewById(R.id.button);
        button.setText(item.getName());
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setEnabled(false);
                benchmark();
            }
        });

        Thread th = new Thread(){
            @Override
            public void run() {
                try {
                    mWorker.onCreate(getApplicationContext());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        th.start();
    }

    private void benchmark() {
        if (count >= PASS) {
            addResult();
            return;
        }
        count++;
        new BenchmarkAsyncTask(mWorker, this).execute();
    }

    private void addResult() {
        long best = Long.MAX_VALUE;
        for (Long l : mResults) {
            best = Math.min(best, l);
        }
        if (best != Long.MAX_VALUE) {
            mAdapter.add(getString(R.string.benchmark_result, best));
        }

        Button button = (Button) findViewById(R.id.button);
        button.setEnabled(true);
    }

    @Override
    public void onResult(long time) {
        mAdapter.add(getString(R.string.benchmark_ms, time));
        mResults.add(time);
        benchmark();
    }

    @Override
    public void onError() {
        mAdapter.add(getString(R.string.benchmark_error));

        Button button = (Button) findViewById(R.id.button);
        button.setEnabled(true);
    }

    @Override
    protected void onDestroy() {
        try {
            mWorker.onDestroy();
        } catch (IOException e) {
            e.printStackTrace();
        }
        super.onDestroy();
    }
}
