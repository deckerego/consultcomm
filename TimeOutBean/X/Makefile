INCLUDE = -I/usr/X11R6/include -I/usr/lib/java2/include -I/usr/lib/java2/include/linux
LDFLAGS = -L/usr/X11R6/lib -rdynamic -lX11 -lXss -lXext 
CFLAGS = -shared $(INCLUDE)
CC=gcc

# These may be useful on Solaris systems; comment out the above definitions and
# define these instead.
#INCLUDE = -I/usr/X11/include -I/usr/j2se/include -I/usr/j2se/include/solaris
#LDFLAGS = -G -L/usr/X11/lib -R/usr/X11/lib -lX11 -lXext
#CFLAGS = $(INCLUDE)
# Known to work with GCC and the SunPro compiler
#CC=cc

all: ../libtimeout.so

../libtimeout.so: timeout.o
	$(CC) $(CFLAGS) -o $@ timeout.o $(LDFLAGS)

clean:
	@rm -f *.o
	@rm -f *.so

realclean: clean
	@rm -f TimeOut.h
