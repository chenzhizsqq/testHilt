package com.example.testhilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint      //将依赖项注入 Android 类
class MainActivity : AppCompatActivity() {

    @Inject lateinit var user:User//定义一个User对象

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //因为是用@Inject定义的，所以可以一旦定义就自动给你生成
        user.name = "朱Bony"
        user.age = 30
        Log.e("TAG", "user: $user")     //user: User(name=朱Bony age30)
    }
}

//创建@Inject 结构的class
class User @Inject constructor()
{
    var name:String=""
    var age = 0
    override fun toString()="User(name=$name age$age)"
}