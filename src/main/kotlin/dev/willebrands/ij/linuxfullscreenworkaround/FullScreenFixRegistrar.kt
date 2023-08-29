package dev.willebrands.ij.linuxfullscreenworkaround

import com.intellij.openapi.diagnostic.logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity
import com.intellij.openapi.wm.ex.WindowManagerEx

@Suppress("UnstableApiUsage")
class FullScreenFixRegistrar : ProjectActivity {
    private fun hookupFrameHelper(project: Project) {
        log.info("Hooking FrameHelper for IdeFrame of project ${project.name}")
        val frame = WindowManagerEx.getInstanceEx().getFrame(project)
        frame?.apply {
            frameHelper?.let {
                setFrameHelper(FullScreenSyncWorkaroundFrameHelper(frame, it))
            }
        }
    }

    companion object {
        val log = logger<FullScreenFixRegistrar>()
    }

    override suspend fun execute(project: Project) {
        hookupFrameHelper(project)
    }
}
