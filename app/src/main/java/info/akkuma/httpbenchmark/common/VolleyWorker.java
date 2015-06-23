package info.akkuma.httpbenchmark.common;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.net.URL;
import java.util.concurrent.CountDownLatch;

/**
 * Created by akkuma on 2015/06/23.
 */
public class VolleyWorker extends BenchmarkWorker {

    private RequestQueue mQueue;
    private CountDownLatch mLatch;
    private boolean mIsError;

    @Override
    protected void onPrepare() {
        super.onPrepare();
        mQueue = Volley.newRequestQueue(getContext());
        mLatch = new CountDownLatch(QUEUE_COUNT);
        mIsError = false;
    }

    @Override
    public long doBenchmark() throws Exception {
        onPrepare();

        long start = System.currentTimeMillis();
        start(getUrl());
        mLatch.await();
        long end = System.currentTimeMillis();

        if (mIsError) throw new Exception("error");
        if (isCancelled()) return Long.MAX_VALUE;

        return end - start;
    }

    private void start(URL url) {
        String urlStr = url.toString();
        for (int i = 0; i < QUEUE_COUNT; i++) {

            StringRequest request = new StringRequest(Request.Method.GET, urlStr, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    if (!getMockResponses().contains(response)) {
                        mIsError = true;
                    }
                    mLatch.countDown();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    mIsError = true;
                    mLatch.countDown();
                }
            });
            mQueue.add(request);
        }
    }

    @Override
    protected String execute(URL url) throws Exception {
        return null;
    }

    @Override
    public void cancel() {
        super.cancel();
        for (int i = 0; i < QUEUE_COUNT; i++) {
            mLatch.countDown();
        }
    }
}
