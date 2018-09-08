package com.github.time69.simple_springmvc.handler.support.adapter;

import com.github.time69.simple_springmvc.Handler;
import com.github.time69.simple_springmvc.HandlerAdapter;
import com.github.time69.simple_springmvc.ModelAndView;
import com.github.time69.simple_springmvc.handler.MethodHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 描述: 执行器，用于需要controller方法处理的情况
 *
 * @author <a href="1348555156@qq.com">LeiLi.Zhang</a>
 * @version 0.0.0
 * @date 2018/8/30
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    @Override
    public boolean supports(Handler handler) {
        return (handler instanceof MethodHandler);
    }

    @Override
    public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws InvocationTargetException, IllegalAccessException {
        return invokeHandler(request, response, handler);
    }

    private ModelAndView invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws InvocationTargetException, IllegalAccessException {
        ModelAndView modelAndView = null;
        if (handler instanceof MethodHandler) {
            //请求参数封装
            Object[] args = getMethodArgumentValues(request, handler);

            //执行处理逻辑
            Object returnValue = invokeMethod(request, (MethodHandler) handler, args);

            //返回值处理
            modelAndView = resolverReturnValue(response, (MethodHandler) handler, returnValue);
        }
        return modelAndView;
    }

    /**
     * 执行请求处理的业务逻辑，即controller方法
     *
     * @param request
     * @param handler
     * @param args
     * @return
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     */
    private Object invokeMethod(HttpServletRequest request, MethodHandler handler, Object[] args) throws InvocationTargetException, IllegalAccessException {
        Method method = handler.getMethod();
        Object beanInstance = handler.getBeanInstance();
        Object returnValue = method.invoke(beanInstance, args);
        return returnValue;
    }

    /**
     * TODO 从请求中获取controller方法的参数，并自动封装为匹配的参数对象列表
     *
     * @param request
     * @param handler
     * @return
     */
    private Object[] getMethodArgumentValues(HttpServletRequest request, Handler handler) {
        // resolverMethodArguments
        return null;
    }

    /**
     * TODO controller方法返回值处理，视图映射 or 直接返回(ResponseBody)
     *
     * @param response
     * @param methodHandler
     * @param returnValue
     * @return
     */
    private ModelAndView resolverReturnValue(HttpServletResponse response, MethodHandler methodHandler, Object returnValue) {
        ModelAndView modelAndView = null;
        if (returnValue != null) {
            // TODO 对返回值适配解析器处理
        }
        // TODO 函数返回值构建为ModelAndView
        if (returnValue instanceof ModelAndView) {
            modelAndView = (ModelAndView) returnValue;
        } else if (returnValue instanceof String) {
            modelAndView = new ModelAndView();
        }
        return modelAndView;
    }
}