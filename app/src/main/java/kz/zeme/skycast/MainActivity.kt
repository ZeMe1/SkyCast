package kz.zeme.skycast

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kz.zeme.skycast.core.navigation.ExternalModulesNavigator
import kz.zeme.skycast.features.main.MainActivityVM
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity(
    R.layout.main_activity,
) {

    private val mainActivityVM: MainActivityVM by viewModel()
    private val navigator: ExternalModulesNavigator by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startObserving()

        if (savedInstanceState != null) return

        mainActivityVM.onCreate(intent.extras, withSplash = true)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        mainActivityVM.onRedirect(intent)
    }

    private fun startObserving() {
        mainActivityVM.navigation.observeEvent(this) { navigation ->
            navigator.navigate(navigation)
        }
    }
}
