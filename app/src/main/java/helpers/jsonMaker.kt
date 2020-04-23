package helpers

import com.jetbrains.handson.mpp.todoapp.json.PostJson

fun createJsonData(id: Int, name: String, author: String, dataArray: MutableList<String>): PostJson {
    val post = PostJson(id, name, author, dataArray)
    return post
}
