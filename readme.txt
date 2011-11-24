Print the jvm settings of the current jvm:

Code:
java -jar jvmoa-1.0.jar

Output:
C:\development\java\workspaces\sts2.8.0\de.tutorials.training\deploy>java -jar jvmoa-1.0.jar
Okt 19, 2011 9:13:26 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printCurrentJVMVersion
Information: 1.7.0 21.0-b17 Java HotSpot(TM) 64-Bit Server VM Oracle Corporation
Okt 19, 2011 9:13:26 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printJvmOptionSummary
Information: ##### JvmOptionSummary:
Okt 19, 2011 9:13:26 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printJvmOptionSummary
Information: Options in Category: product: 478
Okt 19, 2011 9:13:26 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printJvmOptionSummary
Information: #####
Okt 19, 2011 9:13:26 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printCurrentJVMOptions
Information: #### Options in Category: product 478
JvmOption [name=AdaptivePermSizeWeight, origin=DEFAULT, value=20, writeable=false]
JvmOption [name=AdaptiveSizeDecrementScaleFactor, origin=DEFAULT, value=4, writeable=false]
JvmOption [name=AdaptiveSizeMajorGCDecayTimeScale, origin=DEFAULT, value=10, writeable=false]
JvmOption [name=AdaptiveSizePausePolicy, origin=DEFAULT, value=0, writeable=false]
....

Print the jvm settings of a remote jvm:

1) Start your App with JXM Remote enabled e.g.:

Code:  
"C:\Program Files\Java\jdk1.6.0_27\bin\java" -cp jvmoa-1.0.jar -Dcom.sun.management.jmxremote -Dcom.sun.management.jmxremote.port=4711 -Dcom.sun.management.jmxremote.authenticate=false -Dcom.sun.management.jmxremote.ssl=false de.tutorials.java.App

2) Start jvmOptionAnalyzer with -remote Flag

java -jar jvmoa-1.0.jar -remote=service:jmx:rmi:///jndi/rmi://localhost:4711/jmxrmi

Output:
C:\development\java\workspaces\sts2.8.0\de.tutorials.training\deploy>java -jar jvmoa-1.0.jar -remote=service:jmx:rmi:///jndi/rmi://localhost:4711/jmxrmi
Okt 19, 2011 9:21:13 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printCurrentJVMVersion
Information: 1.6.0_27 20.2-b06 Java HotSpot(TM) 64-Bit Server VM Sun Microsystems Inc.
Okt 19, 2011 9:21:13 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printJvmOptionSummary
Information: ##### JvmOptionSummary:
Okt 19, 2011 9:21:13 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printJvmOptionSummary
Information: Options in Category: product: 478
Okt 19, 2011 9:21:13 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printJvmOptionSummary
Information: #####
Okt 19, 2011 9:21:13 PM de.tutorials.java.jvm.tools.optionanalyzer.AbstractJvmOptionsAnalyzer printCurrentJVMOptions
Information: #### Options in Category: product 478
JvmOption [name=AdaptivePermSizeWeight, origin=DEFAULT, value=20, writeable=false]
JvmOption [name=AdaptiveSizeDecrementScaleFactor, origin=DEFAULT, value=4, writeable=false]
JvmOption [name=AdaptiveSizeMajorGCDecayTimeScale, origin=DEFAULT, value=10, writeable=false]
JvmOption [name=AdaptiveSizePausePolicy, origin=DEFAULT, value=0, writeable=false]
JvmOption [name=AdaptiveSizePolicyCollectionCostMargin, origin=DEFAULT, value=50, writeable=false]
JvmOption [name=AdaptiveSizePolicyInitializingSteps, origin=DEFAULT, value=20, writeable=false]
JvmOption [name=AdaptiveSizePolicyOutputInterval, origin=DEFAULT, value=0, writeable=false]
JvmOption [name=AdaptiveSizePolicyWeight, origin=DEFAULT, value=10, writeable=false]
JvmOption [name=AdaptiveSizeThroughPutPolicy, origin=DEFAULT, value=0, writeable=false]
...

