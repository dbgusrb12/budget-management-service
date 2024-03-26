package com.hg.budget.was.core.security.context;

import com.hg.budget.was.core.security.UserDetails;
import org.springframework.util.Assert;

/*
 * UserDetails Context Holder
 * 인터셉터의 preHandle 메서드에서 유효한 인증 정보라고 판단되면 ContextHolder 에 저장한 후 전역헤서 사용 할 수 있도록 처리함.
 * 인터셉터가 끝나면 ContextHolder 는 비워짐.
 */
public class UserDetailsContextHolder {

    private static final ThreadLocal<UserDetails> contextHolder = new ThreadLocal<>();

    public static void clearContext() {
        contextHolder.remove();
    }

    public static UserDetails getContext() {
        return contextHolder.get();
    }

    public static void setContext(UserDetails userDetails) {
        Assert.notNull(userDetails, "Only non-null UserDetailsContext instances are permitted");
        contextHolder.set(userDetails);
    }
}
