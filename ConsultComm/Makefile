CC=$(JDK_HOME)/bin/javac
JAVA=$(JDK_HOME)/bin/java
JAR=$(JDK_HOME)/bin/jar
CLASSES=$(CLASSPATH):XML/crimson.jar:XML/jaxp.jar:XML/xalan.jar
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
	zip $(BINPKG) CsltComm.jar README AUTHORS COPYING CHANGES INSTALL XML graphics/*.gif
	tar -cvzf $(SOURCEPKG) *.java *.form graphics/*.gif README AUTHORS COPYING CHANGES INSTALL CsltMfst

clean:
	rm -f *.class
	rm -f *.def

realclean: clean
	rm -f CsltComm.zip
	rm -f CsltComm.src.tar.gz
	rm -f CsltComm.jar
