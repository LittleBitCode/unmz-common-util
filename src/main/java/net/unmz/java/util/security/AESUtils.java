package net.unmz.java.util.security;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

/**
 * Project Name:
 * 功能描述：
 *
 * @author faritor@unmz.net
 * @version 1.0
 * @date 2018/3/22 18:25
 * @since JDK 1.8
 */
public class AESUtils {

    /**
     * AES 加密
     *
     * @param key 秘钥
     * @param encryptedData 待加密数据
     * @param iv 向量
     * @return
     * @throws Exception
     */
    public static String encryptData(String key, String encryptedData, String iv) throws Exception {
        byte[] sessionKeyArray = Base64Utils.getInstance().decoder(key);
        byte[] encryptedDataArray = Base64Utils.getInstance().decoder(encryptedData);
        byte[] ivArray = Base64Utils.getInstance().decoder(iv);

        return encode(encryptedDataArray, sessionKeyArray, ivArray);
    }

    /**
     * AES 解密
     *
     * @param key 秘钥
     * @param decryptedData 待解密数据
     * @param iv 向量
     * @return
     * @throws Exception
     */
    public static String decryptData(String key, String decryptedData, String iv) throws Exception {
        byte[] sessionKeyArray = Base64Utils.getInstance().decoder(key);
        byte[] decryptedDataArray = Base64Utils.getInstance().decoder(decryptedData);
        byte[] ivArray = Base64Utils.getInstance().decoder(iv);

        return decode(decryptedDataArray, sessionKeyArray, ivArray);
    }


    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据encodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String encode(String content, String secret) {
        return encode(content, secret, null);
    }

    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String decode(String content, String secret) {
        return decode(content, secret, null);
    }

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据encodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String encode(String content, String secret, byte[] iv) {
        return encode(content.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8), iv);
    }

    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String decode(String content, String secret, byte[] iv) {
        return decode(content.getBytes(StandardCharsets.UTF_8), secret.getBytes(StandardCharsets.UTF_8), iv);
    }

    /**
     * 加密
     * 1.构造密钥生成器
     * 2.根据encodeRules规则初始化密钥生成器
     * 3.产生密钥
     * 4.创建和初始化密码器
     * 5.内容加密
     * 6.返回字符串
     */
    public static String encode(byte[] content, byte[] secret, byte[] iv) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据encodeRules规则初始化密钥生成器
            //生成一个256位的随机源,根据传入的字节数组
            keygen.init(256, new SecureRandom(secret));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
            if (iv != null && iv.length == 16) {
                cipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(iv));
            } else {
                cipher.init(Cipher.ENCRYPT_MODE, key);
            }
            //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
//            byte[] byte_encode = content.getBytes(StandardCharsets.UTF_8);
            //9.根据密码器的初始化方式--加密：将数据加密
            byte[] byte_AES = cipher.doFinal(content);
            //10.将加密后的数据转换为字符串
            return new BASE64Encoder().encode(byte_AES);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果有错就返加null
        return null;
    }

    /**
     * 解密
     * 解密过程：
     * 1.同加密1-4步
     * 2.将加密后的字符串反纺成byte[]数组
     * 3.将加密内容解密
     */
    public static String decode(byte[] content, byte[] secret, byte[] iv) {
        try {
            //1.构造密钥生成器，指定为AES算法,不区分大小写
            KeyGenerator keygen = KeyGenerator.getInstance("AES");
            //2.根据encodeRules规则初始化密钥生成器
            //生成一个256位的随机源,根据传入的字节数组
            keygen.init(256, new SecureRandom(secret));
            //3.产生原始对称密钥
            SecretKey original_key = keygen.generateKey();
            //4.获得原始对称密钥的字节数组
            byte[] raw = original_key.getEncoded();
            //5.根据字节数组生成AES密钥
            SecretKey key = new SecretKeySpec(raw, "AES");
            //6.根据指定算法AES自成密码器
            Cipher cipher = Cipher.getInstance("AES");
            //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
            if (iv != null && iv.length == 16) {
                cipher.init(Cipher.DECRYPT_MODE, key, new IvParameterSpec(iv));
            } else {
                cipher.init(Cipher.DECRYPT_MODE, key);
            }
            //8.将加密并编码后的内容解码成字节数组
//            byte[] byte_content = new BASE64Decoder().decodeBuffer(content);
            byte[] byte_decode = cipher.doFinal(content);
            return new String(byte_decode, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //如果有错就返加null
        return null;
    }
}
