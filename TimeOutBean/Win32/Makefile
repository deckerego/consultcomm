TARGET_ALIAS = i586-mingw32
WIN32_DIR = $(HOME)/.cxoffice/dotwine/fake_windows
JAVA_HOME = $(WIN32_DIR)/j2sdk1.4.2_07
XCOMPILE_DIR = /usr/local/cross-compile/$(TARGET_ALIAS)

CC = $(TARGET_ALIAS)-c++
AS = $(TARGET_ALIAS)-as
DLLWRAP = $(TARGET_ALIAS)-dllwrap

INCLUDE = -I$(JAVA_HOME)/include -I$(JAVA_HOME)/include/win32 -I.
LDFLAGS = -L$(XCOMPILE_DIR)/lib -lm -lmingwex -lsupc++ -lstdc++
CFLAGS =  -g -O2 -c $(INCLUDE) -g 
DLLWRAP_FLAGS = --as=$(AS) --driver-name=$(CC) --add-stdcall-alias -s

all: timeout.dll

timeout.dll: timeout.o
	$(DLLWRAP) $(DLLWRAP_FLAGS) --output-def timeout.def -o $@ $^

timeout.o: timeout.cpp
	$(CC) $(CFLAGS) $^

clean:
	@rm -f *.o
	@rm -f *.a
	@rm -f *.def
	@rm -f *.dll

realclean: clean
	@rm -f TimeOut.h
