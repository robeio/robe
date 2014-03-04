package io.robe.common.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

/**
 * Util Class for all file operations.
 */
public class FileOperations {

    public static final String TEMPDIR = System.getProperty("java.io.tmpdir").toString();


    /**
     * Writes inputstream to a temporary file under OS's native temp folder with a unique name.
     *
     * @param in InputStream from any source.
     * @return File instance of temporary file.
     * @throws java.io.IOException
     */
    public static File writeToTemp(InputStream in) throws IOException {

        //Create an unique name and replace all unwanted chars.
        String tempName = UUID.randomUUID().toString().replaceAll("-", "");

        //Create new temp file under OS's temp folder with a random name
        File tempFile = new File(System.getProperty("java.io.tmpdir") + tempName + ".xml");
        if (!tempFile.createNewFile())
            throw new IOException("Can not create temp file :" + tempFile.getParent());

        if (in == null)
            return tempFile;
        //Write file with a 1024 byte buffer.
        FileOutputStream tempOS = new FileOutputStream(tempFile);
        try {
            byte[] buf = new byte[1024];
            int i;
            while ((i = in.read(buf)) != -1) {
                tempOS.write(buf, 0, i);
            }
        } finally {
            if (in != null)
                in.close();
            tempOS.close();
        }
        return tempFile;
    }

    /**
     * Writes inputstream to a file under applications folder with the given name.
     *
     * @param name Name of the file. Don't forget the extension (yyyyyy.xxx).
     * @param in   InputStream from any source.
     * @return File instance of file.
     * @throws java.io.IOException
     */
    public static File writeToApplicationHome(String name, InputStream in) throws IOException {
        //Create new  file under applications running folder.
        File file = new File("./" + name);
        if (!file.createNewFile())
            throw new IOException("Can not create  file :" + file.getParent());

        if (in == null)
            return file;
        //Write file with a 1024 byte buffer.
        FileOutputStream out = new FileOutputStream(file);
        try {
            byte[] buf = new byte[1024];
            int i;
            while ((i = in.read(buf)) != -1) {
                out.write(buf, 0, i);
            }
        } finally {
            if (in != null)
                in.close();
            out.close();
        }
        return file;
    }


}
