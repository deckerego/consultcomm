TARGET_ALIAS = i586-mingw32
WIN32_DIR = /home/jellis/.cxoffice/dotwine/fake_windows
JAVA_DIR = $(WIN32_DIR)/j2sdk1.4.2_07
XCOMPILE_DIR = /usr/local/cross-compile/$(TARGET_ALIAS)

CC = $(TARGET_ALIAS)-gcc
AS = $(TARGET_ALIAS)-as
DLLWRAP = $(TARGET_ALIAS)-dllwrap

INCLUDE = -I$(JAVA_DIR)/include -I$(JAVA_DIR)/include/win32 -I.
LDFLAGS = -L$(XCOMPILE_DIR)/lib -lm -lmingwex -lsupc++ -lstdc++
CFLAGS = -mms-bitfields $(INCLUDE)
DLLWRAP_FLAGS = --as=$(AS) --export-all --driver-name $(CC) -s

all: ../timeout.dll

../timeout.dll: timeout.o
	$(DLLWRAP) $(DLLWRAP_FLAGS) $(CFLAGS) timeout.o --output-def timeout.def -o $@ $(LDFLAGS)

timeout.o: timeout.cpp
	$(CC) -c $(CFLAGS) $^

clean:
	@rm -f *.o
	@rm -f *.def
	@rm -f ../timeout.dll

realclean: clean
	@rm -f TimeOut.h
