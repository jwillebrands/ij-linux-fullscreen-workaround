package dev.willebrands.ij.linuxfullscreenworkaround

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.wm.ex.WindowManagerEx

@Suppress("UnstableApiUsage")
class FullScreenFixRegistrar : StartupActivity {
    private fun hookupFrameHelper(project: Project) {
        log.info("Hooking FrameHelper for IdeFrame of project ${project.name}")
        val frame = WindowManagerEx.getInstanceEx().getFrame(project)
        frame?.apply {
            frameHelper?.let {
                setFrameHelper(FullScreenSyncWorkaroundFrameHelper(it))
            }
        }
    }

    companion object {
        val log = logger<FullScreenFixRegistrar>()
    }

    override fun runActivity(project: Project) {
        hookupFrameHelper(project)
    }
}
