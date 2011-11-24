package de.tutorials.java.jvm.tools.jvmoa.accessor;

import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.openmbean.CompositeData;

import de.tutorials.java.jvm.tools.jvmoa.model.JvmOption;
import de.tutorials.java.jvm.tools.jvmoa.model.UnknownJvmOption;

public class HotspotDiagnosticJvmOptionAccessor implements JvmOptionAccessor {

	private final static String GET_VM_OPTION_METHOD_NAME = "getVMOption";

	private final static String HOTSPOT_DIAGNOSTICS_OBJECT_NAME = "com.sun.management:type=HotSpotDiagnostic";

	private final MBeanServerConnection mbeanServerConnection;

	private final ObjectName hotSpotDiagnosticName;

	public HotspotDiagnosticJvmOptionAccessor(MBeanServerConnection mbeanServerConnection) {
		try {
			this.mbeanServerConnection = mbeanServerConnection;
			this.hotSpotDiagnosticName = new ObjectName(HOTSPOT_DIAGNOSTICS_OBJECT_NAME);
		} catch (MalformedObjectNameException e) {
			throw new RuntimeException(e);
		}
	}

	public JvmOption getVMOption(String optionName) {
		JvmOption result;
		try {
			Object[] paramValues = new Object[] { optionName };
			String[] paramTypes = new String[] { String.class.getName() };
			CompositeData cd = (CompositeData) mbeanServerConnection.invoke(hotSpotDiagnosticName, GET_VM_OPTION_METHOD_NAME, paramValues,paramTypes);
			result = JvmOption.from(cd);
		} catch (Exception e) {
			result = new UnknownJvmOption(optionName);
		}
		return result;
	}
}