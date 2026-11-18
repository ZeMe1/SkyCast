package kz.zeme.skycast.core.navigation

import android.os.Parcelable

interface Navigator {

    interface Navigation : Parcelable

    fun navigate(navigation: Navigation)
}
