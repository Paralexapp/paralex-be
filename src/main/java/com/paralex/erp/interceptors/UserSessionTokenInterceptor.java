package com.paralex.erp.interceptors;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.paralex.erp.entities.UserEntity;
import com.paralex.erp.services.FirebaseService;
import com.paralex.erp.services.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.paralex.erp.ErpApplication.AUTHORIZATION_COOKIE_KEY;
import static com.paralex.erp.ErpApplication.AUTHORIZATION_HEADER_KEY;

@RequiredArgsConstructor
@Component
@Log4j2
public class UserSessionTokenInterceptor implements HandlerInterceptor {
    private final List<String> exemptedRoutes = List.of(
            "POST user",
            "POST /api/v1/auth/register",
            "POST /bail-bond/",
            "GET /bail-bond/",
            "GET /verify-transaction",
            "POST /service-provider/lawyer/profile/",
            "POST /service-provider/lawyer/profile/lawyer/create-review",
            "OPTIONS /service-provider/lawyer/profile/lawyer/create-review",
            "GET /service-provider/lawyer/profile/lawyer/get-reviews",
            "OPTIONS /service-provider/lawyer/profile/lawyer/get-reviews",
            "POST /service-provider/lawyer/profile/my",
            "GET /service-provider/lawyer/profile/",
            "OPTIONS /service-provider/lawyer/profile/",
            "GET /service-provider/lawyer/profile/search",
            "POST /service-provider/driver/profile/",
            "POST /service-provider/driver/profile/rider/create-review",
            "OPTIONS /service-provider/driver/profile/rider/get-reviews",
            "GET /service-provider/driver/profile/rider/get-reviews",
            "OPTIONS /service-provider/driver/profile/rider/create-review",
            "PATCH /service-provider/driver/profile/update-offline-status",
            "GET /service-provider/driver/profile/",
            "POST /litigation-support/",
            "GET /litigation-support/",
            "POST /delivery/request/",
            "POST /delivery/request/assign",
            "POST /delivery/request/re-assign",
            "POST /delivery/request/assignment/accept",
            "POST /delivery/request/assignment/decline",
            "POST /locations/",
            "POST /api/news/post",
            "GET /api/news/get-all",
            "GET /api/news/get-by-section",
            "GET /api/news/{id}",
            "GET /api/notifications/get",
            "GET /api/notifications/get-lawyer-notification",
            "GET /api/notifications/get-rider-notification",
            "POST /api/notifications/create-test-notification",
            "POST /api/notifications/create-test-lawyer-notification",
            "POST /api/notifications/create-test-rider-notification",
            "POST /admin/create-admin",
            "POST /admin/create-lawyer-profile",
            "GET /admin/get-lawyer-by-userId",
            "OPTIONS /admin/get-lawyer-by-userId",
            "OPTIONS /admin/create-lawyer-profile",
            "OPTIONS /admin/create-admin",
            "OPTIONS /admin/login",
            "OPTIONS /admin/get-admin-notification",
            "OPTIONS /admin/get-bail-bond-requests",
            "POST /admin/login",
            "POST /admin/create-test-admin-notification",
            "GET /admin/get-admin-notification",
            "GET /admin/get-bail-bond-requests",
            "POST /api/notifications/mark-as-read",
            "POST /api/notifications/mark-as-read-lawyer",
            "POST /api/notifications/mark-as-read-rider",
            "POST /api/notifications/inbox/mark-as-read",
            "POST /api/notifications/inbox/lawyer/mark-as-read",
            "POST /api/notifications/inbox/rider/mark-as-read",
            "GET /locations/",
            "GET /delivery/request/mine",
            "GET /delivery/request/information",
            "POST /payment/bill/initialize-payment",
            "POST /payment/bill/verify-transaction",
            "GET /payment/verify-transaction",
            "GET /delivery/request/",
            "POST /api/v1/auth/login",
            "POST /api/v1/auth/validate-otp",
            "POST /api/v1/auth/send-otp",
            "POST /api/v1/auth/reset-password",
            "POST /api/v1/auth/update-password/in-app",
            "POST /api/v1/auth/logout",
            "POST /api/v1/auth/initiate-password-reset",
            "POST /api/v1/auth/reset-password",
            "POST /api/v1/auth/upload-to-cloudinary",
            "POST /api/v1/auth/upload-media",
            "GET /api/v1/auth/get-user",
            "GET /api/v1/auth/get-user-by-id/{id}",
            "GET /api/v1/auth/get-user-by-email/email",
            "GET /api/v1/auth/get-registration-level",
            "PUT /api/v1/auth/update-user-profile",
            "PUT /admin/update-user-profile",
            "OPTIONS /admin/update-user-profile",
            "GET /admin/get-all-users",
            "OPTIONS /admin/get-all-users",
            "GET /admin/get-all-admins",
            "OPTIONS /admin/get-all-admins",
            "POST /admin/block",
            "POST /admin/delete-user",
            "OPTIONS /admin/block",
            "OPTIONS /admin/delete-user",
            "GET /admin/get-all-lawyers-profile",
            "OPTIONS /admin/get-all-lawyers-profile",
            "PUT verify",
            "PUT sign-out",
            "GET swagger-ui", "GET v3", "GET favicon.ico",
            "GET error", "PUT error", "POST error", "DELETE error",
            "PUT download");
    private final FirebaseService firebaseService;
    private final UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException, FirebaseAuthException {
        if (pathToExempt(request.getRequestURI(), request.getMethod())) {
            log.info("exempted >> " + request.getRequestURI());
            return true;
        }

        var cookie = getCookie(AUTHORIZATION_COOKIE_KEY, request);
        var headerValue = getHeader(AUTHORIZATION_HEADER_KEY, request);

        log.info("cookie >> " + cookie + " headerValue >> " + headerValue);

        if (cookie == null && headerValue == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }

        var token = getUserDetailsByTokenFromFirebase(Objects.requireNonNullElse(cookie, headerValue));
        var user = token.getEmail() != null ?
                findUserDetailsByEmail(token.getEmail()) :
                findUserDetailsByPhoneNumber(firebaseService.getUserRecord(token.getUid())
                        .getPhoneNumber());

        log.info("user found >> " + user);

        request.setAttribute("user", user);

        return true;
    }

    private UserEntity findUserDetailsByPhoneNumber(String phoneNumber) {
        return userService.findUserByPhoneNumber(phoneNumber).orElse(null);
    }

    private UserEntity findUserDetailsByEmail(String email) {
        return userService.findUserByEmail(email).orElse(null);
    }

    private FirebaseToken getUserDetailsByTokenFromFirebase(String token) throws IOException, FirebaseAuthException {
        return firebaseService.verifyIdToken(token, true);
    }

    private boolean pathToExempt(String uri, String method) {
        log.info("uri >> " + uri + " method >> " + method);
        return exemptedRoutes.stream()
                .anyMatch(fragment -> fragment.split(" ")[0].equalsIgnoreCase(method) &&
                        uri.contains(fragment.split(" ")[1]));
    }

    public String getHeader(String key, HttpServletRequest request) {
        var value = request.getHeader(key);

        return value != null ? Arrays.stream(value
                .split(" "))
                .toList()
                .get(1) :
                null;
    }

    public String getCookie(String name, HttpServletRequest request) {
        var cookies = request.getCookies();

        return cookies != null ? Arrays.stream(cookies)
                .filter(cookie -> cookie.getName().equalsIgnoreCase(name.toLowerCase()))
                .findFirst()
                .map(Cookie::getValue)
                .orElse(null) :
                null;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) {
        // TODO document why this method is empty
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) {
        request.removeAttribute("user");
    }

}
