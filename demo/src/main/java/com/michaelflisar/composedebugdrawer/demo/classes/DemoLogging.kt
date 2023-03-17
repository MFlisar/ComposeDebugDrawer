package com.michaelflisar.composedebugdrawer.demo.classes

import android.content.Context
import com.michaelflisar.lumberjack.FileLoggingSetup
import com.michaelflisar.lumberjack.FileLoggingTree
import com.michaelflisar.lumberjack.L
import timber.log.ConsoleTree

object DemoLogging {

    lateinit var fileLoggingSetup: FileLoggingSetup

    fun init(context: Context) {

        fileLoggingSetup = FileLoggingSetup.NumberedFiles(
            context,
            setup = FileLoggingSetup.Setup(
                fileName = "log",
                fileExtension = "txt",
                logsToKeep = 20
            ),
            sizeLimit = "1MB"
        )

        L.plant(ConsoleTree())
        L.plant(FileLoggingTree(fileLoggingSetup))
    }
}