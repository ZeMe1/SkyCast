package kz.zeme.skycast.core.navigation.extensions

import android.content.Intent
import android.os.Bundle
import kz.zeme.skycast.core.navigation.Navigator

private const val REDIRECT_KEY = "REDIRECT_KEY"

fun Intent.putRedirect(redirect: Navigator.Navigation?) = apply {
    putExtra(REDIRECT_KEY, redirect)
}

fun Intent.getRedirectOrNull(): Navigator.Navigation? =
    getTypedParcelable(REDIRECT_KEY)

fun Bundle.putRedirect(redirect: Navigator.Navigation?) = apply {
    putParcelable(REDIRECT_KEY, redirect)
}

fun Bundle.getRedirectOrNull(): Navigator.Navigation? =
    getTypedParcelable(REDIRECT_KEY)
