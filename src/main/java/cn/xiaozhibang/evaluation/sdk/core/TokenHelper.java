package cn.xiaozhibang.evaluation.sdk.core;
import cn.xiaozhibang.evaluation.sdk.model.domain.CurrentUser;

public class TokenHelper {
    public static String generateTokenOfAdmin(String appId, String ak, String sk, CurrentUser user) {
        return "mock_admin_token";
    }
    public static String generateTokenOfUser(String appId, String ak, String sk, CurrentUser user) {
        return "mock_user_token";
    }
}