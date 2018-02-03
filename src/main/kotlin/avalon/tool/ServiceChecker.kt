package avalon.tool

import avalon.group.Hitokoto
import avalon.util.backend.CoolQBackend
import org.slf4j.LoggerFactory

object ServiceChecker {
    private val logger = LoggerFactory.getLogger(this.javaClass)
    private val services = listOf(CoolQBackend(), Hitokoto.Hitokotor)

    fun check(): Boolean {
        logger.info("Now checking usability of services...")
        var inaccessible = false
        for (service in services) {
            val name = service.javaClass.simpleName
            if (service.available())
                logger.info("   $name: Accessible")
            else {
                logger.error("   $name: Inaccessible")
                inaccessible = true
            }
        }
        if (inaccessible)
            logger.error("Some services are inaccessible! Please make sure you have open the backend, then reopen Avalon a few seconds later.")
        else
            logger.info("Services all checked.")
        return !inaccessible
    }
}