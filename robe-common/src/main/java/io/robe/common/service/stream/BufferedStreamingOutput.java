package io.robe.common.service.stream;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * A buffered {@link javax.ws.rs.core.StreamingOutput} implementation.
 */
public class BufferedStreamingOutput implements StreamingOutput {
    private final BufferedInputStream bufferedInputStream;
    private byte[] buffer;


    /**
     * Creates an instance with the given parameters.
     *
     * @param bufferedInputStream input to stream.
     * @param bufferSize          size of the buffer.
     */
    public BufferedStreamingOutput(BufferedInputStream bufferedInputStream, int bufferSize) {
        this.bufferedInputStream = bufferedInputStream;
        buffer = new byte[bufferSize];//1mb
    }


    /**
     * Called to write the message body.
     *
     * @param output the OutputStream to write to.
     * @throws java.io.IOException                 if an IO error is encountered
     * @throws javax.ws.rs.WebApplicationException if a specific
     *                                             HTTP error response needs to be produced. Only effective if thrown prior
     *                                             to any bytes being written to output.
     */
    @Override
    public void write(OutputStream output) throws IOException, WebApplicationException {
        //Write until available bytes are less than buffer.
        while (bufferedInputStream.available() > buffer.length) {
            bufferedInputStream.read(buffer);
            output.write(buffer);
        }
        //Write one more time to finish and exit.
        buffer = new byte[bufferedInputStream.available()];
        bufferedInputStream.read(buffer);
        output.write(buffer);
        bufferedInputStream.close();
    }
}