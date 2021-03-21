package com.baidetu.shiro;

import cn.hutool.json.JSONUtil;
import com.baidetu.common.Result;
import com.baidetu.util.JwtUtils;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwt过滤器配置
 */
@Component
public class JwtFilter extends AuthenticatingFilter {

    @Autowired
    JwtUtils jwtUtils;

    /**
     * 跨域过滤器处理
     *
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        //跨域处理代码
        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }

    /**
     * 获取token方法，源码中，实际会将token拿去进行登录验证：
     * AuthenticationToken token = createToken(request, response);
     * Subject subject = getSubject(request, response);
     * subject.login(token);
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization"); //获取令牌

        if (StringUtils.isEmpty(jwt)) {
            //没有令牌
            return null;
        }
        //有令牌则封装jwt的token，给executeLogin进行登录操作
        return new JwtToken(jwt);
    }

    /**
     * 校验token，有token则执行登录，登录则会调用上面的createToken（）方法
     *
     * @param servletRequest
     * @param servletResponse
     * @return
     * @throws Exception
     */
    @Override
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt = request.getHeader("Authorization"); //获取令牌

        if (StringUtils.isEmpty(jwt)) {
            //无token，则不用shiro进行登录处理，直接交给controller注解进行过滤拦截
            return true;

        }

        //不为空，校验jwt的token
        Claims claim = jwtUtils.getClaimByToken(jwt);
        if (claim == null || jwtUtils.isTokenExpired(claim.getExpiration())) {
            //过期，抛异常
            throw new ExpiredCredentialsException("token已失效，请重新登录！");
        }

        //校验成功,执行登录
        return executeLogin(servletRequest, servletResponse);
    }

    /**
     * 重写登录失败方法，如果登录失败，则按照Result格式返回信息给前端
     *
     * @param token
     * @param e
     * @param request
     * @param response
     * @return
     */
    @Override
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        //先判断异常是否为空
        Throwable throwable = e.getCause() == null ? e : e.getCause();
        //储存到Result
        Result result = Result.failure(throwable.getMessage());
        //将Result返回前端
        String json = JSONUtil.toJsonStr(result);
        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ioException) {

        }
        return false;
    }
}
