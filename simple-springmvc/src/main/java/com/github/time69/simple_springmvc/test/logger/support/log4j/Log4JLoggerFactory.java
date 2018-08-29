package com.github.time69.simple_springmvc.test.logger.support.log4j;

import com.github.time69.simple_springmvc.test.logger.AbstractLoggerFactory;
import com.github.time69.simple_springmvc.test.logger.Logger;

/**
 * 描述~
 *
 * @author <a href="1348555156@qq.com">LeiLi.Zhang</a>
 * @version 0.0.0
 * @date 2018/8/27
 */
public class Log4JLoggerFactory extends AbstractLoggerFactory {

    @Override
    protected Logger createLog(String name) {
        return new Log4JLogger(org.apache.log4j.Logger.getLogger(name));
    }

    @Override
    protected Logger createLog(Class<?> clazz) {
        return new Log4JLogger(org.apache.log4j.Logger.getLogger(clazz));
    }
}