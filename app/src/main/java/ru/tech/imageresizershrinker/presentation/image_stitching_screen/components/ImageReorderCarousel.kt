package ru.tech.imageresizershrinker.presentation.image_stitching_screen.components

import android.net.Uri
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.burnoutcrew.reorderable.ReorderableItem
import org.burnoutcrew.reorderable.detectReorderAfterLongPress
import org.burnoutcrew.reorderable.rememberReorderableLazyListState
import org.burnoutcrew.reorderable.reorderable
import ru.tech.imageresizershrinker.R
import ru.tech.imageresizershrinker.presentation.root.widget.image.Picture
import ru.tech.imageresizershrinker.presentation.root.widget.modifier.container

@Composable
fun ImageReorderCarousel(
    images: List<Uri>?,
    onReorder: (List<Uri>?) -> Unit,
    modifier: Modifier = Modifier
        .container(RoundedCornerShape(24.dp))
) {
    val data = remember { mutableStateOf(images ?: emptyList()) }
    LaunchedEffect(images) {
        if (data.value.sorted() != images?.sorted()) {
            data.value = images ?: emptyList()
        }
    }
    val state = rememberReorderableLazyListState(
        onMove = { from, to ->
            data.value = data.value.toMutableList().apply {
                add(to.index, removeAt(from.index))
            }
        },
        onDragEnd = { _, _ ->
            onReorder(data.value)
        }
    )

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                fontWeight = FontWeight.Medium,
                text = stringResource(R.string.images_order),
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
                fontSize = 18.sp
            )
        }
        Box {
            LazyRow(
                state = state.listState,
                modifier = Modifier
                    .reorderable(state)
                    .detectReorderAfterLongPress(state),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                itemsIndexed(data.value, key = { _, uri -> uri.hashCode() }) { index, uri ->
                    ReorderableItem(
                        reorderableState = state,
                        key = uri.hashCode()
                    ) { isDragging ->
                        val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)
                        val alpha by animateFloatAsState(if (isDragging) 0.3f else 0.6f)
                        Box(
                            Modifier
                                .size(120.dp)
                                .shadow(elevation, RoundedCornerShape(16.dp))
                                .container(
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.Transparent,
                                    resultPadding = 0.dp
                                )
                        ) {
                            Picture(
                                model = uri,
                                modifier = Modifier.fillMaxSize(),
                                shape = RectangleShape,
                                contentScale = ContentScale.Fit
                            )
                            Box(
                                Modifier
                                    .size(120.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.surfaceContainer.copy(
                                            alpha = alpha
                                        ),
                                        shape = RoundedCornerShape(16.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${index + 1}",
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }
            Box(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .width(12.dp)
                    .height(140.dp)
                    .background(
                        brush = Brush.Companion.horizontalGradient(
                            0f to MaterialTheme.colorScheme.surfaceColorAtElevation(
                                1.dp
                            ),
                            1f to Color.Transparent
                        )
                    )
            )
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .width(12.dp)
                    .height(140.dp)
                    .background(
                        brush = Brush.Companion.horizontalGradient(
                            0f to Color.Transparent,
                            1f to MaterialTheme.colorScheme.surfaceColorAtElevation(
                                1.dp
                            )
                        )
                    )
            )
        }
    }
}