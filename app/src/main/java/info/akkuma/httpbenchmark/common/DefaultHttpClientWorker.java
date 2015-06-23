package info.akkuma.httpbenchmark.common;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.net.URL;

/**
 * Created by akkuma on 2015/06/23.
 */
public class DefaultHttpClientWorker extends BenchmarkWorker {

    private HttpClient mClient;

    @Override
    protected void onPrepare() {
        super.onPrepare();
        mClient = new DefaultHttpClient();
    }

    @Override
    protected String execute(URL url) throws Exception{
        HttpGet httpGet = new HttpGet(url.toURI());
        HttpResponse response = mClient.execute(httpGet);
        String body = EntityUtils.toString(response.getEntity());
        return body;
    }
}
