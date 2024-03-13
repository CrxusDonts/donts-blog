package com.donts.response;

import java.io.Serializable;
public interface StatusCode extends Serializable {

    int code();

    String message();
}
