X11_DIR = /usr/X11R6
JAVA_DIR = /usr/lib/java2

CC=gcc

INCLUDE = -I$(X11_DIR)/include -I$(JAVA_DIR)/include -I$(JAVA_DIR)/include/linux
LDFLAGS = -L$(X11_DIR)/lib -rdynamic -lX11 -lXss -lXext 
CFLAGS = -shared $(INCLUDE)

all: ../libtimeout.so

../libtimeout.so: timeout.o
	$(CC) $(CFLAGS) -o $@ timeout.o $(LDFLAGS)

clean:
	@rm -f *.o
	@rm -f ../libtimeout.so

realclean: clean
	@rm -f TimeOut.h
