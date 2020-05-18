package scraper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

class Utils {
    private static HttpURLConnection getConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        return (HttpURLConnection) url.openConnection();
    }

    static String getGzippedContent(String urlString) throws IOException {
        HttpURLConnection conn = getConnection(urlString);
        // add because some websites (Pexels) return 503 if there is no User-Agent header
        conn.addRequestProperty("User-Agent", "Mozilla/4.0");
        if (isOK(conn.getResponseCode())) {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new GZIPInputStream(conn.getInputStream())))) {
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
                }
            } catch (IOException ignored) {
            }
            return sb.toString();
        }
        return null;
    }

    static String getContent(String urlString) throws IOException {
        HttpURLConnection conn = getConnection(urlString);
        // add because some websites (Pexels) return 503 if there is no User-Agent header
        conn.addRequestProperty("User-Agent", "Mozilla/4.0");
        if (isOK(conn.getResponseCode())) {
            StringBuilder sb = new StringBuilder();
            String line;
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                while ((line = bufferedReader.readLine()) != null) {
                    sb.append(line);
            }
            } catch (IOException ignored) {
            }
            return sb.toString();
        }
        return null;
    }

    private static boolean isOK(int code) {
        return code / 100 == 2;
    }
}
