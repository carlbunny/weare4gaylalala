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
#include "tools.h"

#include <stdio.h>
#include <stdlib.h>
#include <sys/stat.h>
#include <limits.h>
#include <string.h>
#include <ctype.h>

//#include "ui.h"
//#include "version.h"
//#include "MainWindow.h"
#include "InputWindow.h"
#include "pyfa.h"
#include "py.h"
//#include "sp.h"
#include "ime.h"

//extern Display *dpy;
//extern int      iScreen;
//extern int      MAINWND_WIDTH;
//extern int      iMainWindowX;
//extern int      iMainWindowY;
//extern int      iInputWindowX;
//extern int      iInputWindowY;
//extern int      iTempInputWindowX;
//extern int      iTempInputWindowY;
//extern int      iInputWindowWidth;
//extern int      iInputWindowHeight;
//
//extern char     strUserLocale[];
//extern int      iMaxCandWord;
//extern Bool     _3DEffectMainWindow;
//extern _3D_EFFECT _3DEffectInputWindow;
//extern WINDOW_COLOR inputWindowColor;
//extern WINDOW_COLOR mainWindowColor;
//extern MESSAGE_COLOR IMNameColor[];
//extern MESSAGE_COLOR messageColor[];
//extern MESSAGE_COLOR inputWindowLineColor;
//extern MESSAGE_COLOR mainWindowLineColor;
//extern MESSAGE_COLOR cursorColor;
//extern WINDOW_COLOR VKWindowColor;
//extern MESSAGE_COLOR VKWindowFontColor;
//extern MESSAGE_COLOR VKWindowAlphaColor;
//extern ENTER_TO_DO enterToDo;
//
//extern HOTKEYS  hkTrigger[];
//extern HOTKEYS  hkGBK[];
//extern HOTKEYS  hkCorner[];
//extern HOTKEYS  hkPunc[];
//extern HOTKEYS  hkPrevPage[];
//extern HOTKEYS  hkNextPage[];
//extern HOTKEYS  hkWBAddPhrase[];
//extern HOTKEYS  hkWBDelPhrase[];
//extern HOTKEYS  hkWBAdjustOrder[];
//extern HOTKEYS  hkPYAddFreq[];
//extern HOTKEYS  hkPYDelFreq[];
//extern HOTKEYS  hkPYDelUserPhr[];
//extern HOTKEYS  hkLegend[];
//extern HOTKEYS  hkTrack[];
//extern HOTKEYS  hkGetPY[];
//extern HOTKEYS  hkGBT[];
//
//extern KEY_CODE switchKey;
//extern XIMTriggerKey *Trigger_Keys;
//extern INT8     iTriggerKeyCount;
//
extern Bool     bUseGBK;
//extern Bool     bEngPuncAfterNumber;
//extern Bool     bAutoHideInputWindow;
//extern XColor   colorArrow;
//extern Bool     bTrackCursor;
//extern Bool     bCenterInputWindow;
//extern HIDE_MAINWINDOW hideMainWindow;
//extern Bool     bCompactMainWindow;
//extern HIDE_MAINWINDOW hideMainWindow;
//extern int      iFontSize;
//extern int      iMainWindowFontSize;
//
//extern Bool     bUseGBKT;
//
//extern Bool     bChnPunc;
//extern Bool     bCorner;
//extern Bool     bUseLegend;
//
//extern Bool     bPYCreateAuto;
//extern Bool     bPYSaveAutoAsPhrase;
//extern Bool     bPhraseTips;
//extern SEMICOLON_TO_DO semicolonToDo;
//extern Bool     bEngAfterCap;
//
////??��?�䨰��??��?��
//extern Bool     bShowUserSpeed;
//extern Bool     bShowVersion;
//extern Bool     bShowVK;
//
//extern char     strNameOfPinyin[];
//extern char     strNameOfShuangpin[];;
//extern char     strNameOfQuwei[];
//
//extern Bool     bFullPY;
//extern Bool     bDisablePagingInLegend;
//
//extern int      i2ndSelectKey;
//extern int      i3rdSelectKey;
//
//extern char     strFontName[];
//extern char     strFontEnName[];
//
//extern ADJUSTORDER baseOrder;
//extern ADJUSTORDER phraseOrder;
//extern ADJUSTORDER freqOrder;
//
//extern INT8     iIMIndex;
//extern Bool     bLocked;
//
//extern MHPY     MHPY_C[];
//extern MHPY     MHPY_S[];
//
//extern Bool     bUsePinyin;
//extern Bool     bUseSP;
//extern Bool     bUseQW;
//extern Bool     bUseTable;
//
//extern char     strDefaultSP[];
//extern SP_FROM  iSPFrom;
//
////extern Bool     bLumaQQ;
//extern char     cPYYCDZ[];
//
//extern Bool     bDoubleSwitchKey;
//extern Bool     bPointAfterNumber;
//extern Bool     bConvertPunc;
//extern unsigned int iTimeInterval;
//extern uint     iFixedInputWindowWidth;
//extern Bool     bShowInputWindowTriggering;
//
//#ifdef _USE_XFT
//extern Bool     bUseAA;
//#else
//extern char     strUserLocale[];
//#endif
//
//extern INT8     iOffsetX;
//extern INT8     iOffsetY;
//
///*
//#if defined(DARWIN)*/
///* function to reverse byte order for integer
//this is required for Mac machine*/
///*int ReverseInt (unsigned int pc_int)
//{
//    int             mac_int;
//    unsigned char  *p;
//
//    mac_int = pc_int;
//    p = (unsigned char *) &pc_int;
//    mac_int = (p[3] << 24) + (p[2] << 16) + (p[1] << 8) + p[0];
//    return mac_int;
//}
//#endif
//*/
//
//Bool MyStrcmp (char *str1, char *str2)
//{
//    return !strncmp (str1, str2, strlen (str2));
//}
//
///*
// * ?����?��??���?����?????t
// */
//void LoadConfig (Bool bMode)
//{
//    FILE           *fp;
//    char            str[PATH_MAX], *pstr;
//    char            strPath[PATH_MAX];
//    int             i;
//    int             r, g, b;	//�䨲�(�o��?����?
//    Bool            bFromUser = True;
//
//    strcpy (strPath, (char *) getenv ("HOME"));
//    strcat (strPath, "/.fcitx/config");
//
//    if (access (strPath, 0)) {
//	strcpy (strPath, PKGDATADIR "/data/config");
//	bFromUser = False;
//    }
//
//    fp = fopen (strPath, "rt");
//
//    if (!fp || !bFromUser)
//	SaveConfig ();
//
//    if (!fp) {
//	LoadConfig (True);	//?����???��??��
//	return;
//    }
//
//    for (;;) {
//	if (!fgets (str, PATH_MAX, fp))
//	    break;
//
//	i = strlen (str) - 1;
//	while (str[i] == ' ' || str[i] == '\n')
//	    str[i--] = '\0';
//
//	pstr = str;
//	if (*pstr == ' ')
//	    pstr++;
//	if (pstr[0] == '#')
//	    continue;
//
//	if (MyStrcmp (pstr, "��?��???����=")) {
//	    pstr += 9;
//	    if (*pstr == ' ')
//		pstr++;
//	    strcpy (strUserLocale, pstr);
//	}
//	else if (MyStrcmp (pstr, "??��?��?��?(?D)=")) {
//	    pstr += 13;
//	    strcpy (strFontName, pstr);
//	}
//	else if (MyStrcmp (pstr, "??��?��?��?(����)=")) {
//	    pstr += 13;
//	    strcpy (strFontEnName, pstr);
//	}
//	else if (MyStrcmp (pstr, "??��?��?��?�䨮D?=")) {
//	    pstr += 13;
//	    iFontSize = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "?�¡��?����?��?�䨮D?=")) {
//	    pstr += 15;
//	    iMainWindowFontSize = atoi (pstr);
//	}
//#ifdef _USE_XFT
//	else if (MyStrcmp (pstr, "��?��?��1��?AA��?��?=")) {
//	    pstr += 15;
//	    bUseAA = atoi (pstr);
//	}
//#else
//	else if (MyStrcmp (pstr, "��?��???����=")) {
//	    pstr += 9;
//	    strcpy (strUserLocale, pstr);
//	}
//#endif
//	else if (MyStrcmp (pstr, "o��??�䨺??��y=")) {
//	    pstr += 11;
//	    iMaxCandWord = atoi (pstr);
//	    if (iMaxCandWord > 10)
//		iMaxCandWord = MAX_CAND_WORD;
//	}
//	else if (MyStrcmp (pstr, "��y��?o��?����???��?o?=")) {
//	    pstr += 17;
//	    bEngPuncAfterNumber = atoi (pstr);
//	}
//	/*else if (MyStrcmp (pstr, "LumaQQ?��3?=")) {
//	   pstr += 11;
//	   bLumaQQ = atoi (pstr);
//	   } */
//	else if (MyStrcmp (pstr, "?�¡��?����?��?��1��?3D????=")) {
//	    pstr += 21;
//	    _3DEffectMainWindow = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��?��?��?��1��?3D????=")) {
//	    pstr += 17;
//	    _3DEffectInputWindow = (_3D_EFFECT) atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��?��?��??����t2?��?��?��?=")) {
//	    pstr += 19;
//	    bAutoHideInputWindow = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��?��?��?��?��??��?D=")) {
//	    pstr += 15;
//	    bCenterInputWindow = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��?��?��?1��?��?��?��=")) {
//	    pstr += 15;
//	    iFixedInputWindowWidth = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "���a�???��?��?��?��?=")) {
//	    pstr += 15;
//	    bShowInputWindowTriggering = atoi (pstr);
//	}
//	// ��������?a??��?��?
//	else if (MyStrcmp (pstr, "��?��?��???��?��?X=")) {
//	    pstr += 14;
//	    iOffsetX = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��?��?��???��?��?Y=")) {
//	    pstr += 14;
//	    iOffsetY = atoi (pstr);
//	}
//	//********************************************
//	else if (MyStrcmp (pstr, "D��o?o��?����?=")) {
//	    pstr += 11;
//	    bPointAfterNumber = atoi (pstr);
//	}
//	//??��??��?��
//	else if (MyStrcmp (pstr, "??��?�䨰��??��?��=")) {
//	    pstr += 13;
//	    bShowUserSpeed = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "??��?��?��?=")) {
//	    pstr += 13;
//	    bShowVersion = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "?�¡��?����t2??�꨺?=")) {
//	    pstr += 15;
//	    hideMainWindow = (HIDE_MAINWINDOW) atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "??��?D��?a?��?��=")) {
//	    pstr += 13;
//	    bShowVK = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "1a�(���?=") && bMode) {
//	    pstr += 7;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    cursorColor.color.red = (r << 8);
//	    cursorColor.color.green = (g << 8);
//	    cursorColor.color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "?�¡��?����3?�㨦?=") && bMode) {
//	    pstr += 13;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    mainWindowColor.backColor.red = (r << 8);
//	    mainWindowColor.backColor.green = (g << 8);
//	    mainWindowColor.backColor.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "?�¡��?��??��?��?=") && bMode) {
//	    pstr += 13;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    mainWindowLineColor.color.red = (r << 8);
//	    mainWindowLineColor.color.green = (g << 8);
//	    mainWindowLineColor.color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "?�¡��?����?��?����??3?��?=") && bMode) {
//	    int             r1, r2, g1, g2, b1, b2;
//
//	    pstr += 19;
//	    sscanf (pstr, "%d %d %d %d %d %d %d %d %d", &r, &g, &b, &r1, &g1, &b1, &r2, &g2, &b2);
//	    IMNameColor[0].color.red = (r << 8);
//	    IMNameColor[0].color.green = (g << 8);
//	    IMNameColor[0].color.blue = (b << 8);
//	    IMNameColor[1].color.red = (r1 << 8);
//	    IMNameColor[1].color.green = (g1 << 8);
//	    IMNameColor[1].color.blue = (b1 << 8);
//	    IMNameColor[2].color.red = (r2 << 8);
//	    IMNameColor[2].color.green = (g2 << 8);
//	    IMNameColor[2].color.blue = (b2 << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?����3?�㨦?=") && bMode) {
//	    pstr += 13;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    inputWindowColor.backColor.red = (r << 8);
//	    inputWindowColor.backColor.green = (g << 8);
//	    inputWindowColor.backColor.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?��㨬����?��?=") && bMode) {
//	    pstr += 13;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    messageColor[0].color.red = (r << 8);
//	    messageColor[0].color.green = (g << 8);
//	    messageColor[0].color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?��㨮??�쨺?��?��?=") && bMode) {
//	    pstr += 17;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    messageColor[1].color.red = (r << 8);
//	    messageColor[1].color.green = (g << 8);
//	    messageColor[1].color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?���D��o?��?=") && bMode) {
//	    pstr += 13;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    messageColor[2].color.red = (r << 8);
//	    messageColor[2].color.green = (g << 8);
//	    messageColor[2].color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?���̨���???o��??��?��?=") && bMode) {
//	    pstr += 21;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    messageColor[3].color.red = (r << 8);
//	    messageColor[3].color.green = (g << 8);
//	    messageColor[3].color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?��㨮??��䨺�h���?=") && bMode) {
//	    pstr += 17;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    messageColor[4].color.red = (r << 8);
//	    messageColor[4].color.green = (g << 8);
//	    messageColor[4].color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?��㨬����?�(�??��?=") && bMode) {
//	    pstr += 17;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    messageColor[5].color.red = (r << 8);
//	    messageColor[5].color.green = (g << 8);
//	    messageColor[5].color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?���???��??��?��?=") && bMode) {
//	    pstr += 17;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    messageColor[6].color.red = (r << 8);
//	    messageColor[6].color.green = (g << 8);
//	    messageColor[6].color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?���??��?��?=") && bMode) {
//	    pstr += 13;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    inputWindowLineColor.color.red = (r << 8);
//	    inputWindowLineColor.color.green = (g << 8);
//	    inputWindowLineColor.color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "��?��?���?y������?=") && bMode) {
//	    pstr += 13;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    colorArrow.red = (r << 8);
//	    colorArrow.green = (g << 8);
//	    colorArrow.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "D��?a?��?������3?�㨦?=") && bMode) {
//	    pstr += 17;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    VKWindowColor.backColor.red = (r << 8);
//	    VKWindowColor.backColor.green = (g << 8);
//	    VKWindowColor.backColor.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "D��?a?��?������???��?=") && bMode) {
//	    pstr += 17;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    VKWindowAlphaColor.color.red = (r << 8);
//	    VKWindowAlphaColor.color.green = (g << 8);
//	    VKWindowAlphaColor.color.blue = (b << 8);
//	}
//	else if (MyStrcmp (pstr, "D��?a?��?����㡤?o?��?=") && bMode) {
//	    pstr += 17;
//	    sscanf (pstr, "%d %d %d\n", &r, &g, &b);
//	    VKWindowFontColor.color.red = (r << 8);
//	    VKWindowFontColor.color.green = (g << 8);
//	    VKWindowFontColor.color.blue = (b << 8);
//	}
//
//	else if (MyStrcmp (pstr, "�䨰?a/1?��?��?��?����=") && bMode) {
//	    pstr += 16;
//	    SetTriggerKeys (pstr);
//	    SetHotKey (pstr, hkTrigger);
//	}
//	else if (MyStrcmp (pstr, "?D����???��?��?D???��=")) {
//	    pstr += 17;
//	    SetSwitchKey (pstr);
//	}
//	else if (MyStrcmp (pstr, "GBK���(�??D???��=")) {
//	    pstr += 14;
//	    SetHotKey (pstr, hkGBT);
//	}
//	else if (MyStrcmp (pstr, "???��?D����???D??=")) {
//	    pstr += 15;
//	    bDoubleSwitchKey = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "?��?������??????=")) {
//	    pstr += 13;
//	    iTimeInterval = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "1a�(�?��??=")) {
//	    pstr += 9;
//	    SetHotKey (pstr, hkTrack);
//	}
//	else if (MyStrcmp (pstr, "GBK?��3?=")) {
//	    pstr += 8;
//	    SetHotKey (pstr, hkGBK);
//	}
//	else if (MyStrcmp (pstr, "��?o??��DD?a=")) {
//	    pstr += 11;
//	    semicolonToDo = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "�䨮D���???��?��?����??=")) {
//	    pstr += 17;
//	    bEngAfterCap = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��a??����???D��?�(���?=")) {
//	    pstr += 17;
//	    bConvertPunc = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��a??��?��????1��-��3=")) {
//	    pstr += 17;
//	    bDisablePagingInLegend = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��a???��3?=")) {
//	    pstr += 9;
//	    SetHotKey (pstr, hkLegend);
//	}
//	else if (MyStrcmp (pstr, "����2��?�䨰?=")) {
//	    pstr += 9;
//	    SetHotKey (pstr, hkGetPY);
//	}
//	else if (MyStrcmp (pstr, "Enter?��DD?a=")) {
//	    pstr += 12;
//	    enterToDo = (ENTER_TO_DO) atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "��?��???=")) {
//	    pstr += 7;
//	    SetHotKey (pstr, hkCorner);
//	}
//	else if (MyStrcmp (pstr, "?D??�(���?=")) {
//	    pstr += 9;
//	    SetHotKey (pstr, hkPunc);
//	}
//	else if (MyStrcmp (pstr, "��?��?��3=")) {
//	    pstr += 7;
//	    SetHotKey (pstr, hkPrevPage);
//	}
//	else if (MyStrcmp (pstr, "??��?��3=")) {
//	    pstr += 7;
//	    SetHotKey (pstr, hkNextPage);
//	}
//	else if (MyStrcmp (pstr, "�̨�?t��yo��??�䨺?????��=")) {
//	    pstr += 19;
//	    if (!strcasecmp (pstr, "SHIFT")) {
//		i2ndSelectKey = 50;	//�h�SHIFT��?����?��??
//		i3rdSelectKey = 62;	//����SHIFT��?����?��??
//	    }
//	    else if (!strcasecmp (pstr, "CTRL")) {
//		i2ndSelectKey = 37;	//�h�CTRL��?����?��??
//		i3rdSelectKey = 109;	//����CTRL��?����?��??
//	    }
//	}
//
//	else if (MyStrcmp (pstr, "��1��??�䨰?=")) {
//	    pstr += 9;
//	    bUsePinyin = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "?�䨰???3?=")) {
//	    pstr += 9;
//	    strcpy (strNameOfPinyin, pstr);
//	}
//
//	else if (MyStrcmp (pstr, "��1��????��=")) {
//	    pstr += 9;
//	    bUseSP = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "???��??3?=")) {
//	    pstr += 9;
//	    strcpy (strNameOfShuangpin, pstr);
//	}
//	else if (MyStrcmp (pstr, "??��????�䡤?��?=")) {
//	    pstr += 13;
//	    if (*pstr == ' ')
//		pstr++;
//	    strcpy (strDefaultSP, pstr);
//	    iSPFrom = SP_FROM_SYSTEM_CONFIG;
//	}
//	else if (MyStrcmp (pstr, "��1��?????=")) {
//	    pstr += 9;
//	    bUseQW = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "??????3?=")) {
//	    pstr += 9;
//	    strcpy (strNameOfQuwei, pstr);
//	}
//	else if (MyStrcmp (pstr, "��1��???�(�=")) {
//	    pstr += 9;
//	    bUseTable = atoi (pstr);
//	}
//	else if (MyStrcmp (pstr, "������?�䨺?a?D��?�䨺�h�=")) {
//	    pstr += 17;
//	    bPhraseTips = atoi (pstr);
//	}
//
//	else if (MyStrcmp (str, "��1��?��??��=")) {
//	    pstr += 9;
//	    bFullPY = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "?�䨰?��??���h��䨺=")) {
//	    pstr += 13;
//	    bPYCreateAuto = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "�!��?��??���h��䨺=")) {
//	    pstr += 13;
//	    bPYSaveAutoAsPhrase = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "???��?�䨰?3�ꨮ?��?=")) {
//	    pstr += 15;
//	    SetHotKey (pstr, hkPYAddFreq);
//	}
//	else if (MyStrcmp (str, "��?3y?�䨰?3�ꨮ?��?=")) {
//	    pstr += 15;
//	    SetHotKey (pstr, hkPYDelFreq);
//	}
//	else if (MyStrcmp (str, "��?3y?�䨰?��??��䨺�h�=")) {
//	    pstr += 17;
//	    SetHotKey (pstr, hkPYDelUserPhr);
//	}
//	else if (MyStrcmp (str, "?�䨰?��?�䨺?����??��=")) {
//	    pstr += 15;
//	    cPYYCDZ[0] = pstr[0];
//	    cPYYCDZ[1] = pstr[1];
//	}
//	else if (MyStrcmp (str, "?�䨰?�̣���?????�̡�??��?��?=")) {
//	    pstr += 21;
//	    baseOrder = (ADJUSTORDER) atoi (pstr);
//	}
//	else if (MyStrcmp (str, "?�䨰?�䨺�h�????�̡�??��?��?=")) {
//	    pstr += 21;
//	    phraseOrder = (ADJUSTORDER) atoi (pstr);
//	}
//	else if (MyStrcmp (str, "?�䨰?3�ꨮ?�䨺????�̡�??��?��?=")) {
//	    pstr += 23;
//	    freqOrder = (ADJUSTORDER) atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyano��ang=")) {
//	    pstr += 16;
//	    MHPY_C[0].bMode = atoi (pstr);
//	    MHPY_S[5].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyeno��eng=")) {
//	    pstr += 16;
//	    MHPY_C[1].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyiano��iang=")) {
//	    pstr += 18;
//	    MHPY_C[2].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyino��ing=")) {
//	    pstr += 16;
//	    MHPY_C[3].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyouo��u=")) {
//	    pstr += 14;
//	    MHPY_C[4].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyuano��uang=")) {
//	    pstr += 18;
//	    MHPY_C[5].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyco��ch=")) {
//	    pstr += 14;
//	    MHPY_S[0].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyfo��h=")) {
//	    pstr += 13;
//	    MHPY_S[1].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oylo��n=")) {
//	    pstr += 13;
//	    MHPY_S[2].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyso��sh=")) {
//	    pstr += 14;
//	    MHPY_S[3].bMode = atoi (pstr);
//	}
//	else if (MyStrcmp (str, "��?��??��oyzo��zh=")) {
//	    pstr += 14;
//	    MHPY_S[4].bMode = atoi (pstr);
//	}
//    }
//
//    fclose (fp);
//
//    if (!Trigger_Keys) {
//	iTriggerKeyCount = 0;
//	Trigger_Keys = (XIMTriggerKey *) malloc (sizeof (XIMTriggerKey) * (iTriggerKeyCount + 2));
//	Trigger_Keys[0].keysym = XK_space;
//	Trigger_Keys[0].modifier = ControlMask;
//	Trigger_Keys[0].modifier_mask = ControlMask;
//	Trigger_Keys[1].keysym = 0;
//	Trigger_Keys[1].modifier = 0;
//	Trigger_Keys[1].modifier_mask = 0;
//    }
//}
//
//void SaveConfig (void)
//{
//    FILE           *fp;
//    char            strPath[PATH_MAX];
//
//    strcpy (strPath, (char *) getenv ("HOME"));
//    strcat (strPath, "/.fcitx/");
//
//    if (access (strPath, 0))
//	mkdir (strPath, S_IRWXU);
//
//    strcat (strPath, "config");
//    fp = fopen (strPath, "wt");
//    if (!fp) {
//	fprintf (stderr, "\n?T�������?��???t config��?\n");
//	return;
//    }
//
//    fprintf (fp, "[3��D��]\n");
//    fprintf (fp, "??��?��?��?(?D)=%s\n", strFontName);
//    fprintf (fp, "??��?��?��?(����)=%s\n", strFontEnName);
//    fprintf (fp, "??��?��?��?�䨮D?=%d\n", iFontSize);
//    fprintf (fp, "?�¡��?����?��?�䨮D?=%d\n", iMainWindowFontSize);
//    if (strUserLocale[0])
//	fprintf (fp, "��?��???����=%s\n", strUserLocale);
//    else
//	fprintf (fp, "#��?��???����=zh_CN.gb18030\n");
//#ifdef _USE_XFT
//    fprintf (fp, "��?��?��1��?AA��?��?=%d\n", bUseAA);
//#endif
//
//    fprintf (fp, "\n[��?3?]\n");
//    fprintf (fp, "��y��?o��?����???��?o?=%d\n", bEngPuncAfterNumber);
//    fprintf (fp, "Enter?��DD?a=%d\n", enterToDo);
//    fprintf (fp, "��?o??��DD?a=%d\n", (int) semicolonToDo);
//    fprintf (fp, "�䨮D���???��?��?����??=%d\n", bEngAfterCap);
//    fprintf (fp, "��a??����???D��?�(���?=%d\n", bConvertPunc);
//    fprintf (fp, "��a??��?��????1��-��3=%d\n", bDisablePagingInLegend);
//    //fprintf (fp, "LumaQQ?��3?=%d\n", bLumaQQ);
//
//    fprintf (fp, "\n[????]\n");
//    fprintf (fp, "o��??�䨺??��y=%d\n", iMaxCandWord);
//    fprintf (fp, "?�¡��?����?��?��1��?3D????=%d\n", _3DEffectMainWindow);
//    fprintf (fp, "��?��?��?��1��?3D????=%d\n", _3DEffectInputWindow);
//    fprintf (fp, "?�¡��?����t2??�꨺?=%d\n", (int) hideMainWindow);
//    fprintf (fp, "??��?D��?a?��?��=%d\n", (int) bShowVK);
//    fprintf (fp, "��?��?��??����t2?��?��?��?=%d\n", bAutoHideInputWindow);
//    fprintf (fp, "��?��?��?��?��??��?D=%d\n", bCenterInputWindow);
//    fprintf (fp, "���a�???��?��?��?��?=%d\n", bShowInputWindowTriggering);
//    fprintf (fp, "#��?��?��?1��?��?��?��(??������?����??�(���?��?����)��?0�(���?2?1��?��?��?��\n");
//    fprintf (fp, "��?��?��?1��?��?��?��=%d\n", iFixedInputWindowWidth);
//    fprintf (fp, "��?��?��???��?��?X=%d\n", iOffsetX);
//    fprintf (fp, "��?��?��???��?��?Y=%d\n", iOffsetY);
//    fprintf (fp, "D��o?o��?����?=%d\n", bPointAfterNumber);
//    fprintf (fp, "??��?�䨰��??��?��=%d\n", bShowUserSpeed);
//    fprintf (fp, "??��?��?��?=%d\n", bShowVersion);
//
//    fprintf (fp, "1a�(���?=%d %d %d\n", cursorColor.color.red >> 8, cursorColor.color.green >> 8, cursorColor.color.blue >> 8);
//    fprintf (fp, "?�¡��?����3?�㨦?=%d %d %d\n", mainWindowColor.backColor.red >> 8, mainWindowColor.backColor.green >> 8, mainWindowColor.backColor.blue >> 8);
//    fprintf (fp, "?�¡��?��??��?��?=%d %d %d\n", mainWindowLineColor.color.red >> 8, mainWindowLineColor.color.green >> 8, mainWindowLineColor.color.blue >> 8);
//    fprintf (fp, "?�¡��?����?��?����??3?��?=%d %d %d %d %d %d %d %d %d\n", IMNameColor[0].color.red >> 8, IMNameColor[0].color.green >> 8, IMNameColor[0].color.blue >> 8, IMNameColor[1].color.red >> 8, IMNameColor[1].color.green >> 8,
//	     IMNameColor[1].color.blue >> 8, IMNameColor[2].color.red >> 8, IMNameColor[2].color.green >> 8, IMNameColor[2].color.blue >> 8);
//    fprintf (fp, "��?��?����3?�㨦?=%d %d %d\n", inputWindowColor.backColor.red >> 8, inputWindowColor.backColor.green >> 8, inputWindowColor.backColor.blue >> 8);
//    fprintf (fp, "��?��?��㨬����?��?=%d %d %d\n", messageColor[0].color.red >> 8, messageColor[0].color.green >> 8, messageColor[0].color.blue >> 8);
//    fprintf (fp, "��?��?��㨮??�쨺?��?��?=%d %d %d\n", messageColor[1].color.red >> 8, messageColor[1].color.green >> 8, messageColor[1].color.blue >> 8);
//    fprintf (fp, "��?��?���D��o?��?=%d %d %d\n", messageColor[2].color.red >> 8, messageColor[2].color.green >> 8, messageColor[2].color.blue >> 8);
//    fprintf (fp, "��?��?���̨���???o��??��?��?=%d %d %d\n", messageColor[3].color.red >> 8, messageColor[3].color.green >> 8, messageColor[3].color.blue >> 8);
//    fprintf (fp, "#????��??��??��?����?�䨰??D��?��??���??���䨺\n");
//    fprintf (fp, "��?��?��㨮??��䨺�h���?=%d %d %d\n", messageColor[4].color.red >> 8, messageColor[4].color.green >> 8, messageColor[4].color.blue >> 8);
//    fprintf (fp, "��?��?��㨬����?�(�??��?=%d %d %d\n", messageColor[5].color.red >> 8, messageColor[5].color.green >> 8, messageColor[5].color.blue >> 8);
//    fprintf (fp, "#??�(�?��?�䨰?��?�̣���?/?�̨�3�䨺�h�?����1��?????��?\n");
//    fprintf (fp, "��?��?���???��??��?��?=%d %d %d\n", messageColor[6].color.red >> 8, messageColor[6].color.green >> 8, messageColor[6].color.blue >> 8);
//    fprintf (fp, "��?��?���??��?��?=%d %d %d\n", inputWindowLineColor.color.red >> 8, inputWindowLineColor.color.green >> 8, inputWindowLineColor.color.blue >> 8);
//    fprintf (fp, "��?��?���?y������?=%d %d %d\n", colorArrow.red >> 8, colorArrow.green >> 8, colorArrow.blue >> 8);
//    fprintf (fp, "D��?a?��?������3?�㨦?=%d %d %d\n", VKWindowColor.backColor.red >> 8, VKWindowColor.backColor.green >> 8, VKWindowColor.backColor.blue >> 8);
//    fprintf (fp, "D��?a?��?������???��?=%d %d %d\n", VKWindowAlphaColor.color.red >> 8, VKWindowAlphaColor.color.green >> 8, VKWindowAlphaColor.color.blue >> 8);
//    fprintf (fp, "D��?a?��?����㡤?o?��?=%d %d %d\n", VKWindowFontColor.color.red >> 8, VKWindowFontColor.color.green >> 8, VKWindowFontColor.color.blue >> 8);
//
//    fprintf (fp, "\n#3y��??��?D����???��?��?D???��?�(�a��????����?����?��?��?������???a��???��??D??��?????��???\n");
//    fprintf (fp, "[����?��]\n");
//    fprintf (fp, "�䨰?a/1?��?��?��?����=CTRL_SPACE\n");
//    fprintf (fp, "#?D����???��?��?D???�� ?����?����???aL_CTRL R_CTRL L_SHIFT R_SHIFT\n");
//    fprintf (fp, "?D����???��?��?D???��=L_CTRL\n");
//    fprintf (fp, "???��?D����???D??=%d\n", bDoubleSwitchKey);
//    fprintf (fp, "?��?������??????=%u\n", iTimeInterval);
//    fprintf (fp, "1a�(�?��??=CTRL_K\n");
//    fprintf (fp, "GBK?��3?=CTRL_M\n");
//    fprintf (fp, "GBK���(�??D???��=CTRL_ALT_F\n");
//    fprintf (fp, "��a???��3?=CTRL_L\n");
//    fprintf (fp, "����2��?�䨰?=CTRL_ALT_E\n");
//    fprintf (fp, "��?��???=SHIFT_SPACE\n");
//    fprintf (fp, "?D??�(���?=ALT_SPACE\n");
//    fprintf (fp, "��?��?��3=- ,\n");
//    fprintf (fp, "??��?��3== .\n");
//    fprintf (fp, "�̨�?t��yo��??�䨺?????��=SHIFT\n");
//
//    fprintf (fp, "\n[��?��?����]\n");
//    fprintf (fp, "��1��??�䨰?=%d\n", bUsePinyin);
//    fprintf (fp, "��1��????��=%d\n", bUseSP);
//    fprintf (fp, "??��????�䡤?��?=%s\n", strDefaultSP);
//    fprintf (fp, "��1��?????=%d\n", bUseQW);
//    fprintf (fp, "��1��???�(�=%d\n", bUseTable);
//    fprintf (fp, "������?�䨺?a?D��?�䨺�h�=%d\n", bPhraseTips);
//
//    fprintf (fp, "\n[?�䨰?]\n");
//    fprintf (fp, "��1��?��??��=%d\n", bFullPY);
//    fprintf (fp, "?�䨰?��??���h��䨺=%d\n", bPYCreateAuto);
//    fprintf (fp, "�!��?��??���h��䨺=%d\n", bPYSaveAutoAsPhrase);
//    fprintf (fp, "???��?�䨰?3�ꨮ?��?=CTRL_8\n");
//    fprintf (fp, "��?3y?�䨰?3�ꨮ?��?=CTRL_7\n");
//    fprintf (fp, "��?3y?�䨰?��??��䨺�h�=CTRL_DELETE\n");
//    fprintf (fp, "#?�䨰?��?�䨺?����??����?�̨�o?o��?????��?����?2?��a��D????\n");
//    fprintf (fp, "?�䨰?��?�䨺?����??��=%c%c\n", cPYYCDZ[0], cPYYCDZ[1]);
//    fprintf (fp, "#????�̡�??��?��??��?�¡�o0-->2?�̡�??  1-->?��?���̡�??  2-->���?��?���̡�??\n");
//    fprintf (fp, "?�䨰?�̣���?????�̡�??��?��?=%d\n", baseOrder);
//    fprintf (fp, "?�䨰?�䨺�h�????�̡�??��?��?=%d\n", phraseOrder);
//    fprintf (fp, "?�䨰?3�ꨮ?�䨺????�̡�??��?��?=%d\n", freqOrder);
//    fprintf (fp, "��?��??��oyano��ang=%d\n", MHPY_C[0].bMode);
//    fprintf (fp, "��?��??��oyeno��eng=%d\n", MHPY_C[1].bMode);
//    fprintf (fp, "��?��??��oyiano��iang=%d\n", MHPY_C[2].bMode);
//    fprintf (fp, "��?��??��oyino��ing=%d\n", MHPY_C[3].bMode);
//    fprintf (fp, "��?��??��oyouo��u=%d\n", MHPY_C[4].bMode);
//    fprintf (fp, "��?��??��oyuano��uang=%d\n", MHPY_C[5].bMode);
//    fprintf (fp, "��?��??��oyco��ch=%d\n", MHPY_S[0].bMode);
//    fprintf (fp, "��?��??��oyfo��h=%d\n", MHPY_S[1].bMode);
//    fprintf (fp, "��?��??��oylo��n=%d\n", MHPY_S[2].bMode);
//    fprintf (fp, "��?��??��oyso��sh=%d\n", MHPY_S[3].bMode);
//    fprintf (fp, "��?��??��oyzo��zh=%d\n", MHPY_S[4].bMode);
//
//    fclose (fp);
//}
//
//void LoadProfile (void)
//{
//    FILE           *fp;
//    char            str[PATH_MAX], *pstr;
//    char            strPath[PATH_MAX];
//    int             i;
//    Bool            bRetVal;
//
//    iMainWindowX = MAINWND_STARTX;
//    iMainWindowY = MAINWND_STARTY;
//    iInputWindowX = INPUTWND_STARTX;
//    iInputWindowY = INPUTWND_STARTY;
//
//    bRetVal = False;
//    strcpy (strPath, (char *) getenv ("HOME"));
//    strcat (strPath, "/.fcitx/profile");
//
//    fp = fopen (strPath, "rt");
//
//    if (fp) {
//	for (;;) {
//	    if (!fgets (str, PATH_MAX, fp))
//		break;
//
//	    i = strlen (str) - 1;
//	    while (str[i] == ' ' || str[i] == '\n')
//		str[i--] = '\0';
//
//	    pstr = str;
//
//	    if (MyStrcmp (str, "��?��?=")) {
//		pstr += 5;
//
//		if (!strcasecmp (FCITX_VERSION, pstr))
//		    bRetVal = True;
//	    }
//	    else if (MyStrcmp (str, "?�¡��?��????X=")) {
//		pstr += 12;
//		iMainWindowX = atoi (pstr);
//		if (iMainWindowX < 0)
//		    iMainWindowX = 0;
//		else if ((iMainWindowX + MAINWND_WIDTH) > DisplayWidth (dpy, iScreen))
//		    iMainWindowX = DisplayWidth (dpy, iScreen) - MAINWND_WIDTH;
//	    }
//	    else if (MyStrcmp (str, "?�¡��?��????Y=")) {
//		pstr += 12;
//		iMainWindowY = atoi (pstr);
//		if (iMainWindowY < 0)
//		    iMainWindowY = 0;
//		else if ((iMainWindowY + MAINWND_HEIGHT) > DisplayHeight (dpy, iScreen))
//		    iMainWindowY = DisplayHeight (dpy, iScreen) - MAINWND_HEIGHT;
//	    }
//	    else if (MyStrcmp (str, "��?��?���?��????X=")) {
//		pstr += 14;
//		iInputWindowX = atoi (pstr);
//		if (iInputWindowX < 0)
//		    iInputWindowX = 0;
//		else if ((iInputWindowX + iInputWindowWidth) > DisplayWidth (dpy, iScreen))
//		    iInputWindowX = DisplayWidth (dpy, iScreen) - iInputWindowWidth - 3;
//	    }
//	    else if (MyStrcmp (str, "��?��?���?��????Y=")) {
//		pstr += 14;
//		iInputWindowY = atoi (pstr);
//		if (iInputWindowY < 0)
//		    iInputWindowY = 0;
//		else if ((iInputWindowY + INPUTWND_HEIGHT) > DisplayHeight (dpy, iScreen))
//		    iInputWindowY = DisplayHeight (dpy, iScreen) - iInputWindowHeight;
//	    }
//	    else if (MyStrcmp (str, "��?��?��???=")) {
//		pstr += 9;
//		bCorner = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "��?��??D??�(���?=")) {
//		pstr += 13;
//		bChnPunc = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "��?��?GBK=")) {
//		pstr += 8;
//		bUseGBK = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "��?��?1a�(�?��??=")) {
//		pstr += 13;
//		bTrackCursor = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "��?��?��a??=")) {
//		pstr += 9;
//		bUseLegend = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "�̡�?�㨺?��?����=")) {
//		pstr += 11;
//		iIMIndex = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "???1��??��?��?D??=")) {
//		pstr += 15;
//		bLocked = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "?��?��?�꨺?=")) {
//		pstr += 9;
//		bCompactMainWindow = atoi (pstr);
//	    }
//	    else if (MyStrcmp (str, "��?��?��?3?GBK���(�?=")) {
//		pstr += 16;
//		bUseGBKT = atoi (pstr);
//	    }
//	}
//
//	fclose (fp);
//    }
//
//    if (!bRetVal) {
//	SaveConfig ();
//	SaveProfile ();
//    }
//}
//
//void SaveProfile (void)
//{
//    FILE           *fp;
//    char            strPath[PATH_MAX];
//
//    strcpy (strPath, (char *) getenv ("HOME"));
//    strcat (strPath, "/.fcitx/");
//
//    if (access (strPath, 0))
//	mkdir (strPath, S_IRWXU);
//
//    strcat (strPath, "profile");
//    fp = fopen (strPath, "wt");
//
//    if (!fp) {
//	fprintf (stderr, "\n?T�������?��???t profile!\n");
//	return;
//    }
//
//    fprintf (fp, "��?��?=%s\n", FCITX_VERSION);
//    fprintf (fp, "?�¡��?��????X=%d\n", iMainWindowX);
//    fprintf (fp, "?�¡��?��????Y=%d\n", iMainWindowY);
//    fprintf (fp, "��?��?���?��????X=%d\n", iInputWindowX);
//    fprintf (fp, "��?��?���?��????Y=%d\n", iInputWindowY);
//    fprintf (fp, "��?��?��???=%d\n", bCorner);
//    fprintf (fp, "��?��??D??�(���?=%d\n", bChnPunc);
//    fprintf (fp, "��?��?GBK=%d\n", bUseGBK);
//    fprintf (fp, "��?��?1a�(�?��??=%d\n", bTrackCursor);
//    fprintf (fp, "��?��?��a??=%d\n", bUseLegend);
//    fprintf (fp, "�̡�?�㨺?��?����=%d\n", iIMIndex);
//    fprintf (fp, "???1��??��?��?D??=%d\n", bLocked);
//    fprintf (fp, "?��?��?�꨺?=%d\n", bCompactMainWindow);
//
//    fprintf (fp, "��?��?��?3?GBK���(�?=%d\n", bUseGBKT);
//
//    iTempInputWindowX = iInputWindowX;
//    iTempInputWindowY = iInputWindowY;
//
//    fclose (fp);
//}
//
//void SetHotKey (char *strKeys, HOTKEYS * hotkey)
//{
//    char           *p;
//    char            strKey[30];
//    int             i, j;
//
//    p = strKeys;
//
//    while (*p == ' ')
//	p++;
//    i = 0;
//    while (p[i] != ' ' && p[i] != '\0')
//	i++;
//    strncpy (strKey, p, i);
//    strKey[i] = '\0';
//    p += i + 1l;
//    j = ParseKey (strKey);
//    if (j != -1)
//	hotkey[0] = j;
//    if (j == -1)
//	j = 0;
//    else
//	j = 1;
//
//    i = 0;
//    while (p[i] != ' ' && p[i] != '\0')
//	i++;
//    if (p[0]) {
//	strncpy (strKey, p, i);
//	strKey[i] = '\0';
//
//	i = ParseKey (strKey);
//	if (i == -1)
//	    i = 0;
//    }
//    else
//	i = 0;
//
//    hotkey[j] = i;
//}
//
///*
// * ???????t?D��D?������DD
// * �a騰a:???t?D��???DD��2��??a��?DD��|����
// */
//int CalculateRecordNumber (FILE * fpDict)
//{
//    char            strText[101];
//    int             nNumber = 0;
//
//    for (;;) {
//	if (!fgets (strText, 100, fpDict))
//	    break;
//
//	nNumber++;
//    }
//    rewind (fpDict);
//
//    return nNumber;
//}
//
//void SetSwitchKey (char *str)
//{
//    if (!strcasecmp (str, "R_CTRL"))
//	switchKey = R_CTRL;
//    else if (!strcasecmp (str, "R_SHIFT"))
//	switchKey = R_SHIFT;
//    else if (!strcasecmp (str, "L_SHIFT"))
//	switchKey = L_SHIFT;
//    else
//	switchKey = L_CTRL;
//}
//
//void SetTriggerKeys (char *str)
//{
//    int             i;
//    char            strKey[2][30];
//    char           *p;
//
//    //����?������?D??��??�쨦��??��?????����?����?��??��?a2
//    p = str;
//
//    i = 0;
//    iTriggerKeyCount = 0;
//    while (*p) {
//	if (*p == ' ') {
//	    iTriggerKeyCount++;
//	    while (*p == ' ')
//		p++;
//	    strcpy (strKey[1], p);
//	    break;
//	}
//	else
//	    strKey[0][i++] = *p++;
//    }
//    strKey[0][i] = '\0';
//
//    Trigger_Keys = (XIMTriggerKey *) malloc (sizeof (XIMTriggerKey) * (iTriggerKeyCount + 2));
//    for (i = 0; i <= (iTriggerKeyCount + 1); i++) {
//	Trigger_Keys[i].keysym = 0;
//	Trigger_Keys[i].modifier = 0;
//	Trigger_Keys[i].modifier_mask = 0;
//    }
//
//    for (i = 0; i <= iTriggerKeyCount; i++) {
//	if (MyStrcmp (strKey[i], "CTRL_")) {
//	    Trigger_Keys[i].modifier = Trigger_Keys[i].modifier | ControlMask;
//	    Trigger_Keys[i].modifier_mask = Trigger_Keys[i].modifier_mask | ControlMask;
//	}
//	else if (MyStrcmp (strKey[i], "SHIFT_")) {
//	    Trigger_Keys[i].modifier = Trigger_Keys[i].modifier | ShiftMask;
//	    Trigger_Keys[i].modifier_mask = Trigger_Keys[i].modifier_mask | ShiftMask;
//	}
//	else if (MyStrcmp (strKey[i], "ALT_")) {
//	    Trigger_Keys[i].modifier = Trigger_Keys[i].modifier | Mod1Mask;
//	    Trigger_Keys[i].modifier_mask = Trigger_Keys[i].modifier_mask | Mod1Mask;
//	}
//
//	if (Trigger_Keys[i].modifier == 0) {
//	    Trigger_Keys[i].modifier = ControlMask;
//	    Trigger_Keys[i].modifier_mask = ControlMask;
//	}
//
//	p = strKey[i] + strlen (strKey[i]) - 1;
//	while (*p != '_') {
//	    p--;
//	    if (p == strKey[i] || (p == strKey[i] + strlen (strKey[i]) - 1)) {
//		Trigger_Keys = (XIMTriggerKey *) malloc (sizeof (XIMTriggerKey) * (iTriggerKeyCount + 2));
//		Trigger_Keys[i].keysym = XK_space;
//		return;
//	    }
//	}
//	p++;
//
//	if (strlen (p) == 1)
//	    Trigger_Keys[i].keysym = tolower (*p);
//	else if (!strcasecmp (p, "LCTRL"))
//	    Trigger_Keys[i].keysym = XK_Control_L;
//	else if (!strcasecmp (p, "RCTRL"))
//	    Trigger_Keys[i].keysym = XK_Control_R;
//	else if (!strcasecmp (p, "LSHIFT"))
//	    Trigger_Keys[i].keysym = XK_Shift_L;
//	else if (!strcasecmp (p, "RSHIFT"))
//	    Trigger_Keys[i].keysym = XK_Shift_R;
//	else if (!strcasecmp (p, "LALT"))
//	    Trigger_Keys[i].keysym = XK_Alt_L;
//	else if (!strcasecmp (p, "RALT"))
//	    Trigger_Keys[i].keysym = XK_Alt_R;
//	else
//	    Trigger_Keys[i].keysym = XK_space;
//    }
//}
//
Bool CheckHZCharset (char *strHZ)
{
    if (!bUseGBK) {
	//GB2312��?oo��?�(�??1??��?a��o�̨���???��??����??��?��0xA1��?0xFE????(����?��?a0xF7)��?�̨�?t??��??����??��?��0xA1��?0xFE????
	//��������2����?��?�h���??�̡���2?��?��?������?o?���̡�??��?a?���㨦
	int             i;

	for (i = 0; i < strlen (strHZ); i++) {
	    if ((unsigned char) strHZ[i] < (unsigned char) 0xA1 || (unsigned char) strHZ[i] > (unsigned char) 0xF7 || (unsigned char) strHZ[i + 1] < (unsigned char) 0xA1 || (unsigned char) strHZ[i + 1] > (unsigned char) 0xFE)
		return False;
	    i++;
	}
    }

    return True;
}
//
//static char    *gGBKS2TTable = NULL;
//static int      gGBKS2TTableSize = -1;
//
///**
// * ??o����y�a�??data/gbks2t.tab��??����?��a���(�?��???�(���?
// * ��?o�����??�(�??GBK��?��?��a??3��GBK���(�?��?��??��
// *
// * WARNING��o ??o����y����??D?��????����?��?��?��?��???�̡¨�???
// * �a騰a������??��
// */
//char           *ConvertGBKSimple2Tradition (char *strHZ)
//{
//    FILE           *fp;
//    char           *ret;
//    char            strPath[PATH_MAX];
//    int             i, len;
//    unsigned int    idx;
//
//    if (strHZ == NULL)
//	return NULL;
//
//    if (!gGBKS2TTable) {
//	len = 0;
//
//	strcpy (strPath, PKGDATADIR "/data/");
//	strcat (strPath, TABLE_GBKS2T);
//	fp = fopen (strPath, "rb");
//	if (!fp) {
//	    ret = (char *) malloc (sizeof (char) * (strlen (strHZ) + 1));
//	    strcpy (ret, strHZ);
//	    return ret;
//	}
//
//	fseek (fp, 0, SEEK_END);
//	fgetpos (fp, (fpos_t *) & len);
//	if (len > 0) {
//	    gGBKS2TTable = (char *) malloc (sizeof (char) * len);
//	    gGBKS2TTableSize = len;
//	    fseek (fp, 0, SEEK_SET);
//	    fread (gGBKS2TTable, sizeof (char), len, fp);
//	}
//	fclose (fp);
//    }
//
//    i = 0;
//    len = strlen (strHZ);
//    ret = (char *) malloc (sizeof (char) * (len + 1));
//    for (; i < len; ++i) {
//	if (i < (len - 1))
//	    if ((unsigned char) strHZ[i] >= (unsigned char) 0x81
//		&& (unsigned char) strHZ[i] <= (unsigned char) 0xfe &&
//		(((unsigned char) strHZ[i + 1] >= (unsigned char) 0x40 && (unsigned char) strHZ[i + 1] <= (unsigned char) 0x7e) || ((unsigned char) strHZ[i + 1] > (unsigned char) 0x7f && (unsigned char) strHZ[i + 1] <= (unsigned char) 0xfe))) {
//		idx = (((unsigned char) strHZ[i] - (unsigned char) 0x81)
//		       * (unsigned char) 0xbe + ((unsigned char) strHZ[i + 1] - (unsigned char) 0x40)
//		       - ((unsigned char) strHZ[i + 1] / (unsigned char) 0x80)) * 2;
//		if (idx >= 0 && idx < gGBKS2TTableSize - 1) {
//		    //if ((unsigned char)gGBKS2TTable[idx] != (unsigned char)0xa1 && (unsigned char) gGBKS2TTable[idx + 1] != (unsigned char) 0x7f) {
//		    if ((unsigned char) gGBKS2TTable[idx + 1] != (unsigned char) 0x7f) {
//			ret[i] = gGBKS2TTable[idx];
//			ret[i + 1] = gGBKS2TTable[idx + 1];
//			i += 1;
//			continue;
//		    }
//		}
//	    }
//	ret[i] = strHZ[i];
//    }
//    ret[len] = '\0';
//
//    return ret;
//}
//
//int CalHZIndex (char *strHZ)
//{
//    return (strHZ[0] + 127) * 255 + strHZ[1] + 128;
//}
