package info.akkuma.httpbenchmark.common;

import com.koushikdutta.ion.Ion;

import java.net.URL;

/**
 * Created by akkuma on 2015/06/23.
 */
public class IonWorker extends BenchmarkWorker {
    @Override
    protected String execute(URL url) throws Exception {
        return Ion.with(getContext())
                .load(url.toString())
                .asString()
                .get();
    }
}
