package kz.zeme.skycast.home.presentation

import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.livedata.observeAsState
import kz.zeme.skycast.core.activity.extensions.isPermissionGranted
import kz.zeme.skycast.core.fragment.BaseFragment
import kz.zeme.skycast.core.fragment.extensions.composeView
import kz.zeme.skycast.home.navigation.HomeNavigator
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment() {

    private val homeVM: HomeVM by viewModel()
    private val homeNavigator: HomeNavigator by inject()

    private val locationPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions(),
    ) {
        homeVM.onStart()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View = composeView {
        HomeScreen(
            state = homeVM.weatherState.observeAsState(),
            controller = homeVM,
        )
    }

    override fun initViews(view: View) {
        val activity = requireActivity()
        val hasLocationPermission =
            activity.isPermissionGranted(Manifest.permission.ACCESS_COARSE_LOCATION) ||
                activity.isPermissionGranted(Manifest.permission.ACCESS_FINE_LOCATION)

        if (hasLocationPermission || homeVM.weatherState.value != null) {
            homeVM.onStart()
            return
        }

        locationPermissionLauncher.launch(
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
        )
    }

    override fun startObserving() {
        homeVM.navigation.observeEvent(
            owner = viewLifecycleOwner,
            onUpdate = homeNavigator::navigate,
        )
    }
}
