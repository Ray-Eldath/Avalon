package avalon.plugin

import avalon.main.MainServer
import avalon.tool.ObjectCaster
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.system.Configs
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL

object BuildStatus : Runnable {
	private val map = HashMap<String, RepoStatus>()
	private val CI: HashMap<CIs.CI, String>

	init {
		val result = HashMap<CIs.CI, String>()
		ObjectCaster.toStringArray(Configs.getPluginConfigArray("BuildStatus", "listen")).forEach {
			val split = (it as String).split("#")
			result.put(CIs.get(split[0])!!, split[1])
		}
		this.CI = result
		update()
	}

	private fun update() {
		for ((ci, slug) in CI.entries) {
			val status = ci.getStatus(slug)
			if (map.containsKey(slug))
				send(ci, slug, map[slug]!!, status)
			else
				map.put(slug, status)
		}
	}

	private fun send(ci: CIs.CI, slug: String, oldStatus: RepoStatus, nowStatus: RepoStatus) {
		if (oldStatus.label != nowStatus.label)
			for (thisGroup in MainServer.followGroup)
				CURRENT_SERVLET.responseGroup(thisGroup,
						"""BuildStatus更新：$slug at ${nowStatus.website}
${ci.name()}: ${oldStatus.buildStatus.name} -> ${nowStatus.buildStatus.name}
""")
	}

	override fun run() = update()
}

enum class RepoBuildStatus {
	SUCCESS, FAILED, UNKNOWN
}

class RepoStatus(val slug: String, val label: Int, val buildStatus: RepoBuildStatus, val duration: Int, val website: String) {
	override fun toString(): String {
		return "RepoStatus(slug='$slug', label=$label, buildStatus=$buildStatus, duration=$duration, website=$website)"
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as RepoStatus

		return EqualsBuilder().append(this.label, other.label).append(this.slug, other.slug).isEquals
	}

	override fun hashCode(): Int = HashCodeBuilder().append(slug).append(label).hashCode()


}

object CIs {

	fun get(name: String): CI? {
		return when (name) {
			"TravisCI" -> TravisCI
			else -> null
		}
	}

	interface CI {
		fun name(): String
		fun getStatus(repoName: String): RepoStatus
	}

	private object TravisCI : CI {
		override fun getStatus(repoName: String): RepoStatus {
			val root = JSONTokener(URL("https://api.travis-ci.org/repositories/$repoName").openStream()).nextValue() as JSONObject
			val lastBuildStatus = if (root.isNull("last_build_status")) 0 else root.get("last_build_status")
			val lastBuildFinish = if (root.isNull("last_build_finished_at")) 0 else root.get("last_build_finished_at")
			val status = {
				if (lastBuildFinish == 0)
					RepoBuildStatus.UNKNOWN
				else
					RepoBuildStatus.SUCCESS
			}
			return RepoStatus(root.getString("slug"),
					Integer.parseInt(root.getString("last_build_number")),
					status.invoke(),
					if (root.isNull("last_build_duration")) 0 else root.getInt("last_build_duration"),
					"https://travis-ci.org/$repoName"
			)
		}

		override fun name(): String = "TravisCI"
	}
}