INCLUDE = -I/usr/include/glib-1.2 -I/usr/lib/glib/include -I/usr/X11R6/include -I/usr/java/jdk1.3.1/include -I/usr/java/jdk1.3.1/include/linux
LDFLAGS = -lXss -lXext -L/usr/lib -L/usr/X11R6/lib -lgtk -lgdk -rdynamic -lgmodule -lglib -ldl -lXi -lXext -lX11 -lm
CFLAGS = -shared $(INCLUDE)

all: libtimeout.so

libtimeout.so: timeout.o
	gcc $(CFLAGS) -o $@ timeout.o $(LDFLAGS)

clean:
	@rm -f *.o
	@rm -f *.so
