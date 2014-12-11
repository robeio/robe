package io.robe.assets.asset;

import com.google.common.hash.Hashing;
import com.google.common.io.Files;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileAsset {

    private long lastModified;
    private boolean cached;
    private File asset;
    private byte[] cache;
    private String md5 = null;


    /**
     * Creates and asset entry
     *
     * @param path   asset path
     * @param cached to cache or not to cache.
     */
    public FileAsset(String path, boolean cached) {
        setAsset(path);
        this.cached = cached;
        loadAsset();
    }


    /**
     * Sets asset file according to path.
     * It must exist as file.
     *
     * @param path
     */
    public void setAsset(String path) {
        this.asset = new File(path);
        if (!this.asset.exists() || !this.asset.isFile())
            throw new RuntimeException("File does not exist: " + path);
    }


    /**
     * Generates hash (MD5) from asset path and last modification date
     *
     * @return
     */
    public void generateMD5() {
        md5 = Hashing.md5().hashString(asset.getPath() + asset.lastModified(), StandardCharsets.UTF_8).toString();
    }

    /**
     * Loads asset data. If cache is enabled checks modification date and loads to cache.
     * Else just returns file
     *
     * @return
     */
    public byte[] loadAsset() {
            generateMD5();
        if (!cached) {
            return loadAssetFromFile();
        } else if (lastModified < asset.lastModified())
            loadAssetToCache();
        return cache;


    }

    /**
     * Loads asset from the file
     *
     * @return
     */
    private byte[] loadAssetFromFile() {
        try {
            lastModified = asset.lastModified();
            return Files.toByteArray(asset);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    /**
     * Writes asset to a cache (byte array)
     */
    private void loadAssetToCache() {
        try {
            cache = com.google.common.io.Files.toByteArray(asset);
            lastModified = asset.lastModified();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public long getLastModified() {
        return lastModified;
    }

    public String getMd5() {
        return md5;
    }
}
