package io.robe.common.utils.file;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by kamilbukum on 02/03/14.
 */
public class TreeDirectory extends  TreeData {

    private List<TreeData> childList=new ArrayList<TreeData>();

    public TreeDirectory(File file) {
        super(file);
    }

    public List<TreeData> getChildList() {
        return childList;
    }

    public void setChildList(List<TreeData> childList) {
        this.childList = childList;
    }
    
}
