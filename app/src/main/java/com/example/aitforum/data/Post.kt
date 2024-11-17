package com.example.aitforum.data

data class Post(
    var uid: String = "",
    var author: String = "",
    var title: String = "",
    var body: String = "",
    var imgUrl: String = ""
)

//we need this PostWithId. Since the document itself does not have the id and we have to access the
//post through the id so that we can get all of the vars under Post for that individual thing
//Id is the Id plus the document all encapsulated together
data class PostWithId(
    val postId: String,
    val post: Post
)