package info.akkuma.httpbenchmark.common;

import android.content.Context;

import com.squareup.okhttp.mockwebserver.MockResponse;
import com.squareup.okhttp.mockwebserver.MockWebServer;

import org.apache.commons.lang.RandomStringUtils;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by akkuma on 2015/06/23.
 */
public abstract class BenchmarkWorker {

    private static final String TEST_URL_PATH = "/test";
    private static final int RANDOM_TEXT_LENGTH = 1000;
    protected static final int QUEUE_COUNT = 100;

    private MockWebServer mMockWebServer;
    private boolean mInitialized = false;
    private List<String> mMockResponses = new ArrayList<>(QUEUE_COUNT);
    private Context mContext;
    private boolean isCancelled;

    public BenchmarkWorker() {
        mMockWebServer = new MockWebServer();
    }

    public void onCreate(Context context) throws IOException {
        mInitialized = true;
        mMockWebServer.start();
        Logger.getLogger(MockWebServer.class.getName()).setLevel(Level.OFF);
        mContext = context;
        isCancelled = false;
    }

    public boolean isInitialized() {
        return mInitialized;
    }

    public void onDestroy() throws IOException {
        cancel();
        mMockWebServer.shutdown();
        mContext = null;
    }

    public long doBenchmark() throws Exception{
        onPrepare();

        URL url = getUrl();

        long start = System.currentTimeMillis();
        for (int i = 0; i < QUEUE_COUNT; i++) {
            if (isCancelled) return Long.MAX_VALUE;
            String expect = mMockResponses.get(i);
            String body = execute(url);
            if (body == null) throw new Exception();
            if (!mMockResponses.contains(body)) throw new Exception("wrong response: " + expect + ", " + body);
        }
        long end = System.currentTimeMillis();

        return end - start;
    }

    protected void onPrepare() {
        mMockResponses.clear();
        for (int i = 0; i < QUEUE_COUNT; i++) {
            String mockResponse = RandomStringUtils.randomAlphanumeric(RANDOM_TEXT_LENGTH);
            mMockWebServer.enqueue(new MockResponse().setBody(mockResponse));
            mMockResponses.add(mockResponse);
        }
    }

    protected URL getUrl() {
        return mMockWebServer.getUrl(TEST_URL_PATH);
    }

    protected abstract String execute(URL url) throws Exception;

    public Context getContext() {
        return mContext;
    }

    public List<String> getMockResponses() {
        return mMockResponses;
    }

    protected MockWebServer getMockWebServer() {
        return mMockWebServer;
    }

    public boolean isCancelled() {
        return isCancelled;
    }

    public void cancel() {
        isCancelled = true;
    }
}
