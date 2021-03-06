#!/bin/sh


# The HEAP_SIZE variable line defines the Java heap size in MB. 
# That is the total amount of memory available to Guineu.
# Please adjust according to the amount of memory of your computer.
# Maximum value on a 32-bit Mac OS X system is about 2000. 
HEAP_SIZE=1024

# If you have a 64-bit CPU, 64-bit OS and 64-bit JVM installed, you 
# can run Guineu in 64-bit mode and increase the HEAP_SIZE above 
# the limitations of 32-bit platform. In that case, please set the 
# value of USE_64_BIT parameter to "-d64" (without quotes).
USE_64_BIT=

# The TMP_FILE_DIRECTORY parameter defines the location where temporary 
# files (parsed raw data) will be placed. Default is /tmp.
TMP_FILE_DIRECTORY=/tmp

# It is usually not necessary to modify the JAVA_COMMAND parameter, but 
# if you like to run a specific Java Virtual Machine, you may set the 
# path to the java command of that JVM. By default, we use Mac OS X 
# specific path to enforce using Java 6 VM
JAVA_COMMAND=/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Commands/java

# It is not necessary to modify the following section
LOGGING_CONFIG_FILE=conf/logging.properties
JAVA_PARAMETERS="-XX:+UseParallelGC -Xdock:name='Guineu' -Xdock:icon=icons/GuineuIcon.png -Djava.io.tmpdir=$TMP_FILE_DIRECTORY $USE_64_BIT -Dapple.laf.useScreenMenuBar=true -Djava.util.logging.config.file=$LOGGING_CONFIG_FILE -Xms${HEAP_SIZE}m -Xmx${HEAP_SIZE}m"
CLASS_PATH=Guineu.jar
MAIN_CLASS=guineu.main.GuineuCore 

# Make sure we are in the correct directory
SCRIPTDIR=`dirname "$0"`
cd "$SCRIPTDIR"

# Show java version, in case a problem occurs
echo "-version" | xargs $JAVA_COMMAND

# This command starts the Java Virtual Machine
echo "$JAVA_PARAMETERS" -classpath $CLASS_PATH $MAIN_CLASS | xargs $JAVA_COMMAND

