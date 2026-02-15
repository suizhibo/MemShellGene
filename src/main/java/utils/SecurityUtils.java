package utils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 安全工具类
 * 
 * 功能：
 * 1. 输入验证和过滤
 * 2. 编码/解码
 * 3. 文件安全检查
 * 4. 敏感信息处理
 */
public class SecurityUtils {

    private static final Logger LOGGER = Logger.getLogger(SecurityUtils.class.getName());
    
    // 危险字符黑名单
    private static final String[] DANGEROUS_PATTERNS = {
        "../", "..\\", "//", "\\\\", 
        "${", "}",
        "<<script>", "</script>",
        "'", "\"", ";", "|", "&", "$"
    };

    /**
     * 验证输入字符串是否安全
     */
    public static boolean isSafeInput(String input) {
        if (input == null || input.isEmpty()) {
            return true;
        }
        
        for (String pattern : DANGEROUS_PATTERNS) {
            if (input.contains(pattern)) {
                LOGGER.warning("Dangerous pattern detected in input: " + pattern);
                return false;
            }
        }
        return true;
    }

    /**
     * 清理输入字符串
     */
    public static String sanitizeInput(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        
        String sanitized = input;
        for (String pattern : DANGEROUS_PATTERNS) {
            sanitized = sanitized.replace(pattern, "");
        }
        return sanitized.trim();
    }

    /**
     * 验证文件路径是否安全（防止目录遍历）
     */
    public static boolean isSafePath(String path) {
        if (path == null || path.isEmpty()) {
            return false;
        }
        
        // 检查目录遍历
        if (path.contains("..") || path.contains("../") || path.contains("..\\")) {
            LOGGER.warning("Path traversal attempt detected: " + path);
            return false;
        }
        
        // 检查空字节注入
        if (path.contains("\0")) {
            LOGGER.warning("Null byte injection detected");
            return false;
        }
        
        return true;
    }

    /**
     * 安全读取文件
     */
    public static byte[] safeReadFile(File file, long maxSize) throws IOException {
        if (!file.exists() || !file.isFile()) {
            throw new IOException("File does not exist: " + file.getAbsolutePath());
        }
        
        if (file.length() > maxSize) {
            throw new IOException("File too large: " + file.length() + " bytes (max: " + maxSize + ")");
        }
        
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            
            byte[] buffer = new byte[8192];
            int bytesRead;
            long totalRead = 0;
            
            while ((bytesRead = fis.read(buffer)) != -1) {
                totalRead += bytesRead;
                if (totalRead > maxSize) {
                    throw new IOException("File size limit exceeded while reading");
                }
                baos.write(buffer, 0, bytesRead);
            }
            
            return baos.toByteArray();
        }
    }

    /**
     * 计算文件MD5
     */
    public static String calculateMD5(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "MD5 algorithm not available", e);
            return null;
        }
    }

    /**
     * 计算SHA256
     */
    public static String calculateSHA256(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(data);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            LOGGER.log(Level.SEVERE, "SHA-256 algorithm not available", e);
            return null;
        }
    }

    /**
     * Base64编码
     */
    public static String base64Encode(byte[] data) {
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64解码
     */
    public static byte[] base64Decode(String data) {
        return Base64.getDecoder().decode(data);
    }

    /**
     * URL安全的Base64编码
     */
    public static String base64UrlEncode(byte[] data) {
        return Base64.getUrlEncoder().encodeToString(data);
    }

    /**
     * URL安全的Base64解码
     */
    public static byte[] base64UrlDecode(String data) {
        return Base64.getUrlDecoder().decode(data);
    }

    /**
     * 验证IP地址格式
     */
    public static boolean isValidIP(String ip) {
        if (ip == null || ip.isEmpty()) {
            return false;
        }
        
        String[] parts = ip.split("\\.");
        if (parts.length != 4) {
            return false;
        }
        
        try {
            for (String part : parts) {
                int num = Integer.parseInt(part);
                if (num < 0 || num > 255) {
                    return false;
                }
            }
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * 验证端口范围
     */
    public static boolean isValidPort(int port) {
        return port > 0 && port <= 65535;
    }

    /**
     * 脱敏处理（隐藏敏感信息）
     */
    public static String maskSensitive(String input, int visibleChars) {
        if (input == null || input.length() <= visibleChars * 2) {
            return input;
        }
        
        String start = input.substring(0, visibleChars);
        String end = input.substring(input.length() - visibleChars);
        return start + "***" + end;
    }

    /**
     * 生成随机字符串
     */
    public static String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        java.security.SecureRandom random = new java.security.SecureRandom();
        
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    private SecurityUtils() {
        throw new AssertionError("工具类不应被实例化");
    }
}
