package info.akkuma.httpbenchmark.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by akkuma on 2015/06/23.
 */
public class HttpURLConnectionWorker extends BenchmarkWorker {

    @Override
    protected String execute(URL url) throws Exception {

        HttpURLConnection con = (HttpURLConnection) url.openConnection();

        String body = null;

        InputStream stream = null;
        try {
            stream = con.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
            StringBuilder builder = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
            body = builder.toString();

        } catch (IOException e) {
            throw new Exception(e);
        } finally {
            if (stream != null) stream.close();
            con.disconnect();
        }

        return body;
    }
}
