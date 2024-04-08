package com.hg.budget.application.spend;

import com.hg.budget.application.core.code.ApplicationCode;
import com.hg.budget.application.core.exception.ApplicationException;
import com.hg.budget.domain.account.Account;
import com.hg.budget.domain.spend.Spend;
import org.springframework.stereotype.Component;

@Component
public class SpendValidator {

    public void validateExist(Spend spend) {
        if (spend == null) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "지출이 존재하지 않습니다.");
        }
        if (spend.notExist()) {
            throw new ApplicationException(ApplicationCode.BAD_REQUEST, "지출이 존재하지 않습니다.");
        }
    }

    public void validateOwner(Spend spend, Account account) {
        final Account spentUser = spend.getSpentUser();
        if (account.equals(spentUser)) {
            return;
        }
        throw new ApplicationException(ApplicationCode.BAD_REQUEST, "지출 설정자가 아닙니다.");
    }
}
