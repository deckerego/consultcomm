/**
 * Major props to Sidney Chong at http://www.codeproject.com/dll/trackuseridle.asp
 * for explaining how the heck to correctly manage mouse & keyboard event hooks
 * in a Win32 dll.
 */

#include <windows.h>
//#include <crtdbg.h>
#include <stdio.h>
#include <jni.h>
#include "TimeOut.h"


#pragma data_seg(".win32idle")
HHOOK 	keyboardHook = NULL;
HHOOK 	mouseHook = NULL;
LONG	mouseX = -1;
LONG	mouseY = -1;
DWORD	lastEvent = 0;
#pragma data_seg()
#pragma comment(linker, "/section:.win32idle,rws")

HINSTANCE handleInstance = NULL;

LRESULT CALLBACK KeyboardTracker(int code, WPARAM wParam, LPARAM lParam)
{
	if (code==HC_ACTION) lastEvent = GetTickCount();
	return ::CallNextHookEx(keyboardHook, code, wParam, lParam);
}

LRESULT CALLBACK MouseTracker(int code, WPARAM wParam, LPARAM lParam)
{
	if (code==HC_ACTION) {
		MOUSEHOOKSTRUCT* pointer = (MOUSEHOOKSTRUCT*)lParam;
		if (pointer->pt.x != mouseX || pointer->pt.y != mouseY) {
			mouseX = pointer->pt.x;
			mouseY = pointer->pt.y;
			lastEvent = GetTickCount();
		}
	}
	return ::CallNextHookEx(mouseHook, code, wParam, lParam);
}

__declspec(dllexport) BOOL load()
{
	if (keyboardHook == NULL)
		keyboardHook = SetWindowsHookEx(WH_KEYBOARD, KeyboardTracker, handleInstance, 0);
	if (mouseHook == NULL)
		mouseHook = SetWindowsHookEx(WH_MOUSE, MouseTracker, handleInstance, 0);

	lastEvent = GetTickCount();

	if (!keyboardHook || !mouseHook) return FALSE;
	else return TRUE;
}

__declspec(dllexport) void unload()
{
	if (keyboardHook) {
		UnhookWindowsHookEx(keyboardHook);
		keyboardHook = NULL;
	}
	if (mouseHook) {
		UnhookWindowsHookEx(mouseHook);
		mouseHook = NULL;
	}
}

int WINAPI DllMain(HINSTANCE hInstance, DWORD dwReason, LPVOID lpReserved)
{
	switch(dwReason) {
		case DLL_PROCESS_ATTACH:
			handleInstance = hInstance;
			DisableThreadLibraryCalls(handleInstance);
			load();
			break;
		case DLL_PROCESS_DETACH:
			unload();
			break;
	}
	return TRUE;
}

JNIEXPORT jlong JNICALL Java_TimeOut_getIdleTime (JNIEnv *env, jobject obj)
{
	return (GetTickCount() - lastEvent);
}
