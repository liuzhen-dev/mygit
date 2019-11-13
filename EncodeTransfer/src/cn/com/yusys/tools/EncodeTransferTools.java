
/*
 * cn.com.yusys.tools.EncodeTransferTools.java
 * Created by dcz @ 2019-5-10 上午9:40:03
 */

package cn.com.yusys.tools;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EncodeTransferTools
{
    public static void main(String[] args)
    {
        try
        {
            transferFolder(new File("d:/tmp"), "GBK", "UTF-8");
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 转换该文件夹及子文件夹所有文件编码
     * @param folder
     */
    public static void transferFolder(File folder,String srcEncode,String destEncode) throws Exception{
        File[] files = folder.listFiles();
        for (int i = 0; i < files.length; i++)
        {
            if(files[i].isDirectory()){
                transferFolder(files[i],srcEncode,destEncode);
            }else{
                fileEncodeTransfer(files[i],srcEncode,destEncode);
            }
        }
    }

    private static void fileEncodeTransfer(File file, String srcEncode, String destEncode) throws Exception
    {
        try
        {
            byte[] data = readFileContent(file);
            String sdata = new String(data,srcEncode);
            byte[] udata = sdata.getBytes(destEncode);
            writeContentToFile(file.getAbsolutePath(), udata);
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }        
    }
    
    public static void writeContentToFile(String fileName, byte[] content)
            throws Exception
    {
        OutputStream fos = null;
        try
        {
            fos = openOutputStream(new File(fileName));
            fos.write(content, 0, content.length);
            fos.flush();
        } finally
        {
            if (fos != null)
            {
                fos.close();
            }
        }
    }
    
    public static FileOutputStream openOutputStream(File file)
            throws IOException
    {
        return openOutputStream(file, false);
    }

    public static FileOutputStream openOutputStream(File file, boolean append)
            throws IOException
    {
        if (file.exists())
        {
            if (file.isDirectory())
            {
                throw new IOException("File '" + file
                        + "' exists but is a directory");
            }
            if (file.canWrite() == false)
            {
                throw new IOException("File '" + file
                        + "' cannot be written to");
            }
        } else
        {
            File parent = file.getParentFile();
            if (parent != null)
            {
                if (!parent.mkdirs() && !parent.isDirectory())
                {
                    throw new IOException("Directory '" + parent
                            + "' could not be created");
                }
            }
        }
        return new FileOutputStream(file, append);
    }
    
    public static byte[] readFileContent(String fileName) throws IOException
    {
        byte[] fileContent = null;
        InputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            fis = openInputStream(new File(fileName));
            copyStream(fis, baos);
            fileContent = baos.toByteArray();
        } finally
        {
            if (fis != null)
            {
                fis.close();
            }
            if (baos != null)
            {
                baos.close();
            }
        }
        return fileContent;
    }
    
    /**
     * 读文件内容<p>
     * 
     * @param fileName
     *            文件名
     * @return 文件内容
     * @throws Exception
     */
    public static byte[] readFileContent(File file) throws IOException
    {
        byte[] fileContent = null;
        InputStream fis = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            fis = openInputStream(file);
            copyStream(fis, baos);
            fileContent = baos.toByteArray();
        } finally
        {
            if (fis != null)
            {
                fis.close();
            }
            if (baos != null)
            {
                baos.close();
            }
        }
        return fileContent;
    }
    
    public static FileInputStream openInputStream(File file) throws IOException
    {
        if (file.exists())
        {
            if (file.isDirectory())
            {
                throw new IOException("File '" + file
                        + "' exists but is a directory");
            }
            if (file.canRead() == false)
            {
                throw new IOException("File '" + file + "' cannot be read");
            }
        } else
        {
            throw new FileNotFoundException("File '" + file
                    + "' does not exist");
        }
        return new FileInputStream(file);
    }
    
    /**
     * 来源于common-io
     * @param input
     * @param output
     * @return
     * @throws IOException
     */
    public static long copyStream(InputStream input, OutputStream output)
            throws IOException
    {
        byte[] buffer = new byte[4096];
        long count = 0;
        int n = 0;
        while (-1 != (n = input.read(buffer)))
        {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
}


