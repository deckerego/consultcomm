#include <windows.h>
#include <jni.h>
#include "TimeOut.h"

JNIEXPORT jlong JNICALL Java_TimeOut_getIdleTime
  (JNIEnv *, jclass) {
  DWORD tickCount;
  LASTINPUTINFO lastInputInfo;
  lastInputInfo.cbSize = sizeof(LASTINPUTINFO);

  GetLastInputInfo(&lastInputInfo);
  tickCount = GetTickCount();

  return (jlong)(tickCount - lastInputInfo.dwTime);
}
