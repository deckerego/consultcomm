/**
 * Major props to Sidney Chong at http://www.codeproject.com/dll/trackuseridle.asp
 * for explaining how the heck to correctly manage mouse & keyboard event hooks
 * in a Win32 dll.
 */

#include <windows.h>
#include <crtdbg.h>
#include <stdio.h>
#include <jni.h>
#include "ClntComm.h"


HINSTANCE handleInstance = NULL;
HHOOK 	keyboardHook = NULL;
HHOOK 	mouseHook = NULL;
LONG	mouseX = -1;
LONG	mouseY = -1;
DWORD	lastEvent = 0;

LRESULT CALLBACK KeyboardTracker(int code, WPARAM wParam, LPARAM lParam)
{
	if (code==HC_ACTION) lastEvent = GetTickCount();
	return ::CallNextHookEx(keyboardHook, code, wParam, lParam);
}

LRESULT CALLBACK MouseTracker(int code, WPARAM wParam, LPARAM lParam)
{
	if (code==HC_ACTION) {
		MOUSEHOOKSTRUCT* pStruct = (MOUSEHOOKSTRUCT*)lParam;
		if (pStruct->pt.x != mouseX || pStruct->pt.y != mouseY) {
			mouseX = pStruct->pt.x;
			mouseY = pStruct->pt.y;
			lastEvent = GetTickCount();
		}
	}
	return ::CallNextHookEx(mouseHook, code, wParam, lParam);
}

__declspec(dllexport) BOOL load()
{
	if (keyboardHook == NULL) {
		keyboardHook = SetWindowsHookEx(WH_KEYBOARD, KeyboardTracker, handleInstance, 0);
	}
	if (mouseHook == NULL) {
		mouseHook = SetWindowsHookEx(WH_MOUSE, MouseTracker, handleInstance, 0);
	}

	_ASSERT(keyboardHook);
	_ASSERT(mouseHook);

	lastEvent = GetTickCount();

	if (!keyboardHook || !mouseHook)
		return FALSE;
	else
		return TRUE;
}

__declspec(dllexport) void unload()
{
	BOOL bResult;
	if (keyboardHook)
	{
		bResult = UnhookWindowsHookEx(keyboardHook);
		_ASSERT(bResult);
		keyboardHook = NULL;
	}
	if (mouseHook)
	{
		bResult = UnhookWindowsHookEx(mouseHook);
		_ASSERT(bResult);
		mouseHook = NULL;
	}
}

int WINAPI DllMain(HINSTANCE hInstance, DWORD dwReason, LPVOID lpReserved)
{
	switch(dwReason) {
		case DLL_PROCESS_ATTACH:
			DisableThreadLibraryCalls(hInstance);
			handleInstance = hInstance;
			load();
			break;
		case DLL_PROCESS_DETACH:
			unload();
			break;
	}
	return TRUE;
}

JNIEXPORT jlong JNICALL Java_ClntComm_getIdleTime (JNIEnv *env, jobject obj)
{
  return (GetTickCount() - lastEvent);
}