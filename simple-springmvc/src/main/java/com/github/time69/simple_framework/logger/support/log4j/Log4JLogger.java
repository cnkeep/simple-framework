package com.github.time69.simple_framework.logger.support.log4j;

/**
 * 描述~
 *
 * @author <a href="1348555156@qq.com">LeiLi.Zhang</a>
 * @version 0.0.0
 * @date 2018/8/27
 */
class Log4JLogger implements com.github.time69.simple_framework.logger.Logger {

    private final org.apache.log4j.Logger logger;

    Log4JLogger(org.apache.log4j.Logger logger) {
        this.logger = logger;
    }

    @Override
    public String getName() {
        return this.logger.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return this.logger.isTraceEnabled();
    }

    @Override
    public boolean isDebugEnabled() {
        return this.logger.isDebugEnabled();
    }

    @Override
    public void debug(String message, Object... arguments) {
        this.logger.debug(String.format(message, arguments));
    }

    @Override
    public void info(String mesage, Object... arguments) {
        this.logger.info(String.format(mesage, arguments));
    }

    @Override
    public void trace(String mesage, Object... arguments) {
        this.logger.trace(String.format(mesage, arguments));
    }

    @Override
    public void warn(String mesage, Object... arguments) {
        this.logger.warn(String.format(mesage, arguments));
    }

    @Override
    public void error(String mesage, Object... arguments) {
        this.logger.error(String.format(mesage, arguments));
    }
}
