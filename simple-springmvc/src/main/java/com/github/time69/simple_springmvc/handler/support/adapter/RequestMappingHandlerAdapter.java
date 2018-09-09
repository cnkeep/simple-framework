package com.github.time69.simple_springmvc.handler.support.adapter;

import com.github.time69.simple_springmvc.Handler;
import com.github.time69.simple_springmvc.HandlerAdapter;
import com.github.time69.simple_springmvc.ModelAndView;
import com.github.time69.simple_springmvc.annotation.ResponseBody;
import com.github.time69.simple_springmvc.handler.MethodHandler;
import com.github.time69.simple_springmvc.http.MediaType;
import com.github.time69.simple_springmvc.logger.Logger;
import com.github.time69.simple_springmvc.logger.LoggerContext;
import com.github.time69.simple_springmvc.resolver.arguments.MethodHandlerArgsResolver;
import com.github.time69.simple_springmvc.resolver.returnValue.MethodHandlerReturnValueResolver;
import com.github.time69.simple_springmvc.util.JsonUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 描述: 执行器，用于需要controller方法处理的情况
 *
 * @author <a href="1348555156@qq.com">LeiLi.Zhang</a>
 * @version 0.0.0
 * @date 2018/8/30
 */
public class RequestMappingHandlerAdapter implements HandlerAdapter {
    public static final Logger LOGGER = LoggerContext.getLog(RequestMappingHandlerAdapter.class);
    /**
     * 参数解析器列表
     */
    private Map<String, MethodHandlerArgsResolver> argsResolvers = new LinkedHashMap<String, MethodHandlerArgsResolver>(0);

    /**
     * 返回值解析器列表
     */
    private Map<String, MethodHandlerReturnValueResolver> returnValueResolvers = new LinkedHashMap<>(0);

    @Override
    public boolean supports(Handler handler) {
        return (handler instanceof MethodHandler);
    }

    @Override
    public ModelAndView handler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception {
        return invokeHandler(request, response, handler);
    }

    private ModelAndView invokeHandler(HttpServletRequest request, HttpServletResponse response, Handler handler) throws Exception {
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
        Map<String, String[]> parameterMap = request.getParameterMap();
        LOGGER.info("parameterMap:{}",parameterMap);
        return new Object[0];
    }

    /**
     * controller方法返回值处理，视图映射 or 直接返回(ResponseBody)
     *
     * @param response
     * @param methodHandler
     * @param returnValue
     * @return
     */
    private ModelAndView resolverReturnValue(HttpServletResponse response, MethodHandler methodHandler, Object returnValue) throws IOException {
        ModelAndView modelAndView = null;
        if (null == returnValue) {
            return modelAndView;
        }

        if (null != methodHandler.getAnnotation(ResponseBody.class)) {
            /**
             * ResponseBody直接返回，不进行视图解析
             */
            convertReturnValue2Body(response, methodHandler, returnValue);
        } else {
            if (returnValue instanceof ModelAndView) {
                modelAndView = (ModelAndView) returnValue;
            } else if (returnValue instanceof String) {
                modelAndView = new ModelAndView();
                modelAndView.setViewName((String) returnValue);
            }
        }

        return modelAndView;
    }

    /**
     * 返回值转换为执行格式数据直接返回，不做视图解析
     *
     * @param response
     * @param methodHandler
     * @param returnValue
     * @throws IOException
     */
    private void convertReturnValue2Body(HttpServletResponse response, MethodHandler methodHandler, Object returnValue) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_UTF8.getContentType());
        PrintWriter responseWriter = response.getWriter();
        responseWriter.write(null != returnValue ? JsonUtil.serialize(returnValue) : JsonUtil.serialize(""));
        responseWriter.flush();
    }
}
