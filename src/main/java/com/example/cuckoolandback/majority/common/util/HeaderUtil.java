package com.example.cuckoolandback.majority.common.util;

import javax.servlet.http.HttpServletRequest;

public class HeaderUtil {

    private final static String HEADER_AUTHORIZATION = "Authorization";
    private final static String TOKEN_PREFIX = "Bearer";
    private final static String HEADER_REFRESH = "RefreshToken";

    public static String getAccessToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_AUTHORIZATION);

        if (headerValue == null) {
            return null;
        }
        if (headerValue.startsWith(TOKEN_PREFIX)) {
            return headerValue.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
    public static String getRefreshToken(HttpServletRequest request) {
        String headerValue = request.getHeader(HEADER_REFRESH);

        if (headerValue == null) {
            return null;
        }
        return headerValue;
    }
}
