import org.springframework.util.DigestUtils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author mrChen
 * @date 2021/2/5 11:36
 */
public class test {
    static String ll="APP_IDz3QJRrS2IdTIMESTAMP2021-02-04 17:18:53 305TRANS_ID20210204171853305232992Jde2Ug4Swh2724lQMzgyUgKSLvENfsuB";
    String kk="Jde2Ug4Swh2724lQMzgyUgKSLvENfsuB";
    String r="81131ace992ef6c415aad14a2ed28b0d";
    public static void main(String[] args) {
        String s = DigestUtils.md5DigestAsHex(ll.getBytes());
        System.out.println(s);
    }
}
