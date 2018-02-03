package avalon.util

import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import java.util.concurrent.Delayed
import java.util.concurrent.TimeUnit

/**
 * Created by Eldath Ray on 2017/3/22.
 *
 * @author Eldath Ray
 */
class DelayMessage(private val delaySecond: Long, val message: Message, val doWhat: () -> String) : Delayed {
    private val delaySecondEpoch: Long = System.currentTimeMillis() + delaySecond

    override fun getDelay(unit: TimeUnit): Long =
            unit.convert(delaySecondEpoch - System.currentTimeMillis(), TimeUnit.MILLISECONDS)

    override fun compareTo(other: Delayed): Int {
        if (other === this)
            return 0
        if (javaClass != other.javaClass)
            return -1
        val message = other as DelayMessage
        val left = message.delaySecond
        if (left == delaySecond) return 0
        return if (left > delaySecond) 1 else -1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as DelayMessage?
        return EqualsBuilder()
                .append(delaySecondEpoch, that!!.delaySecondEpoch)
                .append(message, that.message)
                .append(doWhat, that.doWhat)
                .isEquals
    }

    override fun hashCode(): Int = HashCodeBuilder(17, 37)
            .append(delaySecondEpoch)
            .append(message)
            .append(doWhat)
            .toHashCode()
}

interface Plugin {
    fun name(): String
    fun version(): String
    fun main()
}

class PluginInfo(val name: String, val copyright: String, val website: String, val classString: String, val fileName: String, val isEnabled: Boolean)
