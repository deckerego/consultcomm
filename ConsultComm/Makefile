CC=/usr/java/jdk1.3.1/bin/javac
JAR=/usr/java/jdk1.3.1/bin/jar
MFST=CsltMfst
CCFLAGS=-O

all:
	$(CC) $(CCFLAGS) *.java

package:
	$(JAR) cfm CsltComm.jar $(MFST) *.class graphics/*.gif graphics/*.bmp
	zip -r CsltComm.zip CsltComm.jar README AUTHORS COPYING CHANGES XML
	tar -czf CsltComm.src.tar.gz *.java *.form graphics/*.gif graphics/*.bmp README AUTHORS COPYING CHANGES CsltMfst

clean:
	@rm -f *.class

