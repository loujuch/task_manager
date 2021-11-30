package com.example.task_plan_manager.Utils;

import com.example.task_plan_manager.Globe;
import com.example.task_plan_manager.User;

import java.io.*;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class FileUtils {

    public final static boolean LOCAL=false;
    public final static boolean CLOUD=true;

    public final static boolean IN=false;
    public final static boolean OUT=true;

    public static boolean init() {
        return createFolder(Globe.getPath()+"/data");
    }

    public static boolean existNow() {
        return existFile(Globe.getPath()+"/now");
    }

    public static boolean createNow() {
        if(existNow()) {
            ErrorUtils.FileExist(Globe.getPath()+"/nom");
            return true;
        }
        try {
            File now=new File(Globe.getPath()+"/now");
            boolean flag=now.createNewFile();
            if(!flag) {
                ErrorUtils.FileCreateError(Globe.getPath()+"/now");
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean deleteNow() {
        return new File(Globe.getPath()+"/now").delete();
    }

    public static boolean writeNow(User user) {
        if(user==null) {
            ErrorUtils.Error();
            return false;
        }
        File now=new File(Globe.getPath()+"/now");
        FileOutputStream fileInputStream = null;
        try {
            fileInputStream=new FileOutputStream(now);
            byte[]nowStream=user.getStringStream();
            if (nowStream==null) {
                ErrorUtils.Error();
                return false;
            }
            fileInputStream.write(nowStream);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static ArrayList<String> readNow() {
        ArrayList<String>list=readFile(Globe.getPath()+"/now");
        if (list==null||list.isEmpty()) return null;
        return list;
    }

    public static boolean eventInit(String path) {
        if (path==null) {
            ErrorUtils.Error();
            return false;
        }
        if (!createFolder(path)) {
            return false;
        }
        if (!(createFile(path+"detail")&&createFile(path+"remark")&&
        createFolder(path+"in/")&&createFolder(path+"out/"))) {
            boolean b = deleteFile(path + "detail") && deleteFile(path + "remark") &&
                    deleteFolder(path + "in/") && deleteFolder(path + "out/");
            return false;
        }
        return true;
    }

    public static boolean tmpInit(String path) {
        if (path==null) {
            ErrorUtils.Error();
            return false;
        }
        return createFolder(path+"tmpIn")&&createFolder(path+"tmpOut");
    }

    public static boolean tmpTo(String path) {
        if (!(deleteFolder(path+"in/")&&deleteFolder(path+"out/"))) {
            return false;
        }
        File in=new File(path+"tmpIn/");
        File out=new File(path+"tmpOut/");
        createFolder(path+"in/");
        createFolder(path+"out/");
        String[]inList=in.list();
        String[]outList=out.list();
        for (String s:inList) {
            moveFile(new File(path+"tmpIn/"+s),path+"in/"+s);
        }
        for (String s:outList)moveFile(new File(path+"tmpOut/"+s),path+"out/"+s);
        return deleteFolder(path+"tmpIn/")&&deleteFolder(path+"tmpOut/");
    }

    public static boolean existFile(String path) {
        return new File(path).exists();
    }

    public static boolean deleteFile(String path) {
        if (path==null) {
            ErrorUtils.Error();
            return false;
        }
        return new File(path).delete();
    }

    public static boolean deleteFolder(String path) {
        if (path==null) {
            ErrorUtils.Error();
            return false;
        }
        return deleteFolderHelper(new File(path));
    }

    private static boolean deleteFolderHelper(File file) {
        if (!file.isDirectory())return file.delete();
        String[]list=file.list();
        assert list != null;
        for(String s:list) {
            if (!deleteFolderHelper(new File(file,s))) {
                return false;
            }
        }
        return file.delete();
    }

    public static ArrayList<String> readFile(String path) {
        File file=new File(path);
        if (!file.exists()) {
            ErrorUtils.FileNoExist(path);
            return null;
        }
        BufferedReader bufferedReader;
        FileInputStream fileInputStream;
        InputStreamReader inputStreamReader;
        ArrayList<String>list=new ArrayList<>();
        String s;
        try {
            fileInputStream =new FileInputStream(file);
            inputStreamReader=new InputStreamReader(fileInputStream);
            bufferedReader=new BufferedReader(inputStreamReader);
            while((s=bufferedReader.readLine())!=null) {
                list.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return list;
    }

    public static boolean createFolder(String path) {
        if (path==null) {
            ErrorUtils.Error();
            return false;
        }
        File file=new File(path);
        if (file.exists())return true;
        return file.mkdir();
    }

    public static boolean createFile(String path) {
        if (path==null) {
            ErrorUtils.Error();
            return false;
        }
        File file=new File(path);
        if (file.exists())return true;
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void openFile(File file) {
        if (file==null) {
            ErrorUtils.Error();
            return;
        }
        try {
            String[]path={"xdg-open",file.getPath()};
            Runtime.getRuntime().exec(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean moveFile(File file, String to) {
        if (file==null||to==null) {
            ErrorUtils.Error();
            return false;
        }
        try {
            Files.move(file.toPath(), Path.of(to));
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            ErrorUtils.FileExist(to);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean copyFile(File file,String to) {
        if (file==null||to==null) {
            ErrorUtils.Error();
            return false;
        }
        try {
            Files.copy(file.toPath(), Path.of(to));
        } catch (FileAlreadyExistsException e) {
            e.printStackTrace();
            ErrorUtils.FileExist(to);
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeFile(String detail,String path) {
        if(detail==null||path==null) {
            ErrorUtils.Error();
            return false;
        }
        File now=new File(path);
        FileOutputStream fileInputStream = null;
        try {
            fileInputStream=new FileOutputStream(now);
            fileInputStream.write(detail.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                assert fileInputStream != null;
                fileInputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return true;
    }
}
