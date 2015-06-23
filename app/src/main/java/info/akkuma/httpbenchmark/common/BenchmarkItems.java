package info.akkuma.httpbenchmark.common;

/**
 * Created by akkuma on 2015/06/23.
 */
public enum BenchmarkItems {

    DEFAULT_HTTP_CLIENT(0, "DefaultHttpClient", DefaultHttpClientWorker.class),
    HTTP_URL_CONNECTION(1, "HttpURLConnection", HttpURLConnectionWorker.class),
    OK_HTTP(2, "OkHttp 2.4.0", OkHttpWorker.class),
    ION(3, "Ion", IonWorker.class),
    VOLLEY(4, "Volley", VolleyWorker.class);

    private int id;
    private String name;
    private Class<? extends BenchmarkWorker> clazz;

    BenchmarkItems(int id, String name, Class<? extends BenchmarkWorker> clazz) {
        this.id = id;
        this.name = name;
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Class<? extends BenchmarkWorker> getWorkerClass() {
        return clazz;
    }

    @Override
    public String toString() {
        return name;
    }

    public static BenchmarkItems findItemById(int id) {
        for (BenchmarkItems item : values()) {
            if (item.id == id) return item;
        }

        return null;
    }
}
