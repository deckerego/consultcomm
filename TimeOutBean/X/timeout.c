#include <stdio.h>
#include <jni.h>
#include <X11/Xlib.h>
#include <X11/extensions/scrnsaver.h>
#include "TimeOut.h"

/**
 * Major thanks to Dan Price for (re)writing this whole library to
 * work correctly, even under Solaris and VNC. Thanks also for
 * plugging the memory leaks and error detection.
*/
static int get_x_idle_time(long *idle) {
  XScreenSaverInfo *ss_info = NULL;
  Display *display;
  static int firsttime = 1; //Is this the first time we've run this method?

  if((display = XOpenDisplay(NULL)) == NULL) {
    printf("Display is null\n");
    return(-1);
  }

  if(firsttime) {
    int evb, erb;
    if(XScreenSaverQueryExtension(display, &evb, &erb) == False) {
      XCloseDisplay(display);
      return(-2);
    }
    firsttime = 0;
  }

  if((ss_info = XScreenSaverAllocInfo()) == NULL) {
    XCloseDisplay(display);
    return(-1);
  }

  if(XScreenSaverQueryInfo(display, DefaultRootWindow(display), ss_info) == 0) {
    XFree(ss_info);
    XCloseDisplay(display);
    return(-1);
  }
  *idle = ss_info->idle;

  XFree(ss_info);
  XCloseDisplay(display);
  return(0);
}

JNIEXPORT jlong JNICALL Java_ClntComm_getIdleTime(JNIEnv *env, jobject obj) {
  int ret;
  long time = 0;

  ret = get_x_idle_time(&time);

  if(ret != 0) return(time);
  return(0);
}

