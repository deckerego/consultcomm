#include <stdio.h>
#include <jni.h>
#include <X11/Xlib.h>
#include <X11/extensions/scrnsaver.h>
#include "ClntComm.h"

unsigned long get_x_idle_time ( )
{
  unsigned long idle;
  XScreenSaverInfo *ss_info = NULL;
  Display *display;

  display = XOpenDisplay(NULL);
  if(display == NULL) 
    printf("Display is null\n");

  ss_info = XScreenSaverAllocInfo ();
  XScreenSaverQueryInfo ( display, DefaultRootWindow ( display ), ss_info );
  idle = ss_info->idle;

  XCloseDisplay(display);

  return ( idle );
}
JNIEXPORT jlong JNICALL Java_ClntComm_getIdleTime (JNIEnv *env, jobject obj)
{
  return ( get_x_idle_time() );
}

int main ( int argc, char *argv[] ) {
  fprintf(stdout, "Idle time: %d", get_x_idle_time());
}
