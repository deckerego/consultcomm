CC=$(JDK_HOME)/bin/javac
JAR=$(JDK_HOME)/bin/jar
CLASSES=$(CLASSPATH):XML/crimson.jar:XML/jaxp.jar:XML/xalan.jar
MFST=CsltMfst
CCFLAGS=-O -deprecation

all:
	$(CC) $(CCFLAGS) -classpath $(CLASSES) *.java

package:
	$(JAR) cfm CsltComm.jar $(MFST) *.class graphics/*.gif
	zip -r CsltComm.zip CsltComm.jar README AUTHORS COPYING CHANGES INSTALL XML graphics/*.gif
	tar -czf CsltComm.src.tar.gz *.java *.form graphics/*.gif README AUTHORS COPYING CHANGES INSTALL CsltMfst

clean:
	@rm -f *.class

realclean: clean
	@rm CsltComm.zip
	@rm CsltComm.src.tar.gz
	@rm CsltComm.jar
