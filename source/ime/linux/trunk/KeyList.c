/***************************************************************************
 *   Copyright (C) 2002~2005 by Yuking                                     *
 *   yuking_net@sohu.com                                                   *
 *                                                                         *
 *   This program is free software; you can redistribute it and/or modify  *
 *   it under the terms of the GNU General Public License as published by  *
 *   the Free Software Foundation; either version 2 of the License, or     *
 *   (at your option) any later version.                                   *
 *                                                                         *
 *   This program is distributed in the hope that it will be useful,       *
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of        *
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the         *
 *   GNU General Public License for more details.                          *
 *                                                                         *
 *   You should have received a copy of the GNU General Public License     *
 *   along with this program; if not, write to the                         *
 *   Free Software Foundation, Inc.,                                       *
 *   59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.             *
 ***************************************************************************/
#include "stdafx.h"
#include "KeyList.h"
#include <string.h>
//#include "xim.h"

//?¨¹?¨¬¨¦¡§?¨¨??¨¢D¡À¨ª¡ê???¨®?¨®¨²?¨¬?¨´?D???¨¹
KEYCODE_LIST    keyCodeList[] = {
    {"LCTRL", L_CTRL}
    ,
    {"RCTRL", R_CTRL}
    ,
    {"LSHIFT", L_SHIFT}
    ,
    {"RSHIFT", R_SHIFT}
    ,
    {"\0", 0}
};

KEY_LIST        keyList[] = {
    {"ENTER", 13}
    ,
    {"LCTRL", 227}
    ,
    {"LSHIFT", 225}
    ,
    {"LALT", 233}
    ,
    {"RCTRL", 228}
    ,
    {"RSHIFT", 226}
    ,
    {"RALT", 234}
    ,
    {"INSERT", 8099}
    ,
    {"HOME", 8080}
    ,
    {"PGUP", 8085}
    ,
    {"END", 8087}
    ,
    {"PGDN", 8086}
    ,
    {"CTRL_CTRL", 300}
    ,
    {"CTRL_LSHIFT", 301}
    ,
    {"CTRL_LALT", 302}
    ,
    {"CTRL_RSHIFT", 303}
    ,
    {"CTRL_RALT", 304}
    ,
    {"SHIFT_LCTRL", 305}
    ,
    {"SHIFT_SHIFT", 306}
    ,
    {"SHIFT_LALT", 307}
    ,
    {"SHIFT_RCTRL", 308}
    ,
    {"SHIFT_RALT", 309}
    ,
    {"ALT_LCTRL", 310}
    ,
    {"ALT_LSHIFT", 311}
    ,
    {"ALT_ALT", 312}
    ,
    {"ALT_RCTRL", 313}
    ,
    {"ALT_RSHIFT", 314}
    ,
    {"\0", 0}
};

int GetKey (unsigned char iKeyCode, int iKeyState, int iCount)
{
    if (!iCount) {		//¨º?SHIFT?¡éCTRL?¡éALT?¨°?¨¹??¦Ì?¡Á¨¦o?¡ê??¨°???¨¹??¨¨?HOME?¡éEND??¨¤¨¤¦Ì??¨¹
	if (iKeyState == KEY_NONE) {
	    if (iKeyCode >= 80 && iKeyCode <= 99)	//¨¦??¡é???¡é¡Á¨®?¡é¨®¨°?¡éHOME?¡éEND?¡éPGUP?¡éPGDN?¡éINSERT¦Ì¨¨
		return 8000 + iKeyCode;
	    if (iKeyCode >= 225 && iKeyCode <= 233)	//¦Ì£¤¡ã¡äSHIFT?¡éCTRL?¡éALT
		return 9000 + iKeyCode;
	}
	else if (iKeyState == KEY_CTRL_COMP) {
	    switch (iKeyCode) {
	    case K_LCTRL:
		return CTRL_CTRL;
	    case K_LSHIFT:
		return CTRL_LSHIFT;
	    case K_LALT:
		return CTRL_LALT;
	    case K_RCTRL:
		return CTRL_CTRL;
	    case K_RSHIFT:
		return CTRL_RSHIFT;
	    case K_RALT:
		return CTRL_RALT;
	    default:
		return iKeyCode + 10000;
	    }
	}
	else if (iKeyState == KEY_SHIFT_COMP) {
	    switch (iKeyCode) {
	    case K_LCTRL:
		return SHIFT_LCTRL;
	    case K_LSHIFT:
		return SHIFT_SHIFT;
	    case K_LALT:
		return SHIFT_LALT;
	    case K_RCTRL:
		return SHIFT_RCTRL;
	    case K_RSHIFT:
		return SHIFT_SHIFT;
	    case K_RALT:
		return SHIFT_RALT;
	    default:
		return iKeyCode + 11000;
	    }
	}
	else if (iKeyState == KEY_ALT_COMP) {
	    switch (iKeyCode) {
	    case K_LCTRL:
		return ALT_LCTRL;
	    case K_LSHIFT:
		return ALT_LSHIFT;
	    case K_LALT:
		return ALT_ALT;
	    case K_RCTRL:
		return ALT_RCTRL;
	    case K_RSHIFT:
		return ALT_RSHIFT;
	    case K_RALT:
		return ALT_ALT;
	    default:
		return iKeyCode + 12000;
	    }
	}
    }
    else {
	//¨®¨¦¨®¨²¡ä¨®D?D¡ä¡Á???¨®D??¡Àe¡ê?¡ä?¡ä|¨®|??????¡ä|¨¤¨ª?a¦Ì¨¨¨ª?
	if (iKeyState != KEY_NONE && iKeyState < KEY_SCROLLLOCK && (iKeyCode >= 97 && iKeyCode <= 122))
	    iKeyCode -= 32;

	if (iKeyState == KEY_CTRL_COMP)
	    return iKeyCode + 1000;
	if (iKeyState == KEY_SHIFT_COMP) {
	    //??¡ä|¨¤¨ª????
	    if (iKeyCode == 32)
		return iKeyCode + 2000;
	}
	if (iKeyState == KEY_ALT_COMP)
	    return iKeyCode + 3000;
	if (iKeyState == KEY_CTRL_SHIFT_COMP)
	    return iKeyCode + 4000;
	if (iKeyState == KEY_CTRL_ALT_COMP)
	    return iKeyCode + 5000;
	if (iKeyState == KEY_ALT_SHIFT_COMP)
	    return iKeyCode + 6000;
	if (iKeyState == KEY_CTRL_ALT_SHIFT_COMP)
	    return iKeyCode + 7000;
    }

    return iKeyCode;
}

/*
 * ?¨´?Y¡Á?¡ä?¨¤¡ä?D???¨¹
 * ?¡Â¨°a¨®?¨®¨²¡ä¨®¨¦¨¨?????t?D?¨¢¨¨?¨¨¨¨?¨¹¨¦¨¨?¡§
 * ¡¤¦Ì??-1¡À¨ª¨º?¨®??¡ì¨¦¨¨??¦Ì?¨¨¨¨?¨¹2??¡ì3?¡ê?¨°?¡ã?¨º?¨°¨°?a?¡äD¡ä¡ä¨ª?¨®?¨°??¨¨¨¨?¨¹2??¨²¨¢D¡À¨ª?D
 */
int ParseKey (char *strKey)
{
    char           *p;
    int             iKeyCode;
    int             iKeyState = 0;
    int             iCount = 0;

    iKeyCode = GetKeyList (strKey);
    if (iKeyCode != -1)
	return iKeyCode;

    if (!strncmp (strKey, "CTRL_ALT_SHIFT_", 15)) {
	iKeyState = KEY_CTRL_ALT_SHIFT_COMP;
	p = strKey + 15;
	iKeyCode = GetKeyList (p);
	if (iKeyCode != -1)
	    iCount = 0;
	else {
	    iCount = 1;
	    if (!strcmp (p, "SPACE"))
		iKeyCode = ' ';
	    else if (!strcmp (p, "DELETE"))
		iKeyCode = DELETE;
	    else if (strlen (p) == 1)
		iKeyCode = p[0];
	    else
		return -1;
	}
    }
    else if (!strncmp (strKey, "CTRL_ALT_", 9)) {
	iKeyState = KEY_CTRL_ALT_COMP;
	p = strKey + 9;
	iKeyCode = GetKeyList (p);
	if (iKeyCode != -1)
	    iCount = 0;
	else {
	    iCount = 1;
	    if (!strcmp (p, "SPACE"))
		iKeyCode = ' ';
	    else if (!strcmp (p, "DELETE"))
		iKeyCode = DELETE;
	    else if (strlen (p) == 1)
		iKeyCode = p[0];
	    else
		return -1;
	}
    }
    else if (!strncmp (strKey, "CTRL_SHIFT_", 11)) {
	iKeyState = KEY_CTRL_SHIFT_COMP;
	p = strKey + 11;
	iKeyCode = GetKeyList (p);
	if (iKeyCode != -1)
	    iCount = 0;
	else {
	    iCount = 1;
	    if (!strcmp (p, "SPACE"))
		iKeyCode = ' ';
	    else if (!strcmp (p, "DELETE"))
		iKeyCode = DELETE;
	    else if (strlen (p) == 1)
		iKeyCode = p[0];
	    else
		return -1;
	}
    }
    else if (!strncmp (strKey, "ALT_SHIFT_", 10)) {
	iKeyState = KEY_ALT_SHIFT_COMP;
	p = strKey + 10;
	iKeyCode = GetKeyList (p);
	if (iKeyCode != -1)
	    iCount = 0;
	else {
	    iCount = 1;
	    if (!strcmp (p, "SPACE"))
		iKeyCode = ' ';
	    else if (!strcmp (p, "DELETE"))
		iKeyCode = DELETE;
	    else if (strlen (p) == 1)
		iKeyCode = p[0];
	    else
		return -1;
	}
    }
    else if (!strncmp (strKey, "CTRL_", 5)) {
	iKeyState = KEY_CTRL_COMP;
	p = strKey + 5;
	iKeyCode = GetKeyList (p);
	if (iKeyCode != -1)
	    iCount = 0;
	else {
	    iCount = 1;
	    if (!strcmp (p, "SPACE"))
		iKeyCode = ' ';
	    else if (!strcmp (p, "DELETE"))
		iKeyCode = DELETE;
	    else if (strlen (p) == 1)
		iKeyCode = p[0];
	    else
		return -1;
	}
    }
    else if (!strncmp (strKey, "ALT_", 4)) {
	iKeyState = KEY_ALT_COMP;
	p = strKey + 4;
	iKeyCode = GetKeyList (p);
	if (iKeyCode != -1)
	    iCount = 0;
	else {
	    iCount = 1;
	    if (!strcmp (p, "SPACE"))
		iKeyCode = ' ';
	    else if (!strcmp (p, "DELETE"))
		iKeyCode = DELETE;
	    else if (strlen (p) == 1)
		iKeyCode = p[0];
	    else
		return -1;
	}
    }
    else if (!strncmp (strKey, "SHIFT_", 6)) {
	iKeyState = KEY_SHIFT_COMP;
	p = strKey + 6;
	iKeyCode = GetKeyList (p);
	if (iKeyCode != -1)
	    iCount = 0;
	else {
	    iCount = 1;
	    if (!strcmp (p, "SPACE"))
		iKeyCode = ' ';
	    else if (!strcmp (p, "DELETE"))
		iKeyCode = DELETE;
	    else if (strlen (p) == 1)
		iKeyCode = p[0];
	    else
		return -1;
	}
    }
    else {
	if (strlen (strKey) == 1)
	    return strKey[0];
	else
	    return -1;
    }

    return GetKey (iKeyCode, iKeyState, iCount);
}

int GetKeyList (char *strKey)
{
    int             i;

    i = 0;
    for (;;) {
	if (!keyList[i].code)
	    break;
	if (!strcmp (strKey, keyList[i].strKey))
	    return keyList[i].code;
	i++;
    }

    return -1;
}

int GetKeyCodeList (char *strKey)
{
    int             i;

    i = 0;
    for (;;) {
	if (!keyCodeList[i].code)
	    break;
	if (!strcmp (strKey, keyCodeList[i].strKey))
	    return keyCodeList[i].code;
	i++;
    }

    return -1;
}
