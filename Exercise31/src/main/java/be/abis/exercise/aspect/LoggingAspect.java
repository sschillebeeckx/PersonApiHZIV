package be.abis.exercise.aspect;

import be.abis.exercise.exception.LoginException;
import be.abis.exercise.model.Person;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingAspect {

	@AfterReturning("AopPointcuts.execLogin()")
	public void logPersonLoggedInCorrectly(JoinPoint jp){
		System.out.println("login correct for "+ jp.getArgs()[0]);
	}
	
	@AfterThrowing(pointcut="AopPointcuts.execLogin()",throwing="exc")
	public void logProblemLogin(JoinPoint jp, LoginException exc){
		System.out.println("login problem for : " + jp.getArgs()[0]);
		System.out.println("exception thrown = " + exc.getMessage());
	}

}
