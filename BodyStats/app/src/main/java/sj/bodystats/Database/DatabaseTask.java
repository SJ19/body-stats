package sj.bodystats.Database;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

import sj.bodystats.MainActivity;
import sj.bodystats.Utility;

/**
 * Created by Corsair on 10-6-2016.
 */
public class DatabaseTask extends AsyncTask<String, Void, String> {

    public static final String READ_QUERY_URL = "http://sjacobs.me/fitness/readquery.php";
    public static final String EXECUTE_QUERY_URL = "http://sjacobs.me/fitness/executequery.php";

    private String query;
    private QueryTypes queryType;
    private Activity activity;
    private boolean enableButtons;

    public DatabaseTask(Activity activity, String query, QueryTypes queryType, boolean enableButtons) {
        this.activity = activity;
        this.query = query;
        this.queryType = queryType;
        this.enableButtons = enableButtons;
    }

    public DatabaseTask(Activity activity, Queries query) {
        this(activity, query.getQuery(), query.getQueryType(), false);
    }

    public DatabaseTask(Activity activity, String query, QueryTypes queryType) {
        this(activity, query, queryType, false);
    }

    public DatabaseTask(Activity activity, Queries query, boolean enableButtons) {
        this(activity, query.getQuery(), query.getQueryType(), enableButtons);
    }

    @Override
    protected String doInBackground(String... params) {
        return readQuery(query);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("app", result);

        // Enable buttons after 4 seconds.
        if (enableButtons) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    ((MainActivity) activity).setWeightInputEnabled(true);
                }
            }, 4000);
        }
    }

    private String readQuery(String query) {
        Utility.println("QUERY: " + query);
        String response = "";
        try {
            URL url;
            if (queryType == QueryTypes.EXECUTE) {
                url = new URL(EXECUTE_QUERY_URL);
            } else {
                url = new URL(READ_QUERY_URL);
            }
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            OutputStream os = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bufferedWriter.write(urlEncode(query));
            bufferedWriter.flush();
            bufferedWriter.close();
            os.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
            String line = "";

            // Get php response
            while ((line = bufferedReader.readLine()) != null) {
                response += "\n" + line;
            }
            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            httpURLConnection.disconnect();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    private String urlEncode(String query) {
        String result = "";
        try {
            result += URLEncoder.encode("query", "UTF-8") + "=" + URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }
}
