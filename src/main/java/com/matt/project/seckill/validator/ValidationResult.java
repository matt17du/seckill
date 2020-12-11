package com.matt.project.seckill.validator;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * @author matt
 * @create 2020-12-07 15:21
 */
public class ValidationResult {

    private boolean hasErrors;

    private Map<String,String> errorMsgMap = new HashMap<>();

    public boolean isHasErrors() {
        return hasErrors;
    }

    public void setHasErrors(boolean hasErrors) {
        this.hasErrors = hasErrors;
    }

    public Map<String, String> getErrorMsgMap() {
        return errorMsgMap;
    }

    public void setErrorMsgMap(Map<String, String> errorMsgMap) {
        this.errorMsgMap = errorMsgMap;
    }

    public String getErrorMsg(Map<String,String> errorMsgMap) {
        return StringUtils.join(errorMsgMap.values(),",");
    }
}
