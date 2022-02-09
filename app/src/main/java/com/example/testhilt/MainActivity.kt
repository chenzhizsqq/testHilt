package com.example.testhilt

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject
import javax.inject.Qualifier

//https://www.jianshu.com/p/f32beb3614e5
/**
 * MainActivity上，一定要加入@AndroidEntryPoint
 * 才能够让当前的Activity知道是调用Hilt
 */
@AndroidEntryPoint      //将依赖项注入 Android 类
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var user: User//定义一个User对象

    @Inject
    lateinit var user2: User2//定义一个User对象

    @Inject
    lateinit var chinaCar: ChinaCar

    @Inject
    lateinit var dog: Dog

    @Inject
    @MadeInCN
    lateinit var chinaCarTest1: ChinaCar
    @Inject
    @MadeInUSA
    lateinit var chinaCar2Test2: ChinaCar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //因为是用@Inject定义的，所以可以一旦定义就自动给你生成
        user.name = "朱Bony"
        user.age = 30
        Log.e("TAG", "user: $user")     //user: User(name=朱Bony age30)


        chinaCar.name = "比亚迪"
        chinaCar.engine.on()
        chinaCar.engine.off()
        Log.e("TAG", "chinaCar.name: ${chinaCar.name}")

        Log.e("TAG", "dog.name: " + dog.name)

        Log.e("TAG", "chinaCarTest1: $chinaCarTest1")
        chinaCarTest1.engine.on()       //ChinaEngine on
        chinaCarTest1.engine.off()      //ChinaEngine off
        Log.e("TAG", "chinaCarTest2: $chinaCar2Test2")
        chinaCar2Test2.engine.on()      //AmericaEngine on
        chinaCar2Test2.engine.off()     //AmericaEngine off

        user2.name = "user2 name"
        user2.age = 18
        user2.showMsg()
        Log.e("TAG", "user2: $user2")     //user: User(name=朱Bony age30)
    }
}

/**
 * ActivityScoped
 * Scope annotation for bindings that should exist for the life of an activity.
 * 如果您使用 @ActivityScoped 将 User2的作用域限定为 ActivityComponent。User2的作用域限定为Activity
 */
@ActivityScoped
class User2 @Inject constructor(@ActivityContext val context: Context) {
    var name: String = ""
    var age = 0
    override fun toString() = "User(name=$name age$age)"
    fun showMsg() = Toast.makeText(context, toString(), Toast.LENGTH_SHORT).show()
}

//创建@Inject 结构的class
class User @Inject constructor() {
    var name: String = ""
    var age = 0
    override fun toString() = "User(name=$name age$age)"
}

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MadeInCN

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class MadeInUSA

class AmericaEngine @Inject constructor() : Engine {
    override fun on() {
        Log.e("zrm", "AmericaEngine on")
    }

    override fun off() {
        Log.e("zrm", "AmericaEngine off")
    }
}

data class Dog(val name: String)

@Module
@InstallIn(ActivityComponent::class)
class DogModule {
    @Provides
    fun provideDog() = Dog("京巴犬")
}

interface Engine {
    fun on()
    fun off()
}

@Module
@InstallIn(ActivityComponent::class)
class CarModule {
    @Provides
    @MadeInCN
    fun provideChinaCar(): ChinaCar {
        return ChinaCar(ChinaEngine())
    }

    @Provides
    @MadeInUSA
    fun provideChinaCar2(): ChinaCar {
        return ChinaCar(AmericaEngine())
    }
}

//创建一个继承Engine的类
class ChinaEngine @Inject constructor() : Engine {
    override fun on() {
        Log.e("zrm", "ChinaEngine on")
    }

    override fun off() {
        Log.e("zrm", "ChinaEngine off")
    }
}

class ChinaCar @Inject constructor(val engine: Engine) {
    lateinit var name: String
}

/**
 * 告诉Hilt 这个module属于的Component
 * ActivityComponent是Hilt定义好的
 */
@Module
@InstallIn(ActivityComponent::class)
interface MainModule {
    @Binds
    fun bindEngine(chinaEngine: ChinaEngine): Engine
}