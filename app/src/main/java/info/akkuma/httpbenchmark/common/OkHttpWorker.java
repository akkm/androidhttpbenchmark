package info.akkuma.httpbenchmark.common;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.net.URL;

/**
 * Created by akkuma on 2015/06/23.
 */
public class OkHttpWorker extends BenchmarkWorker {

    private OkHttpClient mClient;

    @Override
    protected void onPrepare() {
        super.onPrepare();
        mClient = new OkHttpClient();
    }

    @Override
    protected String execute(URL url) throws Exception{
        Request request = new Request.Builder().url(url).get().build();
        Response response = mClient.newCall(request).execute();
        String body = response.body().string();
        return body;
    }
}
