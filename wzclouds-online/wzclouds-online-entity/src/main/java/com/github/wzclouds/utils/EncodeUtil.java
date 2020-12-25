package com.github.wzclouds.utils;

import com.github.wzclouds.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author wz
 */
@Slf4j
public class EncodeUtil {
  /**
   * 加密
   * 1.构造密钥生成器
   * 2.根据ecnodeRules规则初始化密钥生成器
   * 3.产生密钥
   * 4.创建和初始化密码器
   * 5.内容加密
   * 6.返回字符串
   **/
  private static String encodeRules = "wz";

  public static String AESEncode(String content) {
    try {
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      random.setSeed(encodeRules.getBytes());
      //1.构造密钥生成器，指定为AES算法,不区分大小写
      KeyGenerator keygen = KeyGenerator.getInstance("AES");
      //2.根据encodeRules规则初始化密钥生成器
      //生成一个128位的随机源,根据传入的字节数组
      keygen.init(128, random);
      //3.产生原始对称密钥
      SecretKey originalKey = keygen.generateKey();
      //4.获得原始对称密钥的字节数组
      byte[] raw = originalKey.getEncoded();
      //5.根据字节数组生成AES密钥
      SecretKey key = new SecretKeySpec(raw, "AES");
      //6.根据指定算法AES自成密码器
      Cipher cipher = Cipher.getInstance("AES");
      //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密解密(Decrypt_mode)操作，第二个参数为使用的KEY
      cipher.init(Cipher.ENCRYPT_MODE, key);

      //8.获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
      byte[] byteEncode = content.getBytes("utf-8");
      //9.根据密码器的初始化方式--加密：将数据加密
      byte[] byteAes = cipher.doFinal(byteEncode);
      //10.将加密后的数据转换为字符串
      //这里用Base64Encoder中会找不到包
      //解决办法：
      //在项目的Build path中先移除JRE System Library，再添加库JRE System Library，重新编译后就一切正常了。
      //11.将字符串返回
      String code = new BASE64Encoder().encode(byteAes);
      code = code.replace("+", "~");
      return code;
    } catch (NoSuchAlgorithmException | UnsupportedEncodingException | BadPaddingException
      | IllegalBlockSizeException | InvalidKeyException | NoSuchPaddingException e) {
      log.info("加密错误");
      throw BizException.wrap("解码错误");
    }
  }

  /**
   * 解密
   * 解密过程：
   * 1.同加密1-4步
   * 2.将加密后的字符串反纺成byte[]数组
   * 3.将加密内容解密
   **/
  public static String AESDncode(String content) {
    try {
      content = URLDecoder.decode(content);
      content = content.replace("~", "+");
      SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
      random.setSeed(encodeRules.getBytes());
      //1.构造密钥生成器，指定为AES算法,不区分大小写
      KeyGenerator keygen = KeyGenerator.getInstance("AES");
      //2.根据encodeRules规则初始化密钥生成器
      //生成一个128位的随机源,根据传入的字节数组
      keygen.init(128, random);
      //3.产生原始对称密钥
      SecretKey originalKey = keygen.generateKey();
      //4.获得原始对称密钥的字节数组
      byte[] raw = originalKey.getEncoded();
      //5.根据字节数组生成AES密钥
      SecretKey key = new SecretKeySpec(raw, "AES");
      //6.根据指定算法AES自成密码器
      Cipher cipher = Cipher.getInstance("AES");
      //7.初始化密码器，第一个参数为加密(Encrypt_mode)或者解密(Decrypt_mode)操作，第二个参数为使用的KEY
      cipher.init(Cipher.DECRYPT_MODE, key);
      //8.将加密并编码后的内容解码成字节数组
      byte[] byteContent = new BASE64Decoder().decodeBuffer(content);

      byte[] byteDecode = cipher.doFinal(byteContent);
      return new String(byteDecode, "utf-8");
    } catch (NoSuchAlgorithmException | BadPaddingException | IllegalBlockSizeException | IOException |
      InvalidKeyException | NoSuchPaddingException e) {
      log.info("解码错误");
      throw BizException.wrap("解码错误");
    }
  }

  public static void main(String[] args) {
    //加密
    String time = String.valueOf(System.currentTimeMillis());
    String content = URLEncoder.encode(EncodeUtil.AESEncode(String.valueOf(2) + '&' + System.currentTimeMillis()));
    System.out.println("根据输入的规则" + encodeRules + "加密后的密文是:" + content);

    //解密
    System.out.println("根据输入的规则" + encodeRules + "解密后的明文是:" + AESDncode(URLDecoder.decode(content)));
  }
}
