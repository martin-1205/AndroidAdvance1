#include <jni.h>
#include <string>
#include <pthread.h>
#include <sys/select.h>
#include <sys/time.h>
#include <sys/types.h>
#include <stdio.h>
#include <pthread.h>
#include <unistd.h>
#include <android/log.h>

void *test_usleep(void * __attribute__((unused)) arg){
    struct timeval tv;
    while(1) {
        gettimeofday(&tv,NULL);
        long int before = tv.tv_sec*1000000 + tv.tv_usec;
        usleep(5000);//5ms
        gettimeofday(&tv,NULL);
        long int after = tv.tv_sec*1000000 + tv.tv_usec;
        __android_log_print(ANDROID_LOG_INFO,"TEST_SLEEP","usleep:%ldμs\n",after-before);
    }
    return ((void *)0);
}

extern "C"
JNIEXPORT jstring JNICALL
Java_com_martin_android_advance111_MainActivity_startNativeSleepTest(JNIEnv *env, jobject thiz) {
    // TODO: implement startNativeSleepTest()
    std::string hello = "Hello from C++";
    pthread_t tid_usleep;
    pthread_create(&tid_usleep,NULL,test_usleep,NULL);
    return env->NewStringUTF(hello.c_str());
}