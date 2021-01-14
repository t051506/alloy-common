package com.alloy.cloud.common.sentinel.handle;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.csp.sentinel.Tracer;
import com.alloy.cloud.common.core.base.R;
import com.alloy.cloud.common.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.SpringSecurityMessageSource;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * <p>
 * 全局异常处理器结合sentinel 全局异常处理器不能作用在 oauth server https://gitee.com/log4j/pig/issues/I1M2TJ
 * </p>
 *
 */
@Slf4j
@RestControllerAdvice
@ConditionalOnExpression("!'${security.oauth2.client.clientId}'.isEmpty()")
public class GlobalBizExceptionHandler {

	/**
	 * 全局异常.
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public R handleGlobalException(Exception e) {
		log.error("全局异常信息 ex={}", e.getMessage(), e);

		// 业务异常交由 sentinel 记录
		Tracer.trace(e);
		return R.failed(e.getLocalizedMessage());
	}

	/**
	 * AccessDeniedException
	 * @param e the e
	 * @return R
	 */
	@ExceptionHandler(AccessDeniedException.class)
	@ResponseStatus(HttpStatus.FORBIDDEN)
	public R handleAccessDeniedException(AccessDeniedException e) {
		String msg = SpringSecurityMessageSource.getAccessor().getMessage("AbstractAccessDecisionManager.accessDenied",
				e.getMessage());
		log.error("拒绝授权异常信息 ex={}", msg, e);
		return R.failed(e.getLocalizedMessage());
	}

	/**
	 * validation Exception
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({ MethodArgumentNotValidException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R handleBodyValidException(MethodArgumentNotValidException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.failed(fieldErrors.get(0).getDefaultMessage());
	}

	/**
	 * validation Exception (以form-data形式传参)
	 * @param exception
	 * @return R
	 */
	@ExceptionHandler({ BindException.class })
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	public R bindExceptionHandler(BindException exception) {
		List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
		log.warn("参数绑定异常,ex = {}", fieldErrors.get(0).getDefaultMessage());
		return R.failed(fieldErrors.get(0).getDefaultMessage());
	}

	@ExceptionHandler({ConstraintViolationException.class})
	public R bodyValidExceptionHandler(ConstraintViolationException e) {
		log.warn(e.getMessage(), e);
		Set<ConstraintViolation<?>> set = e.getConstraintViolations();
		if (CollUtil.isEmpty(set)) {
			return R.failed();
		} else {
			String error = "";
			Iterator var4 = set.iterator();

			while (var4.hasNext()) {
				ConstraintViolation constraintViolation = (ConstraintViolation) var4.next();
				if (StringUtils.isEmpty(error)) {
					error = constraintViolation.getMessageTemplate();
				} else {
					error = error + ";" + constraintViolation.getMessageTemplate();
				}
			}

			return R.failed(error);
		}
	}

	@ExceptionHandler({BusinessException.class})
	public R exception(BusinessException e) {
		log.error("全局业务异常 ex={}", e.getMessage(), e);
		String msg = e.getMessage();
		if (StringUtils.isEmpty(msg)) {
			msg = "系统业务异常，请稍后再试";
		}
		Tracer.trace(e);
		return R.failed("999", msg);
	}
}
