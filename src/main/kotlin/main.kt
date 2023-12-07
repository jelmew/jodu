package nl.jelmervanamen

import jakarta.enterprise.context.Dependent
import picocli.CommandLine

@CommandLine.Command
class OduCommand(private val oduService: OduService) : Runnable {


    override fun run() {
       oduService.listDirectoryContents()
    }
}

@Dependent
class GreetingService {
    fun sayHello(name: String?) {
        println("Hello $name!")
    }
}