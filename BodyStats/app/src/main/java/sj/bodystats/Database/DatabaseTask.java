package sj.bodystats.Database;

import android.os.AsyncTask;
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

/**
 * Created by Corsair on 10-6-2016.
 */
public class DatabaseTask extends AsyncTask<String, Void, String> {

    public static final String READ_QUERY_URL = "http://sjacobs.me/stronk/readquery.php";
    public static final String EXECUTE_QUERY_URL = "http://sjacobs.me/stronk/executequery.php";

    private String query;
    private QueryTypes queryType;

    public DatabaseTask(String query, QueryTypes queryType) {
        this.query = query;
        this.queryType = queryType;
    }

    public DatabaseTask(Queries query) {
        this.query = query.getQuery();
        this.queryType = query.getQueryType();
    }

    @Override
    protected String doInBackground(String... params) {
        return readQuery(query);
    }

    @Override
    protected void onPostExecute(String result) {
        Log.d("app", result);
    }

    private String readQuery(String query) {
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
