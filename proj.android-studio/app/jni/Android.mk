LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d)
$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d/external)
$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d/cocos)
$(call import-add-path,$(LOCAL_PATH)/../../../cocos2d/cocos/audio/include)

LOCAL_MODULE := MyGame_shared

LOCAL_MODULE_FILENAME := libMyGame

#-------------- .cpp  .c ---------------------
SRC_SUFFIX := *.cpp *.c
SRC_ROOT := $(LOCAL_PATH)/../../../Classes \
			$(LOCAL_PATH)/
# recursive wildcard
rwildcard = $(foreach d,$(wildcard $1*),$(call rwildcard,$d/,$2) $(filter $(subst *,%,$2),$d)))
SRC_FILES := $(call rwildcard,$(SRC_ROOT)/,$(SRC_SUFFIX))
LOCAL_SRC_FILES := hellocpp/main.cpp
LOCAL_SRC_FILES += $(SRC_FILES:$(LOCAL_PATH)/%=%)
#--------------.h  ---------------------

LOCAL_C_INCLUDES := $(LOCAL_PATH)/../../../Classes \
					$(LOCAL_PATH)/CommonFiles \
					$(LOCAL_PATH)/
#-----------------------------------

# _COCOS_HEADER_ANDROID_BEGIN
# _COCOS_HEADER_ANDROID_END


LOCAL_STATIC_LIBRARIES := cocos2dx_static

# _COCOS_LIB_ANDROID_BEGIN
# _COCOS_LIB_ANDROID_END

include $(BUILD_SHARED_LIBRARY)

$(call import-module,.)

# _COCOS_LIB_IMPORT_ANDROID_BEGIN
# _COCOS_LIB_IMPORT_ANDROID_END
