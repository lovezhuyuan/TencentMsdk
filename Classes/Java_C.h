#pragma once
#include "cocos2d.h"
#if(CC_TARGET_PLATFORM == CC_PLATFORM_ANDROID)
#include "platform/android/jni/JniHelper.h"	
#endif
USING_NS_CC;
class Java_C{
public:
	static bool getStaticMethodInfo(const char *className,const char *methodName, const char *paramCode);
};