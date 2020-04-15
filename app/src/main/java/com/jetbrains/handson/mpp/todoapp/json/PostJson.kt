package com.jetbrains.handson.mpp.todoapp.json

class PostJson {
    var postId: Int? = null
    var postName: String? = null
    var postAuthor: String? = null
    var postTag: List<String>? = null

    constructor() : super() {}

    constructor(PostId: Int, PostName: String, PostAuthor: String, tasks: MutableList<String>) : super() {
        this.postId = PostId
        this.postName = PostName
        this.postAuthor = PostAuthor
        this.postTag = tasks
    }
}