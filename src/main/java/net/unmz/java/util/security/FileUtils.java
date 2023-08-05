package net.unmz.java.util.security;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.crypto.CryptoException;
import cn.hutool.crypto.symmetric.AES;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * 文件工具类
 *
 * @author Faritor
 * @date 2023/08/05
 */
public class FileUtils {


    /**
     * 加密文件名
     *
     * @param sourceDir 来源目录
     * @param targetDir 目标目录
     * @param aes       aes
     * @param ignore    忽略
     * @author Faritor
     * @date 2023/08/05
     */
    public static void encryptFilename(String sourceDir, String targetDir, AES aes,
                                       String... ignore) {
        encrypt(sourceDir, targetDir, aes, true, ignore);
    }


    /**
     * 加密忽略文件名
     *
     * @param sourceDir 来源目录
     * @param targetDir 目标目录
     * @param aes       aes
     * @param ignore    忽略
     * @author Faritor
     * @date 2023/08/05
     */
    public static void encryptIgnoreFilename(String sourceDir, String targetDir, AES aes,
                                             String... ignore) {
        encrypt(sourceDir, targetDir, aes, false, ignore);
    }

    /**
     * 加密
     *
     * @param sourceDir       来源目录
     * @param targetDir       目标目录
     * @param aes             aes
     * @param encryptFilename 加密文件名称
     * @param ignore          忽略
     * @author Faritor
     * @date 2023/08/05
     */
    public static void encrypt(String sourceDir, String targetDir, AES aes, boolean encryptFilename, String... ignore) {
        System.out.println("开始加密......");
        System.out.println("资源目录：" + sourceDir);
        System.out.println("输出目录：" + targetDir);
        long startL = System.currentTimeMillis();
        List<String> fileNames = FileUtil.listFileNames(sourceDir);
        System.out.println("资源文件总数量：" + fileNames.size());
        System.out.println("文件列表为：" + JSON.toJSONString(fileNames));
        System.out.println("-----------------start-----------------");
        int i = 1;
        for (String sourceFileName : fileNames) {
            if (StringUtils.isBlank(sourceFileName) || Arrays.asList(ignore).contains(sourceFileName)) {
                continue;
            }

            long start = System.currentTimeMillis();
            System.out.println("当前进度：" + i++ + "/" + fileNames.size());
            System.out.println("当前文件：" + sourceDir + "/" + sourceFileName);

            File sourceFile = new File(sourceDir + "/" + sourceFileName);
            long size = FileUtil.size(sourceFile);
            System.out.println("源文件-名称：" + sourceFileName + ",大小："
                    + size / (1024 * 1024) + "(MB)"
                    + ", " + NumberUtil.div(size, (1024 * 1024 * 1024)) + "(GB)");
            String prefix = FileUtil.getPrefix(sourceFile);
            String suffix = FileUtil.getSuffix(sourceFile);
            String encryptHexName = encryptFilename ? aes.encryptHex(prefix.getBytes()) : prefix;
            String encFileNameDir = targetDir + "/" + encryptHexName + "." + suffix;
            byte[] bytes = FileUtil.readBytes(sourceFile);
            byte[] encrypt = aes.encrypt(bytes);
            FileUtil.writeBytes(encrypt, encFileNameDir);
            long end = System.currentTimeMillis();
            System.out.println("输出文件-名称：" + encryptHexName + "." + suffix);
            System.out.println("小计耗时:" + (end - start) / 1000 + "s");
            System.out.println();
        }
        System.out.println("------------------end----------------");
        System.out.println("加密完成,总计耗时:" + (System.currentTimeMillis() - startL) / 1000 + "s");
    }


    /**
     * 解密文件名
     *
     * @param sourceDir 来源目录
     * @param targetDir 目标目录
     * @param aes       aes
     * @param ignore    忽略
     * @author Faritor
     * @date 2023/08/05
     */
    public static void decryptFilename(String sourceDir, String targetDir, AES aes,
                                       String... ignore) {
        decrypt(sourceDir, targetDir, aes, true, ignore);
    }


    /**
     * 解密忽略文件名
     *
     * @param sourceDir 来源目录
     * @param targetDir 目标目录
     * @param aes       aes
     * @param ignore    忽略
     * @author Faritor
     * @date 2023/08/05
     */
    public static void decryptIgnoreFilename(String sourceDir, String targetDir, AES aes,
                                             String... ignore) {
        decrypt(sourceDir, targetDir, aes, false, ignore);
    }

    /**
     * 解密
     *
     * @param sourceDir       来源目录
     * @param targetDir       目标目录
     * @param aes             aes
     * @param decryptFilename 解密文件名
     * @param ignore          忽略
     * @author Faritor
     * @date 2023/08/05
     */
    public static void decrypt(String sourceDir, String targetDir, AES aes, boolean decryptFilename, String... ignore) {
        try {
            System.out.println("开始解密......");
            System.out.println("资源目录：" + sourceDir);
            System.out.println("输出目录：" + targetDir);
            long startL = System.currentTimeMillis();
            List<String> fileNames = FileUtil.listFileNames(sourceDir);
            System.out.println("资源文件总数量：" + fileNames.size());
            System.out.println("文件列表为：" + JSON.toJSONString(fileNames));
            System.out.println("-----------------start-----------------");
            int i = 1;
            for (String sourceFileName : fileNames) {
                if (StringUtils.isBlank(sourceFileName) || Arrays.asList(ignore).contains(sourceFileName)) {
                    continue;
                }

                long start = System.currentTimeMillis();
                System.out.println("当前进度：" + i++ + "/" + fileNames.size());
                File sourceFile = new File(sourceDir + "/" + sourceFileName);
                long size = FileUtil.size(sourceFile);
                System.out.println("源文件 名称：" + sourceFileName + "，大小："
                        + size / (1024 * 1024) + "(MB)" + ", "
                        + NumberUtil.div(size, (1024 * 1024 * 1024)) + "(GB)");
                String prefix = FileUtil.getPrefix(sourceFile);
                if (prefix.length() < 32) {
                    continue;
                }

                String suffix = FileUtil.getSuffix(sourceFile);
                String decryptName = decryptFilename ? aes.decryptStr(prefix) : prefix;
                String decFileNameDir = targetDir + "/" + decryptName + "." + suffix;
                byte[] bytes = FileUtil.readBytes(sourceFile);
                byte[] decrypt = aes.decrypt(bytes);
                FileUtil.writeBytes(decrypt, decFileNameDir);
                long end = System.currentTimeMillis();
                System.out.println("输出文件-名称：" + decryptName + "." + suffix);
                System.out.println("小计耗时:" + (end - start) / 1000 + "s");
                System.out.println();
            }
            System.out.println("------------------end----------------");
            System.out.println("解密完成,用时总计:" + (System.currentTimeMillis() - startL) / 1000 + "s");
        } catch (CryptoException exception) {
            System.out.println("解密失败，秘钥可能不正确！");
            exception.printStackTrace();
        }
    }


}
