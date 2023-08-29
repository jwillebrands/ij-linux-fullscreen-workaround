package dev.willebrands.ij.linuxfullscreenworkaround

import com.intellij.openapi.wm.impl.IdeFrameImpl

@Suppress("UnstableApiUsage")
internal class FullScreenSyncWorkaroundFrameHelper(private val delegate: IdeFrameImpl.FrameHelper) :
    IdeFrameImpl.FrameHelper by delegate {
    override val isInFullScreen: Boolean?
        get() = if (isCalledFromLinuxFullScreenSynchronizer()) true else delegate.isInFullScreen


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
