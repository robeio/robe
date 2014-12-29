package io.robe.assets.http;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * Servlet for serving assets with configuration.
 * Cache mechanism holds only paths. Actual byte cache is managed by {@link io.robe.assets.http.HttpAsset}
 *
 * @see io.dropwizard.servlets.assets.AssetServlet
 */

public class HttpAssetServlet extends HttpServlet {


    private Cache<String, HttpAsset> cache;
    private static final MediaType DEFAULT_MEDIA_TYPE = MediaType.HTML_UTF_8;

    private final String resourcePath;
    private final String uriPath;
    private final String indexFile;
    private final Charset defaultCharset;
    private final boolean cached;

    /**
     * Creates a new {@code FileAssetServlet} that serves static assets loaded from {@code resourceURL}
     *
     * @param resourcePath   the base URL from which assets are loaded
     * @param uriPath        the URI path fragment in which all requests are rooted
     * @param indexFile      the filename to use when directories are requested, or null to serve no
     *                       indexes
     * @param defaultCharset the default character set
     */
    public HttpAssetServlet(String resourcePath,
                            String uriPath,
                            String indexFile,
                            Charset defaultCharset,
                            boolean cached) {
        this.resourcePath = fixPath(resourcePath);
        uriPath = uriPath.endsWith("/") ? uriPath.substring(0, uriPath.length() - 1) : uriPath;
        this.uriPath = uriPath.isEmpty() ? "/" : uriPath;
        this.indexFile = indexFile;
        this.defaultCharset = defaultCharset;
        this.cached = cached;
        cache = CacheBuilder.newBuilder().build();
    }

    /**
     * Helps to fix path
     *
     * @param path to fix
     * @return
     */
    private String fixPath(String path) {
        if (!path.isEmpty()) {
            if (!path.endsWith("/"))
                return path + '/';
            else
                return path;
        } else
            return path;
    }


    public String getIndexFile() {
        return indexFile;
    }

    @Override
    protected void doGet(HttpServletRequest req,
                         HttpServletResponse resp) throws ServletException, IOException {
        try {
            final StringBuilder builder = new StringBuilder(req.getServletPath());

            // If http is empty redirect to index http.
            if (req.getPathInfo() != null) {
                builder.append(req.getPathInfo());
            } else {
                builder.insert(0, req.getContextPath());
                builder.append("/").append(getIndexFile());
                resp.sendRedirect(builder.toString());
                return;
            }

            String assetPath = builder.toString();
            //Get from cache if not available load it.
            HttpAsset asset = cache.getIfPresent(assetPath);
            if (asset == null) {
                asset = loadAsset(assetPath);
            }
            //If still it is null it means nothing to load.
            if (asset == null) {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // Maybe client got it already
            if (!isDifferentFromClientCache(req, asset)) {
                resp.sendError(HttpServletResponse.SC_NOT_MODIFIED);
                return;
            }

            //Modify response identifiers
            resp.setDateHeader(HttpHeaders.LAST_MODIFIED, asset.getLastModified());
            resp.setHeader(HttpHeaders.ETAG, asset.getETAG());

            decideMimeAndEncoding(req, resp);


            try (ServletOutputStream output = resp.getOutputStream()) {
                output.write(asset.loadAsset());
            }
        } catch (RuntimeException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void decideMimeAndEncoding(HttpServletRequest req, HttpServletResponse resp) {
        //Decide mimetype
        String mime = req.getServletContext().getMimeType(req.getRequestURI());
        MediaType mediaType = (mime == null) ? DEFAULT_MEDIA_TYPE : MediaType.parse(mime);
        if (defaultCharset != null && mediaType.is(MediaType.ANY_TEXT_TYPE)) {
            mediaType = mediaType.withCharset(defaultCharset);
        }
        resp.setContentType(mediaType.type() + '/' + mediaType.subtype());
        if (mediaType.charset().isPresent()) {
            resp.setCharacterEncoding(mediaType.charset().get().toString());
        }
    }

    private HttpAsset loadAsset(String path) {
        if (!path.startsWith(uriPath))
            return null;
        String absolutePath = path.substring(uriPath.length());
        absolutePath = absolutePath.startsWith("/") ? absolutePath.substring(1) : absolutePath;
        absolutePath = absolutePath.endsWith("/") ? absolutePath.substring(0, absolutePath.length() - 1) : absolutePath;
        absolutePath = this.resourcePath + absolutePath;

        HttpAsset asset = new HttpAsset(absolutePath, cached);
        cache.put(path, asset);

        return asset;
    }

    private long msToSec(long time) {
        return time / 1000;
    }

    private boolean isDifferentFromClientCache(HttpServletRequest req, HttpAsset asset) {
        return asset.getETAG().equals(req.getHeader(HttpHeaders.IF_NONE_MATCH)) ||
                (msToSec(req.getDateHeader(HttpHeaders.IF_MODIFIED_SINCE)) >= msToSec(asset.getLastModified()));
    }
}
