package com.beeplay.easylog.logback.util;

import ch.qos.logback.classic.spi.ILoggingEvent;
import com.beeplay.easylog.core.LogMessageThreadLocal;
import com.beeplay.easylog.core.TraceMessage;
import com.beeplay.easylog.core.constant.LogMessageConstant;
import com.beeplay.easylog.core.dto.BaseLogMessage;
import com.beeplay.easylog.core.dto.RunLogMessage;
import com.beeplay.easylog.core.util.DateUtil;
import com.beeplay.easylog.core.util.IpGetter;
import com.beeplay.easylog.core.util.TraceLogMessageFactory;

import java.sql.Timestamp;

public class LogMessageUtil {

    public static BaseLogMessage getLogMessage(final String appName, final ILoggingEvent iLoggingEvent) {
        TraceMessage traceMessage = LogMessageThreadLocal.logMessageThreadLocal.get();
        String formattedMessage = iLoggingEvent.getFormattedMessage();
        if (formattedMessage.startsWith(LogMessageConstant.TRACE_PRE)) {
            return TraceLogMessageFactory.getTraceLogMessage(
                    traceMessage,appName,iLoggingEvent.getTimeStamp());
        }
        RunLogMessage logMessage = new RunLogMessage();
        String ip = IpGetter.getIp();
        logMessage.setServerName(ip);
        logMessage.setAppName(appName);
        logMessage.setContent(iLoggingEvent.getFormattedMessage());
        logMessage.setTraceId(traceMessage.getTraceId());
        logMessage.setDateTime(DateUtil.parseTimestampToStr(
                new Timestamp(iLoggingEvent.getTimeStamp()),
                DateUtil.DATE_TIME_FORMAT_YYYY_MM_DD_HH_MI_SS));
        logMessage.setDtTime(iLoggingEvent.getTimeStamp());
        StackTraceElement stackTraceElement=iLoggingEvent.getCallerData()[0];
        logMessage.setClassName(stackTraceElement.getClassName());
        logMessage.setMethod(stackTraceElement.getMethodName());
        logMessage.setLogLevel(iLoggingEvent.getLevel().toString());
        return logMessage;
    }
}
