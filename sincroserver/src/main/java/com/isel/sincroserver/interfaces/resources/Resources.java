package com.isel.sincroserver.interfaces.resources;

import com.isel.sincroserver.exception.SincroServerException;
import com.isel.sincroserver.resources.Query;

public interface Resources {
    String getMessage(String messageId) throws SincroServerException;
    String getErrorMessage(String errorMessageId) throws SincroServerException;
    String getQuery(String queryId) throws SincroServerException;
}