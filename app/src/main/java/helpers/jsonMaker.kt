package helpers

import com.google.gson.Gson
import com.jetbrains.handson.mpp.todoapp.json.PostJson
import java.io.BufferedReader
import java.io.File

fun createJson(id: Int, name: String, author: String, dataArray: MutableList<String>): String {
    val post = PostJson(id, name, author, dataArray)
    val gson = Gson()
    val jsonString:String = gson.toJson(post)
    return jsonString
}

fun readJson(jsonFile: File): List<String> {
    val gson = Gson()
    val bufferedReader: BufferedReader = jsonFile.bufferedReader()
    val inputString = bufferedReader.use { it.readText() }
    val post = gson.fromJson(inputString, PostJson::class.java)
    return post.postTag!!
}