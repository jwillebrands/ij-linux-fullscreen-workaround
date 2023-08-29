package dev.willebrands.ij.linuxfullscreenworkaround

import com.intellij.openapi.wm.impl.IdeFrameImpl
import com.intellij.openapi.wm.impl.X11UiUtil

@Suppress("UnstableApiUsage")
internal class FullScreenSyncWorkaroundFrameHelper(
    private val frame: IdeFrameImpl,
    private val delegate: IdeFrameImpl.FrameHelper
) :
    IdeFrameImpl.FrameHelper by delegate {
    override val isInFullScreen: Boolean?
        get() = if (wouldTriggerFullScreenSync() && isCalledFromLinuxFullScreenSynchronizer()) true else delegate.isInFullScreen

    private fun wouldTriggerFullScreenSync(): Boolean {
        return frame.isShowing && delegate.isInFullScreen != true && X11UiUtil.isInFullScreenMode(frame)
    }

    private fun isCalledFromLinuxFullScreenSynchronizer(): Boolean {
        val caller = StackWalker.getInstance().walk { stack ->
            stack
                .limit(10)
                .filter { it.className == "com.intellij.openapi.wm.impl.LinuxFullScreenSynchronizer" && it.methodName == "syncFullScreen" }
                .findFirst()
        }
        return caller.isPresent
    }
}
