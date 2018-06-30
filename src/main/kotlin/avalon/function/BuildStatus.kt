package avalon.function

import avalon.main.MainServer
import avalon.tool.ObjectCaster
import avalon.tool.pool.Constants.Basic.CURRENT_SERVLET
import avalon.tool.pool.Constants.Basic.DEBUG
import avalon.tool.pool.Constants.Basic.LANG
import avalon.tool.system.Configs
import org.apache.commons.lang3.builder.EqualsBuilder
import org.apache.commons.lang3.builder.HashCodeBuilder
import org.json.JSONObject
import org.json.JSONTokener
import java.net.URL
import java.time.LocalDateTime

object BuildStatus : Runnable {
	private val repos = HashMap<String, ArrayList<RepoEntry>>()

	init {
		(ObjectCaster.toJSONObjectArray(Configs.getPluginConfigArray("BuildStatus", "listen"))).forEach {
			val name = it.getString("name")
			val ciMap = ArrayList<RepoEntry>()
			val ci = it.getJSONArray("ci")
			(0 until ci.length())
					.map { ci.getString(it).split(":") }
					.forEach { ciMap.add(RepoEntry(it[1], CIs.get(it[0])!!, CIs.get(it[0])!!.getStatus(it[1]))) }
			repos[name] = ciMap
		}
	}

	private fun update() {
		if (DEBUG)
			println("updating...")
		for ((name, list) in repos.entries) {
			val updateMap = ArrayList<Triple<String, RepoStatus, RepoStatus>>()

			list.forEach {
				val slug = it.slug
				val cil = it.ci
				val oldStatus = it.status
				val newStatus = cil.getStatus(slug)
				if (newStatus.buildStatus != oldStatus.buildStatus) {
					updateMap.add(Triple(cil.name(), oldStatus, newStatus))

					list.remove(it)
					list.add(RepoEntry(slug, cil, newStatus))
				}
			}

			if (updateMap.isNotEmpty())
				send(name, updateMap)
		}
		if (DEBUG)
			println(repos)
	}

	private fun send(name: String, updateMap: ArrayList<Triple<String, RepoStatus, RepoStatus>>) {
		for (thisGroup in MainServer.followGroup) {
			val stringList = ArrayList<String>()
			updateMap.forEach {
				stringList.add("${it.first}: ${it.second.buildStatus.name} -> ${it.third.buildStatus.name} at ${it.third.website}")
			}
			CURRENT_SERVLET.responseGroup(thisGroup,
					"${LANG.getString("function.build_status.update").format(name)}\n${stringList.joinToString(separator = "\n")}")
		}
	}

	override fun run() = update()
}

class RepoEntry(val slug: String, val ci: CIs.CI, val status: RepoStatus) {
	override fun toString(): String = "RepoEntry(slug='$slug', ci=$ci, status=$status)"
}

enum class RepoBuildStatus {
	SUCCESS, FAILED, UNKNOWN
}

class RepoStatus(val slug: String, val label: Int, val buildStatus: RepoBuildStatus, val duration: Int, val website: String) {
	override fun toString(): String = "RepoStatus(slug='$slug', label=$label, buildStatus=$buildStatus, duration=$duration, website=$website)"

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (javaClass != other?.javaClass) return false

		other as RepoStatus

		return EqualsBuilder().append(this.label, other.label).append(this.slug, other.slug).isEquals
	}

	override fun hashCode(): Int = HashCodeBuilder().append(slug).append(label).hashCode()


}

object CIs {

	fun get(name: String): CI? =
			when (name) {
				"TravisCI" -> TravisCI
				"AppVeyor" -> AppVeyor
				else -> null
			}

	interface CI {
		fun name(): String
		fun getStatus(repoName: String): RepoStatus
	}

	private object TravisCI : CI {
		override fun getStatus(repoName: String): RepoStatus {
			val root = JSONTokener(URL("https://api.travis-ci.org/repositories/$repoName").openStream()).nextValue() as JSONObject
			val lastBuildStatus = if (root.isNull("last_build_status")) -1 else root.get("last_build_status")
			val lastBuildFinish = if (root.isNull("last_build_finished_at")) 0 else root.get("last_build_finished_at")

			val status =
					when {
						lastBuildFinish == 0 -> RepoBuildStatus.UNKNOWN
						lastBuildStatus == 0 -> RepoBuildStatus.SUCCESS
						else -> RepoBuildStatus.FAILED
					}
			return RepoStatus(root.getString("slug"),
					Integer.parseInt(root.getString("last_build_number")),
					status,
					if (root.isNull("last_build_duration")) 0 else root.getInt("last_build_duration"),
					"https://travis-ci.org/$repoName"
			)
		}

		override fun name(): String = "TravisCI"
	}

	private object AppVeyor : CI {
		override fun getStatus(repoName: String): RepoStatus {

			fun parseDuration(start: LocalDateTime, finish: LocalDateTime): Int {
				val duration = finish.minusHours(start.hour.toLong())
						.minusMinutes(start.minute.toLong())
						.minusSeconds(start.second.toLong())
				return duration.minute * 60 + duration.second
			}

			val root = JSONTokener(URL("https://ci.appveyor.com/api/projects/$repoName").openStream()).nextValue() as JSONObject
			val project = root.getJSONObject("project")
			val build = root.getJSONObject("build")
			return RepoStatus(
					"${project.getString("accountName")}/${project.getString("slug")}",
					build.getInt("buildId"),
					when (build.getString("status")) {
						"success" -> RepoBuildStatus.SUCCESS
						"failed" -> RepoBuildStatus.FAILED
						else -> RepoBuildStatus.UNKNOWN
					},
					if (build.has("finished") && !build.isNull("finished"))
						parseDuration(LocalDateTime.parse(build.getString("started").substringBefore('+')),
								LocalDateTime.parse(build.getString("finished").substringBefore('+')))
					else 0,
					"https://ci.appveyor.com/project/$repoName"
			)
		}

		override fun name(): String = "AppVeyor"
	}
}