CC=$(JDK_HOME)/bin/javac
JAVA=$(JDK_HOME)/bin/java
JAR=$(JDK_HOME)/bin/jar
CLASSES=$(CLASSPATH):XML/crimson.jar:XML/jaxp.jar:XML/xalan.jar
MFST=CsltMfst
CCFLAGS=-O -deprecation
WEBBASE=..
SOURCEPKG=CsltComm.src.tar.gz
BINPKG=CsltComm.zip

all:
	$(CC) $(CCFLAGS) -classpath $(CLASSES) *.java

dryrun:
	$(JAVA) -classpath $(CLASSES):./ CsltComm &

package:
	$(JAR) cfm CsltComm.jar $(MFST) *.class graphics/*.gif
	zip -r $(BINPKG) CsltComm.jar README AUTHORS COPYING CHANGES INSTALL XML graphics/*.gif
	tar -czf $(SOURCEPKG) *.java *.form graphics/*.gif README AUTHORS COPYING CHANGES INSTALL CsltMfst

release: webupdate
	ncftpput upload.sourceforge.net incoming/ $(SOURCEPKG) $(BINPKG)
	mount /mnt/public
	cp $(BINPKG) /mnt/public/Installs/ConsultComm
	cp $(SOURCEPKG) /mnt/public/Installs/ConsultComm
	cp INSTALL /mnt/public/Installs/ConsultComm

webupdate:
	scp $(WEBBASE)/CsltComm.html jtellis@consultcomm.sourceforge.net:/home/groups/c/co/consultcomm/htdocs/index.html

clean:
	rm -f *.class
	rm -f *.def

realclean: clean
	rm -f CsltComm.zip
	rm -f CsltComm.src.tar.gz
	rm -f CsltComm.jar
