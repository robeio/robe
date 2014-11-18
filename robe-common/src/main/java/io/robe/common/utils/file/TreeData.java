package io.robe.common.utils.file;

import java.io.File;

public abstract  class TreeData {

    private final File file;

    public TreeData(File file){
        this.file=file;
    }

    public boolean isFile(){
        return file.isFile();
    }
    public boolean isDirectory(){
       return file.isDirectory();
    }
    
}
