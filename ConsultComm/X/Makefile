INCLUDE = -I/usr/X11R6/include -I/usr/java/j2sdk1.4.1/include -I/usr/java/j2sdk1.4.1/include/linux
LDFLAGS = -L/usr/X11R6/lib -rdynamic -lX11
CFLAGS = -shared $(INCLUDE)

all: libtimeout.so

libtimeout.so: timeout.o
	gcc $(CFLAGS) -o $@ timeout.o $(LDFLAGS)

clean:
	@rm -f *.o
	@rm -f *.so

realclean: clean
	@rm -f ClntComm.h
