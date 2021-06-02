package util;

import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class FileUtil {

    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
    }

    public static void closeStream(Closeable stream) {
        if (stream != null) {
            try {
                stream.close();
            } catch (Exception exp) {
                exp.printStackTrace();
            } finally {
                stream = null;
            }
        }
    }


    public static List<File> splitFile(File file, int sizeOfFileInMB) {
        int counter = 1;
        List<File> files = new ArrayList<>();
        int sizeOfChunk = 1024 * 1024 * sizeOfFileInMB;
        byte[] buffer = new byte[sizeOfChunk];
        String fileName = file.getName();

        try (FileInputStream fis = new FileInputStream(file);
			 BufferedInputStream bis = new BufferedInputStream(fis)) {
            int bytesAmount = 0;
            while ((bytesAmount = bis.read(buffer)) > 0) {
                File newFile = new File(file.getParent(), fileName + "."
                        + String.format("%04d", counter++));
                try (FileOutputStream out = new FileOutputStream(newFile)) {
                    out.write(buffer, 0, bytesAmount);
                }
                files.add(newFile);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return files;
    }
    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        if (uri == null) return null;

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                if (id.startsWith("raw:")) {
                    return id.replaceFirst("raw:", "");
                }
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
									   String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }





    public static boolean deleteDirectory(String path) {
        File dir = new File(path);
        return deleteDirectory(dir);
    }

    public static boolean deleteDirectory(File path) {
        if (path.exists()) {
            File[] files = path.listFiles();
            if (files == null) {
                return false;
            }
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    boolean wasSuccessful = file.delete();
                }
            }
        }
        return (path.delete());
    }

    public static String loadJsonFromAsset(Context context, String fileName) {
        String json = null;
        try {
            InputStream is = context.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    public static String loadJsonFromFile(String filePath) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            File file = new File(filePath);
            InputStream inputStream = FileUtils.openInputStream(file);
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    public static String loadJsonFromFile(File file) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int i;
        try {
            InputStream inputStream = FileUtils.openInputStream(file);
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toString();
    }

    /**
     * 向文件写入字符串
     * @param path
     * @param content
     * @param append 是否追加
     */
    public static boolean writeString2File(String path, String content, boolean append) {
        if (TextUtils.isEmpty(path) || content == null) {
            return false;
        }
        try {
            if (!new File(path).getParentFile().exists()) {
                new File(path).getParentFile().mkdirs();
            }
            FileWriter fileWriter = new FileWriter(path, append);
            fileWriter.write(content);
            fileWriter.flush();
            fileWriter.close();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将文件从asset拷贝到data/data目录下
     * assets/fileDir/fileName copy to data/data/fileDir/file
     * @param context
     * @param fileDir
     * @param fileName
     */
    public static void copyFromAssetsToDataData (Context context, String fileDir, String
    fileName){
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte buffer[] = new byte[1024];
        int length = 0;
        try {
            inputStream = context.getAssets().open(fileDir + "/" + fileName);
            File dstDir = new File(context.getFilesDir().getAbsolutePath() + "/" + fileDir);
            if (!dstDir.exists()) {
                dstDir.mkdirs();
            }

            File dstFile = new File(context.getFilesDir().getAbsolutePath() + "/" + fileDir, fileName);
            outputStream = new FileOutputStream(dstFile);
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 将文件夹copy到data目录下, 如果data目录下已存在该文件夹, 则先删除
     *
     * @param context
     * @param dirName
     * @param deleteExisting true 表示删除已存在的同名目录
     */
    public static boolean copyDirFromAssetsToDataData (Context context, String dirName,
													   boolean deleteExisting){
        if (dirName == null) return false;
        try {
            File dstDir = new File(context.getFilesDir().getAbsolutePath() + "/" + dirName);
            if (dstDir.exists() && dstDir.isDirectory()) {
                if (deleteExisting) {
                    deleteDirectory(dstDir);
                    dstDir.mkdir();
                }
            } else {
                dstDir.mkdir();
            }

            String[] fileList = context.getAssets().list(dirName);
            if (fileList == null) return false;
            for (String fileName : fileList) {
                if (!copyFileFromAssetsToDataData(context, dirName, fileName))
                    return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 将文件copy到data目录下
     * @param context
     * @param dirName
     * @param fileName
     */
    public static boolean copyFileFromAssetsToDataData (Context context, String dirName, String
    fileName){
        boolean copySuccessful = false;
        InputStream inputStream = null;
        OutputStream outputStream = null;
        byte buffer[] = new byte[8192];
        int length = 0;
        try {
            inputStream = context.getAssets().open(dirName + "/" + fileName);
            File dstDir = new File(context.getFilesDir().getAbsolutePath() + "/" + dirName);
            if (!dstDir.exists()) {
                dstDir.mkdirs();
            }

            File dstFile = new File(context.getFilesDir().getAbsolutePath() + "/" + dirName, fileName);
            outputStream = new FileOutputStream(dstFile);
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            inputStream.close();
            outputStream.close();
            copySuccessful = true;
        } catch (Exception e) {
            copySuccessful = false;
            e.printStackTrace();
        }
        return copySuccessful;
    }

    public static void unzip (File zipFile, File targetDirectory) throws IOException {
        ZipInputStream zis = new ZipInputStream(
                new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry ze;
            int count;
            byte[] buffer = new byte[8192];
            while ((ze = zis.getNextEntry()) != null) {
                File file = new File(targetDirectory, ze.getName());
                File dir = ze.isDirectory() ? file : file.getParentFile();
                if (!dir.isDirectory() && !dir.mkdirs())
                    throw new FileNotFoundException("Failed to ensure directory: " +
                            dir.getAbsolutePath());
                if (ze.isDirectory())
                    continue;
                FileOutputStream fout = new FileOutputStream(file);
                try {
                    while ((count = zis.read(buffer)) != -1)
                        fout.write(buffer, 0, count);
                } finally {
                    closeStream(fout);
                }
            }
        } finally {
            zis.close();
        }
    }

    public static byte[] fileToBytes (File file){
        int size = (int) file.length();
        byte[] bytes = new byte[size];
        BufferedInputStream buf = null;
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            buf = new BufferedInputStream(fileInputStream);
            buf.read(bytes, 0, bytes.length);
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeStream(buf);
            closeStream(fileInputStream);
        }
        return bytes;
    }


    public static boolean renameFile(String dir, String filename, String newName) {
        File file = new File(dir, filename);
        if (file.exists()) {
            return file.renameTo(new File(dir, newName));
        }
        return false;
    }

    private static boolean renameFile(String oldName, String newName) {
        File file = new File(oldName);
        if (file.exists()) {
            return new File(oldName).renameTo(new File(newName));
        }
        return false;
    }

    public static String readStringFromFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            BufferedReader buf = null;
            FileReader isReader = null;
            try {
                isReader = new FileReader(path);
                buf = new BufferedReader(isReader);
                StringBuilder sb = new StringBuilder(150);
                char[] bufArray = new char[100];
                while (buf.ready()) {
                    int i = buf.read(bufArray, 0, 100);
                    if (i < 100) {
                        sb.append(bufArray, 0, i);
                        break;
                    } else {
                        sb.append(bufArray);
                    }
                }
                return sb.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                closeStream(buf);
                closeStream(isReader);
            }
        }
        return null;
    }
}
