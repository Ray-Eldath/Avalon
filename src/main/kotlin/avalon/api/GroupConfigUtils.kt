@file:JvmName("GroupConfigUtils")

package avalon.api

import avalon.util.GroupConfig
import org.apache.commons.lang3.ArrayUtils
import java.util.*

private val cache = HashMap<String, LongArray>()

fun getAllowArray(config: GroupConfig, id: String): LongArray? {
	if (cache.containsKey(id))
		return cache[id]

	val permissions = config.permissions
	for (thisPermission in permissions) {
		if (thisPermission.identifier == id) {
			val r = ArrayUtils.addAll(thisPermission.allow, *config.admins)
			cache[id] = r
			return r
		}
	}
	return ArrayUtils.add(config.admins, config.owner)
}
