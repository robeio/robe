package io.robe.common.utils.file;

import io.robe.common.utils.Validations;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains some methods to list files and folders from a directory
 *
 * @author Loiane Groner
 * http://loiane.com (Portuguese)
 * http://loianegroner.com (English)
 */
public class ListFilesUtil {

	public static final String allFilterRegex=".*";
    /**
     * List all the files and folders from a directory
     * @param directoryName to be listed
     */

    public static List<File> listFilesAndFolders(String directoryName){
        return listFilesAndFoldersByRegex(directoryName, allFilterRegex);
    }

    /**
     * list folders and file by String pattern
     * example pattern =*.json
     * @param directoryName use for list in files and folder example : directoryName= /home/MyUser/Dekstop/myfiles/
     * @param pattern use pattern for filenames example : *.json
     * @return
     */
    public static List<File> listFilesAndFolders(String directoryName,String pattern){
        String regexPatter=pattern==null?allFilterRegex:pattern.replace(".","\\.").replace("*",".*");
        return listFilesAndFoldersByRegex(directoryName,pattern);
    }

    /**
     * list folders and file by regex pattern
     * example directoryName= /home/MyUser/Dekstop/myfiles/
     * example pattern =*.json
     *
     * @param directoryName use for list in files and folder example : directoryName= /home/MyUser/Dekstop/myfiles/
     * @param regexPattern use regexPattern for filenames example : /.*\\.json/
     * @return
     */
    public static List<File> listFilesAndFoldersByRegex(String directoryName,String regexPattern){
        File directory = new File(directoryName);

        //get all the files from a directory
        File[] fList = directory.listFiles();
        List<File> fileList=new ArrayList<File>();
        if(fList==null){
            return fileList;
        }
        for(File file:fList){
            if(file.getName().matches(regexPattern)){
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * list folders and file by String pattern
     * example directoryName= /home/MyUser/Dekstop/myfiles/
     * pattern=*
     * @param directoryName to be listed
     */
    public static List<File> listFiles(String directoryName){
        return listFilesByRegex(directoryName,allFilterRegex);
    }

    /**
     *
     * @param directoryName
     * @param pattern
     * @return
     */
    public static List<File> listFiles(String directoryName,String pattern){
        String regexPattern=pattern==null?allFilterRegex:pattern.replace(".","\\.").replace("*",".*");
        return listFilesByRegex(directoryName,regexPattern);
    }

    /**
     *  list  files by regex Pattern
     * example directoryName= /home/MyUser/Dekstop/myfiles/
     * example pattern =/.*.json/
     *
     * @param directoryName
     * @param regexPattern
     * @return
     */
    public static List<File> listFilesByRegex(String directoryName,String regexPattern){

        File directory = new File(directoryName);

        //get all the files from a directory
        File[] fList = directory.listFiles();
        List<File> fileList=new ArrayList<File>();
        if(fList==null){
            return fileList;
        }

        for (File file : fList){
            if (file.isFile()&&file.getName().matches(regexPattern)){
                fileList.add(file);
            }
        }
        return fileList;
    }

    /**
     * List all the folder under a directory
     * @param directoryName to be listed
     */
    public static List<File> listFolders(String directoryName){
        return  listFoldersByRegex(directoryName, allFilterRegex);
    }

    /**
     *
     * @param directoryName
     * @param pattern
     * @return
     */
    public static List<File> listFolders(String directoryName,String pattern){
        String regexPattern=pattern==null? allFilterRegex:pattern.replace(".","\\.").replace("*",".*");
        return  listFoldersByRegex(directoryName,regexPattern);
    }

    /**
     *
     * @param directoryName
     * @param pattern
     * @return
     */
    public static List<File> listFoldersByRegex(String directoryName,String pattern){

        if(Validations.isEmptyOrNull(pattern)){
            pattern= allFilterRegex;
        }

        File directory = new File(directoryName);

        //get all the files from a directory
        File[] fList = directory.listFiles();
        List<File> fileList=new ArrayList<File>();
        if(fList==null){
            return fileList;
        }

        for (File file : fList){
            if (file.isDirectory()&&file.getName().matches(pattern)){
                fileList.add(file);
            }
        }
        return  fileList;
    }

    /**
     * List all files from a directory and its subdirectories
     * @param directoryName to be listed
     */
    public static List<File> listFilesAndFilesSubDirectories(String directoryName){
        return  getFilesAndFilesSubDirectories(directoryName,allFilterRegex);
    }

    /**
     *
     * @param directoryName
     * @param pattern
     * @return
     */
    public static List<File> listFilesAndFilesSubDirectories(String directoryName,String pattern){
        String regexPattern=pattern==null? allFilterRegex:pattern.replace(".","\\.").replace("*",".*");
        return getFilesAndFilesSubDirectories(directoryName,regexPattern);
    }

    /**
     *
     * @param directoryName
     * @param regexPattern
     * @return
     */
    public static List<File> listFilesAndFilesSubDirectoriesByRegex(String directoryName,String regexPattern){
        if(Validations.isEmptyOrNull(regexPattern)){
            regexPattern= allFilterRegex;
        }
        return getFilesAndFilesSubDirectories(directoryName,regexPattern);
    }

    /**
     *
     * @param directoryName
     * @param regexPattern
     * @return
     */
    private static List<File> getFilesAndFilesSubDirectories(String directoryName,String regexPattern){

        File directory = new File(directoryName);

        //get all the files from a directory
        File[] fList = directory.listFiles();
        List<File> fileList=new ArrayList<File>();
        if(fList==null){
            return fileList;
        }
        for (File file : fList){
            if(!file.getName().matches(regexPattern)){
                continue;
            }
            fileList.add(file);
            if (file.isDirectory()){
                getFilesAndFilesSubDirectories(file.getAbsolutePath(),regexPattern);
            }
        }
        return fileList;
    }

    /**
     * get All File and folder List<TreeData>
     * @param directoryName
     * @return
     */
    public static List<TreeData> listFilesAndDirectoriesTree(String directoryName){
        return  getFilesAndDirectoriesTreeByRegex(directoryName, allFilterRegex);
    }

    /**
     *
     * @param directoryName
     * @param pattern
     * @return
     */
    public static List<TreeData> listFilesAndDirectoriesTree(String directoryName,String pattern){
        String regexPattern=pattern==null? allFilterRegex:pattern.replace(".","\\.").replace("*",".*");
        return  getFilesAndDirectoriesTreeByRegex(directoryName, regexPattern);
    }

    /**
     *
     * @param directoryName
     * @param regexPattern
     * @return
     */
    public static List<TreeData> listFilesAndDirectoriesTreeByRegex(String directoryName,String regexPattern){
        if(Validations.isEmptyOrNull(regexPattern)){
            regexPattern= allFilterRegex;
        }
       return getFilesAndDirectoriesTreeByRegex(directoryName, regexPattern);
    }

    /**
     *
     * @param directoryName
     * @param regexPattern
     * @return
     */
    private static List<TreeData> getFilesAndDirectoriesTreeByRegex(String directoryName,String regexPattern){
        File directory = new File(directoryName);

        List<TreeData> treeDataList=new ArrayList<TreeData>();
        File[] fList = directory.listFiles();
        if(fList==null){
            return treeDataList;
        }
        for (File file : fList){
            if(!file.getName().matches(regexPattern)){
                continue;
            }
            if (file.isFile()){
                treeDataList.add(new TreeFile(file));
            } else if (file.isDirectory()){
                TreeDirectory treeDirectory=new TreeDirectory(file);
                treeDataList.add(treeDirectory);
                treeDirectory.setChildList(getFilesAndDirectoriesTreeByRegex(file.getAbsolutePath(),regexPattern));
            }
        }
        return treeDataList;
    }
}