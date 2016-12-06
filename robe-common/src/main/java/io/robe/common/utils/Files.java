package io.robe.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Util Class for all file operations.
 */
public class Files {

    public static final String JAVA_IO_TMP_DIR = "java.io.tmpdir";
    public static final String TEMP_DIR = System.getProperty(JAVA_IO_TMP_DIR);

    private Files() {

    }

    /**
     * Writes input stream to a temporary file under OS's native temp folder with a unique name.
     *
     * @param in InputStream from any source.
     * @return File instance of temporary file.
     * @throws java.io.IOException
     */
    public static File writeToTemp(InputStream in) throws IOException {
        return writeFileToDirectory(in, TEMP_DIR);
    }


    public static File writeFileToDirectory(InputStream in, String path) throws IOException {
        //Create an unique name and replace all unwanted chars.
        String tempName = UUID.randomUUID().toString().replaceAll("-", "");

        //Create new temp file under OS's temp folder with a random name
        File tempFile = new File(path, tempName + ".tmp");

        if (tempFile.createNewFile())
            if (in == null) {
                return tempFile;
            }
        //Write file with a 1024 byte buffer.
        try (FileOutputStream tempOS = new FileOutputStream(tempFile)) {
            byte[] buf = new byte[1024];
            int i = in.read(buf);
            while (i != -1) {
                tempOS.write(buf, 0, i);
                i = in.read(buf);
            }
        } finally {
            in.close();
        }
        return tempFile;
    }

}
