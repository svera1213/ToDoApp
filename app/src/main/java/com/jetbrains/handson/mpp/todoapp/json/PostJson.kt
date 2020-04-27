package com.jetbrains.handson.mpp.todoapp.json

class PostJson {
    var postId: Int? = null
    var postName: String? = null
    var postAuthor: String? = null
    var postTag: List<String>? = null
    var postTaskDone: List<Boolean>? = null
    var postDone: Double? = null

    constructor() : super() {}

    constructor(PostId: Int, PostName: String, PostAuthor: String, tasks: MutableList<String>, Done: Double, TaskDone: List<Boolean>) : super() {
        this.postId = PostId
        this.postName = PostName
        this.postAuthor = PostAuthor
        this.postTag = tasks
        this.postDone = Done
        this.postTaskDone = TaskDone
    }
}