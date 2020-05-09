package com.starfishst.core.fallback;

/** A custom uncaught exception handler that helps adding those unhandled errors to the fallback */
public class FallbackUncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

  @Override
  public void uncaughtException(Thread thread, Throwable throwable) {
    throwable.printStackTrace();
    Fallback.addError(throwable.getClass().getName() + ": " + throwable.getMessage());
  }
}
