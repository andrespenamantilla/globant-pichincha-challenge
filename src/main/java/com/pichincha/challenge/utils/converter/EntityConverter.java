package com.pichincha.challenge.utils.converter;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;


/**
 * Utility class to automatically convert entities with similar structures (IE. Database Entities and Data Transport Objects)
 * This class uses reflection to obtain a list of getters in the source object and attempts to find setters with the same name
 * and same value type in the target object to invoke them. Allows 2 way conversion.
 *
 * @param <X> The Generic Type 2
 * @param <Y> The Generic Type 1
 */
@SuppressWarnings("unchecked")
public class EntityConverter<X, Y> {

  private final Class<X> t1;
  private final Class<Y> t2;

  /**
   * Creates a new instance of the EntityConverter class.
   *
   * @param c1 The class for the Generic Type 1
   * @param c2 The class for the Generic Type 2
   */
  public EntityConverter(Class<X> c1, Class<Y> c2) {
    this.t1 = c1;
    this.t2 = c2;
  }

  /**
   * Converts an object of the Generic Type 2 into a new instance of the Generic type 1
   *
   * @param obj Instance of Generic type 2 object
   * @return Instance of Generic type 1 object
   */
  public X toT1(Y obj) {
    return EntityConverter.convert(obj, this.t1);
  }

  /**
   * Converts an object of the Generic Type 2 into a new instance of the Generic type 1
   *
   * @param obj    Instance of Generic type 2 object
   * @param target Instance of Generic type 1 object
   * @return Instance of Generic type 1 object
   */
  public X toT1(Y obj, X target) {
    return EntityConverter.convert(obj, target);
  }

  /**
   * Converts an object of the Generic Type 1 into a new instance of the Generic type 2
   *
   * @param obj Instance of Generic type 1 object
   * @return Instance of Generic type 2 object
   */
  public Y toT2(X obj) {
    return EntityConverter.convert(obj, this.t2);
  }

  /**
   * Converts an object of the Generic Type 1 into a new instance of the Generic type 2
   *
   * @param obj    Instance of Generic type 1 object
   * @param target Instance of Generic type 2 object
   * @return Instance of Generic type 2 object
   */
  public Y toT2(X obj, Y target) {
    return EntityConverter.convert(obj, target);
  }

  /**
   * Static method to convert from a Genetic type to another.
   *
   * @param obj The instance of the object to convert
   * @param c   The class of the target object to create
   * @param <U> Generic type for destination object
   * @return Instance of the specified type with the values copied from the source object.
   */
  public static <U> U convert(Object obj, Class<?> c) {
    return (U) convertGeneric(obj, c);
  }

  /**
   * Static method to convert from a Genetic type to another.
   *
   * @param obj    The instance of the object to convert
   * @param target the target object to copy the values to.
   * @param <U>    Generic type for destination object
   * @return Instance of the specified type with the values copied from the source object.
   */
  public static <U> U convert(Object obj, U target) {
    return (U) convertGeneric(obj, target);
  }

  /**
   * Attempts to convert an object to a new instance of the given class
   *
   * @param obj The object
   * @param c   The class
   * @return New instance of c with the converted values from obj.
   */
  private static Object convertGeneric(Object obj, Class<?> c) {
    if (obj == null) {
      return null;
    }

    Object builder = getBuilder(c);
    Object target;
    boolean isBuilder = builder != null;

    if (isBuilder) {
      target = builder;
    } else {
      try {
        target = c.newInstance();
      } catch (InstantiationException | IllegalAccessException e) {
        throw new IllegalStateException("Creating new Instance failed.", e);
      }
    }

    List<Method> methods = Arrays.asList(obj.getClass().getMethods());

    methods.stream()
        .filter(x -> x.getName().startsWith("get") && x.getParameterCount() == 0)
        .forEach(y -> EntityConverter.apply(target, obj, y, "get", isBuilder));

    methods.stream()
        .filter(x -> x.getName().startsWith("is") && x.getParameterCount() == 0 && (x.getReturnType() == boolean.class || x.getReturnType() == Boolean.class))
        .forEach(y -> EntityConverter.apply(target, obj, y, "is", isBuilder));

    if (!isBuilder) {
      return target;
    }

    try {
      Method build = builder.getClass().getMethod("build");
      return build.invoke(builder);
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
      throw new IllegalStateException("Unable to invoke build method", e);
    }
  }

  /**
   * Attempts to copy an object values to an existing instance of the given class
   *
   * @param obj    The object
   * @param target the target object to copy the values to.
   * @param c      The class
   * @return New instance of c with the converted values from obj.
   */
  private static Object convertGeneric(Object obj, Object target) {
    if (obj == null) {
      return null;
    }

    List<Method> methods = Arrays.asList(obj.getClass().getMethods());

    methods.stream()
        .filter(x -> x.getName().startsWith("get") && x.getParameterCount() == 0)
        .forEach(y -> EntityConverter.apply(target, obj, y, "get", false));

    methods.stream()
        .filter(x -> x.getName().startsWith("is") && x.getParameterCount() == 0 && (x.getReturnType() == boolean.class || x.getReturnType() == Boolean.class))
        .forEach(y -> EntityConverter.apply(target, obj, y, "is", false));

    return target;
  }

  private static void apply(Object destination, Object source, Method getter, String getterPrefix, boolean isBuilder) {
    if (getter.isAnnotationPresent(DenyConverterAccess.class)) {
      return;
    }

    Object value;
    String getterName = getter.getName();
    String setterName;

    if (isBuilder) {
      String name = getterName.replaceFirst(getterPrefix, "");
      setterName = name.substring(0, 1).toLowerCase() + name.substring(1);
    } else {
      setterName = getterName.replaceFirst(getterPrefix, "set");
    }

    /*
     *   Get the value from the source. If the retrieval of the value is not possible,
     *   the rest of the process is skipped.
     */
    try {
      value = getter.invoke(source);
    } catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException e) {
      throw new IllegalStateException("Getter invoke failed for " + getter.getName() + " failed.", e);
    }

    if (value == null) {
      return;
    }

    if (value instanceof List) {
      value = convertList(destination, getter, setterName, getterPrefix, (List) value);
    } else if (value.getClass().isArray()) {
      value = convertArray(destination, getter, setterName, getterPrefix, (Object[]) value);
    }

    if (value == null) {
      return;
    }

    /*
     *  Attempt to get a getter matching the field name of the setter and assigned the value.
     */
    try {
      Method setter = destination.getClass().getMethod(setterName, getter.getReturnType());

      setter.invoke(destination, value);
      return;

    } catch (NoSuchMethodException ignored) {
      // Ignored since if the method does not exist, it will be handled below.
    } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
      throw new IllegalStateException("Setter invoke failed for getter " + getter.getName() + " failed.", e);
    }

    /*
     *  Will attempt to find a setter with same name to attempt an entity conversion.
     */
    try {
      Method setter = getMatchingSetter(setterName, destination.getClass());

      if (setter != null) {
        if (value instanceof List || value.getClass().isArray()) {
          setter.invoke(destination, value);
        } else {
          Class<?> convertedType = setter.getParameters()[0].getType();
          Object convertedValue = convertGeneric(value, convertedType);
          setter.invoke(destination, convertedValue);
        }
        return;
      }
    } catch (IllegalStateException ignored) {
      // Ignored since the conversion could not be done this way, attempt to continue.
    } catch (InvocationTargetException | IllegalAccessException | IllegalArgumentException e) {
      throw new IllegalStateException("Setter invoke failed for getter " + getter.getName() + " failed.", e);
    }

    /*
     *  If obtaining a setter or invoking the setter fails, an attempt to retrieve the Field
     *  will be done. If the given field is annotated with @AllowConverterAccess, the assignment
     *  will be made, otherwise it will be skipped.
     */
    Field field = getField(destination, getterPrefix, getterName);
    setByField(destination, value, field);
  }

  private static void setByField(Object destination, Object value, Field field) {
    if (field == null) {
      return;
    }

    try {
      boolean wasAccessible = field.isAccessible();
      field.setAccessible(true);
      field.set(destination, value);
      field.setAccessible(wasAccessible);
    } catch (IllegalAccessException e) {
      throw new IllegalStateException("Illegal access to field " + field.getName(), e);
    }
  }

  private static Field getField(Object destination, String getterPrefix, String getterName) {
    String fieldName = getterName.replaceFirst(getterPrefix, "");
    fieldName = fieldName.substring(0, 1).toLowerCase() + fieldName.substring(1);

    Class<?> current = destination.getClass();

    while (current != Object.class) {
      try {
        Field f = current.getDeclaredField(fieldName);

        if (f.isAnnotationPresent(AllowConverterAccess.class)) {
          return f;
        }
      } catch (NoSuchFieldException ignored) {
        // Ignored, if the field does not exist, the mapping for this value will not be done.
      }

      current = current.getSuperclass();
    }

    return null;
  }

  private static List convertList(Object destination, Method getter, String setterName, String getterPrefix, List value) {
    ParameterizedType sourceGenericType = (ParameterizedType) getter.getGenericReturnType();
    Class<?> sourceGenericClass = (Class<?>) sourceGenericType.getActualTypeArguments()[0];
    Class<?> targetGenericClass;
    Method setter = getMatchingSetter(setterName, destination.getClass());

    if (setter != null) {
      ParameterizedType targetGenericType = ((ParameterizedType) setter.getParameters()[0].getAnnotatedType().getType());
      targetGenericClass = (Class<?>) targetGenericType.getActualTypeArguments()[0];
    } else {
      Field field = getField(destination, getterPrefix, getter.getName());

      if (field == null) {
        return null;
      }

      ParameterizedType targetGenericType = (ParameterizedType) field.getGenericType();
      targetGenericClass = (Class<?>) targetGenericType.getActualTypeArguments()[0];
    }

    List listValue = new ArrayList();

    if (Objects.equals(sourceGenericClass, targetGenericClass)) {
      value.stream().forEach(listValue::add);
    } else {
      final Class<?> finalTargetClass = targetGenericClass;
      value.stream().forEach(x -> listValue.add(convertGeneric(x, finalTargetClass)));
    }

    return listValue;
  }

  private static Object convertArray(Object destination, Method getter, String setterName, String getterPrefix, Object[] value) {
    String getterArrayType = getter.getGenericReturnType().toString();
    String setterArrayType;
    Class<?> getterGenericType = getArrayGenericType(getterArrayType);
    Class<?> setterGenericType;

    Method setter = getMatchingSetter(setterName, destination.getClass());

    if (setter != null) {
      setterArrayType = setter.getParameters()[0].getType().toString();
    } else {
      Field field = getField(destination, getterPrefix, getter.getName());

      if (field == null) {
        return null;
      }

      setterArrayType = field.getType().toString();
    }

    setterGenericType = getArrayGenericType(setterArrayType);
    Object ret = Array.newInstance(setterGenericType, value.length);

    for (int i = 0; i < value.length; i++) {
      if (Objects.equals(getterGenericType, setterGenericType)) {
        Array.set(ret, i, value[i]);
      } else {
        Array.set(ret, i, convertGeneric(value[i], setterGenericType));
      }
    }

    return ret;
  }

  private static Class<?> getArrayGenericType(String fullClassType) {
    try {
      return Class.forName(fullClassType.substring(8, fullClassType.length() - 1));
    } catch (ClassNotFoundException e) {
      throw new IllegalStateException("Failed to obtain generic type of array.", e);
    }
  }

  /**
   * Used for cases where there is nested conversion involved.
   *
   * @param setterName  The expected setter name.
   * @param targetClass The target class.
   * @return The proper setter method.
   */
  @SuppressWarnings("PMD.NullAssignment")
  private static Method getMatchingSetter(String setterName, Class<?> targetClass) {
    List<Method> methods = Arrays.asList(targetClass.getMethods());
    Optional<Method> setter = methods.stream().filter(x -> Objects.equals(x.getName(), setterName) && x.getParameterCount() == 1).findFirst();

    if (!setter.isPresent()) {
      return null;
    }

    return setter.isPresent() ? setter.get() : null;
  }

  private static Class<?> getBuilderClass(Class<?> objClass) {
    return Arrays.stream(objClass.getClasses())
        .filter(x -> x.getName().endsWith("Builder"))
        .findFirst()
        .orElse(null);
  }

  @SuppressWarnings("PMD.NullAssignment")
  private static Method getBuilderMethod(Class<?> objClass) {
    try {
      Method m = objClass.getMethod("builder");
      return Modifier.isStatic(m.getModifiers()) && m.getReturnType() == getBuilderClass(objClass) ? m : null;
    } catch (NoSuchMethodException e) {
      return null;
    }
  }

  private static Object getBuilder(Class<?> objClass) {
    if (!objClass.isAnnotationPresent(ConverterUseBuilder.class)) {
      return null;
    }

    try {
      Method m = getBuilderMethod(objClass);
      return m == null ? null : m.invoke(null);
    } catch (IllegalAccessException | InvocationTargetException e) {
      return null;
    }
  }
}

