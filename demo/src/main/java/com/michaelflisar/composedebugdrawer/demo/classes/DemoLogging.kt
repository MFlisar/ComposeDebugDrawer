package com.michaelflisar.composedebugdrawer.demo.classes

import android.content.Context
import com.michaelflisar.lumberjack.core.L
import com.michaelflisar.lumberjack.core.interfaces.IFileLoggingSetup
import com.michaelflisar.lumberjack.implementation.LumberjackLogger
import com.michaelflisar.lumberjack.implementation.plant
import com.michaelflisar.lumberjack.loggers.console.ConsoleLogger
import com.michaelflisar.lumberjack.loggers.file.FileLogger
import com.michaelflisar.lumberjack.loggers.file.FileLoggerSetup

object DemoLogging {

    lateinit var fileLoggingSetup: IFileLoggingSetup

    fun init(context: Context) {

        // 1) init
        L.init(LumberjackLogger)

        // 2) add loggers
        val setup = FileLoggerSetup.Daily.create(context)
        L.plant(ConsoleLogger())
        L.plant(FileLogger(setup))

        fileLoggingSetup = setup
    }
}