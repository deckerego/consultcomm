X11_DIR = /usr/X11
JAVA_DIR = /usr/j2se

CC=cc

INCLUDE = -I$(X11_DIR)/include -I$(JAVA_DIR)/include -I$(JAVA_DIR)/include/solaris
LDFLAGS = -G -L$(X11_DIR)/lib -R$(X11_DIR)/lib -lX11 -lXext
CFLAGS = $(INCLUDE)

all: ../libtimeout.so

../libtimeout.so: timeout.o
	$(CC) $(CFLAGS) -o $@ timeout.o $(LDFLAGS)

clean:
	@rm -f *.o
	@rm -f ../libtimeout.so

realclean: clean
	@rm -f TimeOut.h
