CC=$(JDK_HOME)/bin/javac
JAVA=$(JDK_HOME)/bin/java
JAR=$(JDK_HOME)/bin/jar
CLASSES=$(CLASSPATH)
MFST=CsltMfst
CCFLAGS=-O -deprecation
SOURCEPKG=CsltComm.src.tar.gz
BINPKG=CsltComm.zip

all:
	$(CC) $(CCFLAGS) -classpath $(CLASSES) *.java

dryrun:
	$(JAVA) -classpath $(CLASSES):./ CsltComm &

package:
	$(JAR) cfm CsltComm.jar $(MFST) *.class graphics/*.gif

clean:
	rm -f *.class

realclean: clean
	rm -f *.def
	rm -f CsltComm*.zip
	rm -f CsltComm*.src.tar.gz
	rm -f CsltComm.jar
