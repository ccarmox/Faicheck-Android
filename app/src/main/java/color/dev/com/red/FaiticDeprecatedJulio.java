/*
 * Copyright (c) 2017. Cristian Do Carmo Rodriguez
 */


package color.dev.com.red;

import android.util.Log;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.zip.GZIPInputStream;

public class FaiticDeprecatedJulio implements Serializable {


    private static final String urlMain = "http://faitic.uvigo.es/index.php/es/";
    private static final String urlSubjects = "http://faitic.uvigo.es/index.php/es/materias";
    private static CookieManager cookieManager;
    public static Logger logger;

    private static boolean cCancelDownload = false;
    private static Semaphore sCancelDownload = new Semaphore(1);

    public FaiticDeprecatedJulio(boolean verbose) {
        toDoAtStartup(verbose);
    }


    private static void toDoAtStartup(boolean verbose) {

        Log.v("NAITSIRC", "2");
        startCookieSession();
        Log.v("NAITSIRC", "3");
        logger = new Logger(verbose);

    }

    protected static boolean getCancelDownload() {

        Log.v("NAITSIRC", "9");
        try {

            Log.v("NAITSIRC", "11");
            sCancelDownload.acquire();
            Log.v("NAITSIRC", "12");
            boolean out = cCancelDownload;
            Log.v("NAITSIRC", "13");
            sCancelDownload.release();

            Log.v("NAITSIRC", "15");
            return out;

        } catch (Exception ex) {

            Log.v("NAITSIRC", "19");
            // Weird. Stop the download just in case

            Log.v("NAITSIRC", "21");
            ex.printStackTrace();
            Log.v("NAITSIRC", "22");
            return true;

        }

    }

    protected static void setCancelDownload(boolean value) {

        Log.v("NAITSIRC", "30");
        try {

            Log.v("NAITSIRC", "32");
            sCancelDownload.acquire();
            Log.v("NAITSIRC", "33");
            cCancelDownload = value;
            Log.v("NAITSIRC", "34");
            sCancelDownload.release();

            Log.v("NAITSIRC", "36");
        } catch (Exception ex) {

            Log.v("NAITSIRC", "38");
            ex.printStackTrace();

        }

    }

    private static void startCookieSession() {

        Log.v("NAITSIRC", "46");
        cookieManager = new CookieManager();
        Log.v("NAITSIRC", "47");
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
        Log.v("NAITSIRC", "48");
        cookieManager.getCookieStore();//.removeAll();

        Log.v("NAITSIRC", "50");
        CookieHandler.setDefault(cookieManager);


    }


    public static String lastRequestedURL = "";

    public static String requestDocument(String strurl, String post) throws Exception {

        Log.v("NAITSIRC", "60");
        CookieHandler.setDefault(cookieManager);

        Log.v("NAITSIRC", "62");
        lastRequestedURL = strurl;

        Log.v("NAITSIRC", "64");
        logger.log(Logger.INFO, "Requesting URL: " + strurl);
        Log.v("NAITSIRC", "65");
        logger.log(Logger.INFO, "Post data: " + post);

        Log.v("NAITSIRC", "67");
        logger.log(Logger.INFO, "--- Creating connection ---");

        Log.v("NAITSIRC", "69");
        URL url = new URL(adaptURL(lastRequestedURL, strurl));

        Log.v("NAITSIRC", "71");
        List<HttpCookie> cookiesAssoc = cookieManager.getCookieStore().get(url.toURI());
        Log.v("NAITSIRC", "72");
        String cookiesAssocStr = "";

        Log.v("NAITSIRC", "74");
        for (HttpCookie cookieAssoc : cookiesAssoc) {

            Log.v("NAITSIRC", "76");
            cookiesAssocStr += (cookiesAssocStr.length() > 0 ? "; " : "") + cookieAssoc.getName() + "=" + cookieAssoc.getValue();

        }

        Log.v("NAITSIRC", "80");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        //NUEVO
        Log.v("NAITSIRC", "83");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        Log.v("NAITSIRC", "84");
        Log.v("1", "3");
        Log.v("NAITSIRC", "85");
        // Time out settings
        Log.v("NAITSIRC", "86");
        connection.setConnectTimeout(10000);
        Log.v("NAITSIRC", "87");
        connection.setReadTimeout(10000);

        Log.v("NAITSIRC", "89");
        connection.setDoOutput(true);
        Log.v("NAITSIRC", "90");
        connection.setInstanceFollowRedirects(false);
        Log.v("NAITSIRC", "91");
        connection.setUseCaches(false);

        Log.v("NAITSIRC", "93");
        connection.setRequestProperty("Accept-Encoding", "gzip");

        Log.v("NAITSIRC", "95");
        if (post.length() > 0) {
            Log.v("NAITSIRC", "96");
            connection.setRequestMethod("POST");
            Log.v("NAITSIRC", "97");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.v("NAITSIRC", "98");
            connection.setRequestProperty("charset", "utf-8");
            Log.v("NAITSIRC", "99");
            connection.setRequestProperty("Content-Length", "" + post.length());
        }

        Log.v("NAITSIRC", "102");
        if (cookiesAssocStr.length() > 0) {
            Log.v("NAITSIRC", "103");
            connection.setRequestProperty("Cookie", cookiesAssocStr);
            Log.v("NAITSIRC", "104");
            logger.log(Logger.INFO, "Cookies: " + cookiesAssocStr);
        }

        Log.v("NAITSIRC", "107");
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        Log.v("NAITSIRC", "108");
        writer.write(post.getBytes(StandardCharsets.UTF_8));

        Log.v("NAITSIRC", "110");
        logger.log(Logger.INFO, "--- Petition sent. Reading ---");

        Log.v("NAITSIRC", "112");
        StringBuffer output = new StringBuffer();
        Log.v("NAITSIRC", "113");
        InputStream reader;

        Log.v("NAITSIRC", "115");
        if (connection.getContentEncoding().equals("gzip")) {

            Log.v("NAITSIRC", "117");
            reader = new GZIPInputStream(connection.getInputStream());
            Log.v("NAITSIRC", "118");
            logger.log(Logger.INFO, " + GZIP ENCODED");

        } else {

            Log.v("NAITSIRC", "123");
            reader = connection.getInputStream();

        }

        Log.v("NAITSIRC", "127");
        byte[] temp = new byte[1000];
        Log.v("NAITSIRC", "128");
        int read = reader.read(temp);

        Log.v("NAITSIRC", "130");
        int counter = 0;

        Log.v("NAITSIRC", "132");
        while (read != -1) {
            Log.v("NAITSIRC", "133");
            output.append(new String(temp, 0, read, StandardCharsets.UTF_8));
            Log.v("NAITSIRC", "134");
            read = reader.read(temp);
            Log.v("NAITSIRC", "135");
            counter += read;

        }

        Log.v("NAITSIRC", "139");
        reader.close();

        Log.v("NAITSIRC", "141");
        int status = connection.getResponseCode();

        Log.v("NAITSIRC", "143");
        String headerName;

        Log.v("NAITSIRC", "145");
        for (int i = 1; (headerName = connection.getHeaderFieldKey(i)) != null; i++) {

            Log.v("NAITSIRC", "147");
            if (headerName.toLowerCase().equals("set-cookie")) {

                Log.v("NAITSIRC", "149");
                String cookiesToSet = connection.getHeaderField(i);

                Log.v("NAITSIRC", "151");
                for (String cookieToSet : cookiesToSet.split(";")) {

                    Log.v("NAITSIRC", "153");
                    String[] cookieParameters = cookieToSet.split("=");


                    Log.v("NAITSIRC", "157");
                    if (cookieParameters[0].contains("path") || cookieParameters[0].contains("expire")) {
                        Log.v("NAITSIRC", "158");
                        Log.v("GALLETA NO CARGADA", cookieParameters[1]);
                        Log.v("NAITSIRC", "159");
                    } else {

                        Log.v("NAITSIRC", "161");
                        HttpCookie galleta = new HttpCookie(cookieParameters[0].trim(), cookieParameters[1].trim());
                        Log.v("NAITSIRC", "162");
                        galleta.setPath("/");

                        Log.v("NAITSIRC", "164");
                        cookieManager.getCookieStore().add((url).toURI(), galleta);

                        Log.v("NAITSIRC", "166");
                        logger.log(Logger.INFO, " + Adding cookie \"" + cookieToSet + "\" to uri \"" + (url).toURI().toString() + "\".");

                    }
                }


            }

        }

        Log.v("NAITSIRC", "176");
        List<HttpCookie> cookiesAssoc2 = cookieManager.getCookieStore().get(url.toURI());
        Log.v("NAITSIRC", "177");
        String cookiesAssocStr2 = "";

        Log.v("NAITSIRC", "179");
        for (HttpCookie cookieAssoc2 : cookiesAssoc2) {

            Log.v("NAITSIRC", "181");
            cookiesAssocStr2 += (cookiesAssocStr2.length() > 0 ? "; " : "") + cookieAssoc2.getName() + "=" + cookieAssoc2.getValue();

        }

        Log.v("NAITSIRC", "185");
        Log.v("COOKIES DE DESPUES", "==>" + cookiesAssocStr2);


        Log.v("NAITSIRC", "189");
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_SEE_OTHER) {


            Log.v("NAITSIRC", "194");
            logger.log(Logger.INFO, "--- Redirected ---");

            Log.v("NAITSIRC", "196");
            return requestDocument(adaptURL(lastRequestedURL, connection.getHeaderField("Location")), "");


        } else {

            Log.v("NAITSIRC", "203");
            logger.log(Logger.INFO, "--- Request finished ---\n");

            Log.v("NAITSIRC", "205");
            return output.toString();

        }

    }

    public static void downloadFile(String strurl, String post, String filename) throws Exception {

        Log.v("NAITSIRC", "213");
        if (getCancelDownload()) return;    // Download cancelled, don't dare to continue

        Log.v("NAITSIRC", "215");
        System.out.println(" -- Downloading file from \"" + strurl + "\" and saving to \"" + filename + "\"...");

        Log.v("NAITSIRC", "217");
        lastRequestedURL = strurl;

        Log.v("NAITSIRC", "219");
        logger.log(Logger.INFO, "Requesting URL: " + strurl);
        Log.v("NAITSIRC", "220");
        logger.log(Logger.INFO, "Post data: " + post);

        Log.v("NAITSIRC", "222");
        logger.log(Logger.INFO, "--- Creating connection ---");

        Log.v("NAITSIRC", "224");
        URL url = new URL(strurl);

        Log.v("NAITSIRC", "226");
        List<HttpCookie> cookiesAssoc = cookieManager.getCookieStore().get(url.toURI());
        Log.v("NAITSIRC", "227");
        String cookiesAssocStr = "";

        Log.v("NAITSIRC", "229");
        for (HttpCookie cookieAssoc : cookiesAssoc) {

            Log.v("NAITSIRC", "231");
            cookiesAssocStr += (cookiesAssocStr.length() > 0 ? "; " : "") + cookieAssoc.getName() + "=" + cookieAssoc.getValue();

        }

        Log.v("NAITSIRC", "235");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//NUEVO
        Log.v("NAITSIRC", "237");
        //CookieHandler.setDefault(cookieManager);

        Log.v("NAITSIRC", "239");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        Log.v("NAITSIRC", "240");
        Log.v("1", "3");
        Log.v("NAITSIRC", "241");
        // Time out settings
        Log.v("NAITSIRC", "242");
        connection.setConnectTimeout(10000);
        Log.v("NAITSIRC", "243");
        connection.setReadTimeout(10000);

        Log.v("NAITSIRC", "245");
        connection.setDoOutput(true);
        Log.v("NAITSIRC", "246");
        connection.setInstanceFollowRedirects(false);
        Log.v("NAITSIRC", "247");
        connection.setUseCaches(false);

        Log.v("NAITSIRC", "249");
        if (post.length() > 0) {
            Log.v("NAITSIRC", "250");
            connection.setRequestMethod("POST");
            Log.v("NAITSIRC", "251");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.v("NAITSIRC", "252");
            connection.setRequestProperty("charset", "utf-8");
            Log.v("NAITSIRC", "253");
            connection.setRequestProperty("Content-Length", "" + post.length());
        }

        Log.v("NAITSIRC", "256");
        if (cookiesAssocStr.length() > 0) {
            Log.v("NAITSIRC", "257");
            connection.setRequestProperty("Cookie", cookiesAssocStr);
            Log.v("NAITSIRC", "258");
            logger.log(Logger.INFO, "Cookies: " + cookiesAssocStr);
        }

        Log.v("NAITSIRC", "261");
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        Log.v("NAITSIRC", "262");
        writer.write(post.getBytes(StandardCharsets.UTF_8));

        Log.v("NAITSIRC", "264");
        logger.log(Logger.INFO, "--- Petition sent. Reading ---");

        Log.v("NAITSIRC", "266");
        // Check cookies and if the document redirects

        Log.v("NAITSIRC", "268");
        int status = connection.getResponseCode();

        Log.v("NAITSIRC", "270");
        String headerName;

        Log.v("NAITSIRC", "272");
        for (int i = 1; (headerName = connection.getHeaderFieldKey(i)) != null; i++) {

            Log.v("NAITSIRC", "274");
            if (headerName.toLowerCase().equals("set-cookie")) {

                Log.v("NAITSIRC", "276");
                String cookiesToSet = connection.getHeaderField(i);

                Log.v("NAITSIRC", "278");
                for (String cookieToSet : cookiesToSet.split(";")) {

                    Log.v("NAITSIRC", "280");
                    String[] cookieParameters = cookieToSet.split("=");

                    Log.v("NAITSIRC", "282");
                    //CookieHandler.setDefault(null);


                    Log.v("NAITSIRC", "285");
                    if (cookieParameters[0].contains("path") || cookieParameters[0].contains("expire")) {
                        Log.v("NAITSIRC", "286");
                        Log.v("GALLETA NO CARGADA", cookieParameters[1]);
                        Log.v("NAITSIRC", "287");
                    } else {

                        Log.v("NAITSIRC", "289");
                        HttpCookie galleta = new HttpCookie(cookieParameters[0].trim(), cookieParameters[1].trim());
                        Log.v("NAITSIRC", "290");
                        galleta.setPath("/");

                        Log.v("NAITSIRC", "292");
                        cookieManager.getCookieStore().add(url.toURI(), galleta);

                        Log.v("NAITSIRC", "294");
                        logger.log(Logger.INFO, " + Adding cookie \"" + cookieToSet + "\" to uri \"" + url.toURI().toString() + "\".");

                    }
                }


            }

        }

        Log.v("NAITSIRC", "304");
        List<HttpCookie> cookiesAssoc2 = cookieManager.getCookieStore().get(url.toURI());
        Log.v("NAITSIRC", "305");
        String cookiesAssocStr2 = "";

        Log.v("NAITSIRC", "307");
        for (HttpCookie cookieAssoc2 : cookiesAssoc2) {

            Log.v("NAITSIRC", "309");
            cookiesAssocStr2 += (cookiesAssocStr2.length() > 0 ? "; " : "") + cookieAssoc2.getName() + "=" + cookieAssoc2.getValue();

        }

        Log.v("NAITSIRC", "313");
        Log.v("COOKIES DE DESPUES", "==>" + cookiesAssocStr2);

        Log.v("NAITSIRC", "315");
        // Does the document redirect?

        Log.v("NAITSIRC", "317");
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_SEE_OTHER) {


            Log.v("NAITSIRC", "322");
            logger.log(Logger.INFO, "--- Redirected ---");

            Log.v("NAITSIRC", "324");
            downloadFile(adaptURL(lastRequestedURL, connection.getHeaderField("Location")), "", filename);

            Log.v("NAITSIRC", "326");
            return;

        }

        Log.v("NAITSIRC", "330");
        // OK, the document doesn't redirect. Download it

        Log.v("NAITSIRC", "332");
        InputStream reader;    // Response document

        Log.v("NAITSIRC", "334");
        reader = connection.getInputStream();

        Log.v("NAITSIRC", "336");
        // Let's write the document

        Log.v("NAITSIRC", "338");
        FileOutputStream filewriter;

        Log.v("NAITSIRC", "340");
        int tempfilenumber = 1;
        Log.v("NAITSIRC", "341");
        String tempfilename = filename + ".tmp" + tempfilenumber;

        Log.v("NAITSIRC", "343");
        while (new File(tempfilename).exists()) {

            Log.v("NAITSIRC", "345");
            // Iterates until the file doesn't exist

            Log.v("NAITSIRC", "347");
            tempfilename = filename + ".tmp" + (++tempfilenumber);

        }

        Log.v("NAITSIRC", "351");
        logger.log(Logger.INFO, " + Saving temp as: " + tempfilename);

        Log.v("NAITSIRC", "353");
        filewriter = new FileOutputStream(tempfilename);

        Log.v("NAITSIRC", "355");
        byte[] temp;
        int read;

        Log.v("NAITSIRC", "357");
        try {

            Log.v("NAITSIRC", "359");
            temp = new byte[1000];
            Log.v("NAITSIRC", "360");
            read = reader.read(temp);

            Log.v("NAITSIRC", "362");
            while (read != -1 && !getCancelDownload()) {
                Log.v("NAITSIRC", "363");
                filewriter.write(temp, 0, read);

                Log.v("NAITSIRC", "365");
                read = reader.read(temp);

            }

            Log.v("NAITSIRC", "369");
            // Close the writers

            Log.v("NAITSIRC", "371");
            filewriter.close();
            Log.v("NAITSIRC", "372");
            reader.close();

            Log.v("NAITSIRC", "374");
            if (!getCancelDownload()) {

                Log.v("NAITSIRC", "376");
                // Success. Substitute the file

                Log.v("NAITSIRC", "378");
                logger.log(Logger.INFO, " + Renaming temp to: " + filename);

                Log.v("NAITSIRC", "380");
                File oldfile = new File(filename);
                Log.v("NAITSIRC", "381");
                File tempfile = new File(tempfilename);

                Log.v("NAITSIRC", "383");
                boolean deletingsuccess = true;

                Log.v("NAITSIRC", "385");
                if (oldfile.exists()) {
                    Log.v("NAITSIRC", "386");
                    if (!oldfile.isDirectory()) {

                        Log.v("NAITSIRC", "388");
                        deletingsuccess = oldfile.delete();

                        Log.v("NAITSIRC", "390");
                    } else {

                        Log.v("NAITSIRC", "392");
                        deletingsuccess = false;

                    }
                }

                Log.v("NAITSIRC", "397");
                if (deletingsuccess) {

                    Log.v("NAITSIRC", "399");
                    // Correctly deleted

                    Log.v("NAITSIRC", "401");
                    tempfile.renameTo(oldfile);

                    Log.v("NAITSIRC", "403");
                    System.out.println("Success.");


                }

                Log.v("NAITSIRC", "408");
            } else {

                Log.v("NAITSIRC", "410");
                logger.log(Logger.ERROR, "--- Download cancelled ---\n");

            }

            Log.v("NAITSIRC", "414");
        } catch (Exception ex) {

            Log.v("NAITSIRC", "416");
            ex.printStackTrace();

            Log.v("NAITSIRC", "418");
            try {
                filewriter.close();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }
            Log.v("NAITSIRC", "419");
            try {
                reader.close();
            } catch (Exception ex2) {
                ex2.printStackTrace();
            }

        }

        Log.v("NAITSIRC", "423");
        logger.log(Logger.INFO, "--- Request finished ---\n");


        Log.v("NAITSIRC", "426");
        return;

    }

    public static String getRedirectedURL(String strurl, String post) throws Exception {

        Log.v("NAITSIRC", "432");
        lastRequestedURL = strurl;

        Log.v("NAITSIRC", "434");
        logger.log(Logger.INFO, "Requesting URL: " + strurl);
        Log.v("NAITSIRC", "435");
        logger.log(Logger.INFO, "Post data: " + post);

        Log.v("NAITSIRC", "437");
        logger.log(Logger.INFO, "--- Creating connection ---");

        Log.v("NAITSIRC", "439");
        URL url = new URL(strurl);

        Log.v("NAITSIRC", "441");
        List<HttpCookie> cookiesAssoc = cookieManager.getCookieStore().get(url.toURI());
        Log.v("NAITSIRC", "442");
        String cookiesAssocStr = "";

        Log.v("NAITSIRC", "444");
        for (HttpCookie cookieAssoc : cookiesAssoc) {

            Log.v("NAITSIRC", "446");
            cookiesAssocStr += (cookiesAssocStr.length() > 0 ? "; " : "") + cookieAssoc.getName() + "=" + cookieAssoc.getValue();

        }

        Log.v("NAITSIRC", "450");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//NUEVO
        Log.v("NAITSIRC", "452");
        connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
        Log.v("NAITSIRC", "453");
        Log.v("1", "3");
        Log.v("NAITSIRC", "454");
        // Time out settings
        Log.v("NAITSIRC", "455");
        connection.setConnectTimeout(10000);
        Log.v("NAITSIRC", "456");
        connection.setReadTimeout(10000);

        Log.v("NAITSIRC", "458");
        connection.setDoOutput(true);
        Log.v("NAITSIRC", "459");
        connection.setInstanceFollowRedirects(false);
        Log.v("NAITSIRC", "460");
        connection.setUseCaches(false);

        Log.v("NAITSIRC", "462");
        connection.setRequestProperty("Accept-Encoding", "gzip");

        Log.v("NAITSIRC", "464");
        if (post.length() > 0) {
            Log.v("NAITSIRC", "465");
            connection.setRequestMethod("POST");
            Log.v("NAITSIRC", "466");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            Log.v("NAITSIRC", "467");
            connection.setRequestProperty("charset", "utf-8");
            Log.v("NAITSIRC", "468");
            connection.setRequestProperty("Content-Length", "" + post.length());
        }

        Log.v("NAITSIRC", "471");
        if (cookiesAssocStr.length() > 0) {
            Log.v("NAITSIRC", "472");
            connection.setRequestProperty("Cookie", cookiesAssocStr);
            Log.v("NAITSIRC", "473");
            logger.log(Logger.INFO, "Cookies: " + cookiesAssocStr);
        }

        Log.v("NAITSIRC", "476");
        DataOutputStream writer = new DataOutputStream(connection.getOutputStream());
        Log.v("NAITSIRC", "477");
        writer.write(post.getBytes(StandardCharsets.UTF_8));

        Log.v("NAITSIRC", "479");
        logger.log(Logger.INFO, "--- Petition sent. Waiting for redirecting info ---");

        Log.v("NAITSIRC", "481");
        int status = connection.getResponseCode();

        Log.v("NAITSIRC", "483");
        String headerName;

        Log.v("NAITSIRC", "485");
        // Getting cookies

        Log.v("NAITSIRC", "487");
        for (int i = 1; (headerName = connection.getHeaderFieldKey(i)) != null; i++) {

            Log.v("NAITSIRC", "489");
            if (headerName.toLowerCase().equals("set-cookie")) {

                Log.v("NAITSIRC", "491");
                String cookiesToSet = connection.getHeaderField(i);

                Log.v("NAITSIRC", "493");
                for (String cookieToSet : cookiesToSet.split(";")) {

                    Log.v("NAITSIRC", "495");
                    String[] cookieParameters = cookieToSet.split("=");

                    Log.v("NAITSIRC", "497");
                    //CookieHandler.setDefault(null);
                    Log.v("NAITSIRC", "498");
                    //CookieHandler.setDefault(cookieManager);


                    Log.v("NAITSIRC", "501");
                    if (cookieParameters[0].contains("path") || cookieParameters[0].contains("expire")) {
                        Log.v("NAITSIRC", "502");
                        Log.v("GALLETA NO CARGADA", cookieParameters[1]);
                        Log.v("NAITSIRC", "503");
                    } else {

                        Log.v("NAITSIRC", "505");
                        HttpCookie galleta = new HttpCookie(cookieParameters[0].trim(), cookieParameters[1].trim());
                        Log.v("NAITSIRC", "506");
                        galleta.setPath("/");

                        Log.v("NAITSIRC", "508");
                        cookieManager.getCookieStore().add(url.toURI(), galleta);

                        Log.v("NAITSIRC", "510");
                        logger.log(Logger.INFO, " + Adding cookie \"" + cookieToSet + "\" to uri \"" + url.toURI().toString() + "\".");

                    }
                }


            }

        }

        Log.v("NAITSIRC", "520");
        List<HttpCookie> cookiesAssoc2 = cookieManager.getCookieStore().get(url.toURI());
        Log.v("NAITSIRC", "521");
        String cookiesAssocStr2 = "";

        Log.v("NAITSIRC", "523");
        for (HttpCookie cookieAssoc2 : cookiesAssoc2) {

            Log.v("NAITSIRC", "525");
            cookiesAssocStr2 += (cookiesAssocStr2.length() > 0 ? "; " : "") + cookieAssoc2.getName() + "=" + cookieAssoc2.getValue();

        }

        Log.v("NAITSIRC", "529");
        Log.v("COOKIES DE DESPUES", "==>" + cookiesAssocStr2);


        Log.v("NAITSIRC", "533");
        // Return status, there will be the redirection

        Log.v("NAITSIRC", "535");
        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                || status == HttpURLConnection.HTTP_MOVED_PERM
                || status == HttpURLConnection.HTTP_SEE_OTHER) {


            Log.v("NAITSIRC", "540");
            logger.log(Logger.INFO, "--- Redirected. ---");

            Log.v("NAITSIRC", "542");
            String redURL = adaptURL(lastRequestedURL, connection.getHeaderField("Location"));

            Log.v("NAITSIRC", "544");
            logger.log(Logger.INFO, "URL: " + redURL);
            Log.v("NAITSIRC", "545");
            return redURL;


        } else {

            Log.v("NAITSIRC", "552");
            logger.log(Logger.INFO, "--- Request finished. Not redirected ---\n");

            Log.v("NAITSIRC", "554");
            return null;

        }

    }

    public static String generatePostLogin(String username, String password) throws Exception {

        StringBuffer output = new StringBuffer();

        String documentMain = requestDocument(urlMain, "");

        int formStart = documentMain.toLowerCase().indexOf("<form action=\"/index.php/es/\" method=\"post\" id=\"login-form\"");

        int formEnd = documentMain.toLowerCase().indexOf("</form>", formStart);

        // Form detected

        if (formStart >= 0 && formEnd >= 0) {

            int currentpos = documentMain.toLowerCase().indexOf("<input", formStart);

            while (currentpos >= formStart && currentpos < formEnd) {

                String type = null, name = null, value = null;

                int closer = documentMain.toLowerCase().indexOf(">", currentpos);

                String[] sentence = documentMain.substring(currentpos, closer).split(" ");    // The input divided by the spaces

                for (String sentencePart : sentence) {    // Read the parts of the input

                    String partname = sentencePart.substring(0, sentencePart.indexOf("=") >= 0 ? sentencePart.indexOf("=") : 0);

                    String partvalue = sentencePart.substring(sentencePart.indexOf("=") >= 0 ? sentencePart.indexOf("=") + 1 : 0, sentencePart.length());


                    switch (partname.toLowerCase()) {

                        case "type":
                            type = partvalue.replace("\"", "");
                            break;
                        case "name":
                            name = partvalue.replace("\"", "");
                            break;
                        case "value":
                            value = partvalue.replace("\"", "");
                            break;

                        default:
                            ;

                    }

                }

                if (type != null && name != null && value != null)
                    if (!type.toLowerCase().contains("checkbox")) { // To be sent

                        if (output.length() > 0) output.append("&");

                        output.append(name + "=" + URLEncoder.encode(value, "UTF-8"));

                    }

                // Prepare for next while loop
                currentpos = documentMain.toLowerCase().indexOf("<input", currentpos + 1);

            }


        }

        if (output.length() > 0) output.append("&");
        output.append("username=" + URLEncoder.encode(username, "UTF-8") + "&password=" + URLEncoder.encode(password, "UTF-8"));

        return output.toString();

    }

    public static String faiticLogin(String username, String password) throws Exception {

        Log.v("NAITSIRC", "629");
        String responseToLogin = requestDocument(urlMain, generatePostLogin(username, password));

        Log.v("NAITSIRC", "631");
        int errorToLoginIndex = responseToLogin.indexOf("<dd class=\"error message\">");

        Log.v("NAITSIRC", "633");
        // If there was an error
        Log.v("NAITSIRC", "634");
        if (errorToLoginIndex >= 0) {

            Log.v("NAITSIRC", "636");
            int firstLiError = responseToLogin.indexOf("<li>", errorToLoginIndex);
            Log.v("NAITSIRC", "637");
            int lastLiError = responseToLogin.indexOf("</li>", errorToLoginIndex);

            Log.v("NAITSIRC", "639");
            if (firstLiError > 0 && lastLiError > firstLiError) {

                Log.v("NAITSIRC", "641");
                logger.log(Logger.ERROR, " -- Error: " + responseToLogin.substring(firstLiError + 4, lastLiError) + " -- ");
                Log.v("NAITSIRC", "642");
                return null;

            }

        }

        Log.v("NAITSIRC", "648");
        // No error, go to the document we want (Languages change the destination)

        Log.v("NAITSIRC", "650");
        //return responseToLogin;
        Log.v("NAITSIRC", "651");
        return requestDocument(urlSubjects, "");


    }


    public static String faiticLogout(String documentMain) throws Exception {

        Log.v("NAITSIRC", "659");
        StringBuffer output = new StringBuffer();

        Log.v("NAITSIRC", "661");
        int formStart = documentMain.toLowerCase().indexOf("<form action=\"/index.php/es/materias\" method=\"post\" id=\"login-form\"");

        Log.v("NAITSIRC", "663");
        int formEnd = documentMain.toLowerCase().indexOf("</form>", formStart);

        Log.v("NAITSIRC", "665");
        // Form detected

        Log.v("NAITSIRC", "667");
        if (formStart >= 0 && formEnd >= 0) {

            Log.v("NAITSIRC", "669");
            int currentpos = documentMain.toLowerCase().indexOf("<input", formStart);

            Log.v("NAITSIRC", "671");
            while (currentpos >= formStart && currentpos < formEnd) {

                Log.v("NAITSIRC", "673");
                String type = null, name = null, value = null;

                Log.v("NAITSIRC", "675");
                int closer = documentMain.toLowerCase().indexOf(">", currentpos);

                Log.v("NAITSIRC", "677");
                String[] sentence = documentMain.substring(currentpos, closer).split(" ");    // The input divided by the spaces

                Log.v("NAITSIRC", "679");
                for (String sentencePart : sentence) {    // Read the parts of the input

                    Log.v("NAITSIRC", "681");
                    String partname = sentencePart.substring(0, sentencePart.indexOf("=") >= 0 ? sentencePart.indexOf("=") : 0);

                    Log.v("NAITSIRC", "683");
                    String partvalue = sentencePart.substring(sentencePart.indexOf("=") >= 0 ? sentencePart.indexOf("=") + 1 : 0, sentencePart.length());


                    Log.v("NAITSIRC", "686");
                    switch (partname.toLowerCase()) {

                        case "type":
                            type = partvalue.replace("\"", "");
                            break;
                        case "name":
                            name = partvalue.replace("\"", "");
                            break;
                        case "value":
                            value = partvalue.replace("\"", "");
                            break;

                        default:
                            ;

                    }

                }

                Log.v("NAITSIRC", "698");
                if (type != null && name != null && value != null)
                    Log.v("NAITSIRC", "699");
                if (!type.toLowerCase().contains("checkbox")) { // To be sent

                    Log.v("NAITSIRC", "701");
                    if (output.length() > 0) output.append("&");

                    Log.v("NAITSIRC", "703");
                    output.append(name + "=" + URLEncoder.encode(value, "UTF-8"));

                }

                Log.v("NAITSIRC", "707");
                // Prepare for next while loop
                Log.v("NAITSIRC", "708");
                currentpos = documentMain.toLowerCase().indexOf("<input", currentpos + 1);

            }


        }

        Log.v("NAITSIRC", "715");
        return requestDocument(urlSubjects, output.toString());

    }


    public static ArrayList<Subject> faiticSubjects(String documentToCheck) {    // 0 url 1 name

        Log.v("NAITSIRC", "722");
        ArrayList<Subject> subjectList = new ArrayList<Subject>();

        Log.v("NAITSIRC", "724");
        // Login was unsuccessful
        Log.v("NAITSIRC", "725");
        if (documentToCheck == null) return subjectList;

        Log.v("NAITSIRC", "727");
        // Login successful:

        Log.v("NAITSIRC", "729");
        int subjectIndex = documentToCheck.indexOf("<span class=\"asignatura\"");

        Log.v("NAITSIRC", "731");
        while (subjectIndex >= 0) {

            Log.v("NAITSIRC", "733");
            // Check subjects one by one

            Log.v("NAITSIRC", "735");
            int hrefIndex = documentToCheck.indexOf("<a href=\"", subjectIndex);
            Log.v("NAITSIRC", "736");
            int hrefURLCloserIndex = documentToCheck.indexOf("\"", hrefIndex + "<a href=\"".length());

            Log.v("NAITSIRC", "738");
            int hrefFirstTagCloserIndex = documentToCheck.indexOf(">", hrefURLCloserIndex);
            Log.v("NAITSIRC", "739");
            int hrefSecondTagOpenerIndex = documentToCheck.indexOf("<", hrefFirstTagCloserIndex);


            Log.v("NAITSIRC", "742");
            String subjectURL = documentToCheck.substring(hrefIndex + "<a href=\"".length(), hrefURLCloserIndex);
            Log.v("NAITSIRC", "743");
            String subjectName = documentToCheck.substring(hrefFirstTagCloserIndex + 1, hrefSecondTagOpenerIndex).trim();

            Log.v("NAITSIRC", "745");
            subjectList.add(new Subject(subjectURL, subjectName));

            Log.v("NAITSIRC", "747");
            subjectIndex = documentToCheck.indexOf("<span class=\"asignatura\"", subjectIndex + 1);

        }

        Log.v("NAITSIRC", "751");
        return subjectList;

    }

    public static DocumentFromURL goToSubject(String url) throws Exception {    // 0 is the url and 1 is the document itself

        Log.v("NAITSIRC", "757");
        //CookieHandler.setDefault(cookieManager);

        Log.v("NAITSIRC", "759");
        String documentMain = requestDocument(url, "");

        Log.v("NAITSIRC", "761");
        StringBuffer output = new StringBuffer();

        Log.v("NAITSIRC", "763");
        int formStart = documentMain.toLowerCase().indexOf("<form name='frm'");

        Log.v("NAITSIRC", "765");
        int formEnd = documentMain.toLowerCase().indexOf("</form>", formStart);

        Log.v("NAITSIRC", "767");
        int actionStart = documentMain.indexOf("action='", formStart);
        Log.v("NAITSIRC", "768");
        int actionEnd = documentMain.indexOf("'", actionStart + "action='".length());

        Log.v("NAITSIRC", "770");
        String urlForAction = documentMain.substring(actionStart + "action='".length(), actionEnd);

        Log.v("NAITSIRC", "772");
        // Form detected
        Log.v("NAITSIRC", "773");
        Log.v("GOTOSUBJECT", "x2" + ((formStart >= 0 && formEnd >= 0) ? "true" : "false"));


        Log.v("NAITSIRC", "776");
        if (formStart >= 0 && formEnd >= 0) {

            Log.v("NAITSIRC", "778");
            int currentpos = documentMain.toLowerCase().indexOf("<input", formStart);

            Log.v("NAITSIRC", "780");
            while (currentpos >= formStart && currentpos < formEnd) {

                Log.v("NAITSIRC", "782");
                String type = null, name = null, value = null;

                Log.v("NAITSIRC", "784");
                int closer = documentMain.toLowerCase().indexOf(">", currentpos);

                Log.v("DOCUMENTMAIN", documentMain);
                Log.v("CURRENTPOS", currentpos + "");
                Log.v("CLOSER", closer + "");

                Log.v("NAITSIRC", "786");
                String[] sentence = documentMain.substring(currentpos, closer).split(" ");    // The input divided by the spaces

                Log.v("NAITSIRC", "788");
                for (String sentencePart : sentence) {    // Read the parts of the input

                    Log.v("NAITSIRC", "790");
                    String partname = sentencePart.substring(0, sentencePart.indexOf("=") >= 0 ? sentencePart.indexOf("=") : 0);

                    Log.v("NAITSIRC", "792");
                    String partvalue = sentencePart.substring(sentencePart.indexOf("=") >= 0 ? sentencePart.indexOf("=") + 1 : 0, sentencePart.length());


                    Log.v("NAITSIRC", "795");
                    switch (partname.toLowerCase()) {

                        case "type":
                            type = partvalue.replace("'", "");
                            break;
                        case "name":
                            name = partvalue.replace("'", "");
                            break;
                        case "value":
                            value = partvalue.replace("'", "");
                            break;

                        default:
                            ;

                    }

                }

                Log.v("NAITSIRC", "807");
                if (type != null && name != null && value != null)
                    Log.v("NAITSIRC", "808");
                if (!type.toLowerCase().contains("checkbox")) { // To be sent

                    Log.v("NAITSIRC", "810");
                    if (output.length() > 0) output.append("&");

                    Log.v("NAITSIRC", "812");
                    output.append(name + "=" + URLEncoder.encode(value, "UTF-8"));

                }

                Log.v("NAITSIRC", "816");
                // Prepare for next while loop
                Log.v("NAITSIRC", "817");
                currentpos = documentMain.toLowerCase().indexOf("<input", currentpos + 1);

            }


        }

        Log.v("NAITSIRC", "824");
        return new DocumentFromURL(urlForAction, requestDocument(urlForAction, output.toString()));


    }

    public static final int CLAROLINE = 0;
    public static final int MOODLE = 1;
    public static final int MOODLE2 = 2;
    public static final int UNKNOWN = 99;

    public static int subjectPlatformType(String url) {

        Log.v("NAITSIRC", "836");
        if (url.toLowerCase().contains("/claroline/")) {
            Log.v("NAITSIRC", "837");
            return CLAROLINE;
        } else if (url.toLowerCase().contains("/moodle") && !url.toLowerCase().contains("/moodle2_")) {
            Log.v("NAITSIRC", "840");
            return MOODLE;
        } else if (url.toLowerCase().contains("/moodle2_")) {
            Log.v("NAITSIRC", "843");
            return MOODLE2;
        } else {
            Log.v("NAITSIRC", "846");
            return UNKNOWN;
        }

    }

    public static void logoutSubject(String platformURL, String platformDocument, int platformType) throws Exception {

        Log.v("NAITSIRC", "853");
        if (platformType == CLAROLINE) {

            Log.v("NAITSIRC", "855");
            String logoutURL = platformURL.substring(0, platformURL.lastIndexOf("?") >= 0 ? platformURL.lastIndexOf("?") : platformURL.length()) + "?logout=true";

            Log.v("NAITSIRC", "857");
            requestDocument(logoutURL, "");

        } else if (platformType == MOODLE || platformType == MOODLE2) {

            Log.v("NAITSIRC", "863");
            // More complicated :( pay attention because this is about to start...

            Log.v("NAITSIRC", "865");
            int endOfURLShouldStartWith = platformURL.indexOf("/", platformURL.indexOf("/moodle") + 1);

            Log.v("NAITSIRC", "867");
            if (endOfURLShouldStartWith >= 0) {

                Log.v("NAITSIRC", "869");
                String logoutURLShouldStartWith = platformURL.substring(0, endOfURLShouldStartWith) + "/login/logout.php";
                Log.v("NAITSIRC", "870");
                // This is the url that should appear on the document, but with all the parameters given as GET

                Log.v("NAITSIRC", "872");
                // Let's look for this entry

                Log.v("NAITSIRC", "874");
                int hereIsTheLogoutURL = platformDocument.indexOf(logoutURLShouldStartWith);

                Log.v("NAITSIRC", "876");
                int hereEndsTheLogoutURL = platformDocument.indexOf("\"", hereIsTheLogoutURL);

                Log.v("NAITSIRC", "878");
                //System.out.println("\n\n" + logoutURLShouldStartWith + "\n\n");

                Log.v("NAITSIRC", "880");
                if (hereIsTheLogoutURL >= 0 && hereEndsTheLogoutURL > hereIsTheLogoutURL) {

                    Log.v("NAITSIRC", "882");
                    // Gotcha!

                    Log.v("NAITSIRC", "884");
                    requestDocument(platformDocument.substring(hereIsTheLogoutURL, hereEndsTheLogoutURL), "");

                }

            }

        }

    }

    public static ArrayList<FileFromURL> listDocumentsClaroline(String platformURL) throws Exception {

		/*
Log.v("NAITSIRC", "897");
		 * 0 -> Path (incl. filename)
Log.v("NAITSIRC", "898");
		 * 1 -> URL to file
		 */

        Log.v("NAITSIRC", "901");
        ArrayList<FileFromURL> list = new ArrayList<FileFromURL>();

        Log.v("NAITSIRC", "903");
        int untilWhenUrlToUse = platformURL.indexOf("/", platformURL.indexOf("/claroline") + 1);

        Log.v("NAITSIRC", "905");
        if (untilWhenUrlToUse >= 0) {

            Log.v("NAITSIRC", "907");
            String urlBase = platformURL.substring(0, untilWhenUrlToUse);
            Log.v("NAITSIRC", "908");
            String urlToUse = urlBase + "/document/document.php";
            Log.v("NAITSIRC", "909");
            listDocumentsClarolineInternal(urlToUse, list, urlBase);    // Recursive

        }

        Log.v("NAITSIRC", "913");
        cleanArtifacts(list);
        Log.v("NAITSIRC", "914");
        deleteRepeatedFiles(list);

        Log.v("NAITSIRC", "916");
        return list;

    }

    private static void listDocumentsClarolineInternal(String urlToAnalyse, ArrayList<FileFromURL> list, String urlBase) throws Exception {

        Log.v("NAITSIRC", "922");
        String document;

        Log.v("NAITSIRC", "924");
        try {

            Log.v("NAITSIRC", "926");
            document = requestDocument(urlToAnalyse, "");

            Log.v("NAITSIRC", "928");
        } catch (Exception ex) {

            Log.v("NAITSIRC", "930");
            return;

        }

        Log.v("NAITSIRC", "934");
        if (!urlToAnalyse.equals(lastRequestedURL)) return;        // If the page redirected us

        Log.v("NAITSIRC", "936");
        // Check for documents...

        Log.v("NAITSIRC", "938");
        int dirStart = document.indexOf("<a class=\" item");

        Log.v("NAITSIRC", "940");
        int dirEnd = document.lastIndexOf("End of Claroline Body");

        Log.v("NAITSIRC", "942");
        if (dirStart >= 0 && dirEnd > dirStart) {

            Log.v("NAITSIRC", "944");
            String documentToAnalyse = document.substring(dirStart, dirEnd);

            Log.v("NAITSIRC", "946");
            // First check for files

            Log.v("NAITSIRC", "948");
            int ocurrence = documentToAnalyse.indexOf("goto/index.php");

            Log.v("NAITSIRC", "950");
            while (ocurrence >= 0) {

                Log.v("NAITSIRC", "952");
                int endOfOcurrence = documentToAnalyse.indexOf("\"", ocurrence + 1);

                Log.v("NAITSIRC", "954");
                if (endOfOcurrence > ocurrence) {

                    Log.v("NAITSIRC", "956");
                    String urlGot = urlBase + "/document/" + documentToAnalyse.substring(ocurrence, endOfOcurrence).replace("&amp;", "&").replace(" ", "%20");

                    Log.v("NAITSIRC", "958");
                    String pathForFile = urlGot.substring((urlBase + "/document/goto/index.php/").length(), urlGot.lastIndexOf("?") >= 0 ? urlGot.lastIndexOf("?") : urlGot.length());

                    Log.v("NAITSIRC", "960");
                    list.add(new FileFromURL(urlGot, URLDecoder.decode("/" + pathForFile, "iso-8859-1")));

                }

                Log.v("NAITSIRC", "964");
                ocurrence = documentToAnalyse.indexOf("goto/index.php", ocurrence + 1);

            }


            Log.v("NAITSIRC", "969");
            // Now for directories

            Log.v("NAITSIRC", "971");
            ocurrence = documentToAnalyse.indexOf("/document/document.php?cmd=exChDir");

            Log.v("NAITSIRC", "973");
            while (ocurrence >= 0) {

                Log.v("NAITSIRC", "975");
                int endOfOcurrence = documentToAnalyse.indexOf("\"", ocurrence + 1);

                Log.v("NAITSIRC", "977");
                if (endOfOcurrence > ocurrence) {

                    Log.v("NAITSIRC", "979");
                    String urlGot = urlBase + documentToAnalyse.substring(ocurrence, endOfOcurrence).replace("&amp;", "&").replace(" ", "%20");

                    Log.v("NAITSIRC", "981");
                    listDocumentsClarolineInternal(urlGot, list, urlBase);

                }

                Log.v("NAITSIRC", "985");
                ocurrence = documentToAnalyse.indexOf("/document/document.php?cmd=exChDir", ocurrence + 1);

            }

        }


    }


    public static ArrayList<FileFromURL> listDocumentsMoodle(String platformURL) throws Exception {

		/*
Log.v("NAITSIRC", "999");
		 * 0 -> Path (incl. filename)
Log.v("NAITSIRC", "1000");
		 * 1 -> URL to file
		 */

        Log.v("NAITSIRC", "1003");
        ArrayList<FileFromURL> list = new ArrayList<FileFromURL>();

        Log.v("NAITSIRC", "1005");
        int untilWhenUrlToUse = platformURL.indexOf("/", platformURL.indexOf("/moodle") + 1);

        Log.v("NAITSIRC", "1007");
        if (untilWhenUrlToUse >= 0) {

            Log.v("NAITSIRC", "1009");
            String urlBase = platformURL.substring(0, untilWhenUrlToUse);
            Log.v("NAITSIRC", "1010");
            String urlGetMethod = platformURL.indexOf("?") >= 0 ? platformURL.substring(platformURL.indexOf("?") + 1, platformURL.length()) : "";
            Log.v("NAITSIRC", "1011");
            String urlForResources = urlBase + "/mod/resource/index.php" + (urlGetMethod.length() > 0 ? "?" + urlGetMethod : "");

            Log.v("NAITSIRC", "1013");
            listDocumentsMoodleInternal(urlForResources, list, urlBase);

        }

        Log.v("NAITSIRC", "1017");
        cleanArtifacts(list);
        Log.v("NAITSIRC", "1018");
        deleteRepeatedFiles(list);

        Log.v("NAITSIRC", "1020");
        return list;

    }


    private static void listDocumentsMoodleInternal(String urlToUse, ArrayList<FileFromURL> list, String urlBase) throws Exception {

        Log.v("NAITSIRC", "1027");
        //System.out.println("---Accessed---");

        Log.v("NAITSIRC", "1029");
        String resourcePage;

        Log.v("NAITSIRC", "1031");
        try {

            Log.v("NAITSIRC", "1033");
            resourcePage = requestDocument(urlToUse, "");

            Log.v("NAITSIRC", "1035");
        } catch (Exception ex) {

            Log.v("NAITSIRC", "1037");
            return;

        }

        Log.v("NAITSIRC", "1041");
        if (!urlToUse.equals(lastRequestedURL)) return;        // If the page redirected us

        Log.v("NAITSIRC", "1043");
        // The list of files from this resource

        Log.v("NAITSIRC", "1045");
        int bodyStart = resourcePage.indexOf("<!-- END OF HEADER -->");

        Log.v("NAITSIRC", "1047");
        int bodyEnd = resourcePage.indexOf("<!-- START OF FOOTER -->", bodyStart);

        Log.v("NAITSIRC", "1049");
        if (bodyStart >= 0 && bodyEnd > bodyStart) {

            Log.v("NAITSIRC", "1051");
            String whereToSearch = resourcePage.substring(bodyStart, bodyEnd);

            Log.v("NAITSIRC", "1053");
            int URLStart = whereToSearch.indexOf(urlBase + "/file.php/");
            Log.v("NAITSIRC", "1054");
            int URLEnd = whereToSearch.indexOf("\"", URLStart);

            Log.v("NAITSIRC", "1056");
            if (whereToSearch.indexOf("\'", URLStart) < URLEnd && whereToSearch.indexOf("\'", URLStart) >= 0)
                Log.v("NAITSIRC", "1057");
            URLEnd = whereToSearch.indexOf("\'", URLStart);

            Log.v("NAITSIRC", "1059");
            while (URLStart >= 0 && URLStart < URLEnd) {

                Log.v("NAITSIRC", "1061");
                String urlToFile = whereToSearch.substring(URLStart, URLEnd);
                Log.v("NAITSIRC", "1062");
                urlToFile = urlToFile.replace("&amp;", "&");

                Log.v("NAITSIRC", "1064");
                int filePathStart = urlToFile.indexOf("/", (urlBase + "/file.php/").length() + 1);

                Log.v("NAITSIRC", "1066");
                String filePath = urlToFile.substring(filePathStart, urlToFile.length());

                Log.v("NAITSIRC", "1068");
                list.add(new FileFromURL(urlToFile, URLDecoder.decode(filePath, "iso-8859-1")));    // Added to list

                Log.v("NAITSIRC", "1070");
                // For next loop

                Log.v("NAITSIRC", "1072");
                URLStart = whereToSearch.indexOf(urlBase + "/file.php/", URLEnd);
                Log.v("NAITSIRC", "1073");
                URLEnd = whereToSearch.indexOf("\"", URLStart);

                Log.v("NAITSIRC", "1075");
                if (whereToSearch.indexOf("\'", URLStart) < URLEnd && whereToSearch.indexOf("\'", URLStart) >= 0)
                    Log.v("NAITSIRC", "1076");
                URLEnd = whereToSearch.indexOf("\'", URLStart);

            }

            Log.v("NAITSIRC", "1080");
            // Then directories

            Log.v("NAITSIRC", "1082");
            URLStart = whereToSearch.indexOf("view.php?");
            Log.v("NAITSIRC", "1083");
            URLEnd = whereToSearch.indexOf("\"", URLStart);

            Log.v("NAITSIRC", "1085");
            if (whereToSearch.indexOf("\'", URLStart) < URLEnd && whereToSearch.indexOf("\'", URLStart) >= 0)
                Log.v("NAITSIRC", "1086");
            URLEnd = whereToSearch.indexOf("\'", URLStart);

            Log.v("NAITSIRC", "1088");
            while (URLStart >= 0 && URLStart < URLEnd) {

                Log.v("NAITSIRC", "1090");
                String urlList = urlBase + "/mod/resource/" + whereToSearch.substring(URLStart, URLEnd);
                Log.v("NAITSIRC", "1091");
                urlList = urlList.replace("&amp;", "&").replace(" ", "%20");

                Log.v("NAITSIRC", "1093");
                // We have got the url, but we don't know if it's a folder or not, let's check it

                Log.v("NAITSIRC", "1095");
                try {

                    Log.v("NAITSIRC", "1097");
                    String realurl = getRedirectedURL(urlList, "");

                    Log.v("NAITSIRC", "1099");
                    if (realurl == null) {

                        Log.v("NAITSIRC", "1101");
                        // Folder, recursive search

                        Log.v("NAITSIRC", "1103");
                        listDocumentsMoodleInternal(urlList, list, urlBase);

                        Log.v("NAITSIRC", "1105");
                    } else {

                        Log.v("NAITSIRC", "1107");
                        // Document, let's get the real name

                        Log.v("NAITSIRC", "1109");
                        String realname = "undefined";

                        Log.v("NAITSIRC", "1111");
                        int filePathStart = realurl.indexOf("/", (urlBase + "/file.php/").length() + 1);

                        Log.v("NAITSIRC", "1113");
                        if (filePathStart >= 0) {

                            Log.v("NAITSIRC", "1115");
                            String filePath = realurl.substring(filePathStart, realurl.length());

                            Log.v("NAITSIRC", "1117");
                            list.add(new FileFromURL(realurl, URLDecoder.decode(filePath, "iso-8859-1")));    // Added to list

                        }


                    }


                    Log.v("NAITSIRC", "1125");
                } catch (Exception ex) {

                    Log.v("NAITSIRC", "1127");
                    ex.printStackTrace();

                }


                Log.v("NAITSIRC", "1132");
                // For next loop

                Log.v("NAITSIRC", "1134");
                URLStart = whereToSearch.indexOf("view.php?", URLEnd);
                Log.v("NAITSIRC", "1135");
                URLEnd = whereToSearch.indexOf("\"", URLStart);

                Log.v("NAITSIRC", "1137");
                if (whereToSearch.indexOf("\'", URLStart) < URLEnd && whereToSearch.indexOf("\'", URLStart) >= 0)
                    Log.v("NAITSIRC", "1138");
                URLEnd = whereToSearch.indexOf("\'", URLStart);

            }

        }


    }


    public static ArrayList<FileFromURL> listDocumentsMoodle2(String platformURL) throws Exception {

		/*
Log.v("NAITSIRC", "4");
		 * 0 -> Path (incl. filename)
Log.v("NAITSIRC", "5");
		 * 1 -> URL to file
		 */

        Log.v("NAITSIRC", "8");
        ArrayList<FileFromURL> list = new ArrayList<FileFromURL>();

        Log.v("NAITSIRC", "10");
        int untilWhenUrlToUse = platformURL.indexOf("/", platformURL.indexOf("/moodle") + 1);

        Log.v("NAITSIRC", "12");
        if (untilWhenUrlToUse >= 0) {

            Log.v("NAITSIRC", "14");
            String urlBase = platformURL.substring(0, untilWhenUrlToUse);
            Log.v("NAITSIRC", "15");
            String urlGetMethod = platformURL.indexOf("?") >= 0 ? platformURL.substring(platformURL.indexOf("?") + 1, platformURL.length()) : "";
            Log.v("NAITSIRC", "16");
            String urlForResources = urlBase + "/mod/resource/index.php" + (urlGetMethod.length() > 0 ? "?" + urlGetMethod : "");

            Log.v("NAITSIRC", "18");
            listDocumentsMoodle2Internal(urlForResources, list, urlBase, "");

        }

        Log.v("NAITSIRC", "22");
        cleanArtifacts(list);
        Log.v("NAITSIRC", "23");
        deleteRepeatedFiles(list);

        Log.v("NAITSIRC", "25");
        return list;

    }


    private static void listDocumentsMoodle2Internal(String urlToUse, ArrayList<FileFromURL> list, String urlBase, String folder) throws Exception {

        Log.v("NAITSIRC", "32");
        //System.out.println("---Accessed---");

        Log.v("NAITSIRC", "34");
        String resourcePage;

        Log.v("NAITSIRC", "36");
        try {

            Log.v("NAITSIRC", "38");
            resourcePage = requestDocument(urlToUse, "");

            Log.v("NAITSIRC", "40");
        } catch (Exception ex) {

            Log.v("NAITSIRC", "42");
            return;

        }

        Log.v("NAITSIRC", "46");
        if (!urlToUse.equals(lastRequestedURL)) return;        // If the page redirected us

        Log.v("NAITSIRC", "48");
        // The list of files from this resource

        Log.v("NAITSIRC", "50");
        int bodyStart = resourcePage.indexOf("<div id=\"page-content\"");

        Log.v("NAITSIRC", "52");
        int bodyEnd = resourcePage.indexOf("</section>", bodyStart);

        Log.v("NAITSIRC", "54");
        if (bodyStart >= 0 && bodyEnd > bodyStart) {

            Log.v("NAITSIRC", "56");
            String whereToSearch = resourcePage.substring(bodyStart, bodyEnd);

            Log.v("NAITSIRC", "58");
            int URLStart = whereToSearch.indexOf("view.php?");
            Log.v("NAITSIRC", "59");
            int URLEnd = whereToSearch.indexOf("\"", URLStart);

            Log.v("NAITSIRC", "61");
            if (whereToSearch.indexOf("\'", URLStart) < URLEnd && whereToSearch.indexOf("\'", URLStart) >= 0)
                Log.v("NAITSIRC", "62");
            URLEnd = whereToSearch.indexOf("\'", URLStart);

            Log.v("NAITSIRC", "64");
            while (URLStart >= 0 && URLStart < URLEnd) {

                Log.v("NAITSIRC", "66");
                String urlList = urlBase + "/mod/resource/" + whereToSearch.substring(URLStart, URLEnd);
                Log.v("NAITSIRC", "67");
                urlList = urlList.replace("&amp;", "&");

                Log.v("NAITSIRC", "69");
                // We have got the url, but we don't know if it's a folder or not, let's check it

                Log.v("NAITSIRC", "71");
                int indeximg = whereToSearch.indexOf("<img src=", URLEnd);
                Log.v("NAITSIRC", "72");
                int endofimg = whereToSearch.indexOf(">", indeximg);

                Log.v("NAITSIRC", "74");
                int endofa = whereToSearch.indexOf("<", endofimg);

                Log.v("NAITSIRC", "76");
                int folderindex = whereToSearch.indexOf("folder-24", indeximg);

                Log.v("NAITSIRC", "78");
                String filename = endofimg >= 0 && endofa > endofimg ? whereToSearch.substring(endofimg + 1, endofa).trim() : "undefined";

                Log.v("NAITSIRC", "80");
                if (folderindex >= 0 && folderindex < endofimg) {

                    Log.v("NAITSIRC", "82");
                    // Folder, recursive search

                    Log.v("NAITSIRC", "84");
                    listDocumentsMoodle2Internal(urlList, list, urlBase, folder + "/" + filename);

                    Log.v("NAITSIRC", "86");
                } else {

                    Log.v("NAITSIRC", "88");
                    // Document, let's get the real name

                    Log.v("NAITSIRC", "90");
                    try {

                        Log.v("NAITSIRC", "92");
                        String realurl = getRedirectedURL(urlList, "");
                        Log.v("NAITSIRC", "93");
                        String realname = filename;    // By now

                        Log.v("NAITSIRC", "95");
                        if (realurl != null) {

                            Log.v("NAITSIRC", "97");
                            // Redirected, get the real name

                            Log.v("NAITSIRC", "99");
                            int questionMarkIndex = realurl.indexOf("?");
                            Log.v("NAITSIRC", "100");
                            int lastDivider = realurl.substring(0, questionMarkIndex >= 0 ? questionMarkIndex : realurl.length()).lastIndexOf("/");    // No error because it starts at 0

                            Log.v("NAITSIRC", "102");
                            if (lastDivider >= 0) {

                                Log.v("NAITSIRC", "104");
                                // Got a name

                                Log.v("NAITSIRC", "106");
                                realname = URLDecoder.decode(realurl.substring(lastDivider + 1, questionMarkIndex >= 0 ? questionMarkIndex : realurl.length()), "UTF-8");

                            }


                        }

                        Log.v("NAITSIRC", "113");
                        list.add(new FileFromURL(urlList, folder + "/" + realname));

                        Log.v("NAITSIRC", "115");
                    } catch (Exception ex) {
                        Log.v("NAITSIRC", "116");
                        ex.printStackTrace();
                    }

                }

                Log.v("NAITSIRC", "121");
                // For next loop

                Log.v("NAITSIRC", "123");
                URLStart = whereToSearch.indexOf("view.php?", URLEnd);
                Log.v("NAITSIRC", "124");
                URLEnd = whereToSearch.indexOf("\"", URLStart);

                Log.v("NAITSIRC", "126");
                if (whereToSearch.indexOf("\'", URLStart) < URLEnd && whereToSearch.indexOf("\'", URLStart) >= 0)
                    Log.v("NAITSIRC", "127");
                URLEnd = whereToSearch.indexOf("\'", URLStart);

            }

        }

    }

    protected static void deleteRepeatedFiles(ArrayList<FileFromURL> list) {    // Deletes files with same url

        Log.v("NAITSIRC", "137");
        // Make a copy of list

        Log.v("NAITSIRC", "139");
        int pos = 0;

        Log.v("NAITSIRC", "141");
        while (pos < list.size()) {    // From 0 to size

            Log.v("NAITSIRC", "143");
            FileFromURL element = list.get(pos);    // To compare

            Log.v("NAITSIRC", "145");
            int i = pos + 1;

            Log.v("NAITSIRC", "147");
            while (i < list.size()) {    // From pos+1 to size

                Log.v("NAITSIRC", "149");
                // 1 is url
                Log.v("NAITSIRC", "150");
                if (element.getURL().equals(list.get(i).getURL())) {

                    Log.v("NAITSIRC", "152");
                    list.remove(i);    // Delete element
                    Log.v("NAITSIRC", "153");
                    i--;            // The i index must be reduced

                    Log.v("NAITSIRC", "155");
                    //out.set(i, new String[]{"Repeated:" + out.get(i)[0],out.get(i)[1]});

                }

                Log.v("NAITSIRC", "159");
                i++;
            }

            Log.v("NAITSIRC", "162");
            pos++;

        }

    }

    protected static void cleanArtifacts(ArrayList<FileFromURL> list) {

        Log.v("NAITSIRC", "170");
        for (int i = 0; i < list.size(); i++) {

            Log.v("NAITSIRC", "172");
            FileFromURL element = list.get(i);

            Log.v("NAITSIRC", "174");
            // First for the name
            Log.v("NAITSIRC", "175");
            String name = element.getFileDestination().trim();    // Trim path

            Log.v("NAITSIRC", "177");
            int until = name.indexOf("<");
            Log.v("NAITSIRC", "178");
            until = name.indexOf(">") >= 0 && name.indexOf(">") < until ? name.indexOf(">") : until;

            Log.v("NAITSIRC", "180");
            if (until >= 0) name = name.substring(0, until);    // Delete unwanted exceeded code
            Log.v("NAITSIRC", "181");
            name = name.replaceAll("[*?\"<>|]", "_");    // Correct special characters

            Log.v("NAITSIRC", "183");
            if (name.length() <= 0) name = "undefined"; // Just in case

            Log.v("NAITSIRC", "185");
            // Second for the url

            Log.v("NAITSIRC", "187");
            String url = element.getURL().trim();    // Trim url

            Log.v("NAITSIRC", "189");
            until = url.indexOf("<") <= url.indexOf(">") ? url.indexOf("<") : url.indexOf(">");

            Log.v("NAITSIRC", "191");
            if (until >= 0) url = url.substring(0, until);        // Delete unwanted exceeded code

            Log.v("NAITSIRC", "193");
            list.set(i, new FileFromURL(url, name));

        }

    }

    protected static String adaptURL(String prevURL, String url) {

        Log.v("NAITSIRC", "201");
        Log.v("PENa=>>", url.indexOf("/") + "");
        Log.v("NAITSIRC", "202");
        if (url.indexOf("/") == 0 && prevURL != null) {

            Log.v("NAITSIRC", "204");
            // It is not a complete url, get the previous server

            Log.v("NAITSIRC", "206");
            Log.v("PENa", "1");

            Log.v("NAITSIRC", "208");
            int doubleslashpos = prevURL.indexOf("//");

            Log.v("NAITSIRC", "210");
            if (doubleslashpos >= 0) {

                Log.v("NAITSIRC", "212");
                Log.v("PENa", "2");

                Log.v("NAITSIRC", "214");
                int rootslash = prevURL.indexOf("/", doubleslashpos + 2);

                Log.v("NAITSIRC", "216");
                Log.v("PENa", "3");

                Log.v("NAITSIRC", "218");
                if (rootslash >= 0) {

                    Log.v("NAITSIRC", "220");
                    // The base url
                    Log.v("NAITSIRC", "221");
                    String baseURL = prevURL.substring(0, rootslash);

                    Log.v("NAITSIRC", "223");
                    Log.v("PENa", baseURL);

                    Log.v("NAITSIRC", "225");
                    // The url and the relative url
                    Log.v("NAITSIRC", "226");
                    return baseURL + url;

                }
            }

        }

        Log.v("NAITSIRC", "233");
        Log.v("PENA", "GAYA>" + url);
        Log.v("NAITSIRC", "234");
        // if nothing is reached
        Log.v("NAITSIRC", "235");
        return url;
    }


}