package com.jefisu.manualplus.features_user.presentation.profile_user.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import me.rerere.zoomableimage.ZoomableImage

@Composable
fun GalleryUploader(
    imagesSelected: (List<Pair<Uri, String>>) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val pickedImages = remember { mutableStateListOf<Pair<Uri, String>>() }
    val multiplePhotoPicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents(),
        onResult = { uris ->
            val pairs = uris.map { uri ->
                val imageType = context.contentResolver
                    .getType(uri)
                    ?.split("/")
                    ?.last() ?: "jpg"
                uri to imageType
            }
            pickedImages.addAll(pairs)
            imagesSelected(pickedImages)
        }
    )
    var showImage by remember { mutableStateOf<Uri?>(null) }

    if (showImage != null) {
        Dialog(onDismissRequest = { showImage = null }) {
            Column {
                ZoomableImage(
                    painter = rememberAsyncImagePainter(showImage),
                    modifier = Modifier.size(300.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp)
                ) {
                    Button(onClick = { showImage = null }) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close Icon"
                        )
                        Text(text = "Close")
                    }
                    Button(onClick = {
                        pickedImages.removeIf { it.first == showImage }
                        imagesSelected(pickedImages)
                        showImage = null
                    }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete Icon"
                        )
                        Text(text = "Delete")
                    }
                }
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MaterialTheme.colors.primary)
                .clickable { multiplePhotoPicker.launch("image/*") }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
                tint = MaterialTheme.colors.background,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(12.dp))
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(pickedImages) { (uri, imageType) ->
                AsyncImage(
                    model = uri,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .clickable { showImage = uri }
                )
            }
        }
    }
}