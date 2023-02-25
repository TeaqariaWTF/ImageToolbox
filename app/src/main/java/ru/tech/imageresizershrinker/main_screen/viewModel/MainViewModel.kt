package ru.tech.imageresizershrinker.main_screen.viewModel

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.olshevski.navigation.reimagined.navController
import ru.tech.imageresizershrinker.main_screen.Screen

class MainViewModel : ViewModel() {

    val navController = navController<Screen>(Screen.Main)
    private val _uri = mutableStateOf<Uri?>(null)
    val uri by _uri

    private val _showSelectDialog = mutableStateOf(false)
    val showSelectDialog by _showSelectDialog


    fun updateUri(uri: Uri?) {
        _uri.value = null
        _uri.value = uri
        if(uri != null && navController.backstack.entries.lastOrNull()?.destination == Screen.Main) _showSelectDialog.value = true
    }

    fun hideSelectDialog() {
        _showSelectDialog.value = false
    }
}