package net.unmz.java.util.security;

import cn.hutool.core.io.FileUtil;
import cn.hutool.crypto.symmetric.AES;
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
        List<String> fileNames = FileUtil.listFileNames(sourceDir);
        for (String sourceFileName : fileNames) {
            if (StringUtils.isBlank(sourceFileName) || Arrays.asList(ignore).contains(sourceFileName)) {
                continue;
            }

            File sourceFile = new File(sourceDir + "/" + sourceFileName);
            String prefix = FileUtil.getPrefix(sourceFile);
            String suffix = FileUtil.getSuffix(sourceFile);
            String encryptHexName = encryptFilename ? aes.encryptHex(prefix.getBytes()) : prefix;
            String encFileNameDir = targetDir + "/" + encryptHexName + "." + suffix;
            byte[] bytes = FileUtil.readBytes(sourceFile);
            byte[] encrypt = aes.encrypt(bytes);
            FileUtil.writeBytes(encrypt, encFileNameDir);
        }
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
        List<String> fileNames = FileUtil.listFileNames(sourceDir);
        for (String sourceFileName : fileNames) {
            if (StringUtils.isBlank(sourceFileName) || Arrays.asList(ignore).contains(sourceFileName)) {
                continue;
            }

            File sourceFile = new File(sourceDir + "/" + sourceFileName);
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
        }
    }


}
