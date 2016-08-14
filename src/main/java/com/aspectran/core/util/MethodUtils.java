/**
 * Copyright 2008-2016 Juho Jeong
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.aspectran.core.util;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

public class MethodUtils {

    /**
     * Indicates whether methods should be cached for improved performance.
     * <p>
     * Note that when this class is deployed via a shared classloader in
     * a container, this will affect all webapps. However making this
     * configurable per webapp would mean having a map keyed by context classloader
     * which may introduce memory-leak problems.
     */
    private static volatile boolean cacheEnabled = true;

	/** An empty class array */
	public static final Class<?>[] EMPTY_CLASS_PARAMETERS = new Class[0];

	/** An empty object array */
	public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    /**
     * Stores a cache of MethodDescriptor -> Method in a WeakHashMap.
     * <p>
     * The keys into this map only ever exist as temporary variables within
     * methods of this class, and are never exposed to users of this class.
     * This means that the WeakHashMap is used only as a mechanism for
     * limiting the size of the cache, ie a way to tell the garbage collector
     * that the contents of the cache can be completely garbage-collected
     * whenever it needs the memory. Whether this is a good approach to
     * this problem is doubtful; something like the commons-collections
     * LRUMap may be more appropriate (though of course selecting an
     * appropriate size is an issue).
     * <p>
     * This static variable is safe even when this code is deployed via a
     * shared classloader because it is keyed via a MethodDescriptor object
     * which has a Class as one of its members and that member is used in
     * the MethodDescriptor.equals method. So two components that load the same
     * class via different classloaders will generate non-equal MethodDescriptor
     * objects and hence end up with different entries in the map.
     */
    private static final Map<MethodDescriptor, Reference<Method>> cache = Collections.synchronizedMap(new WeakHashMap<>());

	/**
	 * Sets the value of a bean property to an Object.
	 *
	 * @param object The bean to change
	 * @param setterName The property name or setter method name
	 * @param arg use this argument
	 * @throws NoSuchMethodException the no such method exception
	 * @throws IllegalAccessException the illegal access exception
	 * @throws InvocationTargetException the invocation target exception
	 */
    public static void invokeSetter(Object object, String setterName, Object arg)
    		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		invokeSetter(object, setterName, args);
    }

    /**
     * Sets the value of a bean property to an Object.
     *
     * @param object The bean to change
	 * @param setterName The property name or setter method name
     * @param args use this arguments
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static void invokeSetter(Object object, String setterName, Object[] args)
    		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    	int index = setterName.indexOf('.');
    	if(index > 0) {
    		String getterName = setterName.substring(0, index);
    		Object o = invokeGetter(object, getterName);
    		invokeSetter(o, setterName.substring(index + 1), args);
    	} else {
	    	if(!setterName.startsWith("set")) {
	    		setterName = "set" + setterName.substring(0, 1).toUpperCase(Locale.US) + setterName.substring(1);
	    	}
	    	invokeMethod(object, setterName, args);
    	}
    }

    /**
     * Gets an Object property from a bean.
     *
	 * @param object The bean
	 * @param getterName The property name or getter method name
	 * @return The property value (as an Object)
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static Object invokeGetter(Object object, String getterName)
    		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    	return invokeMethod(object, getterName);
    }

    /**
     * Gets an Object property from a bean.
     *
     * @param object The bean
	 * @param getterName The property name or getter method name
     * @param arg use this argument
     * @return The property value (as an Object)
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static Object invokeGetter(Object object, String getterName, Object arg)
    		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    	Object[] args = { arg };
    	return invokeGetter(object, getterName, args);
    }

    /**
     * Gets an Object property from a bean.
     *
     * @param object The bean
	 * @param getterName The property name or getter method name
     * @param args use this arguments
     * @return The property value (as an Object)
     * @throws NoSuchMethodException the no such method exception
     * @throws IllegalAccessException the illegal access exception
     * @throws InvocationTargetException the invocation target exception
     */
    public static Object invokeGetter(Object object, String getterName, Object[] args)
    		throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    	int index = getterName.indexOf('.');
    	if(index > 0) {
    		String getterName2 = getterName.substring(0, index);
    		Object o = invokeGetter(object, getterName2);
    		return invokeGetter(o, getterName.substring(index + 1), args);
    	} else {
	    	if(!getterName.startsWith("get") && !getterName.startsWith("is")) {
	    		getterName = "get" + getterName.substring(0, 1).toUpperCase(Locale.US) + getterName.substring(1);
	    	}
	    	return invokeMethod(object, getterName, args);
    	}
    }

	/**
	 * <p>Invoke a named method whose parameter type matches the object type.</p>
	 *
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible
	 */
	public static Object invokeMethod(Object object, String methodName)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		return invokeMethod(object, methodName, EMPTY_OBJECT_ARRAY, EMPTY_CLASS_PARAMETERS);
	}

	/**
	 * <p>Invoke a named method whose parameter type matches the object type.</p>
	 *
	 * <p>The behaviour of this method is less deterministic
	 * than <code>invokeExactMethod()</code>.
	 * It loops through all methods with names that match
	 * and then executes the first it finds with compatable parameters.</p>
	 *
	 * <p>This method supports calls to methods taking primitive parameters
	 * via passing in wrapping classes. So, for example, a <code>Boolean</code> class
	 * would match a <code>boolean</code> primitive.</p>
	 *
	 * <p> This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object[] args)}.
	 * </p>
	 *
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeMethod(Object object, String methodName, Object arg)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		return invokeMethod(object, methodName, args);
	}

	/**
	 * <p>Invoke a named method whose parameter type matches the object type.</p>
	 *
	 * <p>The behaviour of this method is less deterministic
	 * than {@link #invokeExactMethod(Object object,String methodName,Object[] args)}.
	 * It loops through all methods with names that match
	 * and then executes the first it finds with compatable parameters.</p>
	 *
	 * <p>This method supports calls to methods taking primitive parameters
	 * via passing in wrapping classes. So, for example, a <code>Boolean</code> class
	 * would match a <code>boolean</code> primitive.</p>
	 *
	 * <p> This is a convenient wrapper for
	 * {@link #invokeMethod(Object object,String methodName,Object[] args,Class[] paramTypes)}.
	 * </p>
	 *
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeMethod(Object object, String methodName, Object[] args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class<?>[] paramTypes;

		if(args == null) {
			args = EMPTY_OBJECT_ARRAY;
			paramTypes = EMPTY_CLASS_PARAMETERS;
		} else {
			int arguments = args.length;
			if(arguments == 0) {
				paramTypes = EMPTY_CLASS_PARAMETERS;
			} else {
				paramTypes = new Class[arguments];
				for(int i = 0; i < arguments; i++) {
					if(args[i] != null)
						paramTypes[i] = args[i].getClass();
				}
			}
		}

		return invokeMethod(object, methodName, args, paramTypes);
	}

	/**
	 * <p>Invoke a named method whose parameter type matches the object type.</p>
	 *
	 * <p>The behaviour of this method is less deterministic
	 * than {@link
	 * #invokeExactMethod(Object object,String methodName,Object[] args,Class[] paramTypes)}.
	 * It loops through all methods with names that match
	 * and then executes the first it finds with compatable parameters.</p>
	 *
	 * <p>This method supports calls to methods taking primitive parameters
	 * via passing in wrapping classes. So, for example, a <code>Boolean</code> class
	 * would match a <code>boolean</code> primitive.</p>
	 *
	 *
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param paramTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeMethod(Object object, String methodName, Object[] args, Class<?>[] paramTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(args == null)
			args = EMPTY_OBJECT_ARRAY;

		if(paramTypes == null)
			paramTypes = EMPTY_CLASS_PARAMETERS;

		Method method = getMatchingAccessibleMethod(object.getClass(), methodName, paramTypes);

		if(method == null)
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());

		return invokeMethod(object, method, args, paramTypes);
	}

	/**
	 * <p>Invoke a method whose parameter type matches exactly the object type.</p>
	 *
	 * <p> This is a convenient wrapper for
	 * {@link #invokeExactMethod(Object object,String methodName,Object[] args)}.
	 * </p>
	 *
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactMethod(Object object, String methodName, Object arg)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		return invokeExactMethod(object, methodName, args);
	}

	/**
	 * <p>Invoke a method whose parameter types match exactly the object types.</p>
	 *
	 * <p> This uses reflection to invoke the method obtained from a call to
	 * <code>getAccessibleMethod()</code>.</p>
	 *
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactMethod(Object object, String methodName, Object[] args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Class<?>[] paramTypes;

		if(args == null) {
			args = EMPTY_OBJECT_ARRAY;
			paramTypes = EMPTY_CLASS_PARAMETERS;
		} else {
			int arguments = args.length;
			if(arguments == 0) {
				paramTypes = EMPTY_CLASS_PARAMETERS;
			} else {
				paramTypes = new Class[arguments];
				for(int i = 0; i < arguments; i++) {
					if(args[i] != null)
						paramTypes[i] = args[i].getClass();
				}
			}
		}

		return invokeExactMethod(object, methodName, args, paramTypes);
	}

	/**
	 * <p>Invoke a method whose parameter types match exactly the parameter types given.</p>
	 *
	 * <p>This uses reflection to invoke the method obtained from a call to
	 * <code>getAccessibleMethod()</code>.</p>
	 *
	 * @param object invoke method on this object
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param paramTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactMethod(Object object, String methodName, Object[] args, Class<?>[] paramTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}

		if(paramTypes == null) {
			paramTypes = EMPTY_CLASS_PARAMETERS;
		}

		Method method = getAccessibleMethod(object.getClass(), methodName, paramTypes);

		if(method == null) {
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on object: " + object.getClass().getName());
		}

		return method.invoke(object, args);
	}

	/**
	 * <p>Invoke a static method whose parameter types match exactly the parameter types given.</p>
	 *
	 * <p>This uses reflection to invoke the method obtained from a call to
	 * {@link #getAccessibleMethod(Class, String, Class[])}.</p>
	 *
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param paramTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactStaticMethod(Class<?> objectClass, String methodName, Object[] args, Class<?>[] paramTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}

		if(paramTypes == null) {
			paramTypes = EMPTY_CLASS_PARAMETERS;
		}

		Method method = getAccessibleMethod(objectClass, methodName, paramTypes);

		if(method == null) {
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + objectClass.getName());
		}

		return method.invoke(null, args);
	}

	/**
	 * <p>Invoke a named static method whose parameter type matches the object type.</p>
	 *
	 * <p>The behaviour of this method is less deterministic
	 * than {@link #invokeExactMethod(Object, String, Object[], Class[])}.
	 * It loops through all methods with names that match
	 * and then executes the first it finds with compatable parameters.</p>
	 *
	 * <p>This method supports calls to methods taking primitive parameters
	 * via passing in wrapping classes. So, for example, a <code>Boolean</code> class
	 * would match a <code>boolean</code> primitive.</p>
	 *
	 * <p> This is a convenient wrapper for
	 * {@link #invokeStaticMethod(Class objectClass,String methodName,Object[] args)}.
	 * </p>
	 *
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeStaticMethod(Class<?> objectClass, String methodName, Object arg)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		return invokeStaticMethod(objectClass, methodName, args);
	}

	/**
	 * <p>Invoke a named static method whose parameter type matches the object type.</p>
	 *
	 * <p>The behaviour of this method is less deterministic
	 * than {@link #invokeExactMethod(Object object,String methodName,Object[] args)}.
	 * It loops through all methods with names that match
	 * and then executes the first it finds with compatable parameters.</p>
	 *
	 * <p>This method supports calls to methods taking primitive parameters
	 * via passing in wrapping classes. So, for example, a <code>Boolean</code> class
	 * would match a <code>boolean</code> primitive.</p>
	 *
	 * <p> This is a convenient wrapper for
	 * {@link #invokeStaticMethod(Class objectClass,String methodName,Object[] args,Class[] paramTypes)}.
	 * </p>
	 *
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeStaticMethod(Class<?> objectClass, String methodName, Object[] args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}
		int arguments = args.length;
		Class<?>[] paramTypes = new Class[arguments];
		for(int i = 0; i < arguments; i++) {
			paramTypes[i] = args[i].getClass();
		}
		return invokeStaticMethod(objectClass, methodName, args, paramTypes);
	}

	/**
	 * <p>Invoke a named static method whose parameter type matches the object type.</p>
	 *
	 * <p>The behaviour of this method is less deterministic
	 * than {@link #invokeExactStaticMethod(Class objectClass,String methodName,Object[] args,Class[] paramTypes)}.
	 * It loops through all methods with names that match
	 * and then executes the first it finds with compatable parameters.</p>
	 *
	 * <p>This method supports calls to methods taking primitive parameters
	 * via passing in wrapping classes. So, for example, a <code>Boolean</code> class
	 * would match a <code>boolean</code> primitive.</p>
	 *
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @param paramTypes match these parameters - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeStaticMethod(Class<?> objectClass, String methodName, Object[] args, Class<?>[] paramTypes)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(args == null)
			args = EMPTY_OBJECT_ARRAY;

		if(paramTypes == null)
			paramTypes = EMPTY_CLASS_PARAMETERS;

		Method method = getMatchingAccessibleMethod(objectClass, methodName, paramTypes);

		if(method == null) {
			throw new NoSuchMethodException("No such accessible method: " + methodName + "() on class: " + objectClass.getName());
		}

		return invokeMethod(null, method, args, paramTypes);
	}

	/**
	 * <p>Invoke a static method whose parameter type matches exactly the object type.</p>
	 *
	 * <p> This is a convenient wrapper for
	 * {@link #invokeExactStaticMethod(Class objectClass,String methodName,Object[] args)}.
	 * </p>
	 *
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param arg use this argument
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactStaticMethod(Class<?> objectClass, String methodName, Object arg)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		Object[] args = { arg };
		return invokeExactStaticMethod(objectClass, methodName, args);
	}

	/**
	 * <p>Invoke a static method whose parameter types match exactly the object types.</p>
	 *
	 * <p> This uses reflection to invoke the method obtained from a call to
	 * {@link #getAccessibleMethod(Class, String, Class[])}.</p>
	 *
	 * @param objectClass invoke static method on this class
	 * @param methodName get method with this name
	 * @param args use these arguments - treat null as empty array
	 * @return The value returned by the invoked method
	 * @throws NoSuchMethodException if there is no such accessible method
	 * @throws InvocationTargetException wraps an exception thrown by the method invoked
	 * @throws IllegalAccessException if the requested method is not accessible via reflection
	 */
	public static Object invokeExactStaticMethod(Class<?> objectClass, String methodName, Object[] args)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		if(args == null) {
			args = EMPTY_OBJECT_ARRAY;
		}

		int arguments = args.length;
		Class<?>[] paramTypes = new Class[arguments];

		for(int i = 0; i < arguments; i++) {
			paramTypes[i] = args[i].getClass();
		}

		return invokeExactStaticMethod(objectClass, methodName, args, paramTypes);
	}

	private static Object invokeMethod(Object object, Method method, Object[] args, Class<?>[] paramTypes)
			throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Class<?>[] methodsParams = method.getParameterTypes();
		if(hasPrimitiveArray(methodsParams)) {
			for(int i = 0; i < methodsParams.length; i++) {
				if(ClassUtils.isPrimitiveArray(methodsParams[i])) {
					if(ClassUtils.isPrimitiveWrapperArray(paramTypes[i])) {
						args[i] = ReflectionUtils.toPrimitiveArray(args[i]);
					}
				}
			}
		}

		return method.invoke(object, args);
	}

	private static boolean hasPrimitiveArray(Class<?>[] paramTypes) {
		for (Class<?> paramType : paramTypes) {
			if (ClassUtils.isPrimitiveArray(paramType))
				return true;
		}

		return false;
	}

	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) with given name and a single parameter.  If no such method
	 * can be found, return <code>null</code>.
	 * Basically, a convenience wrapper that constructs a <code>Class</code>
	 * array for you.</p>
	 *
	 * @param clazz get method from this class
	 * @param methodName get method with this name
	 * @return the accessible method
	 */
	public static Method getAccessibleMethod(Class<?> clazz, String methodName) {
		return getAccessibleMethod(clazz, methodName, EMPTY_CLASS_PARAMETERS);
	}

	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) with given name and a single parameter.  If no such method
	 * can be found, return <code>null</code>.
	 * Basically, a convenience wrapper that constructs a <code>Class</code>
	 * array for you.</p>
	 *
	 * @param clazz get method from this class
	 * @param methodName get method with this name
	 * @param paramType taking this type of parameter
	 * @return the accessible method
	 */
	public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class<?> paramType) {
		Class<?>[] paramTypes = { paramType };
		return getAccessibleMethod(clazz, methodName, paramTypes);
	}

	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) with given name and parameters.  If no such method
	 * can be found, return <code>null</code>.
	 * This is just a convenient wrapper for
	 * {@link #getAccessibleMethod(Method method)}.</p>
	 *
	 * @param clazz get method from this class
	 * @param methodName get method with this name
	 * @param paramTypes with these parameters types
	 * @return the accessible method
	 */
	public static Method getAccessibleMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
		try {
			MethodDescriptor md = new MethodDescriptor(clazz, methodName, paramTypes, true);

			// Check the cache first
			Method method = getCachedMethod(md);
            if(method != null) {
                return method;
            }

			method = getAccessibleMethod(clazz.getMethod(methodName, paramTypes));
			cacheMethod(md, method);

			return method;
		} catch(NoSuchMethodException e) {
			return (null);
		}
	}

    /**
     * <p>Return an accessible method (that is, one that can be invoked via
     * reflection) that implements the specified Method.  If no such method
     * can be found, return <code>null</code>.</p>
     *
     * @param method the method that we wish to call
     * @return the accessible method
     */
    public static Method getAccessibleMethod(Method method) {
        // Make sure we have a method to check
        if(method == null) {
            return null;
        }

        return getAccessibleMethod(method.getDeclaringClass(), method);
    }

    /**
     * <p>Return an accessible method (that is, one that can be invoked via
     * reflection) that implements the specified Method.  If no such method
     * can be found, return <code>null</code>.</p>
     *
     * @param clazz The class of the object
     * @param method The method that we wish to call
     * @return The accessible method
     */
    public static Method getAccessibleMethod(Class<?> clazz, Method method) {
        // Make sure we have a method to check
        if(method == null) {
            return null;
        }

        // If the requested method is not public we cannot call it
        if(!Modifier.isPublic(method.getModifiers())) {
            return null;
        }

        boolean sameClass;
        if(clazz == null) {
			sameClass = true;
			clazz = method.getDeclaringClass();
        } else {
            sameClass = clazz.equals(method.getDeclaringClass());
            if(!method.getDeclaringClass().isAssignableFrom(clazz)) {
                throw new IllegalArgumentException(clazz.getName() + " is not assignable from " + method.getDeclaringClass().getName());
            }
        }

        // If the class is public, we are done
        if(Modifier.isPublic(clazz.getModifiers())) {
            if(!sameClass) {
                ReflectionUtils.makeAccessible(method); // Default access superclass workaround
            }
            return method;
        }

        String methodName = method.getName();
        Class<?>[] paramTypes = method.getParameterTypes();

        // Check the implemented interfaces and subinterfaces
        method = getAccessibleMethodFromInterfaceNest(clazz, methodName, paramTypes);

        // Check the superclass chain
        if(method == null) {
            method = getAccessibleMethodFromSuperclass(clazz, methodName, paramTypes);
        }

        return method;
    }

	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) by scanning through the superclasses. If no such method
	 * can be found, return <code>null</code>.</p>
	 *
	 * @param clazz Class to be checked
	 * @param methodName Method name of the method we wish to call
	 * @param paramTypes The parameter type signatures
	 */
	private static Method getAccessibleMethodFromSuperclass(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
		Class<?> parentClazz = clazz.getSuperclass();

		while(parentClazz != null) {
			if(Modifier.isPublic(parentClazz.getModifiers())) {
				try {
					return parentClazz.getMethod(methodName, paramTypes);
				} catch(NoSuchMethodException e) {
					return null;
				}
			}

			parentClazz = parentClazz.getSuperclass();
		}

		return null;
	}

	/**
	 * <p>Return an accessible method (that is, one that can be invoked via
	 * reflection) that implements the specified method, by scanning through
	 * all implemented interfaces and subinterfaces.  If no such method
	 * can be found, return <code>null</code>.</p>
	 *
	 * <p> There isn't any good reason why this method must be private.
	 * It is because there doesn't seem any reason why other classes should
	 * call this rather than the higher level methods.</p>
	 *
	 * @param clazz Parent class for the interfaces to be checked
	 * @param methodName Method name of the method we wish to call
	 * @param paramTypes The parameter type signatures
	 */
	private static Method getAccessibleMethodFromInterfaceNest(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
		Method method = null;

		// Search up the superclass chain
		for(; clazz != null; clazz = clazz.getSuperclass()) {
			// Check the implemented interfaces of the parent class
			Class<?>[] interfaces = clazz.getInterfaces();

			for(Class<?> anInterface : interfaces) {
				// Is this interface public?
				if(!Modifier.isPublic(anInterface.getModifiers())) {
					continue;
				}

				// Does the method exist on this interface?
				try {
					method = anInterface.getDeclaredMethod(methodName, paramTypes);
				} catch(NoSuchMethodException e) {
					/* Swallow, if no method is found after the loop then this
					 * method returns null.
					 */
				}
				if(method != null) {
					return method;
				}

				// Recursively check our parent interfaces
				method = getAccessibleMethodFromInterfaceNest(anInterface, methodName, paramTypes);
				if(method != null) {
					return method;
				}
			}
		}

		// We did not find anything
		return null;
	}

	/**
	 * <p>Find an accessible method that matches the given name and has compatible parameters.
	 * Compatible parameters mean that every method parameter is assignable from
	 * the given parameters.
	 * In other words, it finds a method with the given name
	 * that will take the parameters given.
	 *
	 * <p>This method is slightly undeterminstic since it loops
	 * through methods names and return the first matching method.</p>
	 *
	 * <p>This method is used by
	 * {@link #invokeMethod(Object object,String methodName,Object[] args,Class[] paramTypes)}.
	 *
	 * <p>This method can match primitive parameter by passing in wrapper classes.
	 * For example, a <code>Boolean</code> will match a primitive <code>boolean</code>
	 * parameter.
	 *
	 * @param clazz find method in this class
	 * @param methodName find method with this name
	 * @param paramTypes find method with compatible parameters
	 * @return The accessible method
	 */
	public static Method getMatchingAccessibleMethod(Class<?> clazz, String methodName, Class<?>[] paramTypes) {
		MethodDescriptor md = new MethodDescriptor(clazz, methodName, paramTypes, false);

		// see if we can find the method directly
		// most of the time this works and it's much faster
		try {
			// Check the cache first
			Method method = getCachedMethod(md);
			if(method != null) {
				return method;
			}

			method = clazz.getMethod(methodName, paramTypes);
			ReflectionUtils.makeAccessible(method); // Default access superclass workaround
			cacheMethod(md, method);

			return method;
		} catch(NoSuchMethodException e) { /* SWALLOW */ }

		// search through all methods
		int paramSize = paramTypes.length;
		Method bestMatch = null;
		Method[] methods = clazz.getMethods();
		float bestMatchWeight = Float.MAX_VALUE;
		float myWeight;

		for(Method method : methods) {
			if(method.getName().equals(methodName)) {
				// compare parameters
				Class<?>[] methodsParams = method.getParameterTypes();
				int methodParamSize = methodsParams.length;
				if(methodParamSize == paramSize) {
					boolean paramMatch = true;
					for(int n = 0; n < methodParamSize; n++) {
						if(!ClassUtils.isAssignable(methodsParams[n], paramTypes[n])) {
							paramMatch = false;
							break;
						}
					}

					if(paramMatch) {
						myWeight = ReflectionUtils.getTypeDifferenceWeight(method.getParameterTypes(), paramTypes);
						if(myWeight < bestMatchWeight) {
							bestMatch = method;
							bestMatchWeight = myWeight;
						}
					}
				}
			}
		}
		
		if(bestMatch != null) {
			cacheMethod(md, bestMatch);
		}
		
		return bestMatch;
	}

    /**
     * Return the method from the cache, if present.
     *
     * @param md The method descriptor
     * @return The cached method
     */
    private static Method getCachedMethod(MethodDescriptor md) {
        if(cacheEnabled) {
            Reference<Method> methodRef = cache.get(md);
            if(methodRef != null) {
                return methodRef.get();
            }
        }
        return null;
    }

    /**
     * Add a method to the cache.
     *
     * @param md The method descriptor
     * @param method The method to cache
     */
    private static void cacheMethod(MethodDescriptor md, Method method) {
        if(cacheEnabled) {
            if(method != null) {
            	cache.put(md, new WeakReference<>(method));
            }
        }
    }

    /**
     * Set whether methods should be cached for greater performance or not,
     * default is <code>true</code>.
     *
     * @param cacheEnabling <code>true</code> if methods should be
     * cached for greater performance, otherwise <code>false</code>
     */
    public static synchronized void setCacheEnabled(boolean cacheEnabling) {
    	cacheEnabled = cacheEnabling;
        if(!cacheEnabled) {
            clearCache();
        }
    }

    /**
     * Clear the method cache.
     * @return the number of cached methods cleared
     */
    public static synchronized int clearCache() {
        int size = cache.size();
        cache.clear();
        return size;
    }
    
	/**
	 * Represents the key to looking up a Method by reflection.
	 */
	private static class MethodDescriptor {
		
		private Class<?> cls;

		private String methodName;

		private Class<?>[] paramTypes;

		private boolean exact;

		private int hashCode;

		/**
		 * The sole constructor.
		 *
		 * @param cls the class to reflect, must not be null
		 * @param methodName the method name to obtain
		 * @param paramTypes the array of classes representing the paramater types
		 * @param exact whether the match has to be exact
		 */
		public MethodDescriptor(Class<?> cls, String methodName, Class<?>[] paramTypes, boolean exact) {
			if(cls == null) {
				throw new IllegalArgumentException("Class cannot be null.");
			}
			if(methodName == null) {
				throw new IllegalArgumentException("Method Name cannot be null.");
			}
			if(paramTypes == null) {
				paramTypes = EMPTY_CLASS_PARAMETERS;
			}

			this.cls = cls;
			this.methodName = methodName;
			this.paramTypes = paramTypes;
			this.exact = exact;

			this.hashCode = methodName.length();
		}

		/**
		 * Checks for equality.
		 * @param obj object to be tested for equality
		 * @return true, if the object describes the same Method
		 */
		@Override
		public boolean equals(Object obj) {
			if(!(obj instanceof MethodDescriptor)) {
				return false;
			}

			MethodDescriptor md = (MethodDescriptor)obj;

			return (
					exact == md.exact &&
					methodName.equals(md.methodName) &&
					cls.equals(md.cls) &&
					Arrays.equals(paramTypes, md.paramTypes)
				);
		}

		/**
		 * Returns the string length of method name. I.e. if the
		 * hashcodes are different, the objects are different. If the
		 * hashcodes are the same, need to use the equals method to
		 * determine equality.
		 * @return the string length of method name
		 */
		@Override
		public int hashCode() {
			return hashCode;
		}
	}

}
