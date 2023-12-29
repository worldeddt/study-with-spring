package webchat.component;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class Utils {
    private static final Logger log = LoggerFactory.getLogger(Utils.class);
    private static final Gson gson = new GsonBuilder().create();
    private static final SecureRandom random = new SecureRandom();

    public static boolean makeDir(String path) {
        File file = new File(path);
        return file.exists() || file.mkdirs();
    }

    public static void deleteFile(File file) {
        if (file.exists()) {
            file.delete();
        } else {
            log.info("File not found! : {}", file.getName());
        }
    }

    // 파일이 존재하고 사이즈가 0보다 크면 , 1
    // 파일이  존재하고 사이즈가 0이면,  0
    // 파일이 없으면, -1;
    public static int existNcheckNullFile(ArrayList<File> fileList, String word) {
        int retVal = -1;

        for (File f : fileList) {
            if (f.isFile()) {
                boolean c = f.getName().contains(word + "-");
                boolean n = (f.length() > 0) ? true : false;

                if (c && n) {
                    retVal = 1;
                    break;
                } else if (c && !n) {
                    retVal = 0;
                    break;
                }
            }
        }

        return retVal;
    }

    public static boolean existFile(ArrayList<File> fileList, String word) {
        boolean result = false;

        for (File f : fileList) {
            if (f.isFile()) {
                result = f.getName().contains(word + "-");
                if (result)
                    break;
            }
        }

        return result;
    }

    /**
     * compareFileExt : file 의 확장자 명, ex. .webm, .txt ... isComparedSameExt : true 면
     * compareFileExt 와 같은 확장자를 가진 파일을 get, false 면 다른 확장자를 가진 파일을 get
     *
     */

    public static void delayExcute(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }).start();
    }

    public static String toDateString(Date date, String format) {
        return new SimpleDateFormat(format).format(date);
    }

    public static String getDigitsFromUri(String uri) {
        // start is after '+' or ':'
        int start = uri.indexOf('+');
        if (start == -1) {
            start = uri.indexOf(':');
        }
        if (start == -1) {
            log.info("[ getDigitsFromUri ] uri({}) is invalid format", uri);
            return uri;
        }
        start += 1;

        // end is '@' or end of string
        int end = uri.indexOf('@');
        if (end == -1) {
            end = uri.indexOf('\0');
        }

        return uri.substring(start, end);
    }

    public static String getDate(String format) {
        Date now = new Date();
        return toDateString(now, format);
    }

    public static String getDate() {
        Date now = new Date();
        return toDateString(now, "yyyyMMddHHmmss");
    }

    public static String getDate(long timestamp, String format) {
        Date now = new Date(timestamp);
        return toDateString(now, format);
    }

    public static String getDate(long timestamp) {
        Date now = new Date(timestamp);
        return toDateString(now, "yyyyMMddHHmmss");
    }

    public static long getTimestamp() {
        Date d = new Date();
        long timestamp = d.getTime();
        log.info("Date = {}, Timestamp = {}", d, timestamp);
        return timestamp;
    }

    public static void getPrintStackTrace(Exception e) {
        StringWriter errors = new StringWriter();
        e.printStackTrace(new PrintWriter(errors));
        log.error(errors.toString());
    }

    public static String getSessionId() {

        //SecureRandom random = new SecureRandom();
        byte bytes[] = new byte[16];
        random.nextBytes(bytes);

        String sessionId = bytesToHex(bytes);
        log.info("sessionId: {}", sessionId);

        return sessionId;
    }

    //private static String regexIPv4 = "^(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}$";
    private static String regexIPv6 = "^(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";
    //private static String regexIPv4andIPv6 = "^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|::(ffff(:0{1,4}){0,1}:){0,1}((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]).){3,3}(25[0-5]|(2[0-4]|1{0,1}[0-9]){0,1}[0-9]))$";

    public static String getMyIp(boolean isIPv4) {
        String result = null;
        /*
         * try {
         * result = InetAddress.getLocalHost().getHostAddress();
         * } catch (UnknownHostException e) {
         * result = "";
         * }
         */

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                log.info("{}, {}, {}", ni.getDisplayName(), ni.getName());
                for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                    log.info("{}, {}, {}", ia.getAddress().getCanonicalHostName(), ia.getAddress().getHostAddress(),
                            ia.getAddress().getHostName());
                    result = ia.getAddress().getHostAddress();
                    if (isIPv4) {
                        if (result.chars().filter($ -> $ == '.').count() != 3 || result.equals("127.0.0.1")) {
                            continue;
                        } else {
                            return result;
                        }
                    }

                    // IPv6 유효성 체크
                    if (Pattern.compile(regexIPv6).matcher(result).matches() == true) {
                        return result;
                    }
                }
            }
        } catch (Exception e) {
            getPrintStackTrace(e);
        }

        return result;
    }

    // 210917 for IPv4 only
    public static String getMyIp(String ifName) {
        String result = null;

        try {
            Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
            while (networkInterfaces.hasMoreElements()) {
                NetworkInterface ni = networkInterfaces.nextElement();
                if (ni.getName().endsWith(ifName)) {
                    for (InterfaceAddress ia : ni.getInterfaceAddresses()) {
                        result = ia.getAddress().getHostAddress();
                        if (result.chars().filter($ -> $ == '.').count() != 3)
                            continue;
                        else
                            return result;
                    }
                }
            }
        } catch (Exception e) {
            getPrintStackTrace(e);
        }

        return result;
    }

    /////////////////////////////////////////////////////////////////////////
    private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "X-Real-IP", "X-RealIP", "REMOTE_ADDR" };

    public static String getClientIpAddressIfServletRequestExist() {

        String ip = "0.0.0.0";

        if (RequestContextHolder.getRequestAttributes() == null) {
            return ip;
        }

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = request.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                ip = ipList.split(",")[0];

                return checkIp(ip);
            }
        }

        ip = request.getRemoteAddr();
        return checkIp(ip);
    }

    private static String checkIp(String ip) {
        if (ip.equals("0:0:0:0:0:0:0:1"))
            ip = "127.0.0.1";

        if (ip.chars().filter($ -> $ == '.').count() != 3)
            log.error("Illegal IP: " + ip);

        return ip;
    }
    /////////////////////////////////////////////////////////////////////////

    public static boolean availablePort(String host, int port) {
        boolean result = false;

        try {
            (new Socket(host, port)).close();
            result = true;
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return result;
    }

    public static byte[] intToBytes(int val) {
        return ByteBuffer.allocate(4).putInt(val).array();
    }

    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String encryptAES256(String msg, String key) throws Exception {

        byte bytes[] = new byte[32];

        random.nextBytes(bytes);

        byte[] saltBytes = bytes;

        // Password-Based Key Derivation function 2
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        // 70000번 해시하여 256 bit 길이의 키를 만든다.
        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);

        SecretKey secretKey = factory.generateSecret(spec);

        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        // 알고리즘/모드/패딩

        // CBC : Cipher Block Chaining Mode

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        cipher.init(Cipher.ENCRYPT_MODE, secret);

        AlgorithmParameters params = cipher.getParameters();

        // Initial Vector(1단계 암호화 블록용)
        byte[] ivBytes = params.getParameterSpec(IvParameterSpec.class).getIV();

        byte[] encryptedTextBytes = cipher.doFinal(msg.getBytes("UTF-8"));

        byte[] buffer = new byte[saltBytes.length + ivBytes.length + encryptedTextBytes.length];

        System.arraycopy(saltBytes, 0, buffer, 0, saltBytes.length);

        System.arraycopy(ivBytes, 0, buffer, saltBytes.length, ivBytes.length);

        System.arraycopy(encryptedTextBytes, 0, buffer, saltBytes.length + ivBytes.length, encryptedTextBytes.length);

        return Base64.getEncoder().encodeToString(buffer);

    }

    public static String decryptAES256(String msg, String key) throws Exception {

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

        ByteBuffer buffer = ByteBuffer.wrap(Base64.getDecoder().decode(msg));

        byte[] saltBytes = new byte[32];

        buffer.get(saltBytes, 0, saltBytes.length);

        byte[] ivBytes = new byte[cipher.getBlockSize()];

        buffer.get(ivBytes, 0, ivBytes.length);

        byte[] encryoptedTextBytes = new byte[buffer.capacity() - saltBytes.length - ivBytes.length];

        buffer.get(encryoptedTextBytes);

        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");

        PBEKeySpec spec = new PBEKeySpec(key.toCharArray(), saltBytes, 70000, 256);

        SecretKey secretKey = factory.generateSecret(spec);

        SecretKeySpec secret = new SecretKeySpec(secretKey.getEncoded(), "AES");

        cipher.init(Cipher.DECRYPT_MODE, secret, new IvParameterSpec(ivBytes));

        byte[] decryptedTextBytes = cipher.doFinal(encryoptedTextBytes);

        return new String(decryptedTextBytes);

    }

    public static List<?> convertObjectToList(Object obj) {
        List<?> list = new ArrayList<>();
        if (obj.getClass().isArray()) {
            list = Arrays.asList((Object[]) obj);
        } else if (obj instanceof Collection) {
            list = new ArrayList<>((Collection<?>) obj);
        }
        return list;
    }

    public static String linuxCommander(String command) {
        String result = "";
        Runtime rt = Runtime.getRuntime();
        Process p = null;
        StringBuffer sb = new StringBuffer();
        try {
            p = rt.exec(command);
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            String cl = null;
            while ((cl = in.readLine()) != null) {
                sb.append(cl);
            }
            result = sb.toString();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
        return result;
    }

    // 판테온 추가 로직.
    public static boolean isPantheon(String counselingInfo) {
        JsonObject info = gson.fromJson(counselingInfo, JsonObject.class);
        return info.has("gcId");
    }

    /////////////////////////// DB 컬럼 복호화용 ////////////////////////////////////////////
    private static int CRYPTO_NPM_KEY_LENGTH = 32;
    private static int CRYPTO_NPM_IV_LENGTH = 16;
    private static String CRYPTO_NPM_HEX = "MD5";
    private static String CRYPTO_NPM_ALGORITHM = "AES";
    private static String CRYPTO_NPM_MODE = "AES/CBC/PKCS5Padding";

    public static String SB_KEY = "74HbakgFNJ";

    public static String decryptAESAsHex(String encMsg, String key) throws Exception {

        byte[] keyBytes = new byte[CRYPTO_NPM_KEY_LENGTH];
        // crypto의 사양상 16 고정
        byte[] ivBytes = new byte[CRYPTO_NPM_IV_LENGTH];

        // 복호화의 key와 iv를 취득
        bytesToKeyAndIv(key, keyBytes, ivBytes);

        Cipher cipher = configKeyAndIv(keyBytes, ivBytes, Cipher.DECRYPT_MODE);

        // JDK 1.8
        //byte[] payload_bytes = DatatypeConverter.parseHexBinary(encMsg);
        // JDK 11. Using Apache's Commons-Codec Library
        byte[] payload_bytes = Hex.decodeHex(encMsg);
        byte[] decrypted = cipher.doFinal(payload_bytes);
        String decryptedWord = new String(decrypted);

        return decryptedWord;
    }

    private static boolean bytesToKeyAndIv(String password, byte[] key_bytes, byte[] iv_bytes) throws Exception {

        // 비밀번호 변환
        byte[] passBytes = password.getBytes(StandardCharsets.UTF_8);

        // 1회째의 해시화 쪼개는 이유는 2회째 이후는 직전에 해시화한 byte[]를 사용하기 때문. 1회째는 비밀번호만
        byte[] passBytesToMd5 = MessageDigest.getInstance(CRYPTO_NPM_HEX).digest(passBytes);

        // 두 번째 이후의 해시화
        int bytesCount = passBytesToMd5.length;
        int maxLen = key_bytes.length + iv_bytes.length;
        ByteBuffer keyAndIvBuffer = ByteBuffer.allocate(maxLen);
        keyAndIvBuffer.put(passBytesToMd5);
        hashingFromSecond(keyAndIvBuffer, passBytesToMd5, passBytes, maxLen, bytesCount);

        // Key와 IV 얻기
        byte[] keyAndIv = new byte[maxLen];
        keyAndIvBuffer.rewind();
        keyAndIvBuffer.get(keyAndIv);
        // key
        System.arraycopy(keyAndIv, 0, key_bytes, 0, key_bytes.length);
        // Iv
        System.arraycopy(keyAndIv, key_bytes.length, iv_bytes, 0, iv_bytes.length);

        return true;
    }

    private static Cipher configKeyAndIv(byte[] keyBytes, byte[] ivBytes, int mode) throws Exception {

        System.out.println("ivBytes: " + ivBytes.toString() + ", String: " + new String(ivBytes, "UTF-8"));

        SecretKey secret = new SecretKeySpec(keyBytes, CRYPTO_NPM_ALGORITHM);
        IvParameterSpec ivSpec = new IvParameterSpec(ivBytes);
        Cipher cipher = Cipher.getInstance(CRYPTO_NPM_MODE);
        cipher.init(mode, secret, ivSpec);

        return cipher;

    }

    private static void hashingFromSecond(ByteBuffer keyAndIvBuffer, byte[] beforeBytes, byte[] passBytes, int maxLen,
                                          int bytesCount) throws Exception {

        if (maxLen <= bytesCount) {
            return;
        }

        // 바이트 배열 결합
        byte[] conectbytes = new byte[beforeBytes.length + passBytes.length];
        System.arraycopy(beforeBytes, 0, conectbytes, 0, beforeBytes.length);
        System.arraycopy(passBytes, 0, conectbytes, beforeBytes.length, passBytes.length);

        // 해시화
        byte[] hashingBytes = MessageDigest.getInstance(CRYPTO_NPM_HEX).digest(conectbytes);

        // 재귀 처리
        bytesCount += hashingBytes.length;
        keyAndIvBuffer.put(hashingBytes);
        hashingFromSecond(keyAndIvBuffer, hashingBytes, passBytes, maxLen, bytesCount);
    }

}
