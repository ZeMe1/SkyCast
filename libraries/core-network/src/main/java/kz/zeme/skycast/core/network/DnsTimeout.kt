package kz.zeme.skycast.core.network

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import okhttp3.Dns
import java.net.InetAddress
import java.net.UnknownHostException

class DnsTimeout(
    private val timeoutMs: Long,
) : Dns {

    override fun lookup(hostname: String): List<InetAddress> = runBlocking {
        withTimeoutOrNull(timeoutMs) {
            InetAddress.getAllByName(hostname).toList()
        } ?: throw UnknownHostException("DNS lookup timed out for $hostname")
    }
}
