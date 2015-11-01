/**
 * Created by Spencer on 10/31/15.
 *
 *
 * Learned: In completing this exercise I learned about HTTP POST and GET requests.
 *
 */
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.MalformedURLException;

import org.joda.time.DateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class main{

    final static String tok = "{\"token\":\"LR7z3Zyq3Q\"}";

    public static void main(String[] args) {

        String myToken = "{\"email\":\"srothsch@ucsd.edu\",\"github\":\"https://github.com/SpencerRothschild/code2040\"}";

        try {

            //StringBuffer token =
            BufferedReader buf = getToken("http://challenge.code2040.org/api/register", myToken);
            String token = token(buf);

            buf = getToken("http://challenge.code2040.org/api/getstring", tok);
            reverse(buf);

            buf = getToken("http://challenge.code2040.org/api/haystack", tok);
            needle(buf);

            buf = getToken("http://challenge.code2040.org/api/prefix", tok);
            prefix(buf);

            buf = getToken("http://challenge.code2040.org/api/time", tok);
            theDate(buf);

            buf = getToken("http://challenge.code2040.org/api/status", tok);
            System.out.println(buf.readLine().toString());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static BufferedReader getToken(String url, String token) throws IOException {

        BufferedReader br = null;
        try{
            URL obj = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setDoOutput(true);

            OutputStream os = connection.getOutputStream();
            os.write(token.getBytes());

            br = new BufferedReader(new InputStreamReader((connection.getInputStream())));

            os.flush();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return br;
    }


    public static String token(BufferedReader buf) throws IOException, JSONException {

        JSONObject ob = new JSONObject(buf.readLine());
        String ret = ob.getString("result");
        return ret;
    }

    public static void reverse(BufferedReader buf) throws JSONException, IOException {
        JSONObject ob = new JSONObject(buf.readLine());
        String sub = ob.getString("result");

        String s = new StringBuffer(sub).reverse().toString();

        JSONObject post = new JSONObject();

        post.put("token", "LR7z3Zyq3Q");
        post.put("string", s);

        buf = getToken("http://challenge.code2040.org/api/validatestring", post.toString());

        System.out.println("after post: " + buf.readLine());
    }

    public static void needle(BufferedReader buf) throws IOException, JSONException {

        JSONObject ob = new JSONObject(buf.readLine());
        JSONObject res = ob.getJSONObject("result");
        JSONArray array = res.getJSONArray("haystack");
        String n = res.getString("needle");

        int ret = 0;
        for(ret = 0; ret < array.length(); ret++){
            if(array.getString(ret).equals(n)){
                break;
            }
        }

        JSONObject post = new JSONObject();
        post.put("token", "LR7z3Zyq3Q");
        post.put("needle", ret);

        buf = getToken("http://challenge.code2040.org/api/validateneedle", post.toString());

        System.out.println("after post: " + buf.readLine());
    }

    public static void prefix(BufferedReader buf) throws IOException, JSONException{

        JSONObject ob = new JSONObject(buf.readLine());
        JSONObject res = ob.getJSONObject("result");
        JSONArray array = res.getJSONArray("array");
        String pre = res.getString("prefix");
        JSONArray ret = new JSONArray();

        for(int i = 0; i < array.length(); i++){
            if(!array.getString(i).startsWith(pre)){
                ret.put(array.getString(i));
            }
        }

        JSONObject post = new JSONObject();

        post.put("token", "LR7z3Zyq3Q");
        post.put("array", ret);

        buf = getToken("http://challenge.code2040.org/api/validateprefix", post.toString());

        System.out.println("after post: " + buf.readLine());
    }

    public static void theDate(BufferedReader buf) throws IOException, JSONException{

        JSONObject ob = new JSONObject(buf.readLine());
        JSONObject res = ob.getJSONObject("result");
        String date = res.getString("datestamp");
        int interval = res.getInt("interval");
        DateTime time = DateTime.parse(date);
        time = time.plusSeconds(interval);

        JSONObject post = new JSONObject();

        post.put("token", "LR7z3Zyq3Q");
        post.put("datestamp", time);

        buf = getToken("http://challenge.code2040.org/api/validatetime", post.toString());

        System.out.println("after post: " + buf.readLine());
    }

}
