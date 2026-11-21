package kz.zeme.skycast.home.navigation

import kz.zeme.skycast.home.presentation.HomeFragment

class HomeRouter {

    companion object {
        const val TAG_HOME = "TAG_HOME"
    }

    fun getHomeFragment(): HomeFragment = HomeFragment()
}
