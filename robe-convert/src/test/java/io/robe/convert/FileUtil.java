package io.robe.convert;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class FileUtil {

    private static final String JAVA_IO_TMP_DIR = "java.io.tmpdir";
    public static final String TEMP_DIR = System.getProperty(JAVA_IO_TMP_DIR).toString();

    public static File getRandomTempFile() throws IOException {
        String tempName = UUID.randomUUID().toString().replaceAll("-", "");
        //Create new temp file under OS's temp folder with a random name
        File tempFile = new File(System.getProperty(JAVA_IO_TMP_DIR) + tempName + ".tmp");
        if (!tempFile.createNewFile()) {
            throw new IOException("Can not create temp file :" + tempFile.getParent());
        }
        return tempFile;

    }
}

