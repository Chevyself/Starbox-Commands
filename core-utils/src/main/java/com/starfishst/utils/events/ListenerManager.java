package com.starfishst.utils.events;

import com.starfishst.core.fallback.Fallback;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

/**
 * Manages calling to events and selecting the respective listeners for the event. Listener are
 * registered using reflection that is why the annotation {@link Listener} is required for the
 * methods that are going to be listening for an event.
 */
public class ListenerManager {

  /** The listeners registered in the manager */
  @NotNull private final Set<EventListener> listeners = new HashSet<>();

  /**
   * Register the listeners from the object.
   *
   * <p>Using reflection this method will get the class of the object doing a loop for each method.
   * The methods that have the annotation {@link Listener} will be attempted to create a listener
   *
   * @param object to create the listeners
   * @throws ListenerRegistrationException if the method does not have parameters, if the method has
   *     more than one parameters and if the parameter does not extend {@link Event}
   */
  public void registerListeners(@NotNull Object object) {
    Class<?> aClass = object.getClass();
    for (Method method : aClass.getMethods()) {
      Listener annotation = method.getAnnotation(Listener.class);
      if (annotation != null) {
        Parameter[] parameters = method.getParameters();
        if (parameters.length == 0) {
          throw new ListenerRegistrationException(
              "Method "
                  + method
                  + "  in "
                  + aClass
                  + " does not have parameters therefor no event was found");
        } else if (parameters.length > 1) {
          throw new ListenerRegistrationException(
              "Method "
                  + method
                  + " in "
                  + aClass
                  + " has more than one parameter which will cause an exception when trying to invoke the method");
        } else if (Event.class.isAssignableFrom(parameters[0].getType())) {
          listeners.add(
              new EventListener(
                  object,
                  method,
                  (Class<? extends Event>) parameters[0].getType(),
                  annotation.priority()));
        } else {
          throw new ListenerRegistrationException(
              "Method " + method + " in " + aClass + " parameter does not extend " + Event.class);
        }
      }
    }
  }

  /**
   * Get all the listeners for certain event. The list will be organized by the {@link
   * EventListener#getPriority()}
   *
   * @param clazz the clazz of the event to get all the listeners
   * @return a list of listeners for the event
   */
  @NotNull
  public List<EventListener> getListeners(@NotNull Class<? extends Event> clazz) {
    List<EventListener> listeners = new ArrayList<>();
    for (EventListener listener : this.listeners) {
      if (listener.getEvent().isAssignableFrom(clazz)) {
        listeners.add(listener);
      }
    }
    listeners.sort(Comparator.comparingInt(EventListener::getPriority));
    return listeners;
  }

  /**
   * Calls an event. This will get all the listeners for the event and call it for each of them
   *
   * @param event the event to be called
   */
  public void call(@NotNull Event event) {
    for (EventListener listener : this.getListeners(event.getClass())) {
      try {
        listener.getMethod().invoke(listener.getListener(), event);
      } catch (IllegalAccessException | InvocationTargetException e) {
        Fallback.addError("Listener method could not be invoked in " + listener);
        e.printStackTrace();
      }
    }
  }

  /**
   * Calls an event. As in {@link #call(Event)} but returns whether it was cancelled
   *
   * @param cancellable the event to be called
   * @return true if the event was cancelled
   * @throws IllegalArgumentException cancellable is not an instance of {@link Event}
   */
  public boolean call(@NotNull Cancellable cancellable) {
    if (cancellable instanceof Event) {
      this.call((Event) cancellable);
      return cancellable.isCancelled();
    } else {
      throw new IllegalArgumentException(cancellable + " must extend " + Event.class);
    }
  }
}
