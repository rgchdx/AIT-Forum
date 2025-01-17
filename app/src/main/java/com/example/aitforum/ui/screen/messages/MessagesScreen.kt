package com.example.aitforum.ui.screen.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.isTraceInProgress
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.aitforum.data.Post
import com.google.firebase.auth.FirebaseAuth
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable


@Composable
fun MessagesScreen(
    messagesViewModel: MessagesViewModel = viewModel(),
    onWriteMessageClick: () -> Unit = {}
) {
    val postListState = messagesViewModel.postsList().collectAsState(
        initial = MessagesUIState.Init)


    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    onWriteMessageClick()
                }
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    tint = Color.White
                )
            }
        }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues)) {
            if (postListState.value == MessagesUIState.Init) {
                Text(text = "Initializing..")
            }
            else if (postListState.value == MessagesUIState.Loading) {
                CircularProgressIndicator()
            } else if (postListState.value is MessagesUIState.Success) {
                // show messages in a list...
                LazyColumn() {
                    items((postListState.value as MessagesUIState.Success).postList){
                        Text(text = it.post.title)
                    }
                }

            } else if (postListState.value is MessagesUIState.Error) {
                LazyColumn() {
                    items((postListState.value as MessagesUIState.Success).postList){
                        PostCard(
                            post = it.post,
                            onRemoveItem = {
                                messagesViewModel.deletePost(it.postId)
                            },
                            currentUserId = FirebaseAuth.getInstance().uid!!
                        )
                    }
                }
            }

        }

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCard(
    post: Post,
    onRemoveItem: () -> Unit = {},
    currentUserId: String = ""
) {
    val zoomState = rememberZoomState()
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp
        ),
        modifier = Modifier.padding(5.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                ) {
                    Text(
                        text = post.title,
                    )
                    Text(
                        text = post.body,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (currentUserId.equals(post.uid)) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "Delete",
                            modifier = Modifier.clickable {
                                onRemoveItem()
                            },
                            tint = Color.Red
                        )
                    }
                }
            }

            //if (post.imgUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    //.data(post.imgUrl)
                    .data("https://firebasestorage.googleapis.com/v0/b/aitforum2024springpeter.appspot.com/o/images%2F02167560-0218-41df-9cbb-cb9787aad275.jpg?alt=media&token=08be5fa2-a202-4f27-9c58-d799b12fe7b2")
                    .crossfade(true)
                    .build(),
                contentDescription = "Image",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(80.dp).zoomable(zoomState)
            )
            //}
        }
    }
}