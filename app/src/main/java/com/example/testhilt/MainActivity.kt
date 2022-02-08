package com.example.testhilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.components.ActivityComponent
import javax.inject.Inject

@AndroidEntryPoint      //将依赖项注入 Android 类
class MainActivity : AppCompatActivity() {

    @Inject lateinit var user:User//定义一个User对象

    @Inject lateinit var chinaCar:ChinaCar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //因为是用@Inject定义的，所以可以一旦定义就自动给你生成
        user.name = "朱Bony"
        user.age = 30
        Log.e("TAG", "user: $user")     //user: User(name=朱Bony age30)


        chinaCar.name="比亚迪"
        chinaCar.engine.on()
        chinaCar.engine.off()
        Log.e("TAG", "chinaCar.name: ${chinaCar.name}")
    }
}

//创建@Inject 结构的class
class User @Inject constructor()
{
    var name:String=""
    var age = 0
    override fun toString()="User(name=$name age$age)"
}

interface Engine{
    fun on()
    fun off()
}

//创建一个继承Engine的类
class ChinaEngine @Inject constructor():Engine{
    override fun on() {
        Log.e("zrm", "ChinaEngine on")
    }
    override fun off() {
        Log.e("zrm", "ChinaEngine off")
    }
}
class ChinaCar @Inject constructor(val engine:Engine){
    lateinit var name:String
}

/**
 * 告诉Hilt 这个module属于的Component
 * ActivityComponent是Hilt定义好的
 */
@Module
@InstallIn(ActivityComponent::class)
interface MainModule {
    @Binds
    fun bindEngine(chinaEngine:ChinaEngine):Engine
}