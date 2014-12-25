package io.robe.assets.http;

import com.google.common.net.HttpHeaders;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Http Asset. Caching is supported with remote files lastModified and expires headers. Etag forwarded automaticly.
 */
public class HttpAsset {
    private boolean cached;
    private URL asset;
    private byte[] cache;
    private long lastModified;
    private long expireAt;
    private String ETAG;


    /**
     * Creates and http entry
     *
     * @param path   http path
     * @param cached to cache or not to cache.
     */
    public HttpAsset(String path, boolean cached) {
        setAsset(path);
        this.cached = cached;
        loadAsset();
    }


    /**
     * Sets http http according to path.
     * It must exist as http.
     *
     * @param path
     */
    public void setAsset(String path) {
        try {
            this.asset = new URL(path);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new RuntimeException("URL does not exist: " + path);

        }

    }


    /**
     * Loads http data. If cache is enabled checks modification date and loads to cache.
     * Else just returns http
     *
     * @return
     */
    public byte[] loadAsset() {
        if (!cached) {
            return loadAssetFromURL();
        } else if (System.currentTimeMillis() >= expireAt)
            loadAssetToCache();
        return cache;


    }

    /**
     * Loads http from the http
     *
     * @return
     */
    private byte[] loadAssetFromURL() {
        try {
            URLConnection cnn = this.asset.openConnection();
            cnn.connect();
            int b = -1;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            while ((b = cnn.getInputStream().read()) != -1)
                stream.write(b);
            stream.flush();
            stream.close();

            this.lastModified = cnn.getLastModified();
            this.expireAt = cnn.getExpiration();
            this.ETAG = cnn.getHeaderField(HttpHeaders.ETAG);

            return stream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes http to a cache (byte array)
     */
    private void loadAssetToCache() {
        try {
            cache = loadAssetFromURL();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }


    public long getLastModified() {
        return lastModified;
    }

    public String getETAG() {
        return ETAG;
    }
}
