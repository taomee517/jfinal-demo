package com.jfc.util;

/**
 * @author LuoTao
 * @email taomee517@qq.com
 * @date 2019/1/11
 * @time 11:08
 */
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.RandomAccessFile;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.slf4j.Logger;

public class FileUtil {
    static Logger logger = LogUtil.getLogger(FileUtil.class);
    static final String S;
    public static final String TEMPPath;
    public static String LINESEPERATOR;
    private static String firstAvailableDisk4Windows;
    public static final String FILE_SEPARATOR;

    public FileUtil() {
    }

    public static String getFirstAvaliableDisk4Windows() {
        if (StrUtil.isBlank(firstAvailableDisk4Windows)) {
            File[] roots = File.listRoots();
            if (roots.length > 1) {
                firstAvailableDisk4Windows = roots[1].getPath();
            } else {
                firstAvailableDisk4Windows = roots[0].getPath();
            }
        }

        return firstAvailableDisk4Windows;
    }

    public static String getFilePathIgnoreOpeatingSystem(String path) {
        return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
    }

    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;

        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];

            int n;
            while((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException var7) {
            logger.error(var7.getMessage(), var7);
        } catch (IOException var8) {
            logger.error(var8.getMessage(), var8);
        }

        return buffer;
    }

    public static void byte2File(byte[] buf, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;

        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {
                dir.mkdirs();
            }

            file = new File(filePath + File.separator + fileName);
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(buf);
        } catch (Exception var19) {
            logger.error(var19.getMessage(), var19);
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException var18) {
                    logger.error(var18.getMessage(), var18);
                }
            }

            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException var17) {
                    logger.error(var17.getMessage(), var17);
                }
            }

        }

    }

    public static boolean writeText(String text, String file) {
        BufferedWriter writer = null;

        boolean var4;
        try {
            FileOutputStream writerStream = new FileOutputStream(file);
            writer = new BufferedWriter(new OutputStreamWriter(writerStream, "UTF-8"));
            writer.write(text);
            var4 = true;
            return var4;
        } catch (Exception var14) {
            logger.error(var14.getMessage(), var14);
            var4 = false;
        } finally {
            if (null != writer) {
                try {
                    writer.close();
                } catch (IOException var13) {
                    logger.error(var13.getMessage(), var13);
                }
            }

        }

        return var4;
    }

    public static void appendText(String text, String fileName) {
        File file = new File(fileName);
        RandomAccessFile raf = null;

        try {
            raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            raf.write(text.getBytes("utf-8"));
        } catch (Exception var13) {
            logger.error(var13.getMessage(), var13);
        } finally {
            if (null != raf) {
                try {
                    raf.close();
                } catch (IOException var12) {
                    logger.error(var12.getMessage(), var12);
                }
            }

        }

    }

    public static void reverseTextFile(File sourceFile, File targetFile) throws IOException {
        BufferedReader source = null;
        BufferedWriter target = null;

        try {
            source = new BufferedReader(new FileReader(sourceFile));
            target = new BufferedWriter(new FileWriter(targetFile));
            List<String> temp = new ArrayList();
            String data = null;

            while((data = source.readLine()) != null) {
                temp.add(data);
            }

            for(int i = temp.size() - 1; i >= 0; --i) {
                String str = (String)temp.get(i);
                target.write(str);
                target.write(LINESEPERATOR);
            }
        } catch (Exception var11) {
            logger.error(var11.getMessage(), var11);
            throw var11;
        } finally {
            if (null != source) {
                source.close();
            }

            if (null != target) {
                target.close();
            }

        }

    }

    public static String readText(String file) throws IOException {
        BufferedReader reader = null;
        InputStreamReader read = null;
        StringBuilder sb = new StringBuilder();
        String postfix = getPostfix(new File(file));
        String sep = "";
        if (Arrays.asList(".sh").contains(postfix)) {
            sep = "\n";
        } else if (Arrays.asList(".bat").contains(postfix)) {
            sep = "\r\n";
        }

        String var7;
        try {
            read = new InputStreamReader(new FileInputStream(file), "UTF-8");
            reader = new BufferedReader(read);
            String line = null;

            while((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append(sep);
            }

            var7 = sb.toString();
            return var7;
        } catch (Exception var21) {
            logger.error(var21.getMessage(), var21);
            var7 = null;
        } finally {
            if (null != reader) {
                try {
                    reader.close();
                } catch (IOException var20) {
                    logger.error(var20.getMessage(), var20);
                }
            }

            if (null != read) {
                try {
                    read.close();
                } catch (IOException var19) {
                    logger.error(var19.getMessage(), var19);
                }
            }

        }

        return var7;
    }

    public static boolean downloadFile(String saveFile, String httpUrl) throws IOException {
        int bytesum = 0;
        boolean byteread = false;
        URL url = null;

        try {
            url = new URL(httpUrl);
        } catch (MalformedURLException var15) {
            logger.error(var15.getMessage(), var15);
            return false;
        }

        InputStream inStream = null;
        FileOutputStream fs = null;

        boolean var8;
        try {
            File f = new File(saveFile);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }

            URLConnection conn = url.openConnection();
            inStream = conn.getInputStream();
            fs = new FileOutputStream(saveFile);
            byte[] buffer = new byte[1204];

            int readIndex;
            while((readIndex = inStream.read(buffer)) != -1) {
                bytesum += readIndex;
                fs.write(buffer, 0,readIndex);
            }

            boolean var10 = true;
            return var10;
        } catch (Exception var16) {
            logger.error(var16.getMessage(), var16);
            var8 = false;
        } finally {
            if (null != inStream) {
                inStream.close();
            }

            if (null != fs) {
                fs.close();
            }

        }

        return var8;
    }

    public static String getPostfix(File file) throws IOException {
        String name = file.getCanonicalPath();
        return getPostfix(name);
    }

    public static String getPostfix(String name) throws IOException {
        return name.indexOf(".") == -1 ? "" : name.substring(name.lastIndexOf("."));
    }

    public static boolean deleteFile(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }

        return flag;
    }

    public static void deleteFolder(String delpath) {
        File file = new File(delpath);
        if (!file.isDirectory()) {
            file.delete();
        } else if (file.isDirectory()) {
            String[] filelist = file.list();
            if (null == filelist) {
                return;
            }

            for(int i = 0; i < filelist.length; ++i) {
                File delfile = new File(delpath + S + filelist[i]);
                if (!delfile.isDirectory()) {
                    delfile.delete();
                } else if (delfile.isDirectory()) {
                    deleteFolder(delpath + S + filelist[i]);
                }
            }

            file.delete();
        }

    }

    public static void createIfNotExsited(File file) throws IOException {
        if (!file.exists()) {
            file.mkdirs();
        }

    }

    public static void createIfNotExsited(String fileName) throws IOException {
        createIfNotExsited(new File(fileName));
    }

    public static String getFileSizeStr(File f) throws Exception {
        return FormetFileSize(getFileSize(f));
    }

    public static long getFileSize(File f) throws Exception {
        FileChannel fc = null;
        FileInputStream fis = null;

        long var3;
        try {
            if (f.exists() && f.isFile()) {
                fis = new FileInputStream(f);
                fc = fis.getChannel();
                var3 = fc.size();
                return var3;
            }

            var3 = 0L;
        } finally {
            if (null != fc) {
                try {
                    fc.close();
                } catch (IOException var16) {
                    ;
                }
            }

            if (null != fis) {
                try {
                    fis.close();
                } catch (IOException var15) {
                    ;
                }
            }

        }

        return var3;
    }

    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024L) {
            fileSizeString = df.format((double)fileS) + "B";
        } else if (fileS < 1048576L) {
            fileSizeString = df.format((double)fileS / 1024.0D) + "K";
        } else if (fileS < 1073741824L) {
            fileSizeString = df.format((double)fileS / 1048576.0D) + "M";
        } else {
            fileSizeString = df.format((double)fileS / 1.073741824E9D) + "G";
        }

        return fileSizeString;
    }

    public static long getFileCount(File f) {
        long size = 0L;
        File[] flist = f.listFiles();
        if (null == flist) {
            return 0L;
        } else {
            size = (long)flist.length;

            for(int i = 0; i < flist.length; ++i) {
                if (flist[i].isDirectory()) {
                    size += getFileCount(flist[i]);
                    --size;
                }
            }

            return size;
        }
    }

    public static List<File> getFilesByPostfix(File f, String postfix, List<File> list) throws IOException {
        File[] flist = f.listFiles();
        if (null == flist) {
            return list;
        } else {
            for(int i = 0; i < flist.length; ++i) {
                if (flist[i].isDirectory()) {
                    getFilesByPostfix(flist[i], postfix, list);
                } else if (getPostfix(flist[i]).equals(postfix)) {
                    list.add(flist[i]);
                }
            }

            return list;
        }
    }

    public static void copyFile(File srcFile, File destFile) throws IOException {
        if (!srcFile.exists()) {
            throw new IllegalArgumentException("文件：" + srcFile + "不存在！");
        } else if (!srcFile.isFile()) {
            throw new IllegalArgumentException(srcFile + "不是文件!");
        } else {
            (new File(destFile.getParent())).mkdirs();
            FileInputStream in = null;
            FileOutputStream out = null;
            FileChannel fcin = null;
            FileChannel fcout = null;

            try {
                in = new FileInputStream(srcFile);
                out = new FileOutputStream(destFile);
                fcin = in.getChannel();
                fcout = out.getChannel();
                fcin.transferTo(0L, fcin.size(), fcout);
            } catch (Exception var10) {
                logger.error(var10.getMessage(), var10);
            } finally {
                if (null != in) {
                    in.close();
                }

                if (null != out) {
                    out.close();
                }

                if (null != fcin) {
                    fcin.close();
                }

                if (null != fcout) {
                    fcout.close();
                }

            }

        }
    }

    public static void copyFolder(File src, File dest) throws IOException {
        copyFolder(src, dest, new ArrayList());
    }

    public static void copyFolder(File src, File dest, List<String> excludedList) throws IOException {
        if (dest.exists()) {
            dest.delete();
        }

        if (src.isDirectory()) {
            if (!dest.exists()) {
                dest.mkdir();
            }

            String[] files = src.list();
            if (null == files) {
                return;
            }

            String[] var4 = files;
            int var5 = files.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String file = var4[var6];
                File srcFile = new File(src, file);
                File destFile = new File(dest, file);
                if (!excludedList.contains(srcFile.getName())) {
                    copyFolder(srcFile, destFile);
                }
            }
        } else {
            List<String> list = getAllParentFolder(src);
            if (!list.contains(src.getName())) {
                copyFile(src, dest);
            }
        }

    }

    public static boolean creatTxtFile(String filename, String txt, String encode) {
        RandomAccessFile mm = null;
        File f = new File(filename);
        if (f.exists()) {
            f.delete();
        }

        boolean var6;
        try {
            mm = new RandomAccessFile(filename, "rw");
            if (StrUtil.isBlank(encode)) {
                mm.writeBytes(txt);
            } else {
                mm.writeBytes(new String(txt.getBytes(encode)));
            }

            boolean var5 = true;
            return var5;
        } catch (IOException var16) {
            logger.error(var16.getMessage(), var16);
            var6 = false;
        } finally {
            if (mm != null) {
                try {
                    mm.close();
                } catch (IOException var15) {
                    logger.error(var15.getMessage(), var15);
                }
            }

        }

        return var6;
    }

    public static boolean creatTxtFile(String filename, String txt) {
        return creatTxtFile(filename, txt, (String)null);
    }

    private static List<String> getAllParentFolder(File f) {
        List<File> roots = Arrays.asList(File.listRoots());
        List<String> list = new ArrayList();
        if (roots.contains(f)) {
            return list;
        } else {
            File temp = f.getParentFile();
            list.add(temp.getName());

            while(!roots.contains(temp)) {
                temp = temp.getParentFile();
                list.add(temp.getName());
            }

            return list;
        }
    }

    public static List<File> getAllChildrenFolders(File f) {
        List<File> list = new ArrayList();
        if (f.isDirectory()) {
            File[] fileList = f.listFiles();
            if (null == fileList) {
                return list;
            }

            File[] var3 = fileList;
            int var4 = fileList.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                File file = var3[var5];
                if (file.isDirectory()) {
                    list.add(file);
                }
            }
        }

        return list;
    }

    public static List<File> getAllChildrenFiles(File f, String postfix) throws IOException {
        List<File> list = new ArrayList();
        if (null == f) {
            return list;
        } else {
            if (f.isDirectory()) {
                File[] fileList = f.listFiles();
                if (null == fileList) {
                    return list;
                }

                File[] var4 = fileList;
                int var5 = fileList.length;

                for(int var6 = 0; var6 < var5; ++var6) {
                    File file = var4[var6];
                    if (!file.isDirectory() && file.getCanonicalPath().toLowerCase().endsWith(postfix.toLowerCase())) {
                        list.add(file);
                    }
                }
            }

            return list;
        }
    }

    static {
        S = File.separator;
        TEMPPath = System.getProperty("java.io.tmpdir").replace("\\", "/");
        LINESEPERATOR = "\n";
        firstAvailableDisk4Windows = null;
        if (!EnvUtil.isWindows()) {
            LINESEPERATOR = "\r\n";
        }

        FILE_SEPARATOR = System.getProperty("file.separator");
    }
}

