package ezy.boost.update;

import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    private static final String TAG = "MD5Utils";
    public static final int MD5_FILE_BUF_LENGTH = 1024 * 100;

    /**
     * Get the md5 for the file. call getMD5(FileInputStream is, int bufLen) inside.
     *
     * @param file
     */
    public static String getMD5(final File file) {
        if (file == null || !file.exists()) {
            return null;
        }

        FileInputStream fin = null;
        try {
            fin = new FileInputStream(file);
            String md5 = getMD5(fin);
            return md5;
        } catch (Exception e) {
            return null;
        } finally {
            closeQuietly(fin);
        }
    }

    /**
     * Closes the given {@code Closeable}. Suppresses any IO exceptions.
     */
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            Log.w(TAG, "Failed to close resource", e);
        }
    }

    public static String getMD5(final InputStream is) {
        //tag: ==result===false===appMd5:64f0bf6db95a2c213dbe822187d5c6ed=====md5:db38fb5a134378051a1c3e355455d849
        if (is == null) {
            return null;
        }
        try {
            BufferedInputStream bis = new BufferedInputStream(is);
            MessageDigest md = MessageDigest.getInstance("MD5");
            StringBuilder md5Str = new StringBuilder(32);

            byte[] buf = new byte[MD5_FILE_BUF_LENGTH];
            int readCount;
            while ((readCount = bis.read(buf)) != -1) {
                md.update(buf, 0, readCount);
            }

            byte[] hashValue = md.digest();

            for (int i = 0; i < hashValue.length; i++) {
                md5Str.append(Integer.toString((hashValue[i] & 0xff) + 0x100, 16).substring(1));
            }
            return md5Str.toString();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 给指定字符串按照md5算法去加密
     *
     * @param psd 需要加密的密码	加盐处理
     * @return md5后的字符串
     */
    public static String encoder(String psd) {
        try {
            //加盐处理
            //psd = psd+"mobilesafe";
            //1,指定加密算法类型
            MessageDigest digest = MessageDigest.getInstance("MD5");
            //2,将需要加密的字符串中转换成byte类型的数组,然后进行随机哈希过程
            byte[] bs = digest.digest(psd.getBytes());
//			System.out.println(bs.length);
            //3,循环遍历bs,然后让其生成32位字符串,固定写法
            //4,拼接字符串过程
            StringBuffer stringBuffer = new StringBuffer();
            for (byte b : bs) {
                int i = b & 0xff;
                //int类型的i需要转换成16机制字符
                String hexString = Integer.toHexString(i);
//				System.out.println(hexString);
                if (hexString.length() < 2) {
                    hexString = "0" + hexString;
                }
                stringBuffer.append(hexString);
            }
            //5,打印测试
//			System.out.println(stringBuffer.toString());
            return stringBuffer.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getMD5(Context context, String fileName) {
        try {
            InputStream open = context.getResources().getAssets().open(fileName);
            return MD5Utils.getMD5(open);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
