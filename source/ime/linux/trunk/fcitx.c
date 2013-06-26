// fcitx.cpp : �������̨Ӧ�ó������ڵ㡣
//

#include "stdafx.h"
#include <iconv.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#include "py.h"


int code_convert(char *inbuf,char *outbuf,size_t* inlen,size_t* outlen)
{
	iconv_t cd;
	char **pin = &inbuf;
	char **pout = &outbuf;
	static char to_charset[]="utf-8";
	static char from_charset[]="gb2312";
	cd = iconv_open(to_charset,from_charset);
	if (cd==0) return -1;
	memset(outbuf,'\0',*outlen);
	if (iconv(cd,pin,inlen,pout,outlen)==(size_t)-1) return -1;
	iconv_close(cd);
	return 0;
}

void input(char* str){
	int i;
	reset();

	for(i=0;i<strlen(str);i++){
		DoPYInput(str[i]);
	}
}

void reset(){
	ResetPYStatus();
}

void pageDown(){
	PYGetCandWords(SM_NEXT);
}

void pageUp(){
	PYGetCandWords(SM_PREV);
}

void getWord(int i,char* str){
	char * t=PYGetCandWord(i);
	if(!t){
		str[0]='\0';
		return;
	}else{
		size_t sl,dl;
		sl=strlen(t);
		dl=2*sl;
		code_convert(t,str,&sl,&dl);
	}
}


//int main()
//{
//	int i;
//	 char* str=(char*)malloc(100);
//	 gets(str);
//	 input(str);
//	 for(i=0;i<3;i++){
//		 getWord(i,str);
//		 printf("%s\n",str);
////
//	 }
//	 printf("你好\n");
//	 return 0;
//}
//
