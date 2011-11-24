package de.tutorials.java.jvm.tools.jvmoa;

import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.util.ServiceLoader;

import javax.management.JMX;
import javax.management.MBeanServer;
import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import de.tutorials.java.jvm.tools.jvmoa.analyzer.JvmOptionsAnalyzer;

public class Main {

	private final static String REMOTE_SWITCH = "-remote=";

	private ServiceLoader<JvmOptionsAnalyzer> jvmOptionsAnalyzerLoader = ServiceLoader.load(JvmOptionsAnalyzer.class);

	private boolean useRemoteMBeanServerConnection;

	private String remoteMBeanServerJmxUrl;

	public static void main(String[] args) {
		new Main().run(args);
	}

	private void run(String[] args) {
		analyzeCommandLine(args);
		getJvmOptionAnalyzer().listCurrentJVMOptions();
	}

	private void analyzeCommandLine(String[] args) {
		for (String arg : args) {
			if (arg.startsWith(REMOTE_SWITCH)) {
				useRemoteMBeanServerConnection = true;
				remoteMBeanServerJmxUrl = arg.substring(REMOTE_SWITCH.length());
			}
		}
	}

	private JvmOptionsAnalyzer getJvmOptionAnalyzer() {
		MBeanServerConnection connection = getMBeanServerConnection();
		RuntimeMXBean runtime = getRuntimeMXBean(connection);
		JvmOptionsAnalyzer jvmOptionsAnalyzer = getJvmOptionAnalyzerFor(runtime);
		jvmOptionsAnalyzer.setMBeanServerConnection(connection);
		jvmOptionsAnalyzer.setRuntimeMXBean(runtime);
		return jvmOptionsAnalyzer;
	}

	protected RuntimeMXBean getRuntimeMXBean(MBeanServerConnection mbeanServerConnection) {
		try {
			return JMX.newMBeanProxy(mbeanServerConnection, new ObjectName(ManagementFactory.RUNTIME_MXBEAN_NAME), RuntimeMXBean.class);
		} catch (Exception e) {
			throw new RuntimeException("Could not get RuntimeMXBean from mbeanServerConnection.", e);
		}
	}

	private JvmOptionsAnalyzer getJvmOptionAnalyzerFor(RuntimeMXBean runtime) {
		for (JvmOptionsAnalyzer jvmOptionsAnalyzer : jvmOptionsAnalyzerLoader) {
			if (jvmOptionsAnalyzer.supports(runtime)) {
				return jvmOptionsAnalyzer;
			}
		}
		throw new RuntimeException("No JVM Option Analyzer found. Unsupported runtime: " + runtime);
	}

	private MBeanServerConnection getMBeanServerConnection() {
		return useRemoteMBeanServerConnection ? getRemoteMBeanServerConnection() : getLocaleMBeanServerConnection();
	}

	private MBeanServer getLocaleMBeanServerConnection() {
		return ManagementFactory.getPlatformMBeanServer();
	}

	private MBeanServerConnection getRemoteMBeanServerConnection() {
		try {
			return JMXConnectorFactory.connect(new JMXServiceURL(remoteMBeanServerJmxUrl)).getMBeanServerConnection();
		} catch (Exception e) {
			throw new RuntimeException("Could not connect to jmxurl: " + remoteMBeanServerJmxUrl, e);
		}
	}
}
