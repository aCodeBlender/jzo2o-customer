package cn.xiaozhibang.evaluation.sdk.core;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.HexUtil;
import cn.hutool.crypto.symmetric.SymmetricAlgorithm;
import cn.hutool.crypto.symmetric.SymmetricCrypto;
import cn.hutool.json.JSONUtil;
import cn.xiaozhibang.evaluation.sdk.model.domain.CurrentUser;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Token生成工具类
 * 用于生成评价系统的认证token
 *
 * @author generated
 */
public class TokenHelper {

    /**
     * 为管理员生成token
     *
     * @param appId 应用ID
     * @param accessKeyId 访问密钥ID
     * @param accessKeySecret 访问密钥秘密
     * @param currentUser 当前用户信息
     * @return token字符串
     */
    public static String generateTokenOfAdmin(String appId, String accessKeyId, String accessKeySecret, CurrentUser currentUser) {
        return generateToken(appId, accessKeyId, accessKeySecret, currentUser, "admin");
    }

    /**
     * 为普通用户生成token
     *
     * @param appId 应用ID
     * @param accessKeyId 访问密钥ID
     * @param accessKeySecret 访问密钥秘密
     * @param currentUser 当前用户信息
     * @return token字符串
     */
    public static String generateTokenOfUser(String appId, String accessKeyId, String accessKeySecret, CurrentUser currentUser) {
        return generateToken(appId, accessKeyId, accessKeySecret, currentUser, "user");
    }

    /**
     * 生成token的核心方法
     *
     * @param appId 应用ID
     * @param accessKeyId 访问密钥ID
     * @param accessKeySecret 访问密钥秘密
     * @param currentUser 当前用户信息
     * @param userType 用户类型
     * @return token字符串
     */
    private static String generateToken(String appId, String accessKeyId, String accessKeySecret, CurrentUser currentUser, String userType) {
        // 构建token payload
        Map<String, Object> payload = new HashMap<>();
        payload.put("appId", appId);
        payload.put("accessKeyId", accessKeyId);
        payload.put("userId", currentUser.getId());
        payload.put("userName", currentUser.getNickName());
        payload.put("userAvatar", currentUser.getUserAvatar());
        payload.put("userType", userType);
        payload.put("timestamp", System.currentTimeMillis());

        // 将payload转为JSON字符串
        String jsonStr = JSONUtil.toJsonStr(payload);

        // 使用accessKeySecret加密
        String encrypted = encryptToken(jsonStr, accessKeySecret);

        // 返回加密后的token
        return encrypted;
    }

    /**
     * 使用AES加密token
     *
     * @param data 待加密数据
     * @param key 加密密钥
     * @return 加密后的token（Base64编码）
     */
    private static String encryptToken(String data, String key) {
        try {
            // 确保密钥长度为16字节（128位）、24字节（192位）或32字节（256位）
            byte[] keyBytes = formatKey(key);

            // 使用AES-ECB模式加密
            SymmetricCrypto aes = new SymmetricCrypto(SymmetricAlgorithm.AES, keyBytes);
            byte[] encrypted = aes.encrypt(data);

            // 返回Base64编码的结果
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("Token加密失败: " + e.getMessage(), e);
        }
    }

    /**
     * 格式化密钥长度，确保符合AES要求
     * AES支持16、24、32字节的密钥
     *
     * @param key 原始密钥
     * @return 格式化后的密钥字节数组
     */
    private static byte[] formatKey(String key) {
        if (key == null) {
            throw new IllegalArgumentException("密钥不能为空");
        }

        byte[] keyBytes = key.getBytes(StandardCharsets.UTF_8);

        // 如果密钥长度小于16，则填充
        if (keyBytes.length < 16) {
            byte[] newKey = new byte[16];
            System.arraycopy(keyBytes, 0, newKey, 0, keyBytes.length);
            return newKey;
        }

        // 如果密钥长度在16-32之间，截取或保留
        if (keyBytes.length <= 32) {
            // 如果长度不是16、24或32，则截取到最接近的有效长度
            if (keyBytes.length <= 16) {
                byte[] newKey = new byte[16];
                System.arraycopy(keyBytes, 0, newKey, 0, Math.min(keyBytes.length, 16));
                return newKey;
            } else if (keyBytes.length <= 24) {
                byte[] newKey = new byte[24];
                System.arraycopy(keyBytes, 0, newKey, 0, keyBytes.length);
                return newKey;
            } else {
                byte[] newKey = new byte[32];
                System.arraycopy(keyBytes, 0, newKey, 0, keyBytes.length);
                return newKey;
            }
        }

        // 如果密钥长度大于32，截取前32字节
        byte[] newKey = new byte[32];
        System.arraycopy(keyBytes, 0, newKey, 0, 32);
        return newKey;
    }
}
