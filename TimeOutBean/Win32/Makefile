CC = i586-mingw32-gcc
AS = i586-mingw32-as
DLLWRAP = i586-mingw32-dllwrap

INCLUDE = -I/home/jellis/.cxoffice/dotwine/fake_windows/j2sdk1.4.2_07/include -I/home/jellis/.cxoffice/dotwine/fake_windows/j2sdk1.4.2_07/include/win32 -I.
LDFLAGS = -L/usr/local/cross-compile/i586-mingw32/lib -lm -lmingwex -lsupc++ -lstdc++
CFLAGS = -mms-bitfields $(INCLUDE)

DLLWRAP_FLAGS = --as=$(AS) --export-all --driver-name $(CC) -s

all: ../timeout.dll

../timeout.dll: timeout.o
	$(DLLWRAP) $(DLLWRAP_FLAGS) $(CFLAGS) timeout.o --output-def timeout.def -o $@ $(LDFLAGS)

timeout.o: timeout.cpp
	$(CC) -c $(CFLAGS) $^

clean:
	@rm -f *.o
	@rm -f *.dll
	@rm -f *.def

realclean: clean
	@rm -f TimeOut.h
