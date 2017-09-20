#include "HelloWorldScene.h"
#include "SimpleAudioEngine.h"

USING_NS_CC;  
   
Scene* HelloWorld::createScene()
{
    return HelloWorld::create();  
}

// on "init" you need to initialize your instance
bool HelloWorld::init()
{   
    //////////////////////////////
    // 1. super init first
    if ( !Scene::init() )
    {
        return false;
    }
    
    auto visibleSize = Director::getInstance()->getVisibleSize();
    Vec2 origin = Director::getInstance()->getVisibleOrigin();

	Vector<MenuItem*> itemVector;
    
	auto moreGame = MenuItemFont::create("MoreGame",CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(moreGame);

	auto checkOperator= MenuItemFont::create("checkOperator",CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(checkOperator);

	auto pay = MenuItemFont::create("pay", CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(pay);

	auto ScreenShotShare = MenuItemFont::create("ScreenShotShare", CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(ScreenShotShare);

	auto qqlogin = MenuItemFont::create("qqlogin", CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(qqlogin);

	auto wxlogin = MenuItemFont::create("wxlogin", CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(wxlogin);

	auto wxQrCodelogin = MenuItemFont::create("wxQrCodelogin", CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(wxQrCodelogin);


	auto loginout = MenuItemFont::create("loginout", CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(loginout);
	

	auto closeItem = MenuItemFont::create("Exit", CC_CALLBACK_1(HelloWorld::btnCallbuck, this));
	itemVector.pushBack(closeItem);

	int i = 0;
	int j = 1;
	//Vec2 vec = visibleSize;
	float width = 0;
	float height = 0;
	for (Vector<MenuItem*>::iterator itr = itemVector.begin(); itr < itemVector.end(); itr++) {
		float widthTemp = (*itr)->getBoundingBox().size.width;
		float heightTemp = (*itr)->getBoundingBox().size.height;
		//取出最大的 宽 高
		if (widthTemp > width) {
			width = widthTemp;
		}
		if (heightTemp > height) {
			height = heightTemp;
		}
	}
	for (Vector<MenuItem*>::iterator itr = itemVector.begin(); itr < itemVector.end(); itr++) {
		if (visibleSize.height - height*j+ origin.y<height) {
			j = 1;
			i++;
		}
		(*itr)->setPosition(Vec2(width*(i+0.5)+origin.x, visibleSize.height - height*j + origin.y));
		j++;
	}
	

    auto menu = Menu::createWithArray(itemVector);
    menu->setPosition(Vec2::ZERO);
    this->addChild(menu, 1);
    return true;
}

void HelloWorld::btnCallbuck(Ref* pSender) {
	std::string str = static_cast<MenuItemFont*>(pSender)->getString();
	bool isok = Java_C::getStaticMethodInfo("org/cocos2dx/cpp/AppActivity", str.c_str(), "()V");
	if(!str.compare("Exit")&&!isok){
		Director::getInstance()->end();
		#if (CC_TARGET_PLATFORM == CC_PLATFORM_IOS)
		exit(0);
		#endif
	}
}


