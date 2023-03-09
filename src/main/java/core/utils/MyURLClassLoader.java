package core.utils;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class MyURLClassLoader {
    private URLClassLoader classLoader;

    public MyURLClassLoader(String jarName) {
        try {
            classLoader = getURLClassLoader(jarName);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public Class loadClass(String className) {
        try {
            //由于我项目中已经有了 commons-beanutils:1.9.4，如果使用 loadClass 方法，加载的是项目 ClassPath 下的 commons-beanutils
            //为了避免这种情况，所以调用了 findClass 方法
            Method method = URLClassLoader.class.getDeclaredMethod("findClass", new Class[]{String.class});
            method.setAccessible(true);
            Class clazz = (Class) method.invoke(this.classLoader, new Object[]{className});
            return clazz;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        return null;
    }

    private URLClassLoader  getURLClassLoader(String jarName) throws MalformedURLException {
        URL url = this.getClass().getClassLoader().getResource("libs" + File.separator + jarName);
        URLClassLoader urlClassLoader = new URLClassLoader(new URL[]{url});
        return urlClassLoader;
    }

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        MyURLClassLoader classLoader = new MyURLClassLoader("commons-beanutils-1.9.2.jar");
        Class clazz = classLoader.loadClass("org.apache.commons.beanutils.BeanComparator");
//        Class clazz = classLoader.loadClass("org.apache.commons.beanutils.BeanComparator");
        Object comparator = clazz.getDeclaredConstructor(new Class[]{String.class}).newInstance(new Object[]{"lowestSetBit"});
    }
}
