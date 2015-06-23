package info.akkuma.httpbenchmark;

import android.os.AsyncTask;

import info.akkuma.httpbenchmark.common.BenchmarkWorker;

/**
 * Created by akkuma on 2015/06/23.
 */
public class BenchmarkAsyncTask extends AsyncTask<Void, Void, Long> {

    private BenchmarkWorker mWorker;
    private OnBenchmarkResultListener mListener;


    public BenchmarkAsyncTask(BenchmarkWorker worker, OnBenchmarkResultListener listener) {
        mWorker = worker;
        mListener = listener;
    }

    @Override
    protected Long doInBackground(Void... params) {
        try {
            return mWorker.doBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Long aLong) {
        if (mListener != null) {
            if (aLong == null) {
                mListener.onError();
            } else {
                mListener.onResult(aLong);
            }
        }
        mListener = null;
        mWorker = null;
    }

    public static interface OnBenchmarkResultListener {
        public void onResult(long time);
        public void onError();
    }
}
